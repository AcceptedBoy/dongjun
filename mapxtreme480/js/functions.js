/**
 * COPYRIGHT 2003, MapInfo Corporation
**/

/**
 * Returns the document object for the map withing the current svg document.
**/
function getMapDocument() {
	return document.embeds[MAP_EMBED_NAME].getSVGDocument();
}

/**
 * Returns the root element for the given SVGElement.
**/
function getRootElement(elt) {
	var outermostSVGElement = null;
	var ancestorSVG = null;
	if(elt.getParentNode() == null) {
		outermostSVGElement = elt;
	} else {
		ancestorSVG = elt;
	}
	while(true) {
		newAncestorSVG = ancestorSVG.getParentNode();
		if(newAncestorSVG == null) {
			break;
		} else {
			ancestorSVG = newAncestorSVG;
		}
		outermostSVGElement = ancestorSVG;
	}
	return outermostSVGElement;
}

/**
 * Returns the first parent with the given id.
 * @returns an SVGElement
**/
function getParentElement(startEl, id) {
	var ancestorSVG = startEl;
	while(true) {
		ancestorSVG = ancestorSVG.getParentNode();
		if(ancestorSVG == null || ancestorSVG.getAttribute("id") == id) {
			break;
		}
	}
	return ancestorSVG;
}

/**
 * Returns the parent element that represents the feature that the given object
 * belongs to.
 * @param svgobj the object whose feature we wish to find.
 * @return the feature element or null if it wasn't found.
**/
function getParentFeatureElement(svgobj) {
	var child   = svgobj;
	var parentn = child.parentNode;
	while(parentn != null && (!isElement(parentn) || /*parentn.getTagName()!="g" ||*/ parentn.getAttribute("id") == null
		|| parentn.getAttribute("id").substring(0, 6) != LAYER_PREFIX)
	) {
		child   = parentn;
		parentn = child.parentNode;
	}

	return parentn;
} //getParentFeatureElement

/**
 * Reloads the data from the server set at the current zoom. This will only
 * update the embed tag containing the SVG map.
**/
function refreshMapData() {

    if(window.status != "Loading map...") {
    	var svgdoc   = getMapDocument();
    	var svgdocel = svgdoc.documentElement;
    	var trans    = svgdocel.currentTranslate;
    	var width    = document.embeds[MAP_EMBED_NAME].window.innerWidth;
    	var height   = document.embeds[MAP_EMBED_NAME].window.innerHeight;
    	var scale    = svgdocel.currentScale;
    	var x  = width / 2;
    	var y  = height / 2;
    	var tx = trans.x;
    	var ty = trans.y;
    	var nx = x/scale + (( 0.0 - trans.x ) / scale);
    	var ny = y/scale + (( 0.0 - trans.y ) / scale);
    	var url      = REQUEST_HANDLER_SERVLET
    					+ "?appname=" + APP_NAME
    					+ "&mapinfobean=svgzoom"
    					+ "&zoomfactor=" + scale
    					+ "&centerx=" + nx
    					+ "&centery=" + ny
    					+ "&width=" + width
    					+ "&height=" + height;
    	document.embeds[MAP_EMBED_NAME].setSRC(url);
    	window.status = "Loading map...";
    }
} //refreshMapData

/**
 * Fixes the scale and translations after reloading the SVG document. It is safe
 * to call this on every document load.
**/
function fixScaleAndTranslate() {
	var svgdoc   = getMapDocument();
	var svgdocel = svgdoc.documentElement;
	svgdocel.currentScale = 1;
	svgdocel.currentTranslate.x = 0;
	svgdocel.currentTranslate.y = 0;
} //fixScaleAndTranslate

/**
 * Hides or shows the given layer.
 * Typical call from a checkbox element: onClick="toggleLayer(this, "Layer_x");
**/
function toggleLayer(checkbox, layer) {
	var svgdoc = getMapDocument();
	var svgobj = svgdoc.getElementById(layer);

	if(svgobj == null) {
		//If we don't find the object, we need to go back to server to get it.
		document.layercontrolform.submit();
	} else if(!checkbox.checked) { //Hide
		svgobj.setAttribute('style','visibility:hidden');
	} else { //Show
		svgobj.setAttribute('style','visibility:visible');
	}
} //toggleLayer

var originalFill = null;

/**
 * Hilites the given object.
 * @param svgobj Any object that supports the fill attribute.
**/
function highlightObject(svgobj) {
	if(svgobj.getNodeType() == ELEMENT_NODE) {
		originalFill = svgobj.getAttribute("fill");
		if(originalFill == null || originalFill == "none" || originalFill == "") {
			svgobj.setAttribute("fill", "#0000FF");
		} else {
			svgobj.setAttribute("fill", invertColor(originalFill));
		}
	}
} //highlightObject

/**
 * Restores the given object to its original color.
 * @param svgobj Any object that supports the fill attribute.
**/
function restoreHighlightObject(svgobj) {
	svgobj.setAttribute("fill", originalFill);
	originalFill = null;
} //restoreHighlightObject

