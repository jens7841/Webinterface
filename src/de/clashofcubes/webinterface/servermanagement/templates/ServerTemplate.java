package de.clashofcubes.webinterface.servermanagement.templates;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.clashofcubes.webinterface.servermanagement.versions.Version;
import de.clashofcubes.webinterface.servermanagement.versions.VersionGroup;

public class ServerTemplate {

	private List<Version> compatibleVersions;
	private List<VersionGroup> availableFor;
	private File templateFolder;
	private String name;

	public ServerTemplate(String name, File templateFolder) {
		this.templateFolder = templateFolder;
		this.compatibleVersions = new ArrayList<>();
		this.availableFor = new ArrayList<>();
		this.name = name;
	}

	public void addAviableFor(VersionGroup versionGroup) {
		if (!availableFor.contains(versionGroup)) {
			availableFor.add(versionGroup);
		}
	}

	public boolean isCompatible(String versionName) {

		for (Version version : compatibleVersions) {
			if (version.getVersionName().equals(versionName)) {
				return true;
			}
		}

		return false;
	}

	public boolean isCompatible(Version version) {
		return isCompatible(version.getVersionName());
	}

	public void addCompatibleVersion(Version version) {
		if (version != null) {
			if (!compatibleVersions.contains(version)) {
				compatibleVersions.add(version);
			}
		}
	}

	public boolean removecompatibleVersion(Version version) {

		if (compatibleVersions.contains(version)) {
			compatibleVersions.remove(version);
			return true;
		}

		return false;
	}

	public File getTemplateFolder() {
		return templateFolder;
	}

	public String getTemplateName() {
		return name;
	}

	public List<Version> getCompatibleVersions() {
		return new ArrayList<>(compatibleVersions);
	}

	public List<VersionGroup> getAvailableFor() {
		return new ArrayList<>(availableFor);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof ServerTemplate) {
			ServerTemplate serverTemplate = (ServerTemplate) obj;
			if (serverTemplate.getTemplateName().equalsIgnoreCase(getTemplateName())) {
				return true;
			}
		}

		return false;
	}
}