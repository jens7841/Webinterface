package de.clashofcubes.webinterface.servermanagement.versions;

import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile;

public class Version {

	private String versionName;
	private ServerFile serverFile;

	public Version(String versionName, ServerFile serverFile) {
		if (versionName == null || serverFile == null)
			throw new IllegalArgumentException("VersionsName or serverFile can not be null!");
		this.versionName = versionName;
		this.serverFile = serverFile;
	}

	public String getVersionName() {
		return versionName;
	}

	public ServerFile getServerFile() {
		return serverFile;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Version) {
			Version version = (Version) obj;
			if (version.getVersionName().equalsIgnoreCase(getVersionName())
					&& version.getServerFile().equals(getServerFile())) {
				return true;
			}
		}

		return false;
	}

}