/**
 * Creates a drop shadow filter if one doesn't already exist and applies it to
 * the given object.
 * @param svgobj Any object that supports the filter attribute.
**/
function setDropShadow(svgobj) {
	var svgdoc = document.embeds[MAP_EMBED_NAME].getSVGDocument();

	// Define an element for the  defs  section that looks like this:
	//<filter id="MIDropShadow" filterUnits="objectBoundingBox" x="-50%" y="-50%" width="200%" height="200%">
	//<feGaussianBlur in="SourceAlpha" stdDeviation="2" result="BlurAlpha"/>
	//<feOffset in="BlurAlpha" dx="4" dy="4" result="OffsetBlurAlpha"/>
	//<feMerge><feMergeNode in="OffsetBlurAlpha"/> <feMergeNode in="SourceGraphic"/></feMerge>
	//</filter>
	var dsFilter = svgdoc.getElementById('MIDropShadow');
	if (dsFilter == null) {
		var svgdeflist = getDefList();
		var svgdef = svgdeflist.item(0);
		// Build filter element
		var filt  = svgdoc.createElement('filter');
		filt.setAttribute('id', 'MIDropShadow');
		filt.setAttribute('filterUnits', 'objectBoundingBox');
		filt.setAttribute('x', '-50%');
		filt.setAttribute('y', '-50%');
		filt.setAttribute('width', '200%');
		filt.setAttribute('height', '200%');
		// Build feMerge
		var feMerge = svgdoc.createElement('feMerge');
		var feMergeNode1 = svgdoc.createElement('feMergeNode');
		feMergeNode1.setAttribute('in', 'OffsetBlurAlpha');
		var feMergeNode2 = svgdoc.createElement('feMergeNode');
		feMergeNode2.setAttribute('in', 'SourceGraphic');
		feMerge.appendChild(feMergeNode1);
		feMerge.appendChild(feMergeNode2);
		// build feOffset
		var feOffset = svgdoc.createElement('feOffset');
		feOffset.setAttribute('in', 'BlurAlpha');
		feOffset.setAttribute('dx', '4');
		feOffset.setAttribute('dy', '4');
		feOffset.setAttribute('result', 'OffsetBlurAlpha');
		// build feGaussianBlur
		var feGaussianBlur = svgdoc.createElement('feGaussianBlur');
		feGaussianBlur.setAttribute('in', 'SourceAlpha');
		feGaussianBlur.setAttribute('stdDeviation', '2');
		feGaussianBlur.setAttribute('result', 'BlurAlpha');
		// Combine elements
		filt.appendChild(feGaussianBlur);
		filt.appendChild(feOffset);
		filt.appendChild(feMerge);
		// add filter to defs
		svgdef.appendChild(filt);
	}

	//Set the object to use the drop shadow
	svgobj.setAttribute('filter', 'url(#MIDropShadow)');
} //setDropShadow

/**
 * Removes the drop shadow from the given object.
**/
function removeDropShadow(svgobj) {
	svgobj.setAttribute("filter", null);
} //removeDropShadow

/**
 * Turns on or off animation for a given layer.
 * @param checkbox Checkbox to check the status of animation for the layer.
 * @param layer The layer index to animate. This is a zero based index.
**/
function toggleAnimate(checkbox, layer) {
	var svgdoc = getMapDocument();
	var svgobj = svgdoc.getElementById(LAYER_PREFIX + (parseInt(layer)));
	if(svgobj != null) {
		animateElement(checkbox, svgobj);
	} else {
		alert("Layer must be visible to animate.");
		return false;
	}
} //toggleAnimate

/**
 * Returns a NodeList of "defs" elements. If the "defs" element doesn't exist
 * in the SVG doc, a new empty one is created.
**/
function getDefList() {
	var svgdoc = getMapDocument();
	var deflist = svgdoc.getElementsByTagName("defs");
	if(deflist == null || deflist.length<1) {
		var def = svgdoc.createElement("defs");
		svgdoc.appendChild(def);
		deflist = svgdoc.getElementsByTagName("defs");
	}
	return deflist;
} //getDefList

/**
 * Creates a radialGradient element and adds it to the first defs element.
**/
function createGradientDef() {
	var svgdoc = getMapDocument();
	var svgdeflist = getDefList();
	var svgdef = svgdeflist.item(0);
	var gradient = svgdoc.getElementById("radGrad");

	if(gradient == null) {
		var gradient = svgdoc.createElement("radialGradient");
		gradient.setAttribute("id", "radGrad");
		gradient.setAttribute("gradientUnits", "objectBoundingBox");
		gradient.setAttribute("x1", "0");
		gradient.setAttribute("y1", "1");
		gradient.setAttribute("x2", "0");
		gradient.setAttribute("y2", "0");

		var stop1 = svgdoc.createElement("stop");
		stop1.setAttribute("offset", "0");
		stop1.setAttribute("style", "stop-color:rgb(213,248,192)");

		var stop2 = svgdoc.createElement("stop");
		stop2.setAttribute("offset", "1");
		stop2.setAttribute("style", "stop-color:rgb(174,208,164)");

		gradient.appendChild(stop1);
		gradient.appendChild(stop2);
		svgdef.appendChild(gradient);
	}
} //createGradientDef

