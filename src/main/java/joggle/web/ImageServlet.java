package joggle.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import joggle.data.Manager;
import joggle.data.Serializer;
import joggle.data.Song;

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

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 5693065655963128553L;
	private static final Logger log = LoggerFactory.getLogger(ImageServlet.class);
	private static final Manager manager = Manager.getInstance();
	private static final String redirect = "/joggle/img/128/junk.png";

	private static final FilenameFilter filter = new FilenameFilter() {
		private final String[] suffixes = {".png", ".jpg", ".jpeg", ".jpe", ".gif"};
		public boolean accept(File dir, String name) {
			for (String s : suffixes) if (name.toLowerCase().endsWith(s)) return true;
			return false;
		}
	};

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long t0 = System.currentTimeMillis();
		String id = request.getRequestURI().split("/")[3]; // TODO: muahahaha
		Song song = manager.find(id);
		if (song != null) {
			if (song.getArtwork()) {
				if (log.isDebugEnabled()) log.debug("song has embedded artwork");
				try {
					AudioFile af = AudioFileIO.read(new File(song.getFile())); Tag tag = af.getTag();
					Artwork aw = tag.getFirstArtwork(); byte[] ba = aw.getBinaryData();
					if (ba == null) log.warn(Serializer.toString(aw));
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType(aw.getMimeType());
					response.setContentLength(ba.length);
					Serializer.copy(new ByteArrayInputStream(ba), response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
				} 
				catch (Exception e) {
					e.printStackTrace();
				} 
			}
			else {
				if (log.isDebugEnabled()) log.debug("search in directory");
				File directory = new File(song.getFile()).getParentFile();
				File[] files = directory.listFiles(filter);
				if (files != null && files.length > 0) {
					File file = files[0];
					String name = file.getName();
					String type = name.substring(name.lastIndexOf('.') + 1);
					if (type.startsWith("jp")) type = "jpeg";
					String mime = "image/" + type;
					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType(mime);
					response.setContentLength((int) file.length());
					Serializer.copy(new FileInputStream(file), response.getOutputStream());
					response.getOutputStream().flush();
					response.getOutputStream().close();
				} 
				else {
					if (log.isDebugEnabled()) log.debug("image not found: " + id + ", sending redirect: " + redirect);
					response.sendRedirect(redirect);
				}
			}
		}
		else {
			log.info("song not found: " + id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		long t1 = System.currentTimeMillis();
		log.info("request: " + id + ", duration: " + (t1 - t0) + "ms");
	}
}
