package joggle.data;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  $Author$
 * @version $Revision$
 */
public class Scanner {

	private static final Logger log = LoggerFactory.getLogger(Scanner.class);
	private final FileFilter filter = new AudioFileFilter(true);
	private long bytes, directories, files;

	public void scan(String s) {
		log.info("Scanning {}", s);
		long t0 = System.currentTimeMillis();
		scan(new File(s));
		long t1 = System.currentTimeMillis();
		log.info("> Scan report for {}", s);
		log.info(">   audio files   {}", files);
		log.info(">   directories   {}", directories);
		log.info(">   scanned bytes {}", FileUtils.byteCountToDisplaySize(bytes));
		log.info(">   time elapsed  {} millis", t1 - t0);
	}

	public void scan(File d) {
		if (d.isDirectory()) {
			log.debug("scanning {}", d);
			File[] fa = d.listFiles(filter);
			for (File f : fa) scan(f);
			directories++;
		}
		else if (d.isFile()) {
			handle(d);
			files++; bytes += d.length();
		}
	}

	private void handle(File f) {
		try {
			AudioFile af = AudioFileIO.read(f); Tag tag = af.getTag();
			String type = af.getAudioHeader().getEncodingType().substring(0, 3).toLowerCase();
			if (tag != null) {
				Manager manager = Manager.getInstance();
				String artist = tag.getFirst(FieldKey.ARTIST);
				String album = tag.getFirst(FieldKey.ALBUM);
				String title = tag.getFirst(FieldKey.TITLE);
				String file = f.getAbsolutePath();
				String id = Serializer.hash(file);
				String t = tag.getFirst(FieldKey.TRACK);
				Integer track = t.matches("[0-9]+") ? Integer.parseInt(t) : null;
				String genre = tag.getFirst(FieldKey.GENRE);
				if (genre.matches("\\(?[0-9]{1,3}\\)?")) genre = manager.getGenre(genre.replace("(", "").replace(")", ""));
				String albumArtist = tag.getFirst(FieldKey.ALBUM_ARTIST);
				Boolean artwork = tag.getFirstArtwork() != null && tag.getFirstArtwork().getBinaryData() != null; // has artwork but no data?
				if (artwork) log.debug("Found artwork in {}", f);
				Song s = new Song(id, type, artist, album, track, title, genre, artwork, file, System.currentTimeMillis(), albumArtist);
				manager.merge(s);
			}
			else {
				log.warn("No meta tag found in {}", f);
			}
		}
		catch (Throwable t) {
			log.warn(t.getMessage());
			log.warn(f.getAbsolutePath());
		}
	}
}
