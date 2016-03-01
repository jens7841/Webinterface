package de.clashofcubes.webinterface.pagemanagement.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.Page;

public class Error404Page extends Page {

	public Error404Page() {
		super(Webinterface.DEFAULT_TEMPLATE_FILE, "404 Not Found!", null, false, true);
	}

	@Override
	public boolean isPage(String[] urlEntries) {
		return false;
	}

	@Override
	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "error/404.jsp");
	}

}