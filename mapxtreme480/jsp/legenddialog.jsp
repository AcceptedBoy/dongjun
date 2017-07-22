<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	import = "com.mapinfo.mapj.Layer, com.mapinfo.theme.Theme"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<html>
<head>
<title><mapinfo:localString string="dialog_legend"/></title>
</head>
<body bgcolor="#FFFFFF">
<center>

<mapxtreme:legendlist showRangedThemes="true" showIndividualValueThemes="true" showOverrideThemes="false" showSelectionThemes="false">
	<p><b><%= layer.getName() %></b>
	<br><mapxtreme:legendelement />
</mapxtreme:legendlist>

</center>
</body>
</html>
</mapinfo:mapapp>
