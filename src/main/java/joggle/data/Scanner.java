package joggle.data;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;

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

	public void scan(String s) {
		log.info("scanning " + s);
		long t0 = System.currentTimeMillis();
		scan(new File(s));
		long t1 = System.currentTimeMillis();
		log.info("scanning " + s + " took " + (t1-t0) + " millis");
	}

	public void scan(File d) {
		if (d.isDirectory()) {
			if (log.isDebugEnabled()) log.debug("scanning " + d);
			File[] fa = d.listFiles(filter);
			for (File f : fa) scan(f);
		}
		else if (d.isFile()) {
			handle(d);
		}
	}

	private void handle(File f) {
		try {
			AudioFile af = AudioFileIO.read(f); Tag tag = af.getTag();
			String type = af.getAudioHeader().getEncodingType().substring(0, 3).toLowerCase();
			if (tag != null) {
				String artist = tag.getFirst(FieldKey.ARTIST); 
				String album = tag.getFirst(FieldKey.ALBUM);
				String title = tag.getFirst(FieldKey.TITLE);
				String file = f.getAbsolutePath();
				String id = Serializer.hash(file);
				String t = tag.getFirst(FieldKey.TRACK);
				Integer track = t.matches("[0-9]+") ? Integer.parseInt(t) : null ;
				String g = tag.getFirst(FieldKey.GENRE);
				String genre = g.length() > 0 ? g : null;
				if (brokenEncoding(artist,f)) artist = fixEncoding(artist);
				if (brokenEncoding(album,f)) album = fixEncoding(album);
				if (brokenEncoding(title,f)) title = fixEncoding(title);
				Boolean artwork = tag.getFirstArtwork() != null && tag.getFirstArtwork().getBinaryData() != null; // has artwork but no data?
				if (artwork) log.info("Found artwork in " + f);
				Song s = new Song(id, type, artist, album, track, title, genre, artwork, file, System.currentTimeMillis());
				Manager.getInstance().merge(s);
			}
			else {
				log.warn("No meta tag found " + f);
			}
		}
		catch (Exception e) {
			log.warn(e.getMessage());
			log.warn(f.getAbsolutePath());
		}
	}

	// TODO: improve this encoding repair... or use a better tag library =)
	private boolean brokenEncoding (String s, File f) {
		boolean b = s.contains("Ã");
		if (b) log.info("Non utf-8 header in " + f.getAbsolutePath());
		return b;
	}

	private String fixEncoding(String s) throws UnsupportedEncodingException {
		String r = new String(s.getBytes("iso-8859-1"), "utf-8");
		if (log.isDebugEnabled()) log.debug("fixEncoding: " + s + " -> " + r);
		return r; 
	}
}