/** The fill prior to setting a gradient fill to the object. **/
var originalGradientFill = null;

/**
 * Turns on or off the gradient effect for the given layer. The layer parameter
 * should be the zero based index of the layer to toggle.
**/
function toggleGradient(checkbox, layer) {
	var svgdoc = getMapDocument();
	var svglayer = svgdoc.getElementById(LAYER_PREFIX + layer);

	if(layerIsLoaded(layer)) {
		createGradientDef();

		if(checkbox.checked) {
			originalGradientFill = svglayer.getAttribute("fill");
			svglayer.setAttribute('fill', 'url(#radGrad)');
		} else {
			svglayer.setAttribute('fill', originalGradientFill);
			originalGradientFill = null;
		}
		return true;
	} else {
		alert("Layer must be visible to apply any filters.");
		return false;
	}
} //toggleGradient

/**
 * Returns true if the layer has been loaded in the SVG document.
**/
function layerIsLoaded(layer) {
	if(layer != null) {
		var svgdoc = getMapDocument();
		var svglayer = svgdoc.getElementById(LAYER_PREFIX + layer);
		return svglayer != null;
	}
	return false;
} //layerIsLoaded

function animateElement(checkbox, svgobj) {
	var svgobj;
	var svgdoc = getMapDocument();
	if (checkbox.checked) {
		var anim  = svgdoc.createElement('animate');
		anim.setAttribute('attributeName', 'opacity');
		anim.setAttribute('attributeType', 'CSS');
		anim.setAttribute('id', 'pulsate');
		anim.setAttribute('values', '1;0.1;0.1;1');
		anim.setAttribute('dur', '2.5s');
		anim.setAttribute('repeatCount', 'indefinite');

		svgobj.appendChild(anim);
	} else {
		var kids = svgobj.getElementsByTagName('animate');
		var kid = kids.item(0);
		svgobj.removeChild(kid);
	}
} //animateElement

/**
 * Returns the inverse of the given color.
 * @param color The hexadecimal form of the color.
**/
function invertColor(color) {
	var rgb = hexToRGB(color);
	for(i=0; i<rgb.length; i++) {
		rgb[i] = (rgb[i] < 128) ? (rgb[i] += 128) : (rgb[i] -= 128);
	}
	return rgbToHex(rgb[0], rgb[1], rgb[2]);
} //invertColor

/**
 * Converts the hexadecimal string to the red, green and blue compenents as
 * integers. The results are stored in an Array.
**/
function hexToRGB(hex) {
	hex = hex.toUpperCase();
	if(hex.charAt(0) == "#") hex = hex.substring(1,hex.length);
	var rgb = new Array(3);
	rgb.r = hex.substring(0,2);
	rgb.g = hex.substring(2,4);
	rgb.b = hex.substring(4,6);
	rgb.r = parseInt(rgb.r,16);
	rgb.g = parseInt(rgb.g,16);
	rgb.b = parseInt(rgb.b,16);
	if(isNaN(rgb.r)) rgb.r = 0;
	if(isNaN(rgb.g)) rgb.g = 0;
	if(isNaN(rgb.b)) rgb.b = 0;
	return rgb;
} //hexToRGM

/**
 * Turns the red, green, and blue components of the color a hexadecimal string.
**/
function rgbToHex(r, g, b) {
	var n = Math.round(b);
	n += Math.round(b) << 8;
	n += Math.round(r) << 16;
	return decToHex(n);
} //rgbToHex

/**
 * Turns decimal integer into hexadecimal string.
**/
function decToHex(num) {
	var i = 0; var j = 20;
	var str = "#";
	while(j >= 0) {
		i = (num >> j)%16;
		if(i >= 10) {
			if(i == 10) str += "A";
			else if(i == 11) str += "B";
			else if(i == 12) str += "C";
			else if(i == 13) str += "D";
			else if(i == 14) str += "E";
			else str += "F";
		} else {
			str += i;
		}
		j -= 4;
	}
	return str;
} //decToHex

/**
 * Return a boolean value telling whether the first argument is an Element object.
**/
function isElement() {
	if(typeof arguments[0] == 'object' && arguments[0].constructor) {
		var criterion =	arguments[0].constructor.toString().match(/element/i);
		return (criterion != null);
	} else if(typeof arguments[0] == 'object' && arguments[0].getTagName) {
		return true;
	}
	return false;
}

/**
 * Return a boolean value telling whether the first argument is an Array object.
**/
function isArray() {
	if(typeof arguments[0] == 'object' && arguments[0].constructor) {
		var criterion =	arguments[0].constructor.toString().match(/array/i);
		return (criterion != null);
	}
	return false;
}

/**
 * Return a boolean value telling whether the first argument is a string.
**/
function isString() {
	if(typeof arguments[0] == 'string') return true;
	if(typeof arguments[0] == 'object' && arguments[0].constructor) {
		var criterion = arguments[0].constructor.toString().match(/string/i);
		return (criterion != null);
	}
	return false;
}

