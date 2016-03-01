package de.clashofcubes.webinterface.navigation;

import java.util.ArrayList;
import java.util.List;

public class NavItem {

	private NavType navType;
	private String name;
	private String url; // z.B. home
	private String urlPrefix; // z.B. wi/
	private List<String> aliases;

	public NavItem(String name, String urlPrefix, String url, NavType navType, String... aliases) {
		this.name = name;
		if (url.startsWith("/")) {
			this.url = url.substring(1);
		} else {
			this.url = url;
		}
		if (this.url.endsWith("/")) {
			this.url = url.substring(0, this.url.length() - 1);
		}
		if (!urlPrefix.endsWith("/")) {
			this.urlPrefix = urlPrefix + "/";
		} else {
			this.urlPrefix = urlPrefix;
		}
		this.navType = navType;
		this.aliases = new ArrayList<>();
		for (String alias : aliases) {
			this.aliases.add(alias);
		}
	}

	public NavType getNavType() {
		return navType;
	}

	public String getName() {
		return name;
	}

	public String getFullUrl() {
		return urlPrefix + url;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public boolean isUrlThisNavItem(String url, boolean compareWithUrlPrefix) {
		String compareUrl = null;
		if (compareWithUrlPrefix) {
			compareUrl = getFullUrl();
		} else {
			compareUrl = getUrl();
		}
		if (url.startsWith("/") && !compareUrl.startsWith("/")) {
			url = url.substring(1);
		}
		if (url.endsWith("/") && !compareUrl.endsWith("/")) {
			url = url.substring(0, url.length() - 1);
		}
		if (compareUrl.equals(url)) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return "NavItem [navType=" + navType + ", name=" + name + ", url=" + url + "]";
	}
}