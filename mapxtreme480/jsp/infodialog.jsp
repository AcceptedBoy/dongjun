<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_infotool"/></title>
</head>
<body bgcolor="#FFFFFF">

<table border="1" cellspacing="0" cellpadding="2" width="400">
<mapxtreme:info>
	<tr><td colspan=2 bgcolor="#CFCFCF"><b><mapxtreme:layername layer="<%= layer.intValue() %>" /></b></td></tr>
	<mapxtreme:featureSet>
		<mapxtreme:feature>
			<tr><td><mapxtreme:featureName />:</td><td><mapxtreme:featureValue /></td></tr>
		</mapxtreme:feature>
	</mapxtreme:featureSet>
</mapxtreme:info>
</table>

</body>
</html>
</mapinfo:mapapp>
