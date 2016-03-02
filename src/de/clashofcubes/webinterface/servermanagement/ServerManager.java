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

import de.clashofcubes.webinterface.servermanagement.exceptions.ServerException;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerFolderAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.exceptions.ServerNameAlreadyExists;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;
import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager;

public class ServerManager {

	private List<Server> servers;
	private File serversRootPath;
	private ServerFileManager serverFileManager;
	private File serversFile;

	public ServerManager(File serversFile, File defaultServerPath, ServerFileManager serverFileManager) {
		servers = new ArrayList<>();
		this.serversRootPath = defaultServerPath;
		this.serverFileManager = serverFileManager;
		this.serversFile = serversFile;
		loadServers();
	}

	public void startServer(String name) throws ServerException {
		Server server = getServer(name);
		if (server == null) {
			throw new ServerException("Server not found!");
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

	public void addServer(String name, File folder, ServerFile serverFile, String startParameter, String stopCommand,
			boolean autostart, boolean createServerFolder, boolean ignoreFolderAlreadyExist)
					throws ServerNameAlreadyExists, ServerFolderAlreadyExists {
		if (getServer(name) != null) {
			throw new ServerNameAlreadyExists("Servername does already exists!");
		}
		if (!ignoreFolderAlreadyExist && folder.exists()) {
			throw new ServerFolderAlreadyExists("Folder does already exists!");
		}

		if (createServerFolder) {
			folder.mkdirs();
			for (File f : serverFile.getFolder().listFiles()) {
				if (!f.getName().equalsIgnoreCase(serverFile.getFile().getName())) {
					try {
						Files.copy(f.toPath(), Paths.get(folder.getAbsolutePath() + "/" + f.getName()),
								StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		servers.add(new Server(name, folder, serverFile, startParameter, stopCommand, autostart));
		saveServers();
		System.out.println("Server " + name + " erfolgreich hinzugefügt!");
	}

	public void addServer(String name, File folder, ServerFile serverFile, String startParameter, String stopCommand,
			boolean autostart, boolean createServerFolder) throws ServerNameAlreadyExists, ServerFolderAlreadyExists {
		addServer(name, folder, serverFile, startParameter, stopCommand, autostart, createServerFolder, false);
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
						ServerFile serverFile = serverFileManager.getFile(data[2]);
						String startParameter = data[3];
						String stopCommand = data[4];
						boolean autoStart = Boolean.getBoolean(data[5]);
						servers.add(new Server(serverName, folder, serverFile, startParameter, stopCommand, autoStart));
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
				writer.print(server.getFolder().getAbsolutePath());
				writer.print(';');
				writer.print(server.getServerFile().getName());
				writer.print(';');
				writer.print(server.getStartParameter());
				writer.print(';');
				writer.print(server.getStopCommand());
				writer.print(';');
				writer.print(server.isAutostart());
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