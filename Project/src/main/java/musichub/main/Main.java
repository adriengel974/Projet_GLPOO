package musichub.main;
import musichub.business.*;

public class Main
{
 	public static void main (String[] args) {

		AbstractServer as = new MusicHubServer();
		String ip = "localhost";
		as.connect(ip);
	}
}