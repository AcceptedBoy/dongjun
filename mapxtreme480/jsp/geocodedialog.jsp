<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapmarker" prefix="mapmarker" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_geocode"/></title>
</head>
<body bgcolor="#CFCFCF">

<center>
<mapmarker:geocode dialog="true">
	<mapmarker:addressInput>
		<table border=0>
		<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
		<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
		<tr>
		<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
		<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
		<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
		</tr>
		<tr><td colspan=3 align=center><input type=submit value="Geocode">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td></tr>
		</table>
	</mapmarker:addressInput>
	<mapmarker:errorInput>
		<b><mapmarker:geocodeError /></b>
		<table border=0>
		<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
		<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
		<tr>
		<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
		<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
		<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
		</tr>
		<tr><td colspan=3 align=center><input type=submit value="<mapinfo:localString string="dialog_geocode"/>">&nbsp;&nbsp;&nbsp;<input type=submit name="<%= com.mapinfo.jsptags.mapmarker.GeocodeBean.PARAMETER_CANCEL %>" value="<mapinfo:localString string="button_cancel"/>"></td></tr>
		</table>
	</mapmarker:errorInput>
	<mapmarker:candidatesInput>
		<mapmarker:candidates size="5" showLongLat="true" />
		<p><mapmarker:mapCandidate />&nbsp;&nbsp;&nbsp;<input type=submit name="<%= com.mapinfo.jsptags.mapmarker.GeocodeBean.PARAMETER_CANCEL %>" value="<mapinfo:localString string="button_cancel"/>">
	</mapmarker:candidatesInput>
	<input type=hidden name="redirect" value="/jsp/geocodedialog.jsp">
</mapmarker:geocode>
</center>

</mapinfo:mapapp>
</body>
</html>
