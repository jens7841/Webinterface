package de.clashofcubes.webinterface;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.navigation.NavItem;
import de.clashofcubes.webinterface.navigation.NavType;
import de.clashofcubes.webinterface.pagemanagement.PageManager;
import de.clashofcubes.webinterface.pagemanagement.pages.HomePage;
import de.clashofcubes.webinterface.pagemanagement.pages.ManagementPage;
import de.clashofcubes.webinterface.pagemanagement.pages.ServerPage;
import de.clashofcubes.webinterface.servermanagement.Server;
import de.clashofcubes.webinterface.servermanagement.ServerManager;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager;
import de.clashofcubes.webinterface.servermanagement.serverfiles.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.templates.ServerTemplateManager;
import de.clashofcubes.webinterface.servermanagement.versions.Version;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroup;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroupManager;
import de.clashofcubes.webinterface.usernamangement.login.LoginManager;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
		maxFileSize = 1024 * 1024 * 100, // 10MB
		maxRequestSize = 1024 * 1024 * 500) // 50MB
public class Webinterface extends HttpServlet implements ServletContextListener {

	private static final long serialVersionUID = 1L;

	public static final String LOGIN_USERNAME_ATTRIBUTE = "login.username";
	public static final String LOGIN_ATTRIBUTE = "login";
	public static final String TITLE_ATTRIBUTE = "title";
	public static final String NAVIGATION_ATTRIBUTE = "navigation";
	public static final String CURRENTPAGE_ATTRIBUTE = "currentpage";
	public static final String BODY_FILE_ATTRIBUTE = "bodyFile";
	public static final String STYLE_LIST_ATTRIBUTE = "styles";
	public static final String PAGE_ERROR_MESSAGE_ATTRIBUTE = "pageerrormessage";
	public static final String LAST_PAGE_ATTRIBUTE = "lastvisitedpage";

	public static final String DEFAULT_TEMPLATE_FILE = "/WEB-INF/webinterface/templateIndex.jsp";
	public static final String DEFALT_TITLE_PREFIX = " - ClashofCubes Webinterface";

	private static final List<NavItem> navigation = new ArrayList<>();
	public static final String URL = "wi";

	private static PageManager pageManager;
	private static ServerFileManager serverFileManager;
	private static ServerManager serverManager;
	private static LoginManager loginManager;
	private static VersionGroupManager versionGroupManager;
	private static ServerTemplateManager serverTemplateManager;

	/*
	 * In der "VersionGroup" befinden sich ne Liste von "Version"en von Servern
	 * die im prinzip das selbe machen (z.B. alle Bukkit versionen, alle spigot
	 * versionen...).
	 * 
	 * Eine "Version" enhält sozusagen eine "ServerFile" mit extras die man so
	 * braucht
	 * 
	 * 
	 * Ein "ServerTemplate" enthält einen Ordner indem sich Dateien und Ordner
	 * befinden können. Diese werden bei benutzen dieses Templates in den
	 * ServerOrdner kopiert. Dieses "ServerTemplate" kann mehrere "VersionGroup"
	 * enthalten zu denen es zugeordnet ist. Dies hat einfluss, wo es überall
	 * verwendet werden kann. z.b. ein Survivalgames template, dass bei der
	 * Bukkit und Spigot VersionsGruppe benutzt werden kann. Wird entsprechen
	 * als auswahl auf der Seite angezeigt.
	 * 
	 * TemplateManager über den man Templates bekommen kann, der sie speichert
	 * und tolle sachen damit macht.
	 * 
	 * 
	 * 
	 */

	public Webinterface() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		boolean b = false;
		if (b)
			return;

		if (request.getSession().getAttribute("login") != null) {
			request.setAttribute(LOGIN_USERNAME_ATTRIBUTE,
					request.getSession().getAttribute(LOGIN_USERNAME_ATTRIBUTE).toString());
		}

		// Navigation
		sendNavigation(request);

