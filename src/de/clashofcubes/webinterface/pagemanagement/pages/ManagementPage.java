package de.clashofcubes.webinterface.pagemanagement.pages;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.Page;
import de.clashofcubes.webinterface.servermanagement.Server;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerFolderAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerNameAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;

public class ManagementPage extends Page {

	public ManagementPage() {
		super(Webinterface.DEFAULT_TEMPLATE_FILE, "Verwaltung" + Webinterface.DEFALT_TITLE_PREFIX, "/verwaltung/", true,
				true);
	}

	@Override
	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {

		if (urlEntries.length >= 2) {
			String urlPage = urlEntries[1];

			switch (urlPage.toLowerCase()) {
			case "addserver":
				addServer(urlEntries, request, response);
				break;

			case "serverlist":
				serverList(urlEntries, request, response);
			default:
				break;
			}

		}
	}

	private void serverList(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {

		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "management/serverlist/body.jsp");

	}

	private void addServer(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		String errormsg = "errormsg";

		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "management/addServer/body.jsp");
		List<String> styles = new ArrayList<>();
		styles.add("/css/webinterface/management/addserver.css");
		request.setAttribute(Webinterface.STYLE_LIST_ATTRIBUTE, styles);

		if (request.getMethod().equalsIgnoreCase("post")) {

			String serverName = request.getParameter("servername");
			String version = request.getParameter("version");

			if (serverName != null && version != null && !serverName.isEmpty() && !version.isEmpty()) {
				ServerFile file = Webinterface.getServerFileManager().getFile(version);
				if (file != null) {
					Server server = Webinterface.getServerManager().getServer(serverName);
					if (server == null) {
						String serverFolderName = serverName.replaceAll(" +", "_");
						try {

							Webinterface.getServerManager().addServer(serverName,
									new File("servers/" + serverFolderName), file, "", "stop", false, true, true);
						} catch (ServerNameAlreadyExists e) {
							e.printStackTrace();
						} catch (ServerFolderAlreadyExists e) {
							e.printStackTrace();
						} catch (InvalidPathException e) {
							request.setAttribute(errormsg, "Verwende m&ouml;glichst keine Sonderzeichen!");
						}
						Webinterface.reloadNavigation();
					} else {
						request.setAttribute(errormsg, "Servername existiert bereits!");
					}
				} else {
					request.setAttribute(errormsg, "Version konnte nicht gefunden werden!");
				}
			} else {
				request.setAttribute(errormsg, "Name und Version d&uuml;rfen nicht leer sein!");
			}
		}

	}

}