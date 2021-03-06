package de.clashofcubes.webinterface.servermanagement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;
import de.clashofcubes.webinterface.servermanagement.serverfiles.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.templates.ServerTemplateManager;
import de.clashofcubes.webinterface.servermanagement.versions.Version;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroupManager;

public class ServerManager {

	private List<Server> servers;
	private File serversRootPath;
	private File serversFile;
	private VersionGroupManager versionGroupManager;
	private ServerTemplateManager templateManager;

	public ServerManager(File serversFile, File defaultServerPath, VersionGroupManager versionGroupManager,
			ServerTemplateManager templateManager) {
		servers = new ArrayList<>();
		this.serversRootPath = defaultServerPath;
		this.serversFile = serversFile;
		this.versionGroupManager = versionGroupManager;
		this.templateManager = templateManager;
	}

	public void startServer(Server server) throws ServerException {
		startServer(server.getName());
	}

	public void startServer(String name) throws ServerException {
		Server server = getServer(name);
		if (server == null) {
			throw new ServerException("Server does not exist!");
		}
		Process process = server.getProcess();
		if (process != null && process.isAlive()) {
			throw new ServerException("Server is already running!");
		}
		server.startServer();
	}

	public void stopServer(String name) throws ServerException {
		Server server = getServer(name);
		if (server == null) {
			throw new ServerException("Server not found!");
		}
		Process process = server.getProcess();
		if (process != null && !process.isAlive()) {
			throw new ServerException("Server is not running!");
		}
		server.stopServer();
	}

	public void addServer(Server server, boolean createServerFolder, boolean ignoreFolderAlreadyExist)
			throws ServerException {
		if (getServer(server.getName()) != null) {
			return;
		}
		if (!ignoreFolderAlreadyExist && server.getServerFolder().exists()) {
			throw new ServerException("Folder does already exists!");
		}

		if (createServerFolder) {
			ServerFile serverFile = server.getVersion().getServerFile();
			File folder = server.getServerFolder();
			folder.mkdirs();
			for (File f : serverFile.getFolder().listFiles()) {
				if (!f.getName().equalsIgnoreCase(serverFile.getFile().getName())) {
					try {
						Files.copy(f.toPath(), Paths.get(folder.getAbsolutePath() + "/" + f.getName()),
								StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						throw new ServerException(e);
					}
				}
			}
			if (server.getTemplate() != null) {
				for (File f : server.getTemplate().getTemplateFolder().listFiles()) {
					if (!f.getName().equalsIgnoreCase(serverFile.getFile().getName())) {
						try {
							Files.copy(f.toPath(), Paths.get(folder.getAbsolutePath() + "/" + f.getName()),
									StandardCopyOption.REPLACE_EXISTING);
						} catch (IOException e) {
							throw new ServerException(e);
						}
					}
				}
			}
		}

		servers.add(server);
		saveServers();
	}

	public Server getServer(String name) {
		for (Server server : servers) {
			if (server.getName().equalsIgnoreCase(name)) {
				return server;
			}
		}
		return null;
	}

	public void loadServers() {

		try {

			if (!serversFile.exists()) {
				if (serversFile.getParentFile() != null)
					serversFile.getParentFile().mkdirs();
				serversFile.createNewFile();
			}

			BufferedReader reader = new BufferedReader(new FileReader(serversFile));

			servers.clear();

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] data = line.split(";");
					if (data.length == 6) {
						String serverName = data[0];
						File folder = new File(data[1]);
						Version version = versionGroupManager.getVersion(data[2]);
						String startParameter = data[3];
						String stopCommand = data[4];
						String template = data[5];
						boolean autoStart = Boolean.getBoolean(data[5]);
						servers.add(new Server(serverName, folder, version, templateManager.getTemplate(template),
								startParameter, stopCommand, autoStart));
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveServers() {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(serversFile));

			for (Server server : servers) {
				writer.print(server.getName());
				writer.print(';');
				writer.print(server.getServerFolder().getAbsolutePath());
				writer.print(';');
				writer.print(server.getVersion().getVersionName());
				writer.print(';');
				writer.print(server.getStartParameter());
				writer.print(';');
				writer.print(server.getStopCommand());
				writer.print(';');
				writer.print(server.isAutoStart());
				writer.print(';');
				writer.print(server.getTemplate().getTemplateName());
				writer.print(';');
				writer.println();
			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Server> getServers() {
		return new ArrayList<>(servers);
	}
}