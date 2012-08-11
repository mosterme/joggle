package joggle.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;
import java.util.logging.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  $Author$
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public class Manager {

	private static final Logger log = LoggerFactory.getLogger(Manager.class);
	private static final Properties properties = new Properties();

	static {
		InputStream stream = Manager.class.getResourceAsStream("/logging.properties");
		try { LogManager.getLogManager().readConfiguration(stream); }
		catch (Exception e) { log.warn(e.getMessage()); }
		finally { IOUtils.closeQuietly(stream); }

		stream = Manager.class.getResourceAsStream("/joggle.properties");
		try { properties.load(stream); log.info("Found joggle.properties in classpath"); }
		catch (IOException e) { log.warn(e.getMessage()); }
		finally { IOUtils.closeQuietly(stream); }

		for (String s : new TreeSet<String>(properties.stringPropertyNames())) {
			log.info("> " + s + "=" + properties.get(s));
		}
	}

	private static final EntityManager manager = Persistence.createEntityManagerFactory(properties.getProperty("joggle.persistence.unit")).createEntityManager();
	private static final Manager instance = new Manager();

	private static final String aat = " order by s.artist, s.album, s.track";
	private static final String ata = " order by s.album, s.track, s.artist";

	public static Manager getInstance() {
		return instance;
	}

	private Manager() {
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public List<String> albums() {
		return manager.createQuery("select distinct(s.album) from Song as s order by s.album").getResultList();
	}

	public List<String> artists() {
		return manager.createQuery("select distinct(s.artist) from Song as s order by s.artist").getResultList();
	}

	public List<Song> byAlbum(String album) {
		return manager.createQuery("select s from Song as s where s.album = :q" + ata).setParameter("q", album).getResultList();
	}

	public List<Song> byArtist(String artist) {
		return manager.createQuery("select s from Song as s where s.artist = :q" + aat).setParameter("q", artist).getResultList();
	}

	public List<Song> byType(String type) {
		return manager.createQuery("select s from Song as s where s.type = :q" + aat).setParameter("q", type).getResultList();
	}

	public Song find(String id) {
		return manager.find(Song.class, id);
	}

	public void merge(Song s) {
		if (log.isDebugEnabled()) log.debug("merging " + s);
		manager.getTransaction().begin();
		manager.merge(s);
		manager.getTransaction().commit();
	}

	public List<Song> search(String keyword) {
		String search = "%" + keyword.trim().toLowerCase() + "%";
		if (log.isDebugEnabled()) log.debug("searching " + search);
		return manager.createQuery("select s from Song as s where lower(s.album) like :q or lower(s.artist) like :q or lower(s.title) like :q" + aat).setParameter("q", search).getResultList();
	}

	public List<Song> songs() {
		return manager.createQuery("select s from Song as s" + aat).getResultList();
	}
}
