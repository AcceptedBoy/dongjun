/**
 * resize.js
 * COPYRIGHT 2000, MapInfo Corporation
**/

/**
 * This fuction will refresh this page so that the map can be resized
 * to fit the window.
**/
function changeSize() {
	resizeCount++;
	setTimeout("refreshPage()", 1000);
}
function refreshPage() {
	resizeCurrent++;
	if(resizeCount==resizeCurrent) {
		this.document.location.href = REQUEST_HANDLER_SERVLET + "?mapinfobean=resizebean&appname=" + APP_NAME + "&width=" + document.mapwidth.width + "&height=" + (document.mapheight.height-60);
	}
}

var resizeCount=0;
var resizeCurrent=0;

/**
 * To NOT have the map change size when the browser window
 * changes size, comment out the following line.
**/
window.onresize=changeSize;
