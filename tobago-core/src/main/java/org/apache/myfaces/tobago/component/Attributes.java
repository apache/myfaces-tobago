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
  column,
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
  rendererType,
  renderAs,
  renderRange,
  renderRangeExtern,
  required,
  resizable,
  rowId,
  row,
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
  sheetAction,
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

  /** This constants are needed for annotations, because they can't use the enums. */
  public static final String EXECUTE = "execute";

  private static final Logger LOG = LoggerFactory.getLogger(Attributes.class);

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

}
