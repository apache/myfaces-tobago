/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 25, 2002 at 12:30:25 PM.
 * $Id$
 */
package com.atanion.tobago;

public class TobagoConstants {

  public static final String SUBCOMPONENT_SEP = "::";

  public static final String ATTR_ACTION = "action";
  public static final String ATTR_ACTION_LISTENER = "actionListener";
  public static final String ATTR_ALIGN = "align";
  public static final String ATTR_ALT = "alt";
  public static final String ATTR_BODY_CONTENT = "bodyContent";
  public static final String ATTR_BORDER = "border";
  public static final String ATTR_CHARSET = "charset";
  public static final String ATTR_CELLSPACING = "layoutCellspacing";
  public static final String ATTR_CLASSES_BLOCKS = "cssClassesBlocks";
  public static final String ATTR_COLUMN_COUNT = "columnCount";
  public static final String ATTR_COLUMN_LAYOUT = "colunmLayout";
  public static final String ATTR_COMMAND_NAME = "commandName";
  public static final String ATTR_DATE_STYLE = "dateStyle";
  public static final String ATTR_DISABLED = "disabled";
  public static final String ATTR_DOCTYPE = "doctype";
  public static final String ATTR_ENCODING_ACTIVE = "encodingActive";
  public static final String ATTR_ENCTYPE = "enctype";
  public static final String ATTR_ESCAPE = "escape";
  public static final String ATTR_FIRST = "first";
  public static final String ATTR_FOCUS = "focus";
//  public static final String ATTR_FOCUS_ID = "focusId";
  public static final String ATTR_FOOTER_HEIGHT = "footerHeight";
  public static final String ATTR_FORMAT_PATTERN = "formatPattern";
  public static final String ATTR_FOR = "for";
  public static final String ATTR_HEIGHT = "height";
  public static final String ATTR_HIDDEN = "hidden";
  public static final String ATTR_HIDE_ICONS = "hideIcons";
  public static final String ATTR_HIDE_JUNCTIONS = "hideJunctions";
  public static final String ATTR_HIDE_ROOT = "hideRoot";
  public static final String ATTR_HIDE_ROOT_JUNCTION = "hideRootJunction";
  public static final String ATTR_I18N = "i18n";
  public static final String ATTR_ID_REFERENCE = "idReference";
  public static final String ATTR_IMMEDIATE = "immediate";
  public static final String ATTR_INLINE = "inline";
  public static final String ATTR_INNER_HEIGHT = "innerHeight";
  public static final String ATTR_INNER_WIDTH = "innerWidth";
  public static final String ATTR_LABEL = "label";
  public static final String ATTR_LAYOUT_DIRECTIVE = "layoutDirective";
  public static final String ATTR_LAYOUT_HEIGHT = "layout_height";
  public static final String ATTR_LAYOUT_MARGIN = "layoutMargin";
  public static final String ATTR_LAYOUT_MARGIN_BOTTOM = "layoutMarginBottom";
  public static final String ATTR_LAYOUT_MARGIN_LEFT = "layoutMarginLeft";
  public static final String ATTR_LAYOUT_MARGIN_RIGHT = "layoutMarginRight";
  public static final String ATTR_LAYOUT_MARGIN_TOP = "layoutMarginTop";
  public static final String ATTR_LAYOUT_ROWS = "layoutRows";
  public static final String ATTR_LAYOUT_TRANSPARENT = "layoutTransparent";
  public static final String ATTR_LAYOUT_TABLE_STYLE = "layoutTableStyle";
  public static final String ATTR_LAYOUT_WIDTH = "layout_width";
  public static final String ATTR_MAX = "max";
  public static final String ATTR_METHOD = "method";
  public static final String ATTR_MIN = "min";
  public static final String ATTR_MULTIPLE_FILES = "multipleFiles";
  public static final String ATTR_MULTISELECT = "multiselect";
  public static final String ATTR_MUTABLE = "mutable";
  public static final String ATTR_NAME = "name";
  public static final String ATTR_NAME_REFERENCE = "nameReference";
  public static final String ATTR_NAVIGATE = "navigate";
  public static final String ATTR_NUMBER_STYLE = "numberStyle";
  public static final String ATTR_ONCHANGE = "onchange";
  public static final String ATTR_ONCLICK = "onclick";
  public static final String ATTR_PAGING = "paging";
  public static final String ATTR_PASSWORD = "password";
  public static final String ATTR_PREFORMATED = "preformated";
  public static final String ATTR_READONLY = "readonly";
  public static final String ATTR_REFERENCE = "reference";
  public static final String ATTR_RENDERER_TYPE = "rendererType";
  public static final String ATTR_RENDER_AS = "renderAs";
  public static final String ATTR_RENDER_RANGE = "renderRange";
  public static final String ATTR_RENDER_RANGE_EXTERN = "renderRangeExtern";
  public static final String ATTR_REQUIRED = "required";
  public static final String ATTR_ROW_ID = "sheetRowId";
  public static final String ATTR_ROW_LAYOUT = "rowLayout";
  public static final String ATTR_ROWS = "rows";
  public static final String ATTR_SCRIPT_FILES = "scriptFiles";
  public static final String ATTR_SCROLLBAR_HEIGHT = "scrollbarHeight";
  public static final String ATTR_SERVER_SIDE_TABS = "serverSideTab";
  public static final String ATTR_SHEET_SORTER = "sheetSorter";
  public static final String ATTR_SIZE = "size";
  public static final String ATTR_SORTABLE = "sortable";
  public static final String ATTR_SPAN_X = "spanX";
  public static final String ATTR_SPAN_Y = "spanY";
  public static final String ATTR_SRC = "src";
  public static final String ATTR_STATE_BINDING = "stateBinding";
  public static final String ATTR_STATE_PREVIEW = "state_preview";
  public static final String ATTR_STYLE = "style";
  public static final String ATTR_STYLE_BODY = "style_body";
  public static final String ATTR_STYLE_CLASS = "styleClass";
  public static final String ATTR_STYLE_HEADER = "style_header";
  public static final String ATTR_STYLE_INNER = "style_inner";
  public static final String ATTR_STYLE_FILES  = "styleFiles";
  public static final String ATTR_SUPPRESSED = "suppressed";
  public static final String ATTR_TARGET = "target";
  public static final String ATTR_TIME_STYLE = "timeStyle";
  public static final String ATTR_TIMEZONE = "timezone";
  public static final String ATTR_TITLE = "title";
  public static final String ATTR_TYPE = "type";
  public static final String ATTR_VALUE = "value";
  public static final String ATTR_UNIT = "unit";
  public static final String ATTR_WIDTH = "width";
  public static final String ATTR_WIDTH_LIST = "widthList";

