<html>
<head>
<%@ taglib uri="/mapinfo" prefix="mapinfo" %>
<%@ taglib uri="/mapxtreme" prefix="mapxtreme" %>
<%@ taglib uri="/wms" prefix="wms" %>

<%@ page
	language = "java"
	contentType = "text/html;charset=UTF-8"
%>
<SCRIPT LANGUAGE="JavaScript">

offset = 175
performOnchange = false
if (document.all) {
  docObj = "document.all."
  styleObj = ".style"
  } else {
  docObj = "document."
  styleObj = ""
}
function openselect(subcat) {
  popupselect = eval(docObj + subcat + styleObj)
  popupselect.visibility = "visible"
}

function closeselect(submenu,subcat){
  popupselect = eval(docObj + subcat + styleObj)
  if (submenu.selectedIndex != 0) {
    popupselect.visibility = "hidden"
    numchoice = submenu.selectedIndex
    choice = submenu[numchoice].value
    addlayer.layername.value = choice
    submenu.selectedIndex = 0
  }
}
function lock() {
  performOnchange = false
}
function unlock() {
  performOnchange = true
}
function selectSub(cat) {
  for (i=1; i <= catnumber; i++) {
    subcat = "sub" + i
    popupselect = eval(docObj + subcat + styleObj)
    popupselect.visibility = "hidden"
  }
  if (performOnchange == true) {
    letsopen = "sub" + cat.selectedIndex
    if (letsopen == "sub0") {
      alert("No category selected")
      choice = "- layer -"
      addlayer.layername.value = choice
      cat.focus()
      } else {
      openselect(letsopen)
      lock()
    }
  }
}


</script>
<title>Remove Layer Dialog</title>

<body bgcolor="#CFCFCF">

<mapinfo:mapapp name="wmsclient">

<wms:removelayer />

</mapinfo:mapapp>

</body>
</html>
