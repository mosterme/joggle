package joggle.data;

import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

/**
 * @author  $Author$
 * @version $Revision$
 */
public class DataTest extends TestCase {
	
	private static final Manager manager = Manager.getInstance();
	private static final String album = "Rap Trax";
	private static final String artist = "The Hate Noise";
	private static final String title = "Drunk Off Chicken";
	private static final String genre = "Hip Hop/Rap";
	private static final String ogg = "ogg";
	private static final String mp3 = "mp3";
	private static final String search = "oF";

	protected void setUp() throws Exception {
		String directory = "src/test/resources";
		Scanner scanner = new Scanner();
		scanner.scan(directory);
	}

	public void testAlbums() {
		List<String> l = manager.albums();
		assertNotNull(l);
		assertFalse(l.isEmpty());
	}

	public void testArtists() {
		List<String> l = manager.artists();
		assertNotNull(l);
		assertFalse(l.isEmpty());
	}
	
	public void testByAlbum() {
		List<Song> l = manager.byAlbum(album);
		assertNotNull(l);
		assertFalse(l.isEmpty());
		Song song = l.get(0);
		assertNotNull(song);
		assertEquals(album, song.getAlbum());
	}
	
	public void testByArtist() {
		List<Song> l = manager.byArtist(artist);
		assertNotNull(l);
		assertFalse(l.isEmpty());
		Song song = l.get(0);
		assertNotNull(song);
		assertEquals(artist, song.getArtist());
	}

	public void testByType() {
		List<Song> l = manager.byType(mp3);
		assertNotNull(l);
		assertFalse(l.isEmpty());
		Song song = l.get(0);
		assertNotNull(song);
		assertEquals(mp3, song.getType());
		l = manager.byType(ogg);
		assertNotNull(l);
		assertTrue(l.isEmpty());
	}
	
	public void testFind() {
		String sha1 = manager.byType(mp3).get(0).getId();
		Song song = manager.find(sha1);
		assertEquals(album, song.getAlbum());
		assertEquals(artist, song.getArtist());
		assertEquals(Integer.valueOf(6), song.getTrack());
		assertEquals(title, song.getTitle());
		assertEquals(genre, song.getGenre());
	}

	public void testSearch() {
		List<Song> l = manager.search(search);
		assertNotNull(l);
		assertFalse(l.isEmpty());
		Song song = l.get(0);
		assertNotNull(song);
		assertTrue(song.getTitle().toLowerCase().contains(search.toLowerCase()));
	}

	public void testSongs() {
		List<Song> l = manager.songs();
		assertNotNull(l);
		assertFalse(l.isEmpty());
	}

	public void testArtwork() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException {
		Manager manager = Manager.getInstance();
		Song song = manager.songs().get(0);
		if (song.getArtwork()) {
			File file = new File(song.getFile());
			AudioFile af = AudioFileIO.read(file); 
			Tag tag = af.getTag();
			Artwork aw = tag.getFirstArtwork();
			assertNotNull(aw);
			assertEquals("image/jpeg", aw.getMimeType());
		}
	}

	public void testGetProperty() {
		String property = manager.getProperty("joggle.persistence.unit");
		assertNotNull(property);
		assertEquals("jogglemem", property);
		assertNull(manager.getProperty("äöüß"));
	}
}
