<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_fontoptions" />: <mapxtreme:layername /></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<table border=0>
<mapxtreme:fontOptions dialog="true">
	<tr><td><mapinfo:localString string="dialog_font" />:</td></tr>
	<tr><td><mapxtreme:fontList /></td></tr>
	<tr><td><mapinfo:localString string="dialog_size" />: <mapxtreme:fontSize/>  <mapinfo:localString string="dialog_color" />: <mapxtreme:fontColor /></td></tr>
	<tr><td><mapxtreme:fontHalo /> <mapinfo:localString string="dialog_halo"/> <mapxtreme:fontHaloColor /></td></tr>
	<tr><td><mapxtreme:fontBold /> <mapinfo:localString string="dialog_bold"/></td></tr>
	<tr><td><mapxtreme:fontItalic /><mapinfo:localString string="dialog_italic"/></td></tr>
	<tr><td><mapxtreme:fontUnderline /><mapinfo:localString string="dialog_underline"/></td></tr>
	<tr><td align=center><input type=submit value="   <mapinfo:localString string="dialog_ok"/>   ">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td></tr>
	<input type=hidden name="redirect" value="/jsp/fontoptionsdialog.jsp">
</mapxtreme:fontOptions>
</table>
</center>

</body>
</html>
</mapinfo:mapapp>
