<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	import = "com.mapinfo.dp.util.Bucketer"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<html>
<head>
<title><mapinfo:localString string="dialog_themes"/></title>
</head>
<body bgcolor="#CFCFCF">
<table border=0 cellspacing=0 cellpadding=0 width="100%" height="100%"><tr><td valign=center align=center>

<mapxtreme:thematic dialog="true">
	<mapxtreme:themelayer>
		<mapinfo:localString string="dialog_selecttable"/>:<br>
		<mapxtreme:themeLayerList />
	</mapxtreme:themelayer>
	<mapxtreme:themecolumn>
		<mapinfo:localString string="dialog_selectcolumn"/>:<br>
		<mapxtreme:themeColumnList />
	</mapxtreme:themecolumn>
	<mapxtreme:themeoptions>
		<table border=0 cellspacing=2 cellpadding=0>
		<tr><td colspan=2><mapinfo:localString string="dialog_distributionmethod"/>:</td></tr><tr><td colspan=2 align=right><mapxtreme:themeDistributionType /></td></tr>
		<tr><td><mapinfo:localString string="dialog_numofranges"/>:</td><td valign=top><mapxtreme:themeBreaks /></td></tr>
		<tr><td valign=top>
			<table border=0 width='100%' cellspacing=0 cellpadding=0>
				<tr><td><mapinfo:localString string="dialog_colors"/>:</td><td align=right><font size=-1><mapinfo:localString string="dialog_hi"/></font></td></tr>
			</table></td>
		<td><mapxtreme:themeEndColor /></td></tr>
		<tr><td align=right><font size='-1'><mapinfo:localString string="dialog_low"/></font></td><td><mapxtreme:themeStartColor /></td></tr>
		</table>
	</mapxtreme:themeoptions>
<p><input type=submit value='   <mapinfo:localString string="dialog_ok"/>   '>&nbsp;&nbsp;&nbsp;<input type=submit name="cancel" value="<mapinfo:localString string="button_cancel"/>">
<input type=hidden name="redirect" value="/jsp/themewizard.jsp">
</mapxtreme:thematic>

</td></tr></table>
</body>
</html>
</mapinfo:mapapp>
