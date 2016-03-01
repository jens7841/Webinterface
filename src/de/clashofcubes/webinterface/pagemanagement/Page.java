package de.clashofcubes.webinterface.pagemanagement;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Page {

	private boolean allSubUrls;
	private String[] urlRoot;
	private String title;
	private String indexFile;
	private boolean mustBeLoggedIn;

	public Page(String indexFile, String title, String urlRoot, boolean allSubUrls, boolean mustBeLoggedIn) {
		this.indexFile = indexFile;
		this.title = title;
		if (urlRoot != null) {
			if (urlRoot.startsWith("/")) {
				urlRoot = urlRoot.substring(1);
			}
			if (urlRoot.endsWith("/")) {
				urlRoot = urlRoot.substring(0, urlRoot.length() - 1);
			}
			this.urlRoot = urlRoot.split("\\/");
		} else {
			this.urlRoot = null;
		}
		this.allSubUrls = allSubUrls;
		this.mustBeLoggedIn = mustBeLoggedIn;
	}

	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {

	}

	public boolean isPage(String[] urlEntries) {

		if (urlRoot == null || urlEntries == null)
			return false;

		if (urlRoot.length == 0 && urlEntries.length == 0) {
			return true;
		}
		if (urlRoot.length > urlEntries.length) {
			return false;
		}

		boolean result = false;
		for (int i = 0; i < urlRoot.length; i++) {
			if (i >= urlEntries.length) {
				break;
			}
			if (urlRoot[i].equalsIgnoreCase(urlEntries[i])) {
				result = true;
			} else {
				result = false;
				break;
			}
		}
		if (result && urlEntries.length > urlRoot.length) {
			if (allSubUrls) {
				return true;
			} else {
				System.out.println("hahahah");
				return false;
			}
		}

		return result;
	}

	public String getIndexFile() {
		return indexFile;
	}

	public String getTitle() {
		return title;
	}

	public boolean userMustBeLoggedIn() {
		return mustBeLoggedIn;
	}

	@Override
	public String toString() {
		return "Page [allSubUrls=" + allSubUrls + ", urlRoot=" + Arrays.toString(urlRoot) + ", title=" + title
				+ ", indexFile=" + indexFile + ", mustBeLoggedIn=" + mustBeLoggedIn + "]";
	}

}