package de.clashofcubes.webinterface.pagemanagement.pages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.Page;

public class HomePage extends Page {

	public HomePage() {
		super(Webinterface.DEFAULT_TEMPLATE_FILE, "Home" + Webinterface.DEFALT_TITLE_PREFIX, "", false, true);
	}

	@Override
	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute(Webinterface.BODY_FILE_ATTRIBUTE, "home/body.jsp");
	}

}