package de.clashofcubes.webinterface.pagemanagement.pages;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.Page;
import de.clashofcubes.webinterface.servermanagement.Server;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager;
import de.clashofcubes.webinterface.servermanagement.versions.Version;

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
				break;

			case "addversion":
				addVersion(urlEntries, request, response);
				break;
			default:
				notFound(request);
				break;
			}

		}
	}

	@SuppressWarnings("resource")
	private void addVersion(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		String errormsg = "errormsg";
		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "management/addVersion/body.jsp");

		List<String> styles = new ArrayList<>();
		styles.add("/css/webinterface/management/formular.css");
		request.setAttribute(Webinterface.STYLE_LIST_ATTRIBUTE, styles);

		if (request.getMethod().equalsIgnoreCase("post")) {

			if (ServletFileUpload.isMultipartContent(request)) {

				try {

					String versionName = request.getParameter("versionname").trim();
					String versionGroupName = request.getParameter("versiongroup").trim();
					String newVersionGroupName = request.getParameter("newversiongroupname").trim();

					Part filePart = request.getPart("serverfile");

					if (versionName != null && !versionName.isEmpty() && versionGroupName != null && filePart != null) {

						String fileName = filePart.getSubmittedFileName();
						if (!fileName.isEmpty()) {
							if (fileName.endsWith(".jar")) {

								if (!(newVersionGroupName != null && !newVersionGroupName.isEmpty()
										&& !versionGroupName.isEmpty())) {

									ServerFileManager serverFileManager = Webinterface.getServerFileManager();

									// serverFileManager.getServerFile()

									// InputStream inputStream =
									// filePart.getInputStream();
									// byte[] data = new
									// byte[inputStream.available()];
									//
									// inputStream.read(data, 0, data.length);
									//
									// FileOutputStream out = new
									// FileOutputStream(new File("test.jar"));
									//
									// for (byte b : data) {
									// out.write(b);
									// }
									// out.close();

								} else {
									request.setAttribute(errormsg,
											"Bitte wähle entweder eine Version-Group oder gebe eine neue ein! (Nicht beides)");
								}
							} else {
								request.setAttribute(errormsg, "Es dürfen nur .jar Dateien hochgeladen werden!");
							}
						} else {
							request.setAttribute(errormsg, "Bitte lade eine Server-Datei hoch!");
						}
					} else {
						request.setAttribute(errormsg, "Bitte f&uuml;lle alles aus und wähle eine Datei aus!");
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				request.setAttribute(errormsg, "Error! Bitte versuche es erneut!");
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
		styles.add("/css/webinterface/management/formular.css");
		request.setAttribute(Webinterface.STYLE_LIST_ATTRIBUTE, styles);

		if (request.getMethod().equalsIgnoreCase("post")) {

			String serverName = request.getParameter("servername");
			String versionParameter = request.getParameter("version");

			if (serverName != null && versionParameter != null && !serverName.isEmpty()
					&& !versionParameter.isEmpty()) {
				Version version = Webinterface.getVersionGroupManager().getVersion(versionParameter);
				if (version != null) {
					Server server = Webinterface.getServerManager().getServer(serverName);
					if (server == null) {
						String serverFolderName = serverName.replaceAll(" +", "_");
						try {

							Webinterface.getServerManager().addServer(new Server(serverName,
									new File("servers/" + serverFolderName), version, null, "", "stop", false), true,
									true);
							request.setAttribute("successmsg",
									"Der Server " + serverName + " wurde erfolgreich hinzugef&uuml;gt");

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

	private void notFound(HttpServletRequest request) {
		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "error/404.jsp");
	}

}