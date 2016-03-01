package de.clashofcubes.webinterface.usernamangement.registration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;
import de.clashofcubes.webinterface.usernamangement.login.User;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegisterServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setAttribute("title", "Webinterface Registration - ClashofCubes DE");
		request.setAttribute("cssStyleFile", "css/login/style.css");

		request.getRequestDispatcher("/WEB-INF/register/index.jsp").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		User user = Webinterface.getLoginManager().register(username, password);

		if (user == null) {
			request.setAttribute("errormsg", "Benutzername schon vergeben!");
		} else {
			request.setAttribute("successmsg",
					"Erfolgreich Registriert! <a href=\"" + request.getContextPath() + "/login/\">Login</a>");
		}
		doGet(request, response);
	}

}
