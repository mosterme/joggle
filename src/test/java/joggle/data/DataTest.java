package joggle.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.LogManager;

import junit.framework.TestCase;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataTest extends TestCase {

	Logger log = LoggerFactory.getLogger(DataTest.class);

	protected void setUp() throws Exception {
		LogManager.getLogManager().readConfiguration(Manager.class.getResourceAsStream("/logging.properties"));
	}

	public void testScanner() throws Exception {
		log.info("test Scanner");
		String directory = "src/test/resources";
		Scanner scanner = new Scanner();
		scanner.scan(directory);
	}

	public void testManager() {
		log.info("test Manager");
		Manager manager = Manager.getInstance();
		List<Song> list = manager.list();
		for (Song s : list) log.info(s.toString());
		assertNotNull(list);
		assertEquals(1, list.size());
		log.info("test by type");
		list = manager.byType("mp3");
		for (Song s : list) log.info(s.toString());
		assertNotNull(list);
		assertEquals(1, list.size());
		Song song = list.get(0);
		assertNotNull(song);
		assertEquals("mp3", song.getType());
		String sha1 = song.getId();
		log.info("test by id");
		song = manager.find(sha1);
		log.info(song.toString());
		assertEquals("Rap Trax", song.getAlbum());
		assertEquals("The Hate Noise", song.getArtist());
		assertEquals(new Integer(6), song.getTrack());
		assertEquals("Drunk Off Chicken", song.getTitle());
		assertEquals("Hip Hop/Rap", song.getGenre());
		log.info("test by album");
		list = manager.byAlbum("Rap Trax");
		for (Song s : list) log.info(s.toString());
		assertNotNull(list);
		assertEquals(1, list.size());
		log.info("test by artist");
		list = manager.byArtist("The Hate Noise");
		for (Song s : list) log.info(s.toString());
		assertNotNull(list);
		assertEquals(1, list.size());
		log.info("test list all albums");
		List<String> albums = manager.albums();
		log.info(Serializer.toJson(albums));
		log.info("test list all artists");
		List<String> artists = manager.artists();
		log.info(Serializer.toJson(artists));
	}

	public void testSerializer() throws NoSuchAlgorithmException, IOException {
		log.info("test Serializer");
		Song snull = new Song();
		String json = Serializer.toJson(snull);
		assertNotNull(json);
		assertEquals("{}", json);
		String string = Serializer.toString(snull);
		assertNotNull(string);
		assertEquals("Song{\"id\":null,\"type\":null,\"artist\":null,\"album\":null,\"track\":null,\"title\":null,\"genre\":null,\"artwork\":null,\"file\":null}",string);
		String sha1s = Serializer.sha1(string);
		assertNotNull(sha1s);
		assertEquals("C2880CC3B54B99F0AAD94FCDCA1974C2BF5D8B9F", sha1s);
		String sha1j = Serializer.sha1(json);
		assertNotNull(sha1j);
		assertEquals("BF21A9E8FBC5A3846FB05B4FA0859E0917B2202F", sha1j);
		assertNull(Serializer.hex(null));
		assertEquals("BJORK", Serializer.normalize("Björk"));
		assertNull(Serializer.decode(null));
		assertEquals("R H P", Serializer.decode("R+H%20P"));
		ByteArrayInputStream bis = new ByteArrayInputStream(string.getBytes());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Serializer.copy(bis, bos);
		assertEquals(string, bos.toString());
	}

	public void testArtwork() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		log.info("test Artwork");
		Manager manager = Manager.getInstance();
		Song song = manager.list().get(0);
		File file = new File(song.getFile());
		AudioFile af = AudioFileIO.read(file); 
		Tag tag = af.getTag();
		Artwork aw = tag.getFirstArtwork();
		assertNotNull(aw);
		assertEquals("image/jpeg", aw.getMimeType());
	}
}