  public static final String FACET_LABEL = "label";
  public static final String FACET_CONFIRMATION = "confirmation";

  public static final String VIEWS_IN_SESSION = "com.atanion.tobago.application.UIViewRoot";

  public static final String ACTION_LISTENER = "com.atanion.tobago.ActionListener";

  // Command types
  public static final String COMMAND_TYPE_SUBMIT = "submit";
  public static final String COMMAND_TYPE_RESET = "reset";
  public static final String COMMAND_TYPE_NAVIGATE = "navigate";
  // todo: find an abstract way, to avoid this
  public static final String COMMAND_TYPE_SCRIPT = "script";

  // used in the "web.xml"
  public static final String CONTEXT_PARAM_DEBUG_MODE = "tobago.debugMode";
  public static final String CONTEXT_PARAM_RESOURCE_DIRECTORIES = "tobago.resourceDirectories";

  public static final String RENDERER_TYPE_BUTTON = "Button";
  public static final String RENDERER_TYPE_LINK = "Link";
  public static final String RENDERER_TYPE_LABEL = "Label";
  public static final String RENDERER_TYPE_TEXT_BOX = "TextBox";

  public static final String VB_DISABLED = "disabled";
  public static final String VB_HIDE_ICONS = "hideIcons";
  public static final String VB_HIDE_JUNCTIONS = "hideJunctions";
  public static final String VB_HIDE_ROOT = "hideRoot";
  public static final String VB_HIDE_ROOT_JUNCTION = "hideRootJunction";
  public static final String VB_MULTISELECT = "multiselect";
  public static final String VB_MUTABLE = "mutable";

}
