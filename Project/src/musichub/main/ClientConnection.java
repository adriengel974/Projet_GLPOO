package musichub.main;

import musichub.business.*;

public class ClientConnection
{
	public static void main (String[] args)
	{
		MusicHubClient c1 = new MusicHubClient();
		c1.connect("localhost");
	}
}