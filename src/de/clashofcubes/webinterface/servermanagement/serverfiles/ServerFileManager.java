package de.clashofcubes.webinterface.servermanagement.serverfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import de.clashofcubes.webinterface.servermanagement.serverfiles.exceptions.ServerException;

public class ServerFileManager {

	private List<ServerFile> files;
	private File dataFile;
	private File rootFolder;

	public ServerFileManager(File file, File serverFilesRootFolder) {
		files = new ArrayList<>();
		this.dataFile = file;
		this.rootFolder = serverFilesRootFolder;
	}

	public ServerFile getServerFile(String name) {

		for (ServerFile serverFile : files) {
			if (serverFile.getName().equalsIgnoreCase(name)) {
				return serverFile;
			}
		}
		return null;
	}

	public void addFile(ServerFile file) {
		if (getServerFile(file.getName()) != null) {
			throw new ServerException("The File was already added");
		}
		files.add(file);
		saveData();
	}

	private void saveData() {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(dataFile));

			for (ServerFile serverFile : files) {
				writer.print(serverFile.getName());
				writer.print(';');
				String path = rootFolder.toURI().relativize(serverFile.getFolder().toURI()).getPath();
				writer.print(path);
				writer.print(';');
				writer.print(serverFile.getFile().getName());
				writer.print(';');
				writer.println();
			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadData() {
		try {

			if (!dataFile.exists()) {
				if (dataFile.getParentFile() != null)
					dataFile.getParentFile().mkdirs();
				dataFile.createNewFile();
			}

			BufferedReader reader = new BufferedReader(new FileReader(dataFile));

			files.clear();

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] data = line.split(";");
					if (data.length == 3) {
						String serverName = data[0];
						String internPathToFolder = data[1];
						String fileName = data[2];
						files.add(new ServerFile(serverName,
								new File(rootFolder.getAbsolutePath() + "\\" + internPathToFolder + "\\" + fileName)));
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getRootFolder() {
		return rootFolder;
	}

	public List<ServerFile> getFiles() {
		return new ArrayList<>(files);
	}
}