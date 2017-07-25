<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_projections"/></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<mapxtreme:projection dialog="true">
	<mapxtreme:projectionCategories />
	<p><mapxtreme:projectionMembers />
	<p><mapxtreme:changeProjection />&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton />
	<input type=hidden name="redirect" value="/jsp/projectiondialog.jsp">
</mapxtreme:projection>
</center>

</body>
</html>
</mapinfo:mapapp>
