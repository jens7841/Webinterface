package de.clashofcubes.webinterface.servermanagement.versions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager;

public class VersionGroupManager {

	private List<VersionGroup> versionGroups;
	private File file;
	private ServerFileManager serverFileManager;

	public VersionGroupManager(File file, ServerFileManager serverFileManager) {
		if (file == null || serverFileManager == null)
			throw new IllegalArgumentException("File or serverFileManger could not be null!");
		this.file = file;
		this.versionGroups = new ArrayList<>();
		this.serverFileManager = serverFileManager;
	}

	public boolean delVersionGroup(VersionGroup versionGroup) {
		return delVersionGroup(versionGroup.getGroupName());
	}

	public boolean delVersionGroup(String groupName) {

		if (groupName != null) {
			VersionGroup versionGroup = getVersionGroup(groupName);
			if (versionGroup != null) {
				versionGroups.indexOf(versionGroup);
				return true;
			}
		}

		return false;
	}

	public Version getVersion(String name) {
		for (VersionGroup versionGroup : versionGroups) {
			for (Version version : versionGroup.getVersions()) {
				if (version.getVersionName().equalsIgnoreCase(name)) {
					return version;
				}
			}
		}
		return null;
	}

	public void addVersionGroup(VersionGroup versionGroup) {
		if (versionGroup != null) {
			if (getVersionGroup(versionGroup.getGroupName()) == null) {
				versionGroups.add(versionGroup);
				saveVersionGroups();
			}
		}
	}

	public VersionGroup getVersionGroup(String groupName) {
		for (VersionGroup versionGroup : versionGroups) {
			if (versionGroup.getGroupName().equalsIgnoreCase(groupName)) {
				return versionGroup;
			}
		}
		return null;
	}

	public void saveVersionGroups() {

		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));

			for (VersionGroup versionGroup : versionGroups) {
				for (Version version : versionGroup.getVersions()) {

					writer.print(versionGroup.getGroupName());
					writer.print(';');
					writer.print(version.getVersionName());
					writer.print(';');
					writer.print(version.getServerFile().getName());
					writer.print(';');
					writer.println();
				}
			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadVersionGroups() {

		if (file == null)
			return;

		try {

			if (!file.exists()) {
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));

			versionGroups.clear();

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] data = line.split(";");
					if (data.length == 3) {
						String groupName = data[0];
						String versionName = data[1];
						String serverFileName = data[2];

						VersionGroup versionGroup = getVersionGroup(groupName);
						if (versionGroup == null) {
							versionGroup = new VersionGroup(groupName);
						}

						versionGroup
								.addVersion(new Version(versionName, serverFileManager.getServerFile(serverFileName)));
						versionGroups.add(versionGroup);
					}
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<VersionGroup> getVersionGroups() {
		return new ArrayList<>(versionGroups);
	}

}