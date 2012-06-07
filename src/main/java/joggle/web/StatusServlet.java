package joggle.web;

import java.io.IOException;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import joggle.data.Manager;
import joggle.data.Serializer;

import org.apache.commons.io.FileUtils;

/**
 * @author  $Author$
 * @version $Revision$
 */
public class StatusServlet extends HttpServlet {

	private static final long serialVersionUID = 3640618359057072513L;
	private static final SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss");

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Writer out = response.getWriter();
		out.write(Serializer.toJson(status())); 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Map status() {
		Manager manager = Manager.getInstance();
		int i = manager.albums().size(), j = manager.artists().size(), k = manager.songs().size();
		Map result = new TreeMap();
		RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
		Runtime rt = Runtime.getRuntime();
		result.put("vmStartTime", df.format(rb.getStartTime()));
		result.put("vmUptime", rb.getUptime()); // TODO: format this 
		result.put("memTotal", FileUtils.byteCountToDisplaySize(rt.totalMemory()));
		result.put("memFree", FileUtils.byteCountToDisplaySize(rt.freeMemory()));
		result.put("memMax", FileUtils.byteCountToDisplaySize(rt.maxMemory()));
		result.put("jgAlbums", i);
		result.put("jgArtists", j);
		result.put("jgSongs", k);
		return result;
	}
}
