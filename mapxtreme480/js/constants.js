/**
 * COPYRIGHT 2003, MapInfo Corporation
 *
 * Naming convetions:
 *    - All relevant elements should have an id attribute to uniquely identify it.
 *    - Elements that represent visual elements should have a type attribute
 *      that describes what it is. For example, all buttons have a type of
 *      button.
 *    - Constants are in all capitol letters.
 *    - Global data members that may change should begin with m_.
**/

/*******************************************************************************
 * Node Types.
*******************************************************************************/
var ELEMENT_NODE                = 1;
var ATTRIBUTE_NODE              = 2;
var TEXT_NODE                   = 3;
var CDATA_SECTION_NODE          = 4;
var ENTITY_REFERENCE_NODE       = 5;
var ENTITY_NODE                 = 6;
var PROCESSING_INSTRUCTION_NODE = 7;
var COMMENT_NODE                = 8;
var DOCUMENT_NODE               = 9;
var DOCUMENT_TYPE_NODE          = 10;
var DOCUMENT_FRAGMENT_NODE      = 11;
var NOTATION_NODE               = 12;

var MAP_EMBED_NAME = "svgmap";
var LAYER_PREFIX   = "Layer_";
var LOG_LEVEL      = LOG_LEVEL_DEBUG;