package de.clashofcubes.webinterface;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
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
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerFileAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerFolderAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerNameAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager;
import de.clashofcubes.webinterface.usernamangement.login.LoginManager;

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

	public static final String DEFAULT_TEMPLATE_FILE = "/WEB-INF/webinterface/templateIndex.jsp";
	public static final String DEFALT_TITLE_PREFIX = " - ClashofCubes Webinterface";

	private static final List<NavItem> navigation = new ArrayList<>();
	public static final String URL = "wi";

	private static PageManager pageManager;
	private static ServerFileManager serverFileManager;
	private static ServerManager serverManager;
	private static LoginManager loginManager;

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
	public void init() throws ServletException {
		super.init();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		pageManager = new PageManager();
		pageManager.addPage(new HomePage());
		pageManager.addPage(new ManagementPage());
		pageManager.addPage(new ServerPage());

		try {
			loginManager = new LoginManager(new File("users.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		serverFileManager = new ServerFileManager(new File("serverFiles.csv"), sce.getServletContext());

		String pathToServerFiles = "/WEB-INF/serverFiles/";
		try {
			String internFolderPath = pathToServerFiles + "spigot-1.8/";
			String fileName = "spigot-1.8.8-R0.1.jar";
			serverFileManager.addFile(new ServerFile("Spigot 1.8", internFolderPath + fileName,
					new File(sce.getServletContext().getRealPath(internFolderPath + fileName)), internFolderPath,
					new File(sce.getServletContext().getRealPath(internFolderPath))));
		} catch (ServerFileAlreadyExists e) {
		}

		serverManager = new ServerManager(new File("servers.csv"), new File("F:/Eclipse/BukkitTest"),
				serverFileManager);
		// testServer();
		try {
			serverManager.addServer("Test", new File("F:/Eclipse/BukkitTest"), serverFileManager.getFile("Spigot 1.8"),
					"", "stop", false, true, true);
		} catch (ServerNameAlreadyExists e) {
		} catch (ServerFolderAlreadyExists e) {
		}

		reloadNavigation();
	}

	public static void reloadNavigation() {
		navigation.clear();
		navigation.add(new NavItem("Home", URL, "", NavType.ENTRY, "home"));
		navigation.add(new NavItem("Status", URL, "/status", NavType.ENTRY));
		// navigation.add(new NavItem("Server", url, "/server",
		// NavType.DROP_DOWN));
		// for (Server server : serverManager.getServers()) {
		// navigation.add(new NavItem(server.getName(), url, "/server/" +
		// server.getName(), NavType.DROP_DOWN_ENTRY));
		// }
		navigation.add(new NavItem("Verwaltung", URL, "/verwaltung", NavType.DROP_DOWN));
		navigation.add(new NavItem("Serverliste", URL, "/verwaltung/serverlist", NavType.DROP_DOWN_ENTRY));
		navigation.add(new NavItem("Server hinzuf&uuml;gen", URL, "/verwaltung/addserver", NavType.DROP_DOWN_ENTRY));
		navigation.add(new NavItem("Userverwaltung", URL, "/verwaltung/usermanagement", NavType.DROP_DOWN_ENTRY));
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
}
