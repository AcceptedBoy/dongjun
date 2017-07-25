/**
 * COPYRIGHT 2003, MapInfo Corporation
 *
 *******************************************************************************
 * EVENT ATTRIBUTES ON GRAPHICS AND CONTAINER ELEMENTS.
 * Events from the SVG document will come into here. These funcitons will
 * determine the layer type and dispatch to functional methods accordingly.
 *
 * Attributes:
 * onfocusin="focusInEvent(evt);" onfocusout="focusOutEvent(evt);"
 * onactivate="activateEvent(evt);" onclick="clickEvent(evt);"
 * onmousedown="mouseDownEvent(evt);" onmouseup="mouseUpEvent(evt);"
 * onmouseover="mouseOverEvent(evt);" onmouseout="mouseOutEvent(evt);"
 * onload="loadEvent(evt);"
*******************************************************************************/

/**
 * This array constant contains a list of Events objects. For each event, the
 * corresponding event method will be called on each object.
**/
var EVENTS_OBJECTS = new Array(new Map());

function focusInEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].focusInEvent();
	}
	return true;
}

function focusOutEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].focusOutEvent();
	}
	return true;
}

function activateEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].activateEvent();
	}
	return true;
}

function clickEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].clickEvent();
	}
	return true;
}

function mouseDownEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].mouseDownEvent(evt);
	}
	return true;
}

function mouseUpEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].mouseUpEvent(evt);
	}
	return true;
}

function mouseMoveEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].mouseMoveEvent(evt);
	}
	return true;
}

function mouseOverEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].mouseOverEvent(evt);
	}
	//alert("Mouse over event: " + evt.getTarget());
	return true;
}

function mouseOutEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].mouseOutEvent(evt);
	}
	return true;
}

/*******************************************************************************
 * DOCUMENT-LEVEL EVENT ATTRIBUTES.
 * Events from the SVG element will come into here.
 *
 * Attributes:
 * onunload="unloadEvent(evt);" onabort="abortEvent(evt);"
 * onerror="errorEvent(evt);" onresize="resizeEvent(evt);"
 * onscroll="scrollEvent(evt);" onzoom="zoomEvent(evt);"
*******************************************************************************/

function loadEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].loadEvent();
	}
	return true;
}

function unloadEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].unloadEvent();
	}
	return true;
}

function abortEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].abortEvent();
	}
	return true;
}

function errorEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].errorEvent();
	}
	return true;
}

function resizeEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].resizeEvent();
	}
	return true;
}

function scrollEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].scrollEvent();
	}
	return true;
}

function zoomEvent(evt) {
	for(i=0; i<EVENTS_OBJECTS.length; i++) {
		EVENTS_OBJECTS[i].zoomEvent();
	}
	return true;
}
