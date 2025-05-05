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

package org.apache.myfaces.tobago.internal.taglib.component;

import jakarta.faces.component.UIData;
import org.apache.myfaces.tobago.apt.annotation.Behavior;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Markup;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRoot;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRootJunction;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;
import org.apache.myfaces.tobago.layout.PaginatorMode;
import org.apache.myfaces.tobago.layout.ShowPosition;
import org.apache.myfaces.tobago.model.Selectable;

/**
 * Render a sheet element.
 */
@Tag(name = "sheet")
@BodyContentDescription(anyTagOf = "<tc:column>* <tc:columnSelector>? <tc:row>?")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISheet",
    uiComponentFacesClass = "jakarta.faces.component.UIData",
    componentFamily = UIData.COMPONENT_FAMILY,
    rendererType = RendererTypes.SHEET,
    allowedChildComponents = {
        "jakarta.faces.Column",
        "org.apache.myfaces.tobago.ColumnSelector"},
    facets = {
        @Facet(
            name = Facets.RELOAD,
            description = "Contains an instance of UIReload",
            allowedChildComponents = "org.apache.myfaces.tobago.Reload"),
        @Facet(
            name = Facets.BEFORE,
            description = "Components to be rendered before the sheet, e.g. paginators"),
        @Facet(
            name = Facets.AFTER,
            description = "Components to be rendered after the sheet, e.g. paginators")
    },
    behaviors = {
        @Behavior(
            name = ClientBehaviors.RELOAD, // XXX replace by click
            isDefault = true),
        @Behavior(name = ClientBehaviors.ROW_SELECTION_CHANGE)
    },
    markups = {
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_BORDERED,
            description = "Add borders to the table cells."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_DARK,
            description = "Set dark background."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_HOVER,
            description = "Background of row changed on hovering."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SMALL,
            description = "Small margins for table cells."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_STRIPED,
            description = "Zebra-striping for table rows."
        ),
        @Markup(
            name = org.apache.myfaces.tobago.context.Markup.STRING_SPREAD,
            description = "Use the full height for the HTML content."
        )
    })
