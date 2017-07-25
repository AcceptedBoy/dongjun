<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_zoomnumeric"/></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<mapxtreme:zoomNumeric dialog="true">
<table border=0>
<tr><td colspan=4><mapinfo:localString string="dialog_zoom"/>:&nbsp;&nbsp;&nbsp;<mapxtreme:zoomRange />&nbsp;<mapxtreme:distanceUnits /></td>
<tr><td colspan=4><mapinfo:localString string="dialog_center"/></td></tr>
<tr><td><mapinfo:localString string="dialog_x"/>:</td><td><mapxtreme:centerX /></td><td><mapinfo:localString string="dialog_y"/>:</td><td><mapxtreme:centerY /></td></tr>
<tr><td colspan=4 align=center><input type=submit value='   <mapinfo:localString string="dialog_ok"/>   '>&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td></tr>
</table>
<input type=hidden name="redirect" value="/jsp/mapdisplaydialog.jsp">
</mapxtreme:zoomNumeric>
</center>

</body>
</mapinfo:mapapp>
</html>
