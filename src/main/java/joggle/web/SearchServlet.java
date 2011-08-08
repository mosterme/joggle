package joggle.web;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import joggle.data.Manager;
import joggle.data.Serializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1146058744384688900L;
	private static final Logger log = LoggerFactory.getLogger(SearchServlet.class);
	private static final Manager manager = Manager.getInstance();

	
	@Override @SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long t0 = System.currentTimeMillis();
		String callback = request.getParameter("$callback");
		String url = Serializer.decode(request.getRequestURI());
		log.info("request:  " + url + ", callback: " + callback);
		List list = null;
		if (url.contains("/type/")) {
			String type = substringAfter(url, "/type/");
			list = manager.byType(type);
		}
		else if (url.contains("/artist/")) {
			String artist = substringAfter(url, "/artist/");
			if (isEmpty(artist)) list = manager.artists();
			else list = manager.byArtist(artist);
		}
		else if (url.contains("/album/")) {
			String album = substringAfter(url, "/album/");
			if (isEmpty(album)) list = manager.albums();
			else list = manager.byAlbum(album);
		}
		else {
			String keywords = url.split("/")[3];
			list = manager.search(keywords);
		}
		String result = callback + "({\"d\":" + Serializer.toJson(list) + "})";
		response.setCharacterEncoding("utf-8");
		Writer out = response.getWriter(); out.write(result); 
		response.setStatus(HttpServletResponse.SC_OK);
		long t1 = System.currentTimeMillis();
		log.info("callback: " + callback + ", results: " + list.size() + ", duration: " + (t1 - t0) + "ms");
	}
	
	private static boolean isEmpty(String s) {
		return s == null || s.length() == 0 || s.trim().length() == 0;
	}

	private static String substringAfter(String s, String a) {
		if (isEmpty(s)) return s; if (a == null) return "";
		int i = s.indexOf(a); if (i == -1) return "";
		return s.substring(i + a.length());
	}
}
