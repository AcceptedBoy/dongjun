/**
 * COPYRIGHT 2003, MapInfo Corporation
**/

function Map() {
	var t = new Events();
	for(var i in t) {
		this[i] = t[i]; // copy the members and methods
	}

	this.zoomEvent = function(evt) {
		var svgdocel = getMapDocument().documentElement;
		if(svgdocel != null) {
			var scale = svgdocel.currentScale;
			if(scale < 1) { //auto refresh the data when zooming out
				refreshMapData();
			}
		}
	}

	this.loadEvent = function(evt) {
		var svgdocel = getMapDocument().documentElement;
		svgdocel.currentScale = 1;
		svgdocel.currentTranslate.x = 0;
		svgdocel.currentTranslate.y = 0;
		window.status = "";
	}

	this.mouseOverEvent = function(evt) {
		var svgobj  = evt.getTarget();
		var parentn = getParentFeatureElement(svgobj);

		//Check to see if this is a layer
		if(parentn != null) {
			var name = parentn.getAttribute("id");
			if(name != null) {
				name = name.substring(name.indexOf("_")+1, name.length);

				if(layerIsLoaded(name)
					&& document.layercontrolform.elements["highlight" + name] != null
					&& document.layercontrolform.elements["highlight" + name].checked
				) { //highlight the feature
					var hl = svgobj.getParentNode();
					if(hl == parentn) {
						hl = svgobj;
					}
					setDropShadow(hl);
					highlightObject(hl);
				}
			}
		}
	}

	this.mouseOutEvent = function(evt) {
		var svgobj  = evt.getTarget();
		var parentn = getParentFeatureElement(svgobj);

		//Check to see if this is a layer
		if(parentn != null) {
			var name = parentn.getAttribute("id");
			if(name != null) {
				name = name.substring(name.indexOf("_")+1, name.length);

				if(layerIsLoaded(name)
					&& document.layercontrolform.elements["highlight" + name] != null
					&& document.layercontrolform.elements["highlight" + name].checked
				) { //restore the feature
					var hl = svgobj.getParentNode();
					if(hl == parentn) {
						hl = svgobj;
					}
					removeDropShadow(hl);
					restoreHighlightObject(hl);
				}
			}
		}
	}
}
