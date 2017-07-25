<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapmarker" prefix="mapmarker" %>
<%@ taglib uri="/routing" prefix="routing" %>
<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<mapinfo:mapapp>
<title><mapinfo:localString string="dialog_routing"/></title>
</head>
<body bgcolor="#CFCFCF">

<routing:route dialog="true">
	<routing:addressInput>
		<center>
		<table border=0>
		<routing:startingAddress>
			<tr><td><b><mapinfo:localString string="dialog_startingaddress"/>:</b></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
			<tr>
			<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
			<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
			<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
			</tr>
		</routing:startingAddress>
		<routing:destinationAddress>
			<tr><td colspan=3><b><mapinfo:localString string="dialog_destinationaddress"/>:</b></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
			<tr>
			<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
			<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
			<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
			</tr>
		</routing:destinationAddress>
		<tr><td colspan=3 align=center><routing:quickestTime />Quickest Time <routing:shortestDistance />Shortest Distance
		<tr><td colspan=3 align=center><input type=submit value="<mapinfo:localString string="dialog_findroute"/>">&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></td></tr>
		</table>
		</center>
	</routing:addressInput>
	<routing:errorInput>
		<center>
		<p><routing:routingError />
		<table border=0>
		<routing:startingAddress>
			<tr><td><b><mapinfo:localString string="dialog_startingaddress"/>:</b></td></tr>
			<tr><td colspan=3><mapmarker:geocodeError /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
			<tr>
			<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
			<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
			<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
			</tr>
		</routing:startingAddress>
		<routing:destinationAddress>
			<tr><td colspan=3><b><mapinfo:localString string="dialog_destinationaddress"/>:</b></td></tr>
			<tr><td colspan=3><mapmarker:geocodeError /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_firm"/><br><mapmarker:firm size="40" /></td></tr>
			<tr><td colspan=3><mapinfo:localString string="dialog_street"/><br><mapmarker:street size="40" /><br><mapmarker:street2 size="40" /></td></tr>
			<tr>
			<td><mapinfo:localString string="dialog_city"/><br><mapmarker:city size="15" /></td>
			<td><mapinfo:localString string="dialog_countrysubdivision"/><br><mapmarker:countrySubdivision size="3" /></td>
			<td><mapinfo:localString string="dialog_postalcode"/><br><mapmarker:postalCode size="5" />-<mapmarker:postalAddOnCode size="4" /></td>
			</tr>
		</routing:destinationAddress>
		<tr><td colspan=3 align=center><input type=submit value="<mapinfo:localString string="dialog_findroute"/>">&nbsp;&nbsp;&nbsp;<routing:newRoute /></td></tr>
		</table>
		</center>
	</routing:errorInput>
	<routing:candidatesInput>
		<routing:startingAddress>
			<p><mapinfo:localString string="dialog_startingcandidates"/>:<br><mapmarker:candidates size="4" />
		</routing:startingAddress>
		<routing:destinationAddress>
			<p><mapinfo:localString string="dialog_destinationcandidates"/>:<br><mapmarker:candidates size="4" />
		</routing:destinationAddress>
		<p><center><input type=submit value="<mapinfo:localString string="dialog_findroute"/>">&nbsp;&nbsp;&nbsp;<routing:newRoute /></center>
	</routing:candidatesInput>
	<routing:routeResultInput>
		<routing:startingAddress>
			<p><mapinfo:localString string="dialog_startingaddress"/>: <mapmarker:street readOnly="true" />, <mapmarker:city readOnly="true" />, <mapmarker:countrySubdivision readOnly="true" />, <mapmarker:postalCode readOnly="true" />-<mapmarker:postalAddOnCode readOnly="true" />
		</routing:startingAddress>
		<routing:destinationAddress>
			<br><mapinfo:localString string="dialog_destinationaddress"/>: <mapmarker:street readOnly="true" />, <mapmarker:city readOnly="true" />, <mapmarker:countrySubdivision readOnly="true" />, <mapmarker:postalCode readOnly="true" />-<mapmarker:postalAddOnCode readOnly="true" />
		</routing:destinationAddress>
		<p><table border=1  bgcolor='#e9e9e9' cellpadding="2" cellspacing="0">
		<routing:routeResult>
			<tr bgcolor="#CFCFCF"><td><routing:direction zoomToIntersection="true" /></td><td><routing:time /> <routing:timeUnit /></td><td><routing:distance /> <routing:distanceUnit /></td></tr>
		</routing:routeResult>
		<tr bgcolor="#CFCFCF"><td>&nbsp;</td><td><b><routing:totalTime /> <routing:timeUnit /></b></td><td><b><routing:totalDistance /> <routing:distanceUnit /></b></td></tr>
		</table>
		<p><center><routing:newRoute />&nbsp;&nbsp;&nbsp;<mapinfo:cancelButton /></center>
	</routing:routeResultInput>
	<input type=hidden name="redirect" value="/jsp/routingdialog.jsp">
</routing:route>

</mapinfo:mapapp>
</body>
</html>
