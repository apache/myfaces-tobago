/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Name constants of the attributes of the Tobago components.
 */
public enum Attributes {

  accessKey,
  action,
  actionListener,
  align,
  alignItems,
  alt,
  applicationIcon,
  autoReload,
  bodyContent,
  border,
  /**
   * Used by a layout manager
   */
  borderBottom,
  /**
   * Used by a layout manager
   */
  borderLeft,
  /**
   * Used by a layout manager
   */
  borderRight,
  /**
   * Used by a layout manager
   */
  borderTop,
  charset,
  clientProperties,
  collapsed,
  collapsedMode,
  columnSpan,
  columnSpacing,
  columns,
  converter,
  createSpan,
  css,
  cssClassesBlocks,
  dateStyle,
  defaultCommand,
  delay,
  directLinkCount,
  disabled,
  enctype,
  escape,
  executePartially,
  expanded,
  execute,
  event,
  extraSmall,
  fieldId,
  first,
  fixed,
  frequency,
  focus,
  focusId,
  /**
   * @deprecated since Tobago 3.0.0
   */
  @Deprecated
  forceVerticalScrollbar,
  formatPattern,
  forValue("for"),
  globalOnly,
  height,
  hidden,
  hover,
  i18n,
  iconSize,
  id,
  immediate,
  image,
  inline,
  itemDescription,
  itemDisabled,
  itemLabel,
  itemImage,
  itemValue,
  jsfResource,
  label,
  labelLayout,
  labelPosition,
  labelWidth,
  large,
  layoutOrder,
  left,
  link,
  /** @deprecated since Tobago 2.0.0 */
  @Deprecated
  margin,
  /**
   * Used by a layout manager
   */
  marginBottom,
  /**
   * Used by a layout manager
   */
  marginLeft,
  /**
   * Used by a layout manager
   */
  marginRight,
  /**
   * Used by a layout manager
   */
  marginTop,
  marked,
  markup,
  max,
  maxSeverity,
  maxNumber,
  maximumHeight,
  maximumWidth,
  method,
  min,
  minSeverity,
  minimumHeight,
  minimumWidth,
  medium,
  modal,
  mode,
  mutable,
  name,
  navigate,
  numberStyle,
  omit,
  /** @deprecated */
  @Deprecated
  onclick,
  /** @deprecated */
  @Deprecated
  onchange,
  orderBy,
  orientation,
  /**
   * Used by a layout manager
   */
  paddingBottom,
  /**
   * Used by a layout manager
   */
  paddingLeft,
  /**
   * Used by a layout manager
   */
  paddingRight,
  /**
   * Used by a layout manager
   */
  paddingTop,
  pageAction,
  pagingTarget,
  password,
  placeholder,
  popupClose,
  popupList,
  popupReset,
  popupCalendarId,
  preferredHeight,
  preferredWidth,
  preformated,
  readonly,
  reference,
  relative,
  rendered,
  /**
   * @deprecated since Tobago 3.0.0
   */
  @Deprecated
  renderedPartially,
  rendererType,
  renderAs,
  renderPartially,
  renderRange,
  renderRangeExtern,
  required,
  resizable,
  resource,
  rowId,
  rowSpan,
  rowSpacing,
  rows,
  scriptFiles,
  scrollbarHeight,
  scrollbars,
  // Attribute name could not be the same as the method name
  // this cause an infinite loop on attribute map
  scrollPosition,
  selectedIndex,
  selectedListString,
  selectable,
  showDirectLinks,
  showDirectLinksArrows,
  showHeader,
  showJunctions,
  showNavigationBar,
  showPageRange,
  showPageRangeArrows,
  showPagingAlways,
  showRoot,
  showRootJunction,
  showRowRange,
  showSummary,
  showDetail,
  size,
  sortable,
  sortActionListener,
  small,
  spanX,
  spanY,
  src,
  state,
  stateChangeListener,
  statePreview,
  style,
  suppressToolbarContainer,
  switchType,
  tabIndex,
  target,
  timeStyle,
  textAlign,
  timezone,
  title,
  tip,
  top,
  transition,
  type,
  value,
  valueChangeListener,
  var,
  unit,
  update,
  validator,
  width,
  widthList,
  zIndex;

