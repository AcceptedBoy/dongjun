<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<html>
<head>
<title><mapinfo:localString string="dialog_printpreview"/></title>
</head>
<body bgcolor="#CFCFCF">

<mapxtreme:printPreviewMap width="640" height="480" />

</body>
</html>
</mapinfo:mapapp>
