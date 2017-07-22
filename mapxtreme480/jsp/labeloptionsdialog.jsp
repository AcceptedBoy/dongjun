<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_labeloptions"/>: <mapxtreme:layername /></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<table border=0>
<mapxtreme:labelOptions dialog="true">
	<tr><td colspan=2><mapinfo:localString string="dialog_labelwith"/>:<br><mapxtreme:labelColumns /></td></tr>
	<tr><td colspan=2><hr></td></tr>
	<tr><td colspan=2><mapxtreme:labelZoomRange /> <mapinfo:localString string="dialog_labelzoomrange"/>:</td></tr>
	<tr>
		<td><mapinfo:localString string="dialog_minzoom"/></td>
		<td><mapxtreme:labelMinZoom size="8" /></td>
	</tr><tr>
		<td><mapinfo:localString string="dialog_maxzoom"/></td>
		<td><mapxtreme:labelMaxZoom size="8" /></td>
	</tr>
	<tr><td colspan=2><mapxtreme:labelDuplicates /> <mapinfo:localString string="dialog_duplicatetext"/></td></tr>
	<tr><td colspan=2><mapxtreme:labelOverlapping /> <mapinfo:localString string="dialog_overlappinglabels"/></td></tr>
	<tr>
		<td colspan=2 align=center><input type=submit value="   <mapinfo:localString string="dialog_ok"/>   ">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td>
	</tr>
	<input type=hidden name="redirect" value="/jsp/labeloptionsdialog.jsp">
</mapxtreme:labelOptions>
</table>
</center>

</body>
</html>
</mapinfo:mapapp>
