package de.clashofcubes.webinterface.servermanagement.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import de.clashofcubes.webinterface.servermanagement.versions.Version;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroup;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroupManager;

public class ServerTemplateManager {

	private File file;
	private List<ServerTemplate> templates;
	private File rootFolder;
	private VersionGroupManager versionGroupManager;

	public ServerTemplateManager(File file, File templateRootFolder, VersionGroupManager versionGroupManager) {
		this.file = file;
		this.rootFolder = templateRootFolder;
		templates = new ArrayList<>();
		this.versionGroupManager = versionGroupManager;
	}

	public ServerTemplate getTemplate(String name) {

		for (ServerTemplate serverTemplate : templates) {
			if (serverTemplate.getTemplateName().equalsIgnoreCase(name)) {
				return serverTemplate;
			}
		}

		return null;
	}

	public void addTemplate(ServerTemplate serverTemplate) {
		if (!templates.contains(serverTemplate)) {
			templates.add(serverTemplate);
			saveTemplates();
		}
	}

	public boolean removeTemplate(ServerTemplate serverTemplate) {
		return removeTemplate(serverTemplate.getTemplateName());
	}

	private boolean removeTemplate(String templateName) {

		for (ServerTemplate serverTemplate : templates) {
			if (serverTemplate.getTemplateName().equalsIgnoreCase(templateName)) {
				templates.remove(serverTemplate);
				return true;
			}
		}

		return false;
	}

	private void saveTemplates() {

		try {
			PrintWriter writer = new PrintWriter(new FileWriter(file));

			for (ServerTemplate template : templates) {

				writer.print(template.getTemplateName());
				writer.print(';');
				String path = rootFolder.toURI().relativize(template.getTemplateFolder().toURI()).getPath();
				writer.print(path);
				writer.print(';');
				writer.println();
				for (VersionGroup versionGroup : template.getAvailableFor()) {
					writer.print(versionGroup.getGroupName());
					writer.print(';');
				}
				writer.println();
				for (Version version : template.getCompatibleVersions()) {
					writer.print(version.getVersionName());
					writer.print(';');
				}
				writer.println();

			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void loadTemplates() {

		if (file == null)
			return;

		try {

			if (!file.exists()) {
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();
				file.createNewFile();
			}

			BufferedReader reader = new BufferedReader(new FileReader(file));

			templates.clear();

			String line;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] data = line.split(";");
					if (data.length == 2) {

						String templateName = data[0];
						String templateFolder = data[1];

						File folder = new File(rootFolder.getAbsolutePath() + "\\" + templateFolder);
						ServerTemplate serverTemplate = new ServerTemplate(templateName, folder);

						line = reader.readLine();
						if (line != null) {
							String[] versionGroups = line.split("\\;");
							for (String group : versionGroups) {
								if (!group.isEmpty()) {
									VersionGroup versionGroup = versionGroupManager.getVersionGroup(group);
									if (versionGroup != null) {
										serverTemplate.addAviableFor(versionGroup);
									}
								}
							}
						}
						line = reader.readLine();
						if (line != null) {
							String[] versions = line.split("\\;");
							for (String versionStr : versions) {
								if (!versionStr.isEmpty()) {
									Version version = versionGroupManager.getVersion(versionStr);
									if (version != null) {
										serverTemplate.addCompatibleVersion(version);
									}
								}
							}
						}

						templates.add(serverTemplate);
					}
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}