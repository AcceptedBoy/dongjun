/**
 * rubberband.js
 * COPYRIGHT 2001, MapInfo Corporation
**/

function startRubber(evt) {
 	if(document.mapcontrol.mapinfobean.value!="zoomin") {
 		//The zoom in tool hasn't been selected
 		return;
 	}
 	
	if(document.all) { //IE
	 	//Set the coordinates of the mapimage
		mapX = getOffsetLeft(document.all.mapimage);
		mapY = getOffsetTop(document.all.mapimage);
		//Set the scroll position
		scrollX = document.body.scrollLeft;
		scrollY = document.body.scrollTop;
	} else if(document.getElementById) {
	 	//Set the coordinates of the mapimage
		mapX = document.map.x;
		mapY = document.map.y;
		//Set the scroll position
		scrollX = window.pageXOffset;
		scrollY = window.pageYOffset;
	} else if(document.layers) {
	 	//Set the coordinates of the mapimage
		mapX = document.map.x;
		mapY = document.map.y;
		//Set the scroll position
		scrollX = window.pageXOffset;
		scrollY = window.pageYOffset;
	}
 	
	if(document.all) {
		if((event.x+scrollX)<mapX || (event.y+scrollY)<mapY 
			|| (event.x+scrollX)>(document.all.mapimage.width + mapX) 
			|| (event.y+scrollY)>(document.all.mapimage.height + mapY)
		) {
			//The user didn't click over the map
			return;
		}
		document.mapcontrol.mapinfobean.value="zoombox";
		var r = document.all.rubberBand;
		r.style.width     = 0;
		r.style.height    = 0;
		r.style.pixelLeft = event.x + scrollX;
		r.style.pixelTop  = event.y + scrollY;
		r.style.visibility= 'visible';
		startX            = (event.x+scrollX) - mapX;
		startY            = (event.y+scrollY) - mapY;
	} else if(document.getElementById) {
		return; //todo: currently doesn't work with Netscape
		if(evt.clientX<mapX || evt.clientY<mapY) {
			return;
		}
		document.mapcontrol.mapinfobean.value="zoombox";
		var r = document.getElementById('rubberBand');
		r.style.width  = 0;
		r.style.height = 0;
		r.style.left   = evt.clientX + 'px';
		r.style.top    = evt.clientY + 'px';
		r.style.visibility = 'visible';
		startX         = r.style.left;
		startY         = r.style.top;
	} else if(document.layers) {
		//todo: currently doesn't work with Netscape
		return;
		if(evt.x<mapX || evt.y<mapY) {
			return;
		}
		document.mapcontrol.mapinfobean.value="zoombox";
		var r = document.rubberBand;
		r.clip.width  = 0;
		r.clip.height = 0;
		r.left        = evt.x;
		r.top         = evt.y;
		r.visibility  = 'show';
		startX        = evt.x;
		startY        = evt.y;
	}
	
	if (document.layers) {
		document.captureEvents(Event.MOUSEMOVE);
	}
	document.onmousemove = moveRubber;
}

function moveRubber(evt) {
	if(document.all) {
		var r = document.all.rubberBand;
		if( (event.x+scrollX)<(startX+mapX)) {
			r.style.pixelLeft = (event.x+scrollX);
			r.style.width = startX - (event.x+scrollX-mapX);
		} else {
			r.style.pixelLeft = startX + mapX;
			r.style.width = (event.x+scrollX) - r.style.pixelLeft;
		}
		if( (event.y+scrollY)<(startY+mapY)) {
			r.style.pixelTop = (event.y+scrollY);
			r.style.height = startY - (event.y+scrollY-mapY);
		} else {
			r.style.pixelTop = startY + mapY;
			r.style.height = (event.y+scrollY) - r.style.pixelTop;
		}
	} else if(document.getElementById) {
		var r = document.getElementById('rubberBand');
		r.style.width = evt.clientX - parseInt(r.style.left);
		r.style.height = evt.clientY - parseInt(r.style.top);
	} else if(document.layers) {
		var r = document.rubberBand;
		r.clip.width = evt.x - r.left;
		r.clip.height = evt.y - r.top;
		r.document.open();
		r.document.write('<TABLE WIDTH="' + r.clip.width + '" HEIGHT="' + r.clip.height + '" BORDER="1"><TR><TD><\/TD><\/TR><\/TABLE>');
		r.document.close();
	}
}

var newURL;
function stopRubber (evt) {
	if(document.mapcontrol.mapinfobean.value!="zoombox") {
		return;
	}
	
	if(document.layers) {
		document.releaseEvents(Event.MOUSEMOVE);
	}
	document.onmousemove = null;
	
	if(document.all) {
		var x1=startX;
		var y1=startY;
		var x2=((event.x+scrollX)-mapX);
		var y2=((event.y+scrollY)-mapY);
		newURL = REQUEST_HANDLER_SERVLET + "?mapinfobean=zoomin&appname=" + APP_NAME + "&zoombox=true"
			+ "&x1=" + ((x1<x2)?(x1):(x2))
			+ "&y1=" + ((y1<y2)?(y1):(y2))
			+ "&x2=" + ((x1<x2)?(x2):(x1))
			+ "&y2=" + ((y1<y2)?(y2):(y1));
		document.location.href = newURL;
	}
}


function getOffsetLeft(el) {
	var ol = el.offsetLeft;
	while((el = el.offsetParent) != null)
		ol += el.offsetLeft;
	return ol;
}
function getOffsetTop(el) {
	var ol = el.offsetTop;
	while((el = el.offsetParent) != null)
		ol += el.offsetTop;
	return ol;
}

document.onmousedown = startRubber;
document.onmouseup = stopRubber;

//The coordinates where the users mousedown event occured.
var startX, startY;
//The coordinates of the upper left hand corner of the map.
var mapX=0, mapY=0;
//The scroll position when the user mousedown event occured.
var scrollX=0, scrollY=0;
