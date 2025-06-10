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

import org.apache.myfaces.tobago.internal.util.StringUtils;

/**
 * Constants for the component type.
 *
 * @since 4.4.0
 */
public enum Tags {

  badge,
  bar,
  box,
  button,
  buttons,
  calendar,
  config,
  column,
  columnNode,
  columnPanel,
  columnSelector,
  date,
  event,
  figure,
  file,
  flexLayout,
  flowLayout,
  footer,
  form,
  gridLayout,
  header,
  hidden,
  in,
  image,
  link,
  links,
  label,
  messages,
  meta,
  metaLink,
  object,
  offcanvas,
  operation,
  out,
  page,
  paginatorList,
  paginatorPage,
  paginatorPanel,
  paginatorRow,
  panel,
  popover,
  popup,
  progress,
  reload,
  row,
  range,
  script,
  section,
  segmentLayout,
  selectBooleanCheckbox,
  selectBooleanToggle,
  selectItem,
  selectItems,
  selectItemsFiltered,
  selectManyCheckbox,
  selectManyList,
  selectManyListbox,
  selectManyShuttle,
  selectOneChoice,
  selectOneRadio,
  selectOneList,
  selectOneListbox,
  selectReference,
  separator,
  sheet,
  splitLayout,
  stars,
  style,
  suggest,
  textarea,
  tab,
  tabGroup,
  toasts,
  toolBar,
  tree,
  treeIcon,
  treeIndent,
  treeLabel,
  treeListbox,
  treeNode,
  treeSelect;

  public static final String BADGE = "badge";
  public static final String BAR = "bar";
  public static final String BOX = "box";
  public static final String BUTTON = "button";
  public static final String BUTTONS = "buttons";
  public static final String CALENDAR = "calendar";
  public static final String CONFIG = "config";
  public static final String COLUMN = "column";
  public static final String COLUMN_NODE = "columnNode";
  public static final String COLUMN_PANEL = "columnPanel";
  public static final String COLUMN_SELECTOR = "columnSelector";
  public static final String EVENT = "event";
  public static final String DATE = "date";
  public static final String FIGURE = "figure";
  public static final String FILE = "file";
  public static final String FLEX_LAYOUT = "flexLayout";
  public static final String FLOW_LAYOUT = "flowLayout";
  public static final String FOOTER = "footer";
  public static final String FORM = "form";
  public static final String GRID_LAYOUT = "gridLayout";
  public static final String HEADER = "header";
  public static final String HIDDEN = "hidden";
  public static final String IN = "in";
  public static final String IMAGE = "image";
  public static final String LINK = "link";
  public static final String LINKS = "links";
  public static final String LABEL = "label";
  public static final String MESSAGES = "messages";
  public static final String META = "meta";
  public static final String META_LINK = "metaLink";
  public static final String OBJECT = "object";
  public static final String OFFCANVAS = "offcanvas";
  public static final String OPERATION = "operation";
  public static final String OUT = "out";
  public static final String PAGE = "page";
  public static final String PAGINATOR_LIST = "paginatorList";
  public static final String PAGINATOR_PAGE = "paginatorPage";
  public static final String PAGINATOR_PANEL = "paginatorPanel";
  public static final String PAGINATOR_ROW = "paginatorRow";
  public static final String PANEL = "panel";
  public static final String POPOVER = "popover";
  public static final String POPUP = "popup";
  public static final String PROGRESS = "progress";
  public static final String RELOAD = "reload";
  public static final String ROW = "row";
  public static final String RANGE = "range";
  public static final String SCRIPT = "script";
  public static final String SECTION = "section";
  public static final String SEGMENT_LAYOUT = "segmentLayout";
  public static final String SELECT_BOOLEAN_CHECKBOX = "selectBooleanCheckbox";
  public static final String SELECT_BOOLEAN_TOGGLE = "selectBooleanToggle";
  public static final String SELECT_ITEM = "selectItem";
  public static final String SELECT_ITEMS = "selectItems";
  public static final String SELECT_ITEMS_FILTERED = "selectItemsFiltered";
  public static final String SELECT_MANY_CHECKBOX = "selectManyCheckbox";
  public static final String SELECT_MANY_LIST = "selectManyList";
  public static final String SELECT_MANY_LISTBOX = "selectManyListbox";
  public static final String SELECT_MANY_SHUTTLE = "selectManyShuttle";
  public static final String SELECT_ONE_CHOICE = "selectOneChoice";
  public static final String SELECT_ONE_RADIO = "selectOneRadio";
  public static final String SELECT_ONE_LIST = "selectOneList";
  public static final String SELECT_ONE_LISTBOX = "selectOneListbox";
  public static final String SELECT_REFERENCE = "selectReference";
  public static final String SEPARATOR = "separator";
  public static final String SHEET = "sheet";
  public static final String SPLIT_LAYOUT = "splitLayout";
  public static final String STARS = "stars";
  public static final String STYLE = "style";
  public static final String SUGGEST = "suggest";
  public static final String TEXTAREA = "textarea";
  public static final String TAB = "tab";
  public static final String TAB_GROUP = "tabGroup";
  public static final String TOASTS = "toasts";
  public static final String TOOL_BAR = "toolBar";
  public static final String TREE = "tree";
  public static final String TREE_ICON = "treeIcon";
  public static final String TREE_INDENT = "treeIndent";
  public static final String TREE_LABEL = "treeLabel";
  public static final String TREE_LISTBOX = "treeListbox";
  public static final String TREE_NODE = "treeNode";
  public static final String TREE_SELECT = "treeSelect";

  public String componentType() {
    return "org.apache.myfaces.tobago." + StringUtils.firstToUpperCase(name());
  }
}
