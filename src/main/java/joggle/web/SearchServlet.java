package joggle.web;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import joggle.data.Manager;
import joggle.data.Serializer;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  $Author$
 * @version $Revision$
 */
public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = 1146058744384688900L;
	private static final Logger log = LoggerFactory.getLogger(SearchServlet.class);
	private static final Manager manager = Manager.getInstance();

	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long t0 = System.currentTimeMillis();
		String callback = request.getParameter("$callback");
		String url = Serializer.decode(request.getRequestURI());
		List list = null;
		if (url.contains("/type/")) {
			String type = FilenameUtils.getName(url);
			list = manager.byType(type);
		}
		else if (url.contains("/artist/")) {
			String artist = FilenameUtils.getName(url);
			if ("".equals(artist)) list = manager.artists();
			else list = manager.byArtist(artist);
		}
		else if (url.contains("/album/")) {
			String album = FilenameUtils.getName(url);
			if ("".equals(album)) list = manager.albums();
			else list = manager.byAlbum(album);
		}
		else if (url.contains("/song/")) {
			String song = FilenameUtils.getName(url);
			if ("".equals(song)) list = manager.songs();
			else list = Collections.singletonList(manager.find(song));
		}
		else {
			String keywords = url.split("/")[3];
			list = manager.search(keywords);
		}
		String result = "{\"d\":" + Serializer.toJson(list) + "}";
		if (callback != null) result = callback + "(" + result + ")";
		response.setCharacterEncoding("utf-8");
		Writer out = response.getWriter(); out.write(result);
		response.setStatus(HttpServletResponse.SC_OK);
		long t1 = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			String string = "search: " + format(url, 40) + " duration: " + (t1 - t0) + "ms" + "\tresults: " + list.size();
			if (log.isDebugEnabled()) string += "\tcallback: " + callback;
			log.info(string);
		}
	}

	private static String format(String s, int m) {
		if (log.isDebugEnabled()) return s;
		String r = s.replaceFirst("/joggle/search/([^/]+)/(.+)$", "$1..$2")
		.replaceFirst("^album", "album.")
		.replaceFirst("^type",  "type..")
		+ "............................";
		return r.substring(0, m - 1) + ".";
	}
}
