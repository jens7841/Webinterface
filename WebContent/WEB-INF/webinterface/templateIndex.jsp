<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="de.clashofcubes.webinterface.navigation.NavType"%>
<%@page import="de.clashofcubes.webinterface.navigation.NavItem"%>
<%@page import="de.clashofcubes.webinterface.Webinterface"%>
<%@page import="java.util.Collection"%>
<%@page import="jdk.nashorn.internal.ir.annotations.Ignore"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core"
	xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description" content="Webinterface  ControlPanel" />
<meta name="author" content="ClashOfCubes DE jens7841" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

<title>${title}</title>
<!-- Bootstrap core CSS -->
<link href="${pageContext.request.contextPath}/css/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/css/bootstrap/assets/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet">
<script
	src="${pageContext.request.contextPath}/css/bootstrap/assets/js/ie-emulation-modes-warning.js"></script>

<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
<link href="${pageContext.request.contextPath}/css/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/webinterface/default.css" rel="stylesheet">
<%
	if (request.getAttribute(Webinterface.STYLE_LIST_ATTRIBUTE) != null) {
		@SuppressWarnings("unchecked")
		List<String> styles = (List<String>) request.getAttribute(Webinterface.STYLE_LIST_ATTRIBUTE);

		for (String path : styles) {
			out.println("<link href=\"" + request.getContextPath() + path + "\" rel=\"stylesheet\">");
		}

	}
%>
</head>

<body>
	<!-- Fixed navbar -->
	<nav class="navbar navbar-default navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
					data-target="#navbar" aria-expanded="false" aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand"
					href="${pageContext.request.contextPath}<% out.print("/"+Webinterface.URL); %>">ClashofCubes.de</a>
			</div>
			<div id="navbar" class="navbar-collapse collapse">
				<ul class="nav navbar-nav">
					<%
						@SuppressWarnings("unchecked")
						List<NavItem> nav = (List<NavItem>) request.getAttribute(Webinterface.NAVIGATION_ATTRIBUTE);
						boolean dropDownIsOpen = false;
						for (NavItem item : nav) {
							String prefix = "";
							String suffix = "</a></li>";
							String url = request.getContextPath() + "/" + item.getFullUrl();

							if (dropDownIsOpen && item.getNavType() != NavType.DROP_DOWN_ENTRY) {
								out.println("</ul>\n</li>");
								dropDownIsOpen = false;
							}

							if (item.isUrlThisNavItem(request.getAttribute(Webinterface.CURRENTPAGE_ATTRIBUTE).toString(), false)) {
								prefix = "<li class=\"active\"><a href=\"" + url + "\">";
							} else {
								prefix = "<li><a href=\"" + request.getContextPath() + "/" + item.getFullUrl() + "\">";
							}

							String entry = prefix + item.getName() + suffix;

							if (item.getNavType() == NavType.ENTRY) {
								out.println(entry);
							} else if (item.getNavType() == NavType.DROP_DOWN) {
								out.println("<li class=\"dropdown\"><a href=\"" + url
										+ "\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\" aria-haspopup=\"true\" aria-expanded=\"false\">"
										+ item.getName() + " <span class=\"caret\"></span></a> \n<ul class=\"dropdown-menu\">");
								dropDownIsOpen = true;
							} else if (item.getNavType() == NavType.DROP_DOWN_ENTRY) {
								out.println(entry);
							}
						}

						if (dropDownIsOpen) {
							out.println("</ul>\n</li>");
							dropDownIsOpen = false;
						}
					%>

				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li><a href="${pageContext.request.contextPath}/logout/">Logout</a></li>
				</ul>
			</div>
			<!--/.nav-collapse -->
		</div>
	</nav>

	<div class="container">

		<%
			if (request.getAttribute(Webinterface.PAGE_ERROR_MESSAGE_ATTRIBUTE) != null) {
		%>
		<div id="error" class="alert alert-danger" role="alert">
			<%
				out.println(request.getAttribute(Webinterface.PAGE_ERROR_MESSAGE_ATTRIBUTE));
			%>
		</div>
		<%
			}
		%>

		<!-- Main component for a primary marketing message or call to action -->
		<jsp:include page="${bodyFile}" />

	</div>
	<!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
		window.jQuery
				|| document
						.write('<script src="${pageContext.request.contextPath}/css/bootstrap/assets/js/vendor/jquery.min.js"><\/script>')
	</script>
	<script src="${pageContext.request.contextPath}/css/bootstrap/dist/js/bootstrap.min.js"></script>
	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="${pageContext.request.contextPath}/css/bootstrap/assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
