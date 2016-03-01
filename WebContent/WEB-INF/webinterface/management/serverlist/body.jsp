<%@page import="de.clashofcubes.webinterface.servermanagement.Server"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.ServerManager"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFile"%>
<%@page import="java.util.List"%>
<%@page import="de.clashofcubes.webinterface.Webinterface"%>
<%@page import="de.clashofcubes.webinterface.servermanagement.serverfiles.ServerFileManager"%>
<div class="jumbotron">
	<h2>Serverliste</h2>
	<table class="table table-striped">
		<thead>
			<tr>
				<th>#</th>
				<th>Server Name</th>
				<th>Version</th>
				<th>Anderes Zeugs</th>
			</tr>
		</thead>
		<tbody>
			<%
				ServerManager serverManager = Webinterface.getServerManager();
				int number = 1;
				String serverPath = "/server/";
				String seperator = "/";
				for (Server server : serverManager.getServers()) {
			%>
			<tr>
				<td>
					<%
						out.print(number);
					%>
				</td>
				<%
					StringBuilder hrefUrl = new StringBuilder();
						hrefUrl.append(request.getServletContext().getContextPath());
						hrefUrl.append(seperator);
						hrefUrl.append(Webinterface.URL);
						hrefUrl.append(serverPath);
						hrefUrl.append(server.getName());
				%>
				<td><a href="<%out.print(hrefUrl);%>"> <%
 	out.print(server.getName());
 %>
				</a></td>
				<td>
					<%
						out.print(server.getServerFile().getName());
					%>
				</td>
				<td></td>
			</tr>
			<%
				number++;
				}
			%>
		</tbody>
	</table>
</div>