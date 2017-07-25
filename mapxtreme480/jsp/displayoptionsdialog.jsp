<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_displayoptions" />: <mapxtreme:layername /></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<table border=0>
<mapxtreme:displayOptions dialog="true">
	<tr><td colspan=2><mapxtreme:displayZoomRange /> <mapinfo:localString string="dialog_zoomrange" />:</td></tr>
	<tr>
		<td><mapinfo:localString string="dialog_minzoom" /></td>
		<td><mapxtreme:displayMinZoom size="8" /></td>
	</tr><tr>
		<td><mapinfo:localString string="dialog_maxzoom" /></td>
		<td><mapxtreme:displayMaxZoom size="8" /></td>
	</tr><tr>
		<td colspan=2 align=center><input type=submit value="   <mapinfo:localString string="dialog_ok" />   ">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td>
	</tr>
	<input type=hidden name="redirect" value="/jsp/displayoptionsdialog.jsp">
</mapxtreme:displayOptions>
</table>
</center>

</body>
</html>
</mapinfo:mapapp>
