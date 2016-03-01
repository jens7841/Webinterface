package de.clashofcubes.webinterface.servermanagement.serverfiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import de.clashofcubes.webinterface.servermanagement.exceptions.ServerFileAlreadyExists;

public class ServerFileManager {

	private List<ServerFile> files;
	private File dataFile;
	private ServletContext servletContext;

	public ServerFileManager(File file, ServletContext servletContext) {
		files = new ArrayList<>();
		this.dataFile = file;
		this.servletContext = servletContext;
		loadData();
	}

	public ServerFile getFile(String name) {

		for (ServerFile serverFile : files) {
			if (serverFile.getName().equalsIgnoreCase(name)) {
				return serverFile;
			}
		}
		return null;
	}

	public void addFile(ServerFile file) throws ServerFileAlreadyExists {
		if (getFile(file.getName()) != null) {
			throw new ServerFileAlreadyExists("The File was already added");
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
				writer.print(serverFile.getInternFolderPath());
				writer.print(';');
				writer.print(serverFile.getFile().getName());
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
						String internPathToFile = internPathToFolder + "/" + fileName;
						files.add(new ServerFile(serverName, internPathToFile, new File(internPathToFile),
								internPathToFolder, new File(servletContext.getRealPath(internPathToFolder))));
					}
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ServerFile> getFiles() {
		return new ArrayList<>(files);
	}
}