public interface SheetTagDeclaration
    extends HasIdBindingAndRendered, IsVisual, IsShowRoot, IsShowRootJunction, HasVar {
  /**
   * <p>
   * LayoutConstraints for column layout.
   * Contains a space separated list of layout tokens '&lt;n&gt;fr', '&lt;x&gt;px', '&lt;x&gt;%' or 'auto',
   * where x is a number and n is an integer.
   * </p>
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setColumns(String columns);

  /**
   * Flag indicating the header should be rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setShowHeader(String showHeader);

  /**
   * The number of rows to display, starting with the one identified by the
   * "first" property.
   * <br> The default has been changed from 100 to 0 because this is the default
   * in the JSF standard (since Tobago 1.5).
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "int",
      defaultValue = "0")
  void setRows(String rows);

  /**
   * Zero-relative row number of the first row to be displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "int",
      defaultValue = "0")
  void setFirst(String first);

  /**
   * The sheet's data.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List", "jakarta.servlet.jsp.jstl.sql.Result",
      "java.sql.ResultSet", "java.lang.Object", "jakarta.faces.model.DataModel"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * Flag indicating whether or not the paging panel should be display, if it is not needed for paging.<br>
   * <ul>
   * <li>showPagingAlways="false" which is the default means, that the paging footer should be displayed,
   * only when it is needed.
   * <ul>
   * <li>When the rows="0" paging is not needed, so the footer will not be rendered,
   * <li>when rows="N", N &gt; 0 and the size of the data value is &lt;= N paging is not needed
   * and the footer will not be rendered,
   * <li>in any other case the paging footer will be displayed.
   * </ul>
   * <li>showPagingAlways="true" means, that the paging footer should be displayed in any case.
   * </ul>
   *
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowPagingAlways(String showPagingAlways);

  /**
   * The count of rendered direct paging links in the sheet's footer.
   *
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "9")
  void setDirectLinkCount(String directLinkCount);

  /**
   * Flag indicating whether or not a range of direct paging links should be
   * rendered in the sheet's footer.
   *
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.ShowPosition",
      defaultValue = ShowPosition.CENTER,
      allowedValues = {
          ShowPosition.LEFT, ShowPosition.CENTER, ShowPosition.RIGHT, ShowPosition.NONE
      },
      defaultCode = "org.apache.myfaces.tobago.layout.ShowPosition.center")
  void setShowDirectLinks(String showDirectLinks);

  /**
   * Flag indicating whether and where the range pages should be
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index displayed page directly.
   *
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.ShowPosition",
      defaultValue = ShowPosition.RIGHT,
      allowedValues = {
          ShowPosition.LEFT, ShowPosition.CENTER, ShowPosition.RIGHT, ShowPosition.NONE
      },
      defaultCode = "org.apache.myfaces.tobago.layout.ShowPosition.right")
  void setShowPageRange(String showPageRange);

  /**
   * Flag indicating whether or not the range of displayed rows should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index of the start row directly.
   *
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.ShowPosition",
      defaultValue = ShowPosition.LEFT,
      allowedValues = {
          ShowPosition.LEFT, ShowPosition.CENTER, ShowPosition.RIGHT, ShowPosition.NONE
      },
      defaultCode = "org.apache.myfaces.tobago.layout.ShowPosition.left")
  void setShowRowRange(String showRowRange);

  /**
   * Indicating the selection mode of the sheet.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.Selectable",
      defaultValue = Selectable.MULTI,
      allowedValues = {
          Selectable.NONE, Selectable.SINGLE, Selectable.SINGLE_OR_NONE, Selectable.MULTI
      },
      defaultCode = "org.apache.myfaces.tobago.model.Selectable.multi")
  void setSelectable(String selectable);

  /**
   * Sheet state saving object.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.model.SheetState",
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED,
      generate = false)
  void setState(String state);

  /**
   * Method binding representing a stateChangeListener method that will be
   * notified when the state was changed by the user.
   * The expression must evaluate to a public method that takes a
   * SheetStateChangeEvent parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "jakarta.faces.event.ActionEvent")
  void setStateChangeListener(String stateChangeListener);

  /**
   * Method binding representing an actionListener method that will be
   * invoked when sorting was requested by the user.
   * Use this if your application needs special handling for sorting columns.
   * If this is not set and the sortable attribute column is set to true the sheet
   * implementation will use a default sort method.
   * The expression must evaluate to a public method which takes an
   * ActionEvent as parameter and with a return type of void.
   * The method will receive a {@link org.apache.myfaces.tobago.event.SortActionEvent}.
   * The method should sort according to the sortColumnId and direction getting from
   * the sheet's {@link org.apache.myfaces.tobago.model.SheetState} object.
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = {},
      expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "jakarta.faces.event.ActionEvent")
  void setSortActionListener(String sortActionListener);

  /**
   * Lazy loading by scroll event.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setLazy(String lazy);

  /**
   * Indicate how many rows must be loaded when lazy loading is enabled and how many rows are loaded at once.
   * Default is 50.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "50")
  void setLazyRows(String lazyRows);

  /**
   * Flag indicating if paging arrows are shown near direct links
   *
   * @since 2.0.0
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowDirectLinksArrows(String showDirectLinksArrows);

  /**
   * Flag indicating if paging arrows are shown near page range
   *
   * @since 2.0.0
   * @deprecated Use paginator attribute or paginator tags instead.
   */
  @Deprecated(since = "5.15.0, 6.7.0", forRemoval = true)
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  void setShowPageRangeArrows(String showPageRangeArrows);

  /**
   * The maximum count of multi-sorted columns to indicate.
   *
   * @since 5.3.0
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setMaxSortColumns(String maxSortColumns);

  /**
   * Create paginator components automatically or do it manually.
   * <ul>
   *   <li>
   *      <b>custom</b>: no paginator is created automatically, you have to do it manually.
   *      This can be done
   *      using a <code>&lt;tc:paginatorPanel&gt;</code>
   *      with e.g. a <code>&lt;tc:paginatorList&gt;</code> and/or some other
   *      paginator components.
   *   </li>
   *   <li>
   *     <b>auto</b>: a appropriate paginator is used.
   *   </li>
   *   <li>
   *     <b>list</b>: a <code>&lt;tc:paginatorList&gt;</code> is used.
   *   </li>
   *   <li>
   *     <b>page</b>: a <code>&lt;tc:paginatorPage&gt;</code> is used.
   *   </li>
   *   <li>
   *     <b>row</b>: a <code>&lt;tc:paginatorRow&gt;</code> is used.
   *   </li>
   *   <li>
   *     <b>useShowAttributes</b> (default):
   *     is deprecated, use for compatibility.
   *     The used paginators are defined by the show-attributes:
   *     <code>showDirectLinks</code>,
   *     <code>showPageRange</code>,
   *     <code>showRowRange</code>,
   *     <code>directLinkCount</code>,
   *     <code>showDirectLinksArrows</code>,
   *     <code>showPageRangeArrows</code>,
   *     <code>showPagingAlways</code>.
   *   </li>
   * </ul>
   *
   * @since 5.15.0, 6.7.0
   */
  @TagAttribute
  @UIComponentTagAttribute(
      type = "org.apache.myfaces.tobago.layout.PaginatorMode",
      defaultValue = PaginatorMode.USE_SHOW_ATTRIBUTES,
      allowedValues = {
          PaginatorMode.AUTO, PaginatorMode.CUSTOM, PaginatorMode.LIST, PaginatorMode.PAGE, PaginatorMode.ROW,
          PaginatorMode.USE_SHOW_ATTRIBUTES
      },
      defaultCode = "org.apache.myfaces.tobago.layout.PaginatorMode.useShowAttributes")
  void setPaginator(String paginator);

  /**
   * Flag indicating that the rows of the sheet are readonly.
   * The readonly attribute is a performance optimization hint used during
   * {@link jakarta.faces.event.PhaseId#APPLY_REQUEST_VALUES} and
   * {@link jakarta.faces.event.PhaseId#PROCESS_VALIDATIONS} and
   * {@link jakarta.faces.event.PhaseId#UPDATE_MODEL_VALUES}.
   * When set to true, it signals the rows of the sheet are read-only und
   * doesn't require updates potentially saving processing time.
   * This optimization should only be applied when there are no non-readonly
   * {@link jakarta.faces.component.EditableValueHolder} components in the sheet rows.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setReadonlyRows(String readonly);
}
