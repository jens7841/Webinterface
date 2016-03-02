package de.clashofcubes.webinterface.pagemanagement.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.Page;
import de.clashofcubes.webinterface.servermanagement.Server;
import de.clashofcubes.webinterface.servermanagement.ServerManager;

public class ServerPage extends Page {

	private static final String SERVER_NAME = "serverName";

	public ServerPage() {
		super(Webinterface.DEFAULT_TEMPLATE_FILE, "Server", "/server/", true, true);
	}

	@Override
	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		if (urlEntries.length <= 1) {

		} else {
			ServerManager serverManager = Webinterface.getServerManager();
			Server server = serverManager.getServer(urlEntries[1]);
			if (server != null) {
				request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "server/body.jsp");

				request.setAttribute(SERVER_NAME, server.getName());

			} else {
				request.setAttribute(Webinterface.PAGE_ERROR_MESSAGE_ATTRIBUTE, "Server konnte nicht gefunden werden!");
			}
		}
	}

}