package de.clashofcubes.webinterface.pagemanagement.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.pagemanagement.Page;

public class LoginForwardingPage extends Page {

	public LoginForwardingPage() {
		super("", "", "", true, false);
	}

	@Override
	public void loadPage(String[] urlEntries, HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getServletContext().getContextPath() + "/login");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
