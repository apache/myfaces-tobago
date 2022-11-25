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

import java.lang.invoke.MethodHandles;

/**
 * Name constants of the attributes of the Tobago components.
 */
public enum Attributes {

  accessKey,
  action,
  actionExpression,
  actionListener,
  actionListenerExpression,
  align,
  alignItems,
  alt,
  autocomplete,
  autoReload,
  autoSpacing,
  backgroundImage,
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
  bottom,
  charset,
  clientProperties,
  closed,
  collapsed,
  collapsedMode,
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  column,
  columnSpan,
  columnSpacing,
  columns,
  /**
   * @deprecated since 4.3.0
   */
  @Deprecated
  compact,
  confirmation,
  content,
  converter,
  customClass,
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  createSpan,
  css,
  cssClassesBlocks,
  dateStyle,
  defaultCommand,
  delay,
  directLinkCount,
  disabled,
  display,
  dropZone,
  enctype,
  escape,
  expanded,
  execute,
  event,
  extra2Large,
  extraLarge,
  extraSmall,
  fieldId,
  file,
  filter,
  first,
  fixed,
  flexGrow,
  flexShrink,
  flexBasis,
  fragment,
  frequency,
  focus,
  focusOnError,
  focusId,
  formatPattern,
  forValue("for"),
  globalOnly,
  gridColumn,
  gridRow,
  gridTemplateColumns,
  gridTemplateRows,
  group,
  help,
  height,
  hidden,
  hover,
  href,
  hreflang,
  httpEquiv,
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
  justify,
  justifyContent,
  keepLineBreaks,
  label,
  labelLayout,
  labelPosition,
  labelWidth,
  large,
  layoutOrder,
  lazy,
  left,
  level,
  lang,
  link,
  localMenu,
  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  margin,
  /**
   * Used by a layout manager
   */
  marginBottom,
  /**
   * Used by a layout manager
   */
  marginExtra2Large,
  /**
   * Used by a layout manager
   */
  marginExtraLarge,
  /**
   * Used by a layout manager
   */
  marginExtraSmall,
  /**
   * Used by a layout manager
   */
  marginLarge,
  /**
   * Used by a layout manager
   */
  marginLeft,
  /**
   * Used by a layout manager
   */
  marginMedium,
  /**
   * Used by a layout manager
   */
  marginRight,
  /**
   * Used by a layout manager
   */
  marginSmall,
  /**
   * Used by a layout manager
   */
  marginTop,
  marked,
  markup,
  max,
  maxSeverity,
  maxNumber,
  maximumItems,
  maximumHeight,
  maximumWidth,
  maxHeight,
  maxWidth,
  media,
  method,
  min,
  minHeight,
  minWidth,
  minSeverity,
  minimumCharacters,
  minimumHeight,
  minimumWidth,
  medium,
  modal,
  mode,
  multiple,
  mutable,
  name,
  navigate,
  numberStyle,
  /**
   * Used by a layout manager
   */
  offsetExtra2Large,
  /**
   * Used by a layout manager
   */
  offsetExtraSmall,
  /**
   * Used by a layout manager
   */
  offsetExtraLarge,
  /**
   * Used by a layout manager
   */
  offsetLarge,
  /**
   * Used by a layout manager
   */
  offsetMedium,
  /**
   * Used by a layout manager
   */
  offsetSmall,
  /**
   * Used by a layout manager
   */
  overwriteExtra2Large,
  /**
   * Used by a layout manager
   */
  overwriteExtraLarge,
  /**
   * Used by a layout manager
   */
  overwriteExtraSmall,
  /**
   * Used by a layout manager
   */
  overwriteLarge,
  /**
   * Used by a layout manager
   */
  overwriteMarginExtra2Large,
  /**
   * Used by a layout manager
   */
  overwriteMarginExtraLarge,
  /**
   * Used by a layout manager
   */
  overwriteMarginExtraSmall,
  /**
   * Used by a layout manager
   */
  overwriteMarginLarge,
  /**
   * Used by a layout manager
   */
  overwriteMarginMedium,
  /**
   * Used by a layout manager
   */
  overwriteMarginSmall,
  /**
   * Used by a layout manager
   */
  overwriteMedium,
  /**
   * Used by a layout manager
   */
  overwriteSmall,
  omit,
  /**
   * @deprecated since 3.0.0
   */
  @Deprecated
  onclick,
  /**
   * @deprecated since 3.0.0
   */
  @Deprecated
  onchange,
  open,
  orderBy,
  orientation,
  outcome,
  overflowX,
  overflowY,
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
  plain,
  popupClose,
  popupList,
  popupReset,
  popupCalendarId,
  position,
  preferredHeight,
  preferredWidth,
  preformated,
  readonly,
  reference,
  rel,
  relative,
  rendered,
  renderedIndex,
  rendererType,
  renderAs,
  renderRange,
  /**
   * @deprecated since 4.2.0
   */
  @Deprecated
  renderRangeExtern,
  required,
  resizable,
  rev,
  right,
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  rigid,
  rowId,
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  row,
  rowSpan,
  rowSpacing,
  rows,
  sandbox,
  sanitize,
  scriptFiles,
  scrollbarHeight,
  scrollbars,
  // Attribute name could not be the same as the method name
  // this cause an infinite loop on attribute map
  scrollPosition,
  selectable,
  selectedIndex,
  selectedLabel,
  selectedListString,
  selector,
  sheetAction,
  showCheckbox,
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
  sortActionListenerExpression,
  maxSortColumns,
  small,
  spanX,
  spanY,
  src,
  state,
  stateChangeListener,
  stateChangeListenerExpression,
  statePreview,
  step,
  style,
  switchType,
  tabChangeListener,
  tabChangeListenerExpression,
  tabIndex,
  target,
  timeStyle,
  textAlign,
  timezone,
  title,
  tip,
  todayButton,
  top,
  totalCount,
  transition,
  type,
  value,
  valueChangeListener,
  var,
  verticalAlign,
  unit,
  unselectedLabel,
  update,
  validator,
  viewport,
  waitOverlayDelayAjax,
  waitOverlayDelayFull,
  width,
  widthList,
  zIndex;

  /**
   * This constants are needed for annotations, because they can't use the enums.
   */
  public static final String EXECUTE = "execute";

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final String explicit;

  Attributes() {
    this(null);
  }

  Attributes(final String explicit) {
    this.explicit = explicit;
  }

  public String getName() {
    if (explicit != null) {
      return explicit;
    } else {
      return name();
    }
  }

  public static Attributes valueOfFailsafe(final String name) {
    try {
      return Attributes.valueOf(name);
    } catch (final IllegalArgumentException e) {
      if (name.equals("for")) {
        return Attributes.forValue;
      }
      LOG.warn("Can't find enum for {} with name '{}'", Attributes.class.getName(), name);
      return null;
    }
  }

}
