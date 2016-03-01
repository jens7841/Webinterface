package de.clashofcubes.webinterface.servermanagement.serverfiles;

import java.io.File;

public class ServerFile {

	private String name;
	private File file;
	private String internFilePath;
	private String internFolderPath;
	private File folder;

	public ServerFile(String name, String internFilePath, File file, String internFolderPath, File folder) {
		this.name = name;
		this.internFilePath = internFilePath;
		this.file = file;
		this.folder = folder;
		this.internFolderPath = internFolderPath;
	}

	public String getName() {
		return name;
	}

	public String getInternFilePath() {
		return internFilePath;
	}

	public File getFile() {
		return file;
	}

	public File getFolder() {
		return folder;
	}

	@Override
	public String toString() {
		return "ServerFile [name=" + name + ", internFilePath=" + internFilePath + "]";
	}

	public String getInternFolderPath() {
		return internFolderPath;
	}
}