<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<html>
<head>
<title><mapinfo:localString string="dialog_selectioninfo"/></title>
</head>
<body bgcolor="#FFFFFF">

<mapxtreme:selectionInfo>
	<p><b><%= layerObj.getName() %></b><br>
	<table border=1><tr bgcolor="#CFCFCF">
	<mapxtreme:feature>
		<td><mapxtreme:featureName /></td>
	</mapxtreme:feature>
	</tr>
	<mapxtreme:featureSet>
		<tr>
		<mapxtreme:feature>
			<td><mapxtreme:featureValue /></td>
		</mapxtreme:feature>
		</tr>
	</mapxtreme:featureSet>
	</table>
</mapxtreme:selectionInfo>

</body>
</html>
</mapinfo:mapapp>
