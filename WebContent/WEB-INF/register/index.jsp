<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta name="description" content="" />
<meta name="author" content="" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />

<title>${title}</title>

<link href="${pageContext.request.contextPath}/css/bootstrap/dist/css/bootstrap.min.css"
	rel="stylesheet" />

<link
	href="${pageContext.request.contextPath}/css/bootstrap/assets/css/ie10-viewport-bug-workaround.css"
	rel="stylesheet" />

<link href="${pageContext.request.contextPath}/css/login/style.css" rel="stylesheet" />

<script
	src="${pageContext.request.contextPath}/css/bootstrap/assets/js/ie-emulation-modes-warning.js"></script>

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

	<div class="container">

		<form class="form-signin" method="post">
			<h2 class="form-signin-heading">Registrieren</h2>
			<label class="sr-only">Username</label> <input type="text" id="username" name="username" class="form-control"
				placeholder="Username" required autofocus /><label class="sr-only">Passwort</label> <input
				type="password" name="password" id="password" class="form-control" placeholder="Password" required />

			<button class="btn btn-lg btn-primary btn-block" type="submit">Registrieren</button>
		</form>
		<%
			if (request.getAttribute("successmsg") != null) {
		%>
		<div class="alert alert-success" role="alert">
			<strong> <%
 	out.println(request.getAttribute("successmsg"));
 %>
			</strong>
		</div>
		<%
			}
		%>

		<%
			if (request.getAttribute("errormsg") != null) {
		%>
		<div class="alert alert-danger" role="alert">
			<strong> <%
 	out.println(request.getAttribute("errormsg"));
 %>
			</strong>
		</div>
		<%
			}
		%>

	</div>
	<!-- /container -->


	<!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
	<script
		src="${pageContext.request.contextPath}/css/bootstrap/assets/js/ie10-viewport-bug-workaround.js"></script>
</body>
</html>
