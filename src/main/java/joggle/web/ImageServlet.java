package joggle.web;

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
		long t0 = System.currentTimeMillis(); File file = null;
		String id = request.getRequestURI().split("/")[3]; // TODO: muahahaha
		Song song = manager.find(id);
		if (song != null) {
			File directory = new File(song.getFile()).getParentFile();
			File[] files = directory.listFiles(filter);
			if (files != null && files.length > 0) {
				file = files[0]; response.setStatus(HttpServletResponse.SC_OK);
			}
		}
		else {
			log.info("song not found: " + id);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		
		if (file != null) {
			String name = file.getName();
			String type = name.substring(name.lastIndexOf('.') + 1);
			if (type.startsWith("jp")) type = "jpeg";
			String mime = "image/" + type;
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
		long t1 = System.currentTimeMillis();
		log.info("request: " + (file != null ? id : redirect) + ", duration: " + (t1 - t0) + "ms");
	}
}
