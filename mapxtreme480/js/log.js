/**
 * COPYRIGHT 2003, MapInfo Corporation
**/

var LOG_LEVEL_ERROR = 0;
var LOG_LEVEL_WARN  = 1;
var LOG_LEVEL_INFO  = 2;
var LOG_LEVEL_DEBUG = 3;

/** Outputs debuging messages. **/
function debug(str) {
	if(LOG_LEVEL >= LOG_LEVEL_DEBUG) {
		window.status = "Debug: " + str;
		alert("Debug: " + str);
	}
}

/** Outputs information messages. **/
function info(str) {
	if(LOG_LEVEL >= LOG_LEVEL_INFO) {
		window.status = "Info: " + str;
		alert("Info: " + str);
	}
}

/** Outputs warning messages. **/
function warn(str) {
	if(LOG_LEVEL >= LOG_LEVEL_WARN) {
		window.status = "Warn: " + str;
		alert("Warn: " + str);
	}
}

/** Outputs error messages. **/
function error(str) {
	if(LOG_LEVEL >= LOG_LEVEL_ERROR) {
		window.status = "Error: " + str;
		alert("Error: " + str);
	}
}