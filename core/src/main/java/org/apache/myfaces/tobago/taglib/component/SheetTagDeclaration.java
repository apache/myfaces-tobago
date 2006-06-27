package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 08.04.2006
 * Time: 15:30:50
 * To change this template use File | Settings | File Templates.
 */

/**
 * Render a sheet element.
 */
@Tag(name = "sheet")
@BodyContentDescription(anyTagOf = "<tc:column>* <tc:columnSelector>?")
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UIData",
    rendererType = "Sheet")

public interface SheetTagDeclaration extends TobagoTagDeclaration, HasIdBindingAndRendered {
  /**
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('<x>*', '<x>px' or '<x>%').
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setColumns(String columns);

  /**
   * Flag indicating the header should rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  void setShowHeader(String showHeader);

  /**
   * Please use "rows" instead.
   * The number of rows to display, starting with the one identified by the
   * "pageingStart" property.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "100")
  @Deprecated
  void setPagingLength(String pagingLength);

  /**
   * The number of rows to display, starting with the one identified by the
   * "pageingStart/first" property.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "100")
  void setRows(String pagingLength);

  /**
   * Please use "first" instead.
   * Zero-relative row number of the first row to be displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  @Deprecated
  void setPagingStart(String pagingStart);

  /**
   * Zero-relative row number of the first row to be displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  void setFirst(String pagingStart);

  /**
   * The sheet's data.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List", "javax.servlet.jsp.jstl.sql.Result",
      "java.sql.ResultSet", "java.lang.Object", "javax.faces.model.DataModel"})
  void setValue(String value);

  /**
   * Name of a request-scope attribute under which the model data for the row
   * selected by the current value of the "rowIndex" property
   * (i.e. also the current value of the "rowData" property) will be exposed.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  void setVar(String var);

  /**
   * The count of rendered direct paging links in the sheet's footer.<br />
   * The <strong>default</strong> is 9.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "9")
  void setDirectLinkCount(String directLinkCount);

  /**
   * Flag indicating whether or not this sheet should reserve space for
   * vertical toolbar when calculating column width's.<br>
   * Possible values are: <pre>
   *      'auto'  : sheet try to estimate the need of scrollbar,
   *                this is the default.
   *      'true'  : space for scroolbar is reserved.
   *      'false' : no space is reserved.
   *      </pre>
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "auto",
      allowedValues = {"auto", "true", "false"})
  void setForceVerticalScrollbar(String forceVerticalScrollbar);

  /**
   * Flag indicating whether or not a range of direct paging links should be
   * rendered in the sheet's footer.<br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none",
      allowedValues = {"left", "center", "right", "none"})
  void setShowDirectLinks(String showDirectLinks);

  /**
   * Flag indicating whether and where the range pages should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index displayed page directly.<br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none",
      allowedValues = {"left", "center", "right", "none"})
  void setShowPageRange(String showPageRange);

  /**
   * Flag indicating whether or not the range of displayed rows should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index of the start row directly. <br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none",
      allowedValues = {"left", "center", "right", "none"})
  void setShowRowRange(String showRowRange);

  /**
   * Sheet state saving object.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.SheetState")
  void setState(String state);

  /**
   * MethodBinding representing an stateChangeListener method that will be
   * notified when the state was changed by the user.
   * The expression must evaluate to a public method that takes an
   * StateChangeEvent parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.el.MethodBinding",
       expression = DynamicExpression.METHOD_BINDING)
  void setStateChangeListener(String stateChangeListener);

  /**
   * MethodBinding representing an actionListener method that will be
   * invoked when sorting was requested by the user.
   * Use this if your application needs special handling for sorting columns.
   * If this is not set and the sortable attribute column is set to true the sheet
   * implementation will use a default sort method.
   * The expression must evaluate to a public method that takes an
   * ActionEvent parameter, with a return type of void.
   * The method will recieve a SortActionEvent,
   * The method should sort according to the sortColumnId and direction getting from
   * the sheets SheetState object.
   * The expression must evaluate to a public method that takes an
   * ActionEvent parameter, with a return type of void.
   * The method will recieve a {@link org.apache.myfaces.tobago.event.SortActionEvent},
   * The method should sort according to the sortColumnId and direction getting from
   * the sheets {@link org.apache.myfaces.tobago.model.SheetState} object.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.el.MethodBinding",
       expression = DynamicExpression.METHOD_BINDING)
  void setSortActionListener(String sortActionListener);

}
