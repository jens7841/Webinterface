<%@page import="de.clashofcubes.webinterface.servermanagement.versions.Version"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.versions.VersionGroup"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.versions.VersionGroupManager"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile"%>
<%@page import="java.util.List"%>
<%@page import="de.clashofcubes.webinterface.Webinterface"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager"%>
<div class="jumbotron">
	<h2>Server hinzuf&uuml;gen</h2>
	<form method="post">
		<p>
			Name:
			<input type="text" name="servername" placeholder="Name des Servers">
		</p>
		<p>
			Version: <select style="font-style: italic;" name="version">
				<option style="font-style: italic;" selected="selected" value="">Bitte w&auml;hlen</option>
				<%
					VersionGroupManager versionGroupManager = Webinterface.getVersionGroupManager();

					for (VersionGroup group : versionGroupManager.getVersionGroups()) {
						out.println("<option style=\"font-style: italic;font-weight: bold;\" disabled=\"disabled\">-- " + group.getGroupName()
								+ " --</option>");
						for (Version version : group.getVersions()) {
							out.println("<option style=\"font-style: normal;\" value=\"" + version.getVersionName() + "\">"
									+ version.getVersionName() + "</option>");
						}
					}

				%>
			</select>
		</p>
		<p>
			<input style="margin-top: 20px;" type="submit" value="Server hinzuf&uuml;gen" />
		</p>

	</form>
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
</div>