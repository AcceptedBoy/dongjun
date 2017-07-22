<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_viewentirelayer"/></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<mapxtreme:viewEntireLayer dialog="true">
<table border=0 height="100%">
<tr><td align=center><mapinfo:localString string="dialog_viewentirelayer"/>:<br><mapxtreme:viewEntireLayerList /></td>
<tr><td align=center><input type=submit value='   <mapinfo:localString string="dialog_ok"/>   '>&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td></tr>
</table>
<input type=hidden name="redirect" value="/jsp/viewentirelayerdialog.jsp">
</mapxtreme:viewEntireLayer>
</center>

</body>
</mapinfo:mapapp>
</html>
