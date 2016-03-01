<%@page import="de.clashofcubes.webinterface.Webinterface"%>
<div class="jumbotron">
	<h2>
		Herzlich Willkommen
		<%
		out.write((String) request.getAttribute(Webinterface.LOGIN_USERNAME_ATTRIBUTE));
	%>
	</h2>
	<p>Willkommen auf dem neuen Webinterface von ClashofCubes DE.</p>
</div>
<div class="jumbotron">
	<h2>Status</h2>
	<h2>
		<span class="label label-success">OK</span>
	</h2>
</div>