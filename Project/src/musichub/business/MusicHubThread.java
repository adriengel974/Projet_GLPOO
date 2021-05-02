package musichub.business;

import java.io.*;
import java.net.*;
	
public class MusicHubThread extends Thread{
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
			System.out.println("server received a text: " + choice);

			 do {
				choice = (String) input.readObject();
				System.out.println("server received a text: " + choice);
				output.writeObject(user);

				switch (choice.charAt(0)) 	{

					case 'g': //display songs of an album, ordered by genre


					case '-': //delete an existing playlist


					//case 's': //save elements, albums, playlists


					case 'c': //add a new song


					case 'a': //add a new album


					case '+': //add a song to an album


					case 'p': //create a new playlist from existing songs and audio books


					case 'l': //add a new audiobook
						user = input.readObject();
						break;

					default:
						break;
				}
			}while(choice.charAt(0) != 'q');

		} catch (IOException | ClassNotFoundException ex) {
			System.out.println("Server exception: " + ex.getMessage());
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