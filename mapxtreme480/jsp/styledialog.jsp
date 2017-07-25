<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ taglib uri="/wms" prefix="wms" %>

<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<title>Style Chooser Dialog</title>

<body bgcolor="#CFCFCF">

<mapinfo:mapapp name="wmsclient">

<wms:style />

</mapinfo:mapapp>

</body>
</html>
