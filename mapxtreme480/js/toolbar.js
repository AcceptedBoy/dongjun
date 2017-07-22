/**
 * toolbar.js
 * COPYRIGHT 2001, MapInfo Corporation
**/

/**
 * This function changes the image in the toolbar to indicate that that tool
 * has been selected. It also sets document.mapcontrol.mapinfobean.value
 * so that when the user clicks on the map, its value will be passed
 * to the RequestHandler servlet. When developing tools that require
 * the user to click on the map (e.g. zooming, panning, etc.) they must
 * call this function. For example, the HTML for the zoom out tool is:
 *
 * <a href='' onClick="changeImage('zoomout'); return false;">
 * <img src='/mapcontrols13/images/zoomout.gif' border=0 name=zoomout alt='Zoom Out'></a>
 *
 * Note that each tool should have two gif images in the IMAGES_ALIAS
 * directory with one ending in s.gif that is the gif shown when the
 * tool has been selected.
 *
 * When the user clicks on the map, the map form will be submitted
 * to the RequestHandler servlet. One of the paramters passed to the
 * servlet is "mapinfobean." When developing your own Control or Tool, you
 * can check the value of this parameter to determine if the user is
 * requesting information regarding your Control or Tool.
**/
function changeImage(imgName) {
	if(!document.mapcontrol.mapinfobean) { return; }
	var testString = imgName + "s.gif";
	document.mapcontrol.target = "_self";
	//Toggle off the image selected
	if(document.images[imgName].src.indexOf(testString) != -1) {
		document.images[imgName].src = IMAGES_ALIAS + imgName + ".gif";
		window.focus();
		if(document.mapcontrol.mapinfobean) { document.mapcontrol.mapinfobean.value=""; }
		return;
	}
	//Turn off all of the images.
	if(document.recenter) {
		document.recenter.src=IMAGES_ALIAS + "recenter.gif";
	}
	if(document.zoomin) {
		document.zoomin.src=IMAGES_ALIAS + "zoomin.gif";
	}
	if(document.zoomout) {
		document.zoomout.src=IMAGES_ALIAS + "zoomout.gif";
	}
	if(document.infotool) {
		document.infotool.src=IMAGES_ALIAS + "infotool.gif";
	}
	if(document.radius) {
		document.radius.src=IMAGES_ALIAS + "radius.gif";
	}
	if(document.select) {
		document.select.src=IMAGES_ALIAS + "select.gif";
	}
	/***** ADD ADDITIONAL TOOLS HERE *****/
	
	//Turn on the image passed into this function
	document.images[imgName].src = IMAGES_ALIAS + imgName + "s.gif";
	if(document.mapcontrol.mapinfobean) { document.mapcontrol.mapinfobean.value=imgName; }
	window.focus();
}
