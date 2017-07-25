<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<html>
<head>
<title><mapinfo:localString string="dialog_open"/></title>
</head>
<body bgcolor="#CFCFCF">
<center>
<table border=0 cellspacing=0 cellpadding=0 height="100%"><tr><td>
<mapinfo:localString string="dialog_choosefile"/>:
</td></tr><tr><td>
<mapxtreme:open>
</td></tr><tr><td align=center>
<br><input type=submit value="<mapinfo:localString string="dialog_open"/>">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton/>
</mapxtreme:open>
</td></tr></table>
</center>
</body>
</html>
</mapinfo:mapapp>
