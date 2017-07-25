<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_layercontrol"/></title>
</head>
<body bgcolor="#FFFFFF">

<center>
<mapxtreme:layercontrol dialog="true">
	<table border=0 cellspacing=0 cellpadding=2>
	<tr bgcolor="#C1C1C1"><td><img src="../images/visible.gif" alt="<mapinfo:localString string="dialog_visible"/>"></td><td><img src="../images/autolabel.gif" alt="<mapinfo:localString string="dialog_label"/>"><td><img src="../images/selectable.gif" alt="<mapinfo:localString string="dialog_select"/>"></td><td></td><td colspan=5><b><mapinfo:localString string="dialog_name"/></b></td></tr>
	<mapxtreme:layerlist>
	<tr>
		<td><mapxtreme:visible/></td>
		<td><mapxtreme:label/></td>
		<td><mapxtreme:select/></td>
		<td align=right><mapxtreme:layerIndex /></td>
		<% if(layer!=null && featureLayer.booleanValue()) { %>
			<td><mapxtreme:layername tableInfo="true" page="tableinfodialog.jsp" /></td>
			<td><mapxtreme:displayOptionsTool page="displayoptionsdialog.jsp" /></td>
			<td><mapxtreme:labelOptionsTool page="labeloptionsdialog.jsp" /></td>
			<td><mapxtreme:fontOptionsTool page="fontoptionsdialog.jsp" /></td>
		<% } else if (layer!=null) { %>
			<td><mapxtreme:layername tableInfo="false" /></td>
			<td colspan=3><mapxtreme:displayOptionsTool page="displayoptionsdialog.jsp" /></td>
		<% } else { %>
			<td colspan=4><mapxtreme:layername /></td>
		<% } %>
		<td><mapxtreme:removeLayer width="20" height="20" /></td>
	</tr>
	<input type=hidden name="redirect" value="/jsp/layercontroldialog.jsp">
	</mapxtreme:layerlist>
	</table>
	<br><input type=submit value="<mapinfo:localString string="dialog_refresh"/>" />&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton />
</mapxtreme:layercontrol>
</center>

</body>
</html>
</mapinfo:mapapp>
