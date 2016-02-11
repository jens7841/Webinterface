<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
          "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
	xmlns:ui="http://xmlns.jcp.org/jsf/facelets">
<head>
<title><ui:insert name="title">Default title</ui:insert></title>
</head>

<body>

	<div id="htmlheader">
		<jsp:include page="${htmlheader}" />
	</div>


	<div id="content">
		<ui:insert name="content">
    	Content area.  See comments below this line in the source.
    	<!--  include your content file or uncomment the include below and create content.xhtml in this directory -->
			<!-- <div> -->
			<!-- <ui:include src="content.xhtml"/> -->
			<!-- </div> -->
		</ui:insert>
	</div>

	<div id="footer">
		<ui:insert name="footer">
    	Footer area.  See comments below this line in the source.
    	<!--  include your header file or uncomment the include below and create footer.xhtml in this directory -->
			<!--<ui:include src="footer.xhtml"/>  -->
		</ui:insert>
	</div>

</body>

</html>
