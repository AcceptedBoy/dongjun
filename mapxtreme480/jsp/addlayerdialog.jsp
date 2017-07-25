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

if (document.all) {
  docObj = "document.all."
  styleObj = ".style"
} else {
  docObj = "document."
  styleObj = ""
}

offset = 175
performOnchange = false

function openselect(subcat) {
  popupselect = eval(docObj + subcat + styleObj)
  popupselect.visibility = "visible"
}

function closeselect(submenu,fieldname){
  hiddenfield = eval(docObj + fieldname)
  if (submenu.selectedIndex != 0) {
    numchoice = submenu.selectedIndex
    choice = submenu[numchoice].value
    //addlayer.layername.value = choice
    hiddenfield.value = choice
    submenu.selectedIndex = numchoice
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
    //close all layer lists
    subcat = "layers" + i
    popupselect = eval(docObj + subcat + styleObj)
    popupselect.visibility = "hidden"
    //close all mimetype lists
    subcat = "mimetypes" + i
    popupselect = eval(docObj + subcat + styleObj)
    popupselect.visibility = "hidden"
  }
  if (performOnchange == true) {
    letsopen = "layers" + cat.selectedIndex
    if (letsopen == "layers0") {
      alert("No category selected")
      choice = "- layer -"
      addlayer.layername.value = choice
      cat.focus()
    } else {
      openselect(letsopen)
      lock()
    }
    letsopen = "mimetypes" + cat.selectedIndex
    if (letsopen == "mimetypes0") {
      alert("No category selected")
      choice = "image/gif"
      addlayer.mimetypes.value = choice
      cat.focus()
    } else {
      openselect(letsopen)
      lock()
    }
  }
}


</script>
<title>Add Layer Dialog</title>

<body bgcolor="#CFCFCF">

<mapinfo:mapapp name="wmsclient">

<wms:addlayer />

</mapinfo:mapapp>


</body>
</html>
