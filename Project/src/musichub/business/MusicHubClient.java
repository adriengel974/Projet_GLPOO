package musichub.business;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.Iterator;

public class MusicHubClient {
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;

	private static void printAvailableCommands() {
		System.out.println("t: display the album titles, ordered by date");
		System.out.println("g: display songs of an album, ordered by genre");
		System.out.println("d: display songs of an album");
		System.out.println("u: display audiobooks ordered by author");
		System.out.println("c: add a new song");
		System.out.println("a: add a new album");
		System.out.println("+: add a song to an album");
		System.out.println("l: add a new audiobook");
		System.out.println("p: create a new playlist from existing songs and audio books");
		System.out.println("-: delete an existing playlist");
		System.out.println("s: save elements, albums, playlists");
		System.out.println("q: quit program");
	}
	
	public void connect(String ip)
	{
		int port = 6666;
        try  {
			//create the socket; it is defined by an remote IP address (the address of the server) and a port number
			socket = new Socket(ip, port);


			//create the streams that will handle the objects coming and going through the sockets
			output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            //MusicHubUser user = new MusicHubUser();
            Scanner scan = new Scanner(System.in);
			String choice = scan.nextLine();

			System.out.println("text sent to the server: " + choice);
			output.writeObject(choice);	//serialize and write the String to the stream

			MusicHubUser user = (MusicHubUser) input.readObject();	//deserialize and read the user object from the stream
			System.out.println(user.getAlbumsTitlesSortedByDate());

			while(choice.charAt(0) != 'q'){
				choice = scan.nextLine();
				System.out.println("text sent to the server: " + choice);
				output.writeObject(choice);
				user = (MusicHubUser) input.readObject();

				//user = (MusicHubUser) input.readObject();
				switch (choice.charAt(0)) 	{
					case 'h':
						printAvailableCommands();
						break;
					case 't':
						//album titles, ordered by date
						System.out.println(user.getAlbumsTitlesSortedByDate());
						break;

					case 'g':
						//display songs of an album, ordered by genre
						System.out.println("Songs of an album sorted by genre will be displayed; enter the album name, available albums are:");
						System.out.println(user.getAlbumsTitlesSortedByDate());

						System.out.println("Entrez le nom d\'un album");
						String albumTitle = scan.nextLine();
						try {
							System.out.println(user.getAlbumSongsSortedByGenre(albumTitle));
						} catch (NoAlbumFoundException ex) {
							System.out.println("No album found with the requested title " + ex.getMessage());
						}
						break;

					case 'd':
						//display songs of an album
						System.out.println("Songs of an album will be displayed; enter the album name, available albums are:");
						System.out.println(user.getAlbumsTitlesSortedByDate());

						albumTitle = scan.nextLine();
						try {
							System.out.println(user.getAlbumSongs(albumTitle));
						} catch (NoAlbumFoundException ex) {
							System.out.println("No album found with the requested title " + ex.getMessage());
						}
						break;

					case 'u':
						//display audiobooks ordered by author
						System.out.println(user.getAudiobooksTitlesSortedByAuthor());
						break;

					case 'c':
						//add a new song
						System.out.println("Enter a new song");
						System.out.println("Song title: ");
						String title = scan.nextLine();
						System.out.println("Song genre (jazz, classic, hiphop, rock, pop, rap):");
						String genre = scan.nextLine();
						System.out.println("Song artist: ");
						String artist = scan.nextLine();
						System.out.println ("Song length in seconds: ");
						int length = Integer.parseInt(scan.nextLine());
						System.out.println("Song content: ");
						String content = scan.nextLine();
						Song s = new Song (title, artist, length, content, genre);
						user.addElement(s);
						System.out.println("New element list: ");
						Iterator<AudioElement> it = user.elements();
						while (it.hasNext()) System.out.println(it.next().getTitle());
						System.out.println("Song created!");

						output.writeObject(user);
						break;


					case 'a':
						//add a new album
						System.out.println("Enter a new album: ");
						System.out.println("Album title: ");
						String aTitle = scan.nextLine();
						System.out.println("Album artist: ");
						String aArtist = scan.nextLine();
						System.out.println ("Album length in seconds: ");
						int aLength = Integer.parseInt(scan.nextLine());
						System.out.println("Album date as YYYY-DD-MM: ");
						String aDate = scan.nextLine();
						Album a = new Album(aTitle, aArtist, aLength, aDate);
						user.addAlbum(a);
						System.out.println("New list of albums: ");
						Iterator<Album> ita = user.albums();
						while (ita.hasNext()) System.out.println(ita.next().getTitle());
						System.out.println("Album created!");

						output.writeObject(user);
						break;


					case '+':
						//add a new audiobook
						System.out.println("Add an existing song to an existing album");
						System.out.println("Type the name of the song you wish to add. Available songs: ");
						Iterator<AudioElement> itae = user.elements();
						while (itae.hasNext()) {
							AudioElement ae = itae.next();
							if ( ae instanceof Song) System.out.println(ae.getTitle());
						}
						String songTitle = scan.nextLine();

						System.out.println("Type the name of the album you wish to enrich. Available albums: ");
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
						System.out.println("Song added to the album!");

						output.writeObject(user);
						break;


					case 'l':
						//create a new playlist from existing songs and audio books
						System.out.println("Enter a new audiobook: ");
						System.out.println("AudioBook title: ");
						String bTitle = scan.nextLine();
						System.out.println("AudioBook category (youth, novel, theater, documentary, speech)");
						String bCategory = scan.nextLine();
						System.out.println("AudioBook artist: ");
						String bArtist = scan.nextLine();
						System.out.println ("AudioBook length in seconds: ");
						int bLength = Integer.parseInt(scan.nextLine());
						System.out.println("AudioBook content: ");
						String bContent = scan.nextLine();
						System.out.println("AudioBook language (french, english, italian, spanish, german)");
						String bLanguage = scan.nextLine();
						AudioBook b = new AudioBook (bTitle, bArtist, bLength, bContent, bLanguage, bCategory);
						user.addElement(b);
						System.out.println("Audiobook created! New element list: ");
						Iterator<AudioElement> itl = user.elements();
						while (itl.hasNext()) System.out.println(itl.next().getTitle());

						output.writeObject(user);
						break;


					case 'p':
						//delete an existing playlist
						System.out.println("Add an existing song or audiobook to a new playlist");
						System.out.println("Existing playlists:");
						Iterator<PlayList> itpl = user.playlists();
						while (itpl.hasNext()) {
							PlayList pl = itpl.next();
							System.out.println(pl.getTitle());
						}
						System.out.println("Type the name of the playlist you wish to create:");
						String playListTitle = scan.nextLine();
						PlayList pl = new PlayList(playListTitle);
						user.addPlaylist(pl);
						System.out.println("Available elements: ");

						Iterator<AudioElement> itael = user.elements();
						while (itael.hasNext()) {
							AudioElement ae = itael.next();
							System.out.println(ae.getTitle());
						}
						while (choice.charAt(0)!= 'n') 	{
							System.out.println("Type the name of the audio element you wish to add or 'n' to exit:");
							String elementTitle = scan.nextLine();
							try {
								user.addElementToPlayList(elementTitle, playListTitle);
							} catch (NoPlayListFoundException ex) {
								System.out.println (ex.getMessage());
							} catch (NoElementFoundException ex) {
								System.out.println (ex.getMessage());
							}

							System.out.println("Type y to add a new one, n to end");
							choice = scan.nextLine();
						}
						System.out.println("Playlist created!");

						output.writeObject(user);
						break;


					case '-':
						//save elements, albums, playlists
						System.out.println("Delete an existing playlist. Available playlists:");
						Iterator<PlayList> itp = user.playlists();
						while (itp.hasNext()) {
							PlayList p = itp.next();
							System.out.println(p.getTitle());
						}
						String plTitle = scan.nextLine();
						try {
							user.deletePlayList(plTitle);
						}	catch (NoPlayListFoundException ex) {
							System.out.println (ex.getMessage());
						}
						System.out.println("Playlist deleted!");

						output.writeObject(user);
						break;


					case 's':
						//save elements, albums, playlists
						user.saveElements();
						user.saveAlbums();
						user.savePlayLists();
						System.out.println("Elements, albums and playlists saved!");

						output.writeObject(user);
						break;

					default:
						break;
				}
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