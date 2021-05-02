package musichub.main;
import musichub.business.*;
import java.util.*;

public class Main
{
 	public static void main (String[] args) {
		AbstractServer as = new MusicHubServer();
		String ip = "localhost";
		as.connect(ip);
	}
}