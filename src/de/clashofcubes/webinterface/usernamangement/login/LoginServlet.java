package de.clashofcubes.webinterface.usernamangement.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		User user = Webinterface.getLoginManager().getUser(request);
		if (user != null) {
			Webinterface.getLoginManager().setSession(user, request);
			response.sendRedirect(request.getContextPath() + "/wi/");
		} else {

			request.setAttribute("title", "Webinterface Login - ClashofCubes DE");
			request.setAttribute("cssStyleFile", "css/login/style.css");

			request.getRequestDispatcher("/WEB-INF/login/index.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String remberMeString = request.getParameter("rememberMe");
		boolean rememberMe = false;
		if (remberMeString != null && remberMeString.equalsIgnoreCase("true")) {
			rememberMe = true;
		}

		User user = Webinterface.getLoginManager().login(username, password, request, response, true, rememberMe);

		if (user == null) {
			request.setAttribute("errormsg", "Benutzername oder Passwort falsch!");
		}
		doGet(request, response);
	}

}
