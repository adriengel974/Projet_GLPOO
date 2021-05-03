
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import musichub.business.*;
import musichub.util.XMLHandler;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class TestsAlbumXMLHandler {

	// On vérifie les différents constructeur de Album en comparant les valeurs attendus et les valeurs récupérées avec les différentes méthodes, ce qui permet de tester les méthodes par la même occasion
	@Test
	public void testConstructeurAlbum1() {
		UUID uuidalbum = UUID.randomUUID();
		Album albumtest = new Album("TitreAlbum", "Mika", 800, uuidalbum.toString(), "2012-08-08",new ArrayList<>());
		assertEquals("TitreAlbum", albumtest.getTitle());
		assertEquals("Mika", albumtest.getArtist());
		assertEquals(800, albumtest.getLengthInSeconds());
		assertEquals("2012-08-08", new SimpleDateFormat("2012-08-08").format(albumtest.getDate()));
		assertEquals(uuidalbum, albumtest.getUuid());
		assertTrue(albumtest.getSongs().isEmpty());

	}
	@Test
	public void testConstructeurAlbum2() {
		Album albumtest = new Album("TitreAlbum", "Mika", 800, "2012-08-08");
		assertEquals("TitreAlbum", albumtest.getTitle());
		assertEquals("Mika", albumtest.getArtist());
		assertEquals("2012-08-08", new SimpleDateFormat("2012-08-08").format(albumtest.getDate()));
		assertEquals(800, albumtest.getLengthInSeconds());
		assertTrue(albumtest.getSongs().isEmpty());

	}

	@Test
	public void testConstructeurXmlAlbum() throws Exception {
		XMLHandler xmlHandlertest = new XMLHandler();
		NodeList albumNodestest = xmlHandlertest.parseXMLFile("testfiles/albumtest.xml");
		assertNotNull(albumNodestest);
		Album albumtest = readAlbumFromXML(albumNodestest);
		assertEquals("Best of Beatles", albumtest.getTitle());
		assertEquals("The Beatles", albumtest.getArtist());
		assertEquals(2160, albumtest.getLengthInSeconds());
		assertEquals(3, albumtest.getSongs().size());
		assertEquals(new UUID(0,0), albumtest.getUuid());
		assertEquals("1970-12-10", new SimpleDateFormat("1970-12-10").format(albumtest.getDate()));

	}

	@Test
	public void testAlbumRandomSong() throws Exception {
		XMLHandler xmlHandler = new XMLHandler();
		NodeList albumNodes = xmlHandler.parseXMLFile("testfiles/albumtest.xml");
		assertNotNull(albumNodes);
		Album albumtest = readAlbumFromXML(albumNodes);
		assertTrue(albumtest.getSongs().containsAll(albumtest.getSongsRandomly()));
	}
// On crée puis on lit dans un fichier XML afin de le comparer à l'album précédement crée
	@Test
	public void testCreateXMLElement() throws Exception {
		XMLHandler xmlHandlertest = new XMLHandler();
		Document documenttest = xmlHandlertest.createXMLDocument();
		Element root = documenttest.createElement("albums");
		documenttest.appendChild(root);
		UUID uuidtest = UUID.randomUUID();
		ArrayList<UUID> songstest = new ArrayList<>();
		songstest.add(UUID.randomUUID());
		Album albumtest = new Album("TitreAlbum", "Mika", 800,uuidtest.toString(), "2012-08-08",songstest);
		albumtest.createXMLElement(documenttest, root);
		Album albumtest2 = readAlbumFromXML(root.getChildNodes());
		assertEquals(albumtest.getTitle(),albumtest2.getTitle());
		assertEquals(albumtest.getArtist(), albumtest2.getArtist());
		assertEquals(albumtest.getLengthInSeconds(), albumtest2.getLengthInSeconds());
		assertEquals(albumtest.getDate(), albumtest2.getDate());
		assertEquals(albumtest.getSongs().size(),albumtest2.getSongs().size());
		assertEquals(albumtest.getSongs().get(0),albumtest2.getSongs().get(0));


	}

// La boucle va parcourir l'album et à chaque fois qu'elle tombe sur un album elle passe au suivant si se dernier existe ça continue sinon ça retourne une exception
	public static Album readAlbumFromXML(NodeList albumNodes) throws Exception {
			for (int k = 0; k < albumNodes.getLength(); k++) {
				if (albumNodes.item(k).getNodeType() == Node.ELEMENT_NODE) {
					Element albumElementtest = (Element) albumNodes.item(k);
					if (albumElementtest.getNodeName().equals("album")) {
						return new Album(albumElementtest);
					}
				}
			}
			throw new Exception("L'album n'a pas été trouvé");
	}
}
