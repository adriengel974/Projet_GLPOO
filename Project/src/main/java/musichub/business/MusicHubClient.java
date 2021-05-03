package musichub.business;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Iterator;

public class MusicHubClient implements Colors {

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;

	public void connect(String ip) {
		int port = 6666;
		try  {
			//create the socket; it is defined by an remote IP address (the address of the server) and a port number
			socket = new Socket(ip, port);


			//create the streams that will handle the objects coming and going through the sockets
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());

			System.out.println(PURPLE + "\n\n\t\tWelcome on MusicHub!\n" + DEFAULT);
			var user = (MusicHubUser) input.readObject(); //deserialize and read the user object from the stream
			user.userConnection();
			System.out.println(BOLD + "\nType h to have the commands" + DEFAULT);

			Scanner scan = new Scanner(System.in);
			String choice = scan.nextLine();

			output.writeObject(choice);	//serialize and write the String to the stream

			if (user.getUserValue().equals("user")){
				while(choice.charAt(0) != 'q') {
					choice = scan.nextLine();
					output.writeObject(choice);
					//System.out.println(YELLOW + "text sent to the server: " + DEFAULT + choice); //this line allows us to see if the output worked
					user = (MusicHubUser) input.readObject();
					//System.out.println(BLUE + "New object received from the server" + DEFAULT); //this line allows us to see if the input worked

					switch (choice.charAt(0)) {
						case 'h':
							user.userAvailableCommands();
							break;
						case 't':
							//album titles, ordered by date
							System.out.println(user.getAlbumsTitlesSortedByDate());
							break;
						case 'u':
							//display audiobooks ordered by author
							System.out.println(user.getAudiobooksTitlesSortedByAuthor());
							break;

						case 'g':
							//display songs of an album, ordered by genre
							System.out.println(CYAN + "\nSongs of an album sorted by genre will be displayed \nAvailable albums are:" + DEFAULT);
							System.out.println(user.getAlbumsTitlesSortedByDate());

							System.out.println("Enter the name of an album");
							String albumTitle = scan.nextLine();
							try {
								System.out.println(user.getAlbumSongsSortedByGenre(albumTitle));
							} catch (NoAlbumFoundException ex) {
								System.out.println(RED + "\nNo album found with the requested title " + DEFAULT + ex.getMessage());
							}
							break;

						case 'd':
							//display songs of an album
							System.out.println(CYAN + "\nSongs of an album will be displayed \nAvailable albums are:" + DEFAULT);
							System.out.println(user.getAlbumsTitlesSortedByDate());

							System.out.println("Enter the name of an album: ");
							albumTitle = scan.nextLine();
							try {
								System.out.println(user.getAlbumSongs(albumTitle));
							} catch (NoAlbumFoundException ex) {
								System.out.println(RED + "\nNo album found with the requested title : " + DEFAULT + ex.getMessage());
							}
							break;
						case 'q':
							System.out.println("You have been disconnected from the server!");
							break;
						default:
							System.out.println("Unknown command");
							break;
					}
				}
				user = (MusicHubUser) input.readObject();
				scan.close();

			} else if (user.getUserValue().equals("admin")){
				while(choice.charAt(0) != 'q'){
					choice = scan.nextLine();
					output.writeObject(choice);
					//System.out.println(YELLOW + "text sent to the server: " + DEFAULT + choice); //this line allows us to see if the output worked
					user = (MusicHubUser) input.readObject();
					//System.out.println(BLUE + "New object received from the server" + DEFAULT); //this line allows us to see if the input worked

					switch (choice.charAt(0)){
						case 'h':
							user.userAvailableCommands();
							break;
						case 't':
							//album titles, ordered by date
							System.out.println(user.getAlbumsTitlesSortedByDate());
							break;
						case 'u':
							//display audiobooks ordered by author
							System.out.println(user.getAudiobooksTitlesSortedByAuthor());
							break;

						case 'g':
							//display songs of an album, ordered by genre
							System.out.println(CYAN + "\nSongs of an album sorted by genre will be displayed \nAvailable albums are:" + DEFAULT);
							System.out.println(user.getAlbumsTitlesSortedByDate());

							System.out.println("Enter the name of an album");
							String albumTitle = scan.nextLine();
							try {
								System.out.println(user.getAlbumSongsSortedByGenre(albumTitle));
							} catch (NoAlbumFoundException ex) {
								System.out.println("\nNo album found with the requested title " + ex.getMessage());
							}
							break;

						case 'd':
							//display songs of an album
							System.out.println(CYAN + "\nSongs of an album will be displayed \nAvailable albums are:" + DEFAULT);
							System.out.println(user.getAlbumsTitlesSortedByDate());

							System.out.println("Enter the name of an album: ");
							albumTitle = scan.nextLine();
							try {
								System.out.println(user.getAlbumSongs(albumTitle));
							} catch (NoAlbumFoundException ex) {
								System.out.println(RED + "\nNo album found with the requested title : "+ DEFAULT + ex.getMessage());
							}
							break;

						case 'c':
							//add a new song
							System.out.println(CYAN + "\nEnter a new song" + DEFAULT);
							System.out.println(BOLD + "\nSong title: ");
							String title = scan.nextLine();
							System.out.println("\nSong genre (jazz, classic, hiphop, rock, pop, rap):");
							String genre = scan.nextLine();
							System.out.println("\nSong artist: ");
							String artist = scan.nextLine();
							System.out.println ("\nSong length in seconds: ");
							int length = Integer.parseInt(scan.nextLine());
							System.out.println("\nSong content: ");
							String content = scan.nextLine();
							Song s = new Song (title, artist, length, content, genre);
							user.addElement(s);
							System.out.println("\nNew element list: ");
							Iterator<AudioElement> it = user.elements();
							while (it.hasNext()) System.out.println(it.next().getTitle());
							System.out.println("\nSong created!" + DEFAULT);

							output.writeObject(user);
							break;

						case 'a':
							//add a new album
							System.out.println(CYAN + "\nEnter a new album: " + DEFAULT);
							System.out.println(BOLD + "\nAlbum title: ");
							String aTitle = scan.nextLine();
							System.out.println("\nAlbum artist: ");
							String aArtist = scan.nextLine();
							System.out.println ("\nAlbum length in seconds: ");
							int aLength = Integer.parseInt(scan.nextLine());
							System.out.println("\nAlbum date as YYYY-DD-MM: ");
							String aDate = scan.nextLine();
							Album a = new Album(aTitle, aArtist, aLength, aDate);
							user.addAlbum(a);
							System.out.println("\nNew list of albums: ");
							Iterator<Album> ita = user.albums();
							while (ita.hasNext()) System.out.println(ita.next().getTitle());
							System.out.println("\nAlbum created!" + DEFAULT);

							output.writeObject(user);
							break;

						case '+':
							//add a song to an album
							System.out.println(CYAN + "\nAdd an existing song to an existing album" + DEFAULT);
							System.out.println(BOLD + "\nType the name of the song you wish to add. Available songs: ");
							Iterator<AudioElement> itae = user.elements();
							while (itae.hasNext()) {
								AudioElement ae = itae.next();
								if ( ae instanceof Song) System.out.println(ae.getTitle());
							}
							String songTitle = scan.nextLine();

							System.out.println("\nType the name of the album you wish to enrich. Available albums: ");
							Iterator<Album> ait = user.albums();
							while (ait.hasNext()) {
								Album al = ait.next();
								System.out.println(al.getTitle());
							}
							String titleAlbum = scan.nextLine();
							try {
								user.addElementToAlbum(songTitle, titleAlbum);
							} catch (NoAlbumFoundException ex){
								System.out.println (ex.getMessage());
							} catch (NoElementFoundException ex){
								System.out.println (ex.getMessage());
							}
							System.out.println("\nSong added to the album!" + DEFAULT);

							output.writeObject(user);
							break;

						case 'l':
							//add a new audiobook
							System.out.println(CYAN + "\nEnter a new audiobook: " + DEFAULT);
							System.out.println(BOLD + "\nAudioBook title: ");
							String bTitle = scan.nextLine();
							System.out.println("\nAudioBook category (youth, novel, theater, documentary, speech)");
							String bCategory = scan.nextLine();
							System.out.println("\nAudioBook artist: ");
							String bArtist = scan.nextLine();
							System.out.println ("\nAudioBook length in seconds: ");
							int bLength = Integer.parseInt(scan.nextLine());
							System.out.println("\nAudioBook content: ");
							String bContent = scan.nextLine();
							System.out.println("\nAudioBook language (french, english, italian, spanish, german)");
							String bLanguage = scan.nextLine();
							AudioBook b = new AudioBook (bTitle, bArtist, bLength, bContent, bLanguage, bCategory);
							user.addElement(b);
							System.out.println("\nAudiobook created! New element list: " + DEFAULT);
							Iterator<AudioElement> itl = user.elements();
							while (itl.hasNext()) System.out.println(itl.next().getTitle());

							output.writeObject(user);
							break;

						case 'p':
							//create a new playlist from existing songs and audio books
							System.out.println(CYAN + "\nAdd an existing song or audiobook to a new playlist" + DEFAULT);
							System.out.println(BOLD + "\nExisting playlists:");
							Iterator<PlayList> itpl = user.playlists();
							while (itpl.hasNext()) {
								PlayList pl = itpl.next();
								System.out.println(pl.getTitle());
							}
							System.out.println("\nType the name of the playlist you wish to create:");
							String playListTitle = scan.nextLine();
							PlayList pl = new PlayList(playListTitle);
							user.addPlaylist(pl);
							System.out.println("\nAvailable elements: ");

							Iterator<AudioElement> itael = user.elements();
							while (itael.hasNext()) {
								AudioElement ae = itael.next();
								System.out.println(ae.getTitle());
							}
							while (choice.charAt(0)!= 'n') 	{
								System.out.println(DEFAULT + GREEN + "\nType the name of the audio element you wish to add or 'n' to exit:" + DEFAULT);
								String elementTitle = scan.nextLine();
								try {
									user.addElementToPlayList(elementTitle, playListTitle);
								} catch (NoPlayListFoundException ex) {
									System.out.println (ex.getMessage());
								} catch (NoElementFoundException ex) {
									System.out.println (ex.getMessage());
								}

								System.out.println(GREEN + "\nType y to add a new one, n to end" + DEFAULT);
								choice = scan.nextLine();
							}
							System.out.println(BOLD + "\nPlaylist created!" + DEFAULT);

							output.writeObject(user);
							break;

						case '-':
							//delete an existing playlist
							System.out.println(CYAN + "\nDelete an existing playlist. Available playlists:" + DEFAULT);
							Iterator<PlayList> itp = user.playlists();
							while (itp.hasNext()) {
								PlayList p = itp.next();
								System.out.println(p.getTitle());
							}
							System.out.println(BOLD + "\nEnter the name of the playlist you want to delete :");
							String plTitle = scan.nextLine();
							try {
								user.deletePlayList(plTitle);
							}	catch (NoPlayListFoundException ex) {
								System.out.println (ex.getMessage());
							}
							System.out.println("\nPlaylist deleted!" + DEFAULT);

							output.writeObject(user);
							break;

					/*case 's':
						//save elements, albums, playlists
						user.saveElements();
						user.saveAlbums();
						user.savePlayLists();
						System.out.println("Elements, albums and playlists saved!");

						output.writeObject(user);
						break;*/

						case 'q':
							System.out.println("You have been disconnected from the server!");
							break;
						default:
							System.out.println("Unknown command");
							break;
					}
				}
				user = (MusicHubUser) input.readObject();
				scan.close();
			}

		} catch  (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		catch  (IOException ioe) {
			ioe.printStackTrace();
		}
		catch  (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
		finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}