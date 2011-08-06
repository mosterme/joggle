package joggle.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unchecked")
public class Manager {

	private static final Logger log = LoggerFactory.getLogger(Manager.class);
	private static final EntityManager manager = Persistence.createEntityManagerFactory("jogglemem").createEntityManager();
	private static final Manager instance = new Manager();

	private static final String aat = " order by s.artist, s.album, s.track";
	private static final String ata = " order by s.album, s.track, s.artist";

	private Manager() {
	}

	public static Manager getInstance() {
		return instance;
	}

	public List<Song> list() {
		return manager.createQuery("select s from Song as s" + aat).getResultList();
	}

	public List<Song> byType(String type) {
		return manager.createQuery("select s from Song as s where s.type = ?" + aat).setParameter(1, type).getResultList();
	}

	public List<Song> byArtist(String artist) {
		return manager.createQuery("select s from Song as s where s.artist = ?" + aat).setParameter(1, artist).getResultList();
	}

	public List<Song> byAlbum(String album) {
		return manager.createQuery("select s from Song as s where s.album = ?" + ata).setParameter(1, album).getResultList();
	}
	
	public List<Song> search(String keyword) {
		String search = "%" + keyword.trim().toLowerCase() + "%";
		if (log.isDebugEnabled()) log.debug("searching " + search);
		log.info("searching " + search);
		return manager.createQuery("select s from Song as s where lower(s.album) like :search or lower(s.artist) like :search or lower(s.title) like :search" + aat).setParameter("search", search).getResultList();
	}

	public List<String> artists() {
		return manager.createQuery("select distinct(s.artist) from Song as s order by s.artist").getResultList();
	}

	public List<String> albums() {
		return manager.createQuery("select distinct(s.album) from Song as s order by s.album").getResultList();
	}

	public void merge(Song s) {
		if (log.isDebugEnabled()) log.debug("merging " + s);
		manager.getTransaction().begin();
		manager.merge(s);
		manager.getTransaction().commit();
	}

	public Song find(String id) {
		return manager.find(Song.class, id);
	}
}
