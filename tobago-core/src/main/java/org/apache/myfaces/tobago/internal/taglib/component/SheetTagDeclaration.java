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

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Facet;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasRenderedPartially;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasVar;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRoot;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsShowRootJunction;
import org.apache.myfaces.tobago.internal.taglib.declaration.IsVisual;

import javax.faces.component.UIData;

/**
 * Render a sheet element.
 */
@Tag(name = "sheet")
@BodyContentDescription(anyTagOf = "<tc:column>* <tc:columnSelector>? <tc:columnEvent>?")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISheet",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISheet",
    interfaces = "org.apache.myfaces.tobago.event.SortActionSource2",
    uiComponentFacesClass = "javax.faces.component.UIData",
    componentFamily = UIData.COMPONENT_FAMILY,
    rendererType = RendererTypes.SHEET,
    allowedChildComponenents = {
        "javax.faces.Column",
        "org.apache.myfaces.tobago.ColumnSelector"},
    facets = {@Facet(name = Facets.RELOAD, description = "Contains an instance of UIReload",
                     allowedChildComponenents = "org.apache.myfaces.tobago.Reload")})
public interface SheetTagDeclaration 
    extends HasIdBindingAndRendered, IsVisual, HasRenderedPartially, IsShowRoot, IsShowRootJunction, HasVar {
  /**
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('&lt;x>*', '&lt;x>px' or '&lt;x>%').
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
   * <br/> The default has been changed from 100 to 0 because this is the default
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
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List", "javax.servlet.jsp.jstl.sql.Result",
      "java.sql.ResultSet", "java.lang.Object", "javax.faces.model.DataModel"},
      expression = DynamicExpression.VALUE_EXPRESSION_REQUIRED)
  void setValue(String value);

  /**
   * Flag indicating whether or not the paging panel should be display, if it is not needed for paging.<br />
   * <ul>
   * <li>showPagingAlways="false" which is the default means, that the paging footer should be displayed,
   * only when it is needed.</li>
   * <ul>
   * <li>When the rows="0" paging is not needed, so the footer will not be rendered,</li>
   * <li>when rows="N", N &gt; 0 and the size of the data value is &lt;= N paging is not needed
   * and the footer will not be rendered,</li>
   * <li>in any other case the paging footer will be displayed.</li>
   * </ul>
   * <li>showPagingAlways="true" means, that the paging footer should be displayed in any case.</li>
   * </ul>
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  void setShowPagingAlways(String showPagingAlways);

  /**
   * The count of rendered direct paging links in the sheet's footer.<br />
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "9")
  void setDirectLinkCount(String directLinkCount);

  /**
   * Flag indicating whether or not this sheet should reserve space for
   * vertical toolbar when calculating column width's.<br />
   * Possible values are: <pre>
   *      'auto'  : sheet try to estimate the need of scrollbar.
   *      'true'  : space for scrollbar is reserved.
   *      'false' : no space is reserved.
   *      </pre>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "auto",
      allowedValues = {"auto", "true", "false"})
  void setForceVerticalScrollbar(String forceVerticalScrollbar);

  /**
   * Flag indicating whether or not a range of direct paging links should be
   * rendered in the sheet's footer.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "center",
      allowedValues = {"left", "center", "right", "none"})
  void setShowDirectLinks(String showDirectLinks);

  /**
   * Flag indicating whether and where the range pages should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index displayed page directly.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "right",
      allowedValues = {"left", "center", "right", "none"})
  void setShowPageRange(String showPageRange);

  /**
   * Flag indicating whether or not the range of displayed rows should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index of the start row directly.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "left",
      allowedValues = {"left", "center", "right", "none"})
  void setShowRowRange(String showRowRange);

  /**
   * Flag indicating whether or not the sheet should be selectable.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "multi",
      allowedValues = {"none", "single", "singleOrNone", "multi"})
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
      methodSignature = "javax.faces.event.ActionEvent")
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
      expression = DynamicExpression.METHOD_BINDING_REQUIRED,
      methodSignature = "javax.faces.event.ActionEvent")
  void setSortActionListener(String sortActionListener);


    /**
     * Flag indicating if paging arrows are shown near direct links
     * @since 2.0.0
     */
    @TagAttribute
    @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
    void setShowDirectLinksArrows(String showDirectLinksArrows);

    /**
     * Flag indicating if paging arrows are shown near page range
     * @since 2.0.0
     */
    @TagAttribute
    @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
    void setShowPageRangeArrows(String showPageRangeArrows);

}
