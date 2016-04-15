<%@page import="de.clashofcubes.webinterface.servermanagement.versions.Version"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.versions.VersionGroup"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.versions.VersionGroupManager"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile"%>
<%@page import="java.util.List"%>
<%@page import="de.clashofcubes.webinterface.Webinterface"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager"%>
<div class="jumbotron">
	<h2>Version hinzuf&uuml;gen</h2>
	<form method="post" enctype="multipart/form-data">
		<p>
			Name:
			<input type="text" name="versionname" placeholder="Name der Version" />
		</p>
		<p>
			Server-Datei:
			<input style="width: auto;" type="file" name="serverfile" accept=".jar" />
		</p>
		<p>
			Version-Group:
			<select name="versiongroup">
				<option selected="selected" value="">Bitte w&auml;hlen</option>
				<%
					VersionGroupManager versionGroupManager = Webinterface.getVersionGroupManager();

					for (VersionGroup group : versionGroupManager.getVersionGroups()) {
						out.println("<option style=\"font-weight: bold;\" value=\"" + group.getGroupName() + "\"> "
								+ group.getGroupName() + " </option>");
					}
				%>
			</select>
		</p>
		<p>
			Neue Version-Group Name:
			<input type="text" name="newversiongroupname" placeholder="Neue VersionGroup erstellen" />
			(Leer lassen f&uuml;r VersionGroup aus Liste)
		</p>
		<p>
			<input style="margin-top: 20px;" type="submit" value="Version hinzuf&uuml;gen" />
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