<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_tableinfo"/>: <mapxtreme:layername /></title>
</head>
<body bgcolor="#FFFFFF">

<table border=1 cellspacing=0 cellpadding=2>
	<tr bgcolor="#CFCFCF"><td>&nbsp;</td>
	<mapxtreme:tableColumns>
		<td><b><mapxtreme:columnName /></b></td>
	</mapxtreme:tableColumns>
	</tr>
	<mapxtreme:tableinfo rows="100">
		<tr><td align=right><mapxtreme:rowNum /></td>
			<mapxtreme:tableColumns>
				<td><mapxtreme:columnValue /></td>
			</mapxtreme:tableColumns>
		</tr>
	</mapxtreme:tableinfo>
</table>

<p><mapxtreme:nextTableInfo />

</body>
</html>
</mapinfo:mapapp>
