package de.clashofcubes.webinterface.servermanagement.serverfiles;

import java.io.File;

public class ServerFile {

	private String name;
	private File file;
	private File folder;

	public ServerFile(String name, File file) {
		this.name = name;
		if (file != null) {
			String path = file.getAbsolutePath();
			folder = new File(path.substring(0, path.lastIndexOf("\\")));
		}
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}

	public File getFolder() {
		return folder;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ServerFile) {
			ServerFile serverFile = (ServerFile) obj;
			if (serverFile.getFile().equals(getFile()) && serverFile.getFolder().equals(getFolder())
					&& serverFile.getName().equalsIgnoreCase(getName())) {
				return true;
			}
		}

		return false;
	}
}