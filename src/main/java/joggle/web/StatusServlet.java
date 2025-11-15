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
        SimpleDateFormat df = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
		result.put("application.revision", manager.getProperty("joggle.application.revision"));
		result.put("application.timestamp", manager.getProperty("joggle.application.timestamp"));
		result.put("application.version", manager.getProperty("joggle.application.version"));
		result.put("manager.albums", i);
		result.put("manager.artists", j);
		result.put("manager.songs", k);
		Runtime rt = Runtime.getRuntime();
		result.put("memory.free", FileUtils.byteCountToDisplaySize(rt.freeMemory()));
		result.put("memory.max", FileUtils.byteCountToDisplaySize(rt.maxMemory()));
		result.put("memory.total", FileUtils.byteCountToDisplaySize(rt.totalMemory()));
		RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
		result.put("vm.started", df.format(rb.getStartTime()));
		result.put("vm.uptime", rb.getUptime()); // TODO: format this
		// OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
		// result.put("system.load", os.getSystemLoadAverage());
		return result;
	}
}
