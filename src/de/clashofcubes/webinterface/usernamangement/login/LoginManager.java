package de.clashofcubes.webinterface.usernamangement.login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.clashofcubes.webinterface.Webinterface;

public class LoginManager {

	private static final String SHA = "SHA-512";
	private File userFile;
	private List<User> userList;

	public LoginManager(File userFile) throws IOException {
		this.userFile = userFile;
		this.userList = new ArrayList<>();
		readUsersFromFile();
	}

	public User register(String username, String password) {
		if (getUser(username) == null) {
			password = getSHA(password);
			User u = new User(username, password);
			userList.add(u);
			saveUserList();
			return u;
		}
		return null;
	}

	private void saveUserList() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(userFile, false));
			for (User user : userList) {
				writer.println(user.getName() + ";" + user.getPassword());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}

	public User getUser(String username) {
		if (username == null) {
			return null;
		}

		for (User user : userList) {
			if (user.getName().equalsIgnoreCase(username)) {
				return user;
			}
		}

		return null;
	}

	public void setSession(User user, HttpServletRequest request) {
		if (user != null) {
			request.getSession().setAttribute(Webinterface.LOGIN_ATTRIBUTE, "true");
			request.getSession().setAttribute(Webinterface.LOGIN_USERNAME_ATTRIBUTE, user.getName());
		}
	}

	public void logout(User user, HttpServletRequest request, HttpServletResponse response) {
		if (user != null && request != null && response != null) {
			request.getSession().setAttribute(Webinterface.LOGIN_ATTRIBUTE, null);
			request.getSession().setAttribute(Webinterface.LOGIN_USERNAME_ATTRIBUTE, null);

			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equalsIgnoreCase(Webinterface.LOGIN_USERNAME_ATTRIBUTE)) {
						cookie.setMaxAge(0);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
			}
		}

	}

	public User login(String username, String password, HttpServletRequest request, HttpServletResponse response,
			boolean addSession, boolean addCookie) {

		password = getSHA(password);

		User user = getUser(username);
		if (user != null && user.getPassword().equals(password)) {
			if (addSession && request != null) {
				setSession(user, request);
			}
			if (addCookie && response != null) {
				Cookie cookie = new Cookie(Webinterface.LOGIN_USERNAME_ATTRIBUTE, user.getName());
				cookie.setMaxAge(864000); // 10 tage
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			return user;
		}
		return null;
	}

	private String getSHA(String s) {

		MessageDigest m = null;
		try {
			m = MessageDigest.getInstance(SHA);
			m.update(s.getBytes(), 0, s.length());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return new BigInteger(1, m.digest()).toString(16);
	}

	private void readUsersFromFile() throws IOException {
		if (userFile.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(userFile));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\\;");
				userList.add(new User(split[0], split[1]));
			}
			reader.close();
		} else {
			userFile.mkdirs();
			userFile.delete();
			userFile.createNewFile();
		}
	}

	public User getUser(HttpServletRequest request) {
		if (request.getSession().getAttribute(Webinterface.LOGIN_ATTRIBUTE) != null) {
			String username = (String) request.getSession().getAttribute(Webinterface.LOGIN_USERNAME_ATTRIBUTE);

			if (username != null) {
				User user = getUser(username);
				if (user != null && user.getName() != null) {
					return user;
				}
			}
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null && cookie.getName() != null
						&& cookie.getName().equalsIgnoreCase(Webinterface.LOGIN_USERNAME_ATTRIBUTE)
						&& cookie.getValue() != null) {
					User user = getUser(cookie.getValue());
					if (user != null && user.getName() != null) {
						return user;
					}
				}
			}
		}

		return null;
	}

}