		// Page
		pageManager.loadPage(request, response);
	}

	private void sendNavigation(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		String currentpage = "";
		if (pathInfo != null) {
			String[] split = pathInfo.split("/");
			for (String u : split) {
				if (!u.isEmpty()) {
					currentpage += u + "/";
				}
			}
		}

		request.setAttribute(CURRENTPAGE_ATTRIBUTE, currentpage);
		request.setAttribute(NAVIGATION_ATTRIBUTE, navigation);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		// add Pages
		pageManager = new PageManager();
		pageManager.addPage(new HomePage());
		pageManager.addPage(new ManagementPage());
		pageManager.addPage(new ServerPage());

		// aktiviere Login Manager
		loginManager = new LoginManager(new File("users.csv"));
		try {
			loginManager.readUsersFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// aktiviere Server File Manager
		String serverFilesPath = "/META-INF/serverFiles/";
		serverFileManager = new ServerFileManager(new File("serverFiles.csv"),
				new File(sce.getServletContext().getRealPath(serverFilesPath)));
		serverFileManager.loadData();

		// TEST vom ServerFileManager
		// ServerFile Spigot 1.8
		String fileName = "spigot-1.8.8-R0.1.jar";
		ServerFile serverFile = new ServerFile("Spigot 1.8",
				new File(serverFileManager.getRootFolder().getAbsolutePath() + "/Spigot/Spigot1.8/" + fileName));
		try {
			serverFileManager.addFile(serverFile);
		} catch (ServerException e) {

		}
		// aktiviere versionGroupManager
		versionGroupManager = new VersionGroupManager(new File("versionGroups.csv"), serverFileManager);
		versionGroupManager.loadVersionGroups();

		// TEST vom VersionGroupManager
		// VersionGroup Spigot mit der Spigot 1.8 Versions (+ServerFile) drin
		VersionGroup versionGroup = new VersionGroup("Spigot");
		versionGroup.addVersion(new Version("Spigot 1.8", serverFile));
		versionGroupManager.addVersionGroup(versionGroup);

		// aktiviere ServerTemplateManager
		serverTemplateManager = new ServerTemplateManager(new File("templates.csv"),
				new File(sce.getServletContext().getRealPath("META-INF/serverFiles/")), versionGroupManager);

		// aktiviere ServerManager
		serverManager = new ServerManager(new File("servers.csv"), new File("F:/Eclipse/BukkitTest"),
				versionGroupManager, serverTemplateManager);

		serverManager.loadServers();

		// TEST vom ServerManager
		// add Server Test mit Spigot 1.8
		try {
			serverManager.addServer(new Server("Test", new File("F:/Eclipse/BukkitTest"),
					versionGroupManager.getVersion("Spigot 1.8"), null, "", "stop", false), true, true);
		} catch (ServerException e) {
		}
		// aktualisiere Navigation
		reloadNavigation();
	}

	public static void reloadNavigation() {
		navigation.clear();
		navigation.add(new NavItem("Home", URL, "", NavType.ENTRY, "home"));
		navigation.add(new NavItem("Status", URL, "/status", NavType.ENTRY));

		navigation.add(new NavItem("Serverliste", URL, "/verwaltung/serverlist", NavType.ENTRY));

		navigation.add(new NavItem("Hinzuf&uuml;gen", URL, "/verwaltung", NavType.DROP_DOWN));
		navigation.add(new NavItem("Server", URL, "/verwaltung/addserver", NavType.DROP_DOWN_ENTRY));
		navigation.add(new NavItem("Version", URL, "/verwaltung/addversion", NavType.DROP_DOWN_ENTRY));
		navigation.add(new NavItem("Template", URL, "/verwaltung/addtemplate", NavType.DROP_DOWN_ENTRY));

		navigation.add(new NavItem("Verwaltung", URL, "/verwaltung", NavType.DROP_DOWN));
	}

	public void testServer() {
		try {
			serverManager.startServer("test");
		} catch (ServerException e) {
			e.printStackTrace();
		}

		Server server = serverManager.getServer("test");

		Process process = server.getProcess();
		new Thread() {
			@Override
			public void run() {
				while (process.isAlive()) {

					try {
						System.out.print((char) process.getInputStream().read());
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
			};
		}.start();

		new Thread() {
			@Override
			public void run() {
				System.out.println("waiting....");
				try {
					Thread.sleep(7000);
					System.out.println("Noch 3 Sekunden!!");
					Thread.sleep(1000);
					System.out.println("Noch 2 Sekunden!!");
					Thread.sleep(1000);
					System.out.println("Noch 1 Sekunden!!");
					Thread.sleep(1000);
					System.out.println("Stop!");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				PrintStream out = new PrintStream(process.getOutputStream());
				out.println("stop");
				out.flush();
				System.out.println("Stopped!");
			};
		}.start();
	}

	public static ServerFileManager getServerFileManager() {
		return serverFileManager;
	}

	public static ServerManager getServerManager() {
		return serverManager;
	}

	public static LoginManager getLoginManager() {
		return loginManager;
	}

	public static VersionGroupManager getVersionGroupManager() {
		return versionGroupManager;
	}
}
