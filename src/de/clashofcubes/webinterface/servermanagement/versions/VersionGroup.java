package de.clashofcubes.webinterface.servermanagement.versions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VersionGroup {

	private String groupName;
	private List<Version> versions;

	public VersionGroup(String groupName) {
		this(groupName, null);
	}

	public VersionGroup(String groupName, Collection<Version> versions) {
		this(groupName, new ArrayList<>(versions));
	}

	public VersionGroup(String groupName, List<Version> versions) {
		this.groupName = groupName;
		if (versions != null) {
			this.versions = new ArrayList<>(versions);
		} else {
			this.versions = new ArrayList<>();
		}
	}

	public void addVersion(Version version) {
		if (version != null) {
			if (getVersion(version.getVersionName()) == null) {
				versions.add(version);
			}
		}
	}

	public String getGroupName() {
		return groupName;
	}

	public Version getVersion(String versionName) {
		if (versionName != null) {
			for (Version version : versions) {
				if (version.getVersionName().equalsIgnoreCase(versionName)) {
					return version;
				}
			}
		}
		return null;
	}

	public boolean delVersion(Version version) {
		return delVersion(version.getVersionName());
	}

	public boolean delVersion(String versionName) {
		if (versions != null) {
			Version version = getVersion(versionName);
			if (versions != null) {
				versions.remove(version);
				return true;
			}
		}
		return false;
	}

	public List<Version> getVersions() {
		return new ArrayList<>(versions);
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof VersionGroup) {
			VersionGroup versionGroup = (VersionGroup) obj;
			if (versionGroup.getGroupName().equalsIgnoreCase(getGroupName())) {
				return true;
			}
		}

		return false;
	}

}