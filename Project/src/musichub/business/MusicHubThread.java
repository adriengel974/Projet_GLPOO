package musichub.business;

import java.io.*;
import java.net.*;
	
public class MusicHubThread extends Thread implements Colors{
	private final Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;

	public MusicHubThread(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());

			Object user = new MusicHubUser(); //serialize and write the MusicHubUser object to the stream
			output.writeObject(user);

			String choice = (String)input.readObject(); //read the object received through the stream and deserialize it
			if (choice.length() == 0) System.exit(0);
			System.out.println(GREEN + "server received a text: " + DEFAULT + choice);

			 while(choice.charAt(0) != 'q'){
				choice = (String) input.readObject();
				System.out.println(GREEN + "server received a text: " + DEFAULT + choice);
				output.writeObject(user);
				System.out.println(RED + "The object was sent to the client" + DEFAULT);

				switch (choice.charAt(0)) 	{
					case 'h': //display available commands
					case 't': //display the album titles, ordered by date
					case 'u': //display audiobooks ordered by author
					case 'd': //display songs of an album
					case 'g': //display songs of an album, ordered by genre
						System.out.println(YELLOW +"Display command" + DEFAULT);
						break;

					case '-': //delete an existing playlist
					//case 's': //save elements, albums, playlists
					case 'c': //add a new song
					case 'a': //add a new album
					case '+': //add a song to an album
					case 'p': //create a new playlist from existing songs and audio books
					case 'l': //add a new audiobook
						user = input.readObject();
						System.out.println(PURPLE + "New object received from the client" + DEFAULT);
						break;

					case 'q': //quitting the server
						System.out.println("Client disconnected");
					default:
						System.out.println("Unknown command");
						break;
				}
			}

		} catch (IOException | ClassNotFoundException ex) {
			System.out.println(RED + "Server exception: " + DEFAULT + ex.getMessage());
			ex.printStackTrace();

		} finally {
			try {
				output.close();
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}
}