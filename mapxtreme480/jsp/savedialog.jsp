<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_save"/></title>
</head>
<body bgcolor="#CFCFCF">
<center>
<table border=0 cellspacing=0 cellpadding=0 height="100%"><tr><td>
<mapinfo:localString string="dialog_filename"/>:
</td></tr><tr><td>
<mapxtreme:save>
</td></tr><tr><td align=center>
<br><input type=submit value="<mapinfo:localString string="dialog_save"/>">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton />
<input type=hidden name="redirect" value="/jsp/savedialog.jsp">
</mapxtreme:save>
</td></tr></table>
</center>
</body>
</mapinfo:mapapp>
</html>