  private static final Logger LOG = LoggerFactory.getLogger(Attributes.class);

  /** This constants are needed for annotations, because they can't use the enums. */
  public static final String EXECUTE = "execute";

  private final String explicit;

  Attributes() {
    this(null);
  }

  Attributes(String explicit) {
    this.explicit = explicit;
  }

  public String getName() {
    if (explicit != null) {
      return explicit;
    } else {
      return name();
    }
  }

  public static Attributes valueOfFailsafe(String name) {
    try {
      return Attributes.valueOf(name);
    } catch (IllegalArgumentException e) {
      LOG.warn("Can't find enum for {} with name '{}'", Attributes.class.getName(), name);
      return null;
    }
  }

  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ACCESS_KEY = "accessKey";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ACTION = "action";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ACTION_LISTENER = "actionListener";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ALIGN = "align";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ALIGN_ITEMS = "alignItems";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ALT = "alt";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String APPLICATION_ICON = "applicationIcon";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String AUTO_RELOAD = "autoReload";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BODY_CONTENT = "bodyContent";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BORDER = "border";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BORDER_BOTTOM = "borderBottom";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BORDER_LEFT = "borderLeft";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BORDER_RIGHT = "borderRight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String BORDER_TOP = "borderTop";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CHARSET = "charset";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CLIENT_PROPERTIES = "clientProperties";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String COLUMN_SPAN = "columnSpan";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String COLUMN_SPACING = "columnSpacing";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String COLUMNS = "columns";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CONVERTER = "converter";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CREATE_SPAN = "createSpan";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CSS = "css";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String CSS_CLASSES_BLOCKS = "cssClassesBlocks";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String DATE_STYLE = "dateStyle";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String DEFAULT_COMMAND = "defaultCommand";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String DELAY = "delay";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String DIRECT_LINK_COUNT = "directLinkCount";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String DISABLED = "disabled";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ENCTYPE = "enctype";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ESCAPE = "escape";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String EXPANDED = "expanded";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String EVENT = "event";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String EXTRA_SMALL = "extraSmall";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FIELD_ID = "fieldId";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FIRST = "first";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FREQUENCY = "frequency";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FOCUS = "focus";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FOCUS_ID = "focusId";
  /** @deprecated Since Tobago 3.0.0. */
  @Deprecated
  public static final String FORCE_VERTICAL_SCROLLBAR = "forceVerticalScrollbar";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FORMAT_PATTERN = "formatPattern";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String FOR = "for";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String GLOBAL_ONLY = "globalOnly";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String HEIGHT = "height";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String HIDDEN = "hidden";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String HOVER = "hover";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String I18N = "i18n";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ICON_SIZE = "iconSize";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ID = "id";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String IMMEDIATE = "immediate";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String IMAGE = "image";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String INLINE = "inline";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ITEM_DESCRIPTION = "itemDescription";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ITEM_DISABLED = "itemDisabled";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ITEM_LABEL = "itemLabel";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ITEM_IMAGE = "itemImage";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ITEM_VALUE = "itemValue";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String JSF_RESOURCE = "jsfResource";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LABEL = "label";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LABEL_LAYOUT = "labelLayout";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LABEL_POSITION = "labelPosition";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LABEL_WIDTH = "labelWidth";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LARGE = "large";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LAYOUT_HEIGHT = "layoutHeight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LAYOUT_ORDER = "layoutOrder";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LAYOUT_WIDTH = "layoutWidth";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LEFT = "left";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String LINK = "link";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARGIN_BOTTOM = "marginBottom";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARGIN_LEFT = "marginLeft";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARGIN_RIGHT = "marginRight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARGIN_TOP = "marginTop";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARKED = "marked";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MARKUP = "markup";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MAX = "max";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MAX_SEVERITY = "maxSeverity";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MAX_NUMBER = "maxNumber";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MAXIMUM_HEIGHT = "maximumHeight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MAXIMUM_WIDTH = "maximumWidth";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String METHOD = "method";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MIN = "min";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MIN_SEVERITY = "minSeverity";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MINIMUM_HEIGHT = "minimumHeight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MINIMUM_WIDTH = "minimumWidth";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MEDIUM = "medium";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MODAL = "modal";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MODE = "mode";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String MUTABLE = "mutable";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String NAME = "name";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String NAVIGATE = "navigate";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String NUMBER_STYLE = "numberStyle";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String OMIT = "omit";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ORDER_BY = "orderBy";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ORIENTATION = "orientation";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PADDING_BOTTOM = "paddingBottom";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PADDING_LEFT = "paddingLeft";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PADDING_RIGHT = "paddingRight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PADDING_TOP = "paddingTop";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PAGE_ACTION = "pageAction";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PAGING_TARGET = "pagingTarget";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PASSWORD = "password";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String POPUP_CLOSE = "popupClose";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String POPUP_LIST = "popupList";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String POPUP_RESET = "popupReset";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String POPUP_CALENDAR_ID = "popupCalendarId";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PREFERRED_HEIGHT = "preferredHeight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PREFERRED_WIDTH = "preferredWidth";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String PREFORMATED = "preformated";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String READONLY = "readonly";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String REFERENCE = "reference";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RELATIVE = "relative";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDERED = "rendered";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDERED_PARTIALLY = "renderedPartially";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDERER_TYPE = "rendererType";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDER_AS = "renderAs";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDER_RANGE = "renderRange";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RENDER_RANGE_EXTERN = "renderRangeExtern";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String REQUIRED = "required";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RESIZABLE = "resizable";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String RESOURCE = "resource";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ROW_ID = "rowId";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ROW_SPAN = "rowSpan";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ROW_SPACING = "rowSpacing";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String ROWS = "rows";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SCRIPT_FILES = "scriptFiles";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SCROLLBAR_HEIGHT = "scrollbarHeight";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SCROLLBARS = "scrollbars";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  // Attribute name could not be the same as the method name
  // this cause an infinite loop on attribute map
  public static final String SCROLL_POSITION = "attrScrollPosition";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SELECTED_INDEX = "selectedIndex";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SELECTED_LIST_STRING = "selectedListString";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SELECTABLE = "selectable";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_DIRECT_LINKS = "showDirectLinks";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_HEADER = "showHeader";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_JUNCTIONS = "showJunctions";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_NAVIGATION_BAR = "showNavigationBar";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_PAGE_RANGE = "showPageRange";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_ROOT = "showRoot";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_ROOT_JUNCTION = "showRootJunction";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_ROW_RANGE = "showRowRange";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_SUMMARY = "showSummary";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SHOW_DETAIL = "showDetail";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SIZE = "size";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SMALL = "small";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SORTABLE = "sortable";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SPAN_X = "spanX";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SPAN_Y = "spanY";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SRC = "src";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String STATE = "state";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String STATE_PREVIEW = "statePreview";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String STYLE = "style";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String STYLE_CLASS = "styleClass";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SUPPRESS_TOOLBAR_CONTAINER = "suppressToolbarContainer";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String SWITCH_TYPE = "switchType";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TAB_INDEX = "tabIndex";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TARGET = "target";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TIME_STYLE = "timeStyle";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TEXT_ALIGN = "textAlign";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TIMEZONE = "timezone";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TITLE = "title";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TIP = "tip";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TOP = "top";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TRANSITION = "transition";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String TYPE = "type";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String VALUE = "value";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String VALUE_CHANGE_LISTENER = "valueChangeListener";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String VAR = "var";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String UNIT = "unit";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String UPDATE = "update";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String VALIDATOR = "validator";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String WIDTH = "width";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String WIDTH_LIST = "widthList";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String WIDTH_LIST_STRING = "widthListString";
  /** @deprecated Since Tobago 3.0.0. Please use the enum */
  @Deprecated
  public static final String Z_INDEX = "zIndex";
}
