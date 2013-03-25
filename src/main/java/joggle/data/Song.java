package joggle.data;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.google.gson.annotations.Expose;

@Entity
public class Song implements Serializable {

	private static final long serialVersionUID = 1250923664972575134L;

	@Expose private String id;
	@Expose private String type;
	@Expose private String artist;
	@Expose private String album;
	@Expose private Integer track;
	@Expose private String title;
	@Expose private String genre;
	private Boolean artwork;
	private String file;
	private Long modified;
	private String albumArtist;

	public Song() {
	}

	public Song(String id, String type, String artist, String album, Integer track, String title, String genre, Boolean artwork, String file, Long modified, String albumArtist) {
		this.id = id;
		this.type = type;
		this.artist = artist;
		this.album = album;
		this.track = track;
		this.title = title;
		this.genre = genre;
		this.artwork = artwork;
		this.file = file;
		this.modified = modified;
		this.albumArtist = albumArtist;
	}

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Integer getTrack() {
		return track;
	}

	public void setTrack(Integer track) {
		this.track = track;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public Boolean getArtwork() {
		return artwork;
	}

	public void setArtwork(Boolean artwork) {
		this.artwork = artwork;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public String getAlbumArtist() {
		return albumArtist;
	}

	public void setAlbumArtist(String albumArtist) {
		this.albumArtist = albumArtist;
	}

	@Override
	public String toString() {
		return Serializer.toString(this);
	}
}
