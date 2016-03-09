package de.clashofcubes.webinterface.pagemanagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.pagemanagement.pages.Error404Page;
import de.clashofcubes.webinterface.pagemanagement.pages.LoginForwardingPage;

public class PageManager {

	private List<Page> pages;

	private Page error404Page = new Error404Page();
	private Page loginPage = new LoginForwardingPage();

	public PageManager() {
		this.pages = new ArrayList<>();
	}

	private Page getCorrectPage(String[] urlEntries, boolean isLoggedIn) {

		for (Page page : pages) {
			if (page.isPage(urlEntries)) {
				if (page.userMustBeLoggedIn() && !isLoggedIn) {
					return loginPage;
				}
				return page;
			}
		}

		if (error404Page.userMustBeLoggedIn() && !isLoggedIn) {
			return loginPage;
		}

		return error404Page;
	}

	public void loadPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String cp = request.getAttribute(Webinterface.CURRENTPAGE_ATTRIBUTE).toString();
		String[] urlEntries = cp.split("\\/");

		boolean isLoggedIn = request.getAttribute(Webinterface.LOGIN_USERNAME_ATTRIBUTE) != null;
		Page page = getCorrectPage(urlEntries, isLoggedIn);

		if (page.getTitle() != null && !page.getTitle().isEmpty()) {
			request.setAttribute(Webinterface.TITLE_ATTRIBUTE, page.getTitle());
		}

		page.loadPage(urlEntries, request, response);

		if (page.getIndexFile() != null && !page.getIndexFile().isEmpty()) {
			if (page != null && page.getIndexFile() != null) {
				request.getRequestDispatcher(page.getIndexFile()).forward(request, response);
			}
		}
		request.getSession().setAttribute(Webinterface.LAST_PAGE_ATTRIBUTE, request.getRequestURL().toString());

	}

	public void addPage(Page page) {
		this.pages.add(page);
	}

}