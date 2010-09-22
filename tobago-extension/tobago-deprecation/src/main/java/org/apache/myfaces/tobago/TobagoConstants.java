package org.apache.myfaces.tobago;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @see org.apache.myfaces.tobago.component.Attributes
 * @see org.apache.myfaces.tobago.component.Facets
 * @see org.apache.myfaces.tobago.component.RendererTypes
 * @see org.apache.myfaces.tobago.internal.component.UICommandBase
 * @see org.apache.myfaces.tobago.component.OnComponentCreated
 * @see org.apache.myfaces.tobago.renderkit.HtmlUtils
 * @see org.apache.myfaces.tobago.util.ComponentUtils
 * @deprecated
 */
@Deprecated
public final class TobagoConstants {

  /** @deprecated Please use ComponentUtils.SUB_SEPARATOR instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String SUBCOMPONENT_SEP = "::";

  /** @deprecated Please use AbstractUICommand instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String COMMAND_TYPE_SUBMIT = "submit";
  /** @deprecated Please use AbstractUICommand instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String COMMAND_TYPE_RESET = "reset";
  /** @deprecated Please use AbstractUICommand instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String COMMAND_TYPE_NAVIGATE = "navigate";
  /** @deprecated Please use AbstractUICommand instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String COMMAND_TYPE_SCRIPT = "script";

  /** @deprecated Please use AbstractUIPage instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FORM_ACCEPT_CHARSET = "utf-8";

  /** @deprecated Please use OnComponentCreated instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String TOBAGO_COMPONENT_CREATED = "org.apache.myfaces.tobago.CREATION_MARKER";

  /** @deprecated Please use HtmlUtils instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String CHAR_NON_BEAKING_SPACE = "\u00a0";

  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ACTION = "action";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ACTION_ONCLICK = "onclick";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ACTION_LINK = "link";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ACTION_LISTENER = "actionListener";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ALIGN = "align";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ALT = "alt";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_AUTO_RELOAD = "autoReload";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_BODY_CONTENT = "bodyContent";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_BORDER = "border";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CHARSET = "charset";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CALENDAR_DATE_INPUT_ID = "CalendarDateInputId";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CELLSPACING = "cellspacing";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CLASSES_BLOCKS = "cssClassesBlocks";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CLIENT_PROPERTIES = "clientProperties";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_COLUMNS = "columns";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CONVERTER = "converter";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_DATE_STYLE = "dateStyle";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_DEFAULT_COMMAND = "defaultCommand";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_DELAY = "delay";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_DIRECT_LINK_COUNT = "directLinkCount";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_DISABLED = "disabled";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ENCTYPE = "enctype";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ESCAPE = "escape";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_EXPANDED = "expanded";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_EVENT = "event";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FIRST = "first";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FREQUENCY = "frequency";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FOCUS = "focus";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FOCUS_ID = "focusId";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FOOTER_HEIGHT = "footerHeight";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FORCE_VERTICAL_SCROLLBAR = "forceVerticalScrollbar";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FORMAT_PATTERN = "formatPattern";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_FOR = "for";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_GLOBAL_ONLY = "globalOnly";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_HEIGHT = "height";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_HIDDEN = "hidden";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_HOVER = "hover";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_I18N = "i18n";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ICON_SIZE = "iconSize";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ID = "id";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_IMMEDIATE = "immediate";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_IMAGE = "image";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_INLINE = "inline";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_INNER_HEIGHT = "innerHeight";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_INNER_WIDTH = "innerWidth";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ITEM_DESCRIPTION = "itemDescription";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ITEM_DISABLED = "itemDisabled";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ITEM_LABEL = "itemLabel";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ITEM_IMAGE = "itemImage";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ITEM_VALUE = "itemValue";

  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LABEL = "label";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LABEL_POSITION = "labelPosition";
  //  public static final String ATTR_LABEL_WITH_ACCESS_KEY = "labelWithAccessKey";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LAYOUT_HEIGHT = "layoutHeight";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARGIN = "margin";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARGIN_BOTTOM = "marginBottom";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARGIN_LEFT = "marginLeft";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARGIN_RIGHT = "marginRight";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARGIN_TOP = "marginTop";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARKED = "marked";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MODAL = "modal";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LAYOUT_ORDER = "layoutOrder";
  //public static final String ATTR_LAYOUT_TABLE_STYLE = "layoutTableStyle";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LAYOUT_WIDTH = "layoutWidth";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_LEFT = "left";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MARKUP = "markup";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MAX = "max";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MAX_SEVERITY = "maxSeverity";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MAX_NUMBER = "maxNumber";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MENU_POPUP = "menuPopup";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MENU_POPUP_TYPE = "menuPopupType";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_COMMAND_TYPE = "menuType";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_METHOD = "method";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MIN = "min";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MIN_SEVERITY = "minSeverity";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MINIMUM_SIZE = "minimunSize";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MODE = "mode";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_MUTABLE = "mutable";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_NAME = "name";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_NAVIGATE = "navigate";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_NUMBER_STYLE = "numberStyle";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ONCHANGE = "onchange";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ONCLICK = "onclick";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ORDER_BY = "orderBy";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ORIENTATION = "orientation";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_PAGE_MENU = "pageMenu";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_PASSWORD = "password";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_POPUP_LIST = "popupList";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDERED_PARTIALLY = "renderedPartially";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_POPUP_CLOSE = "popupClose";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_POPUP_RESET = "popupReset";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_POPUP_CALENDAR_ID = "popupCalendarId";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_PREFORMATED = "preformated";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_READONLY = "readonly";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_REFERENCE = "reference";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RELATIVE = "relative";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDERED = "rendered";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDERER_TYPE = "rendererType";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDER_AS = "renderAs";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDER_RANGE = "renderRange";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_RENDER_RANGE_EXTERN = "renderRangeExtern";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_REQUIRED = "required";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ROW_ID = "sheetRowId";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ROWS = "rows";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SCRIPT_FILES = "scriptFiles";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SCROLLBAR_HEIGHT = "scrollbarHeight";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SCROLLBARS = "scrollbars";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SELECTED_INDEX = "selectedIndex";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SELECTED_LIST_STRING = "selectedListString";
  //  public static final String ATTR_SERVER_SIDE_TABS = "serverSideTab";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SORTABLE = "sortable";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_CREATE_SPAN = "createSpan";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SELECTABLE = "selectable";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_DIRECT_LINKS = "showDirectLinks";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_HEADER = "showHeader";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_ICONS = "showIcons";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_JUNCTIONS = "showJunctions";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_PAGE_RANGE = "showPageRange";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_ROOT = "showRoot";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_ROOT_JUNCTION = "showRootJunction";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_ROW_RANGE = "showRowRange";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_SUMMARY = "showSummary";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SHOW_DETAIL = "showDetail";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SPAN_X = "spanX";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SPAN_Y = "spanY";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SRC = "src";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STATE = "state";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STATE_PREVIEW = "state_preview";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE = "style";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE_BODY = "style_body";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE_CLASS = "styleClass";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE_HEADER = "style_header";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE_INNER = "style_inner";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_STYLE_FILES = "styleFiles";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SUPPPRESS_TOOLBAR_CONTAINER = "suppressToolbarContainer";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_SWITCH_TYPE = "switchType";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TARGET = "target";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TIME_STYLE = "timeStyle";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TIMEZONE = "timezone";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TITLE = "title";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TIP = "tip";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TOP = "top";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TRANSITION = "transition";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TYPE = "type";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_VALUE = "value";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_VALUE_CHANGE_LISTENER = "valueChangeListener";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_VAR = "var";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_UNIT = "unit";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_UPDATE = "update";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_WIDTH = "width";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_WIDTH_LIST = "widthList";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_WIDTH_LIST_STRING = "widthListString";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_APPLICATION_ICON = "applicationIcon";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_VALIDATOR = "validator";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_TAB_INDEX = "tabIndex";
  /** @deprecated Please use Attributes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String ATTR_ZINDEX = "zIndex";

  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_ACTION = "action";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
 @Deprecated
  public static final String FACET_CONFIRMATION = "confirmation";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_LABEL = "label";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_LAYOUT = "layout";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_LAYOUT_DEFAULT = "layoutDefault";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_IMAGE = "image ";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_ITEMS = "items";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_MENUBAR = "menuBar";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_MENUPOPUP = "menupopup";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_PAGER_LINKS = "pagerLinks";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_PAGER_PAGE = "pagerPage";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_PAGER_ROW = "pagerRow";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_PICKER = "picker";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_PICKER_POPUP = "pickerPopup";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_POPUP = "popup";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_RELOAD = "reload";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_TOOL_BAR = "toolBar";
  /** @deprecated Please use Facets instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String FACET_TOOL_BAR_COMMAND = "toolBarCommand";

  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_BOX = "Box";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_BUTTON = "Button";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_CALENDAR = "Calendar";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_DATE = "Date";
  /** @deprecated Not existing any longer. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_DEFAULT_LAYOUT = "DefaultLayout";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_FILE = "File";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_GRID_LAYOUT = "GridLayout";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_HIDDEN = "Hidden";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_IN = "In";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_IMAGE = "Image";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_LINK = "Link";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_LABEL = "Label";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_MENUBAR = "MenuBar";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_MENUCOMMAND = "MenuCommand";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_OUT = "Out";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_PANEL = "Panel";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_POPUP = "Popup";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_DATE_PICKER = "DatePicker";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_BOOLEAN_CHECKBOX = "SelectBooleanCheckbox";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_MANY_CHECKBOX = "SelectManyCheckbox";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_MANY_LISTBOX = "SelectManyListbox";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_ONE_CHOICE = "SelectOneChoice";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_ONE_RADIO = "SelectOneRadio";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SELECT_ONE_LISTBOX = "SelectOneListbox";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_TEXT_AREA = "Textarea";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_TIME = "Time";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_TOOL_BAR = "ToolBar";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_TREE_NODE = "TreeNode";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_TREE_OLD_NODE = "TreeOldNode";
  /** @deprecated Please use RendererTypes instead. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_SHEET = "Sheet";
  /** @deprecated Not existing any longer. Will be removed after Tobago 1.5 */
  @Deprecated
  public static final String RENDERER_TYPE_VERBATIM = "Verbatim";

  @Deprecated
  public static final String ATTR_NAME_REFERENCE = "nameReference";
  @Deprecated
  public static final String ATTR_DISABLED_REFERENCE = "disabledReference";
  @Deprecated
  public static final String ATTR_ID_REFERENCE = "idReference";
  @Deprecated
  public static final String ATTR_TIP_REFERENCE = "tipReference";

  private TobagoConstants() {
    // to prevent instantiation
  }
}
