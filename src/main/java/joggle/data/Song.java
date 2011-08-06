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
	private String file;

	@Override
	public String toString() {
		return Serializer.toString(this);
	}

	public Song() {
	}

	public Song(String id, String type, String artist, String album, Integer track, String title, String genre, String file) {
		this.id = id;
		this.type = type;
		this.artist = artist;
		this.album = album;
		this.track = track;
		this.title = title;
		this.genre = genre;
		this.file = file;
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
}
