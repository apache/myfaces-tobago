/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIData;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

import javax.faces.component.UIComponent;

/**
 * Render a sheet element.
 */
@Tag(name = "sheet")
@BodyContentDescription(anyTagOf = "<tc:column>* <tc:columnSelector>?")
public class SheetTag extends TobagoTag implements HasIdBindingAndRendered {

  private String var;
  private String showRowRange = "none";
  private String showPageRange = "none";
  private String showDirectLinks = "none";
  private String directLinkCount = "9";
  private String showHeader;
  private String pagingStart = "0";
  private String pagingLength = "100";
  private String columns;
  private String value;
  private String forceVerticalScrollbar;
  private String state;
  private String stateChangeListener;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    // todo: implement uidata with overridden processUpdates to store state
    return UIData.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    var = null;
    showRowRange = "none";
    showPageRange = "none";
    showDirectLinks = "none";
    directLinkCount = "9";
    showHeader = null;
    pagingStart = "0";
    pagingLength = "100";
    columns = null;
    value = null;
    forceVerticalScrollbar = null;
    state = null;
    stateChangeListener = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    UIData data = (UIData) component;

    ComponentUtil.setStringProperty(data, ATTR_SHOW_ROW_RANGE, showRowRange);
    ComponentUtil.setStringProperty(data, ATTR_SHOW_PAGE_RANGE, showPageRange);
    ComponentUtil.setStringProperty(data, ATTR_SHOW_DIRECT_LINKS, showDirectLinks);
    ComponentUtil.setIntegerProperty(data, ATTR_DIRECT_LINK_COUNT, directLinkCount);
    ComponentUtil.setBooleanProperty(data, ATTR_SHOW_HEADER, showHeader);
    ComponentUtil.setIntegerProperty(data, ATTR_FIRST, pagingStart);
    ComponentUtil.setIntegerProperty(data, ATTR_ROWS, pagingLength);
    ComponentUtil.setStringProperty(data, ATTR_COLUMNS, columns);
    ComponentUtil.setStringProperty(data, ATTR_VALUE, value);
    ComponentUtil.setStringProperty(data, ATTR_FORCE_VERTICAL_SCROLLBAR, forceVerticalScrollbar);
    // todo: works this? or use that: data.setVar(var);
    ComponentUtil.setStringProperty(data, ATTR_VAR, var);
    data.getAttributes().put(ATTR_INNER_WIDTH, new Integer(-1));
    ComponentUtil.setValueBinding(component, ATTR_STATE, state);
    ComponentUtil.setStateChangeListener(data, stateChangeListener);

  }

  public String getColumns() {
    return columns;
  }


  /**
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('<x>*', '<x>px' or '<x>%').
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getShowHeader() {
    return showHeader;
  }

  /**
   * Flag indicating the header should rendered.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Boolean", defaultValue = "true")
  public void setShowHeader(String showHeader) {
    this.showHeader = showHeader;
  }

  public String getPagingLength() {
    return pagingLength;
  }

  /**
   * The number of rows to display, starting with the one identified by the
   * "pageingStart" property.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "100")
  public void setPagingLength(String pagingLength) {
    this.pagingLength = pagingLength;
  }

  public String getPagingStart() {
    return pagingStart;
  }

  public String getStateChangeListener() {
    return stateChangeListener;
  }

  /**
   * Zero-relative row number of the first row to be displayed.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "0")
  public void setPagingStart(String pagingStart) {
    this.pagingStart = pagingStart;
  }

  public String getValue() {
    return value;
  }

  /**
   * The sheet's data.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Object[]", "java.util.List", "javax.servlet.jsp.jstl.sql.Result",
      "java.sql.ResultSet", "java.lang.Object", "javax.faces.model.DataModel"})
  public void setValue(String value) {
    this.value = value;
  }

  public String getVar() {
    return var;
  }

  /**
   * Name of a request-scope attribute under which the model data for the row
   * selected by the current value of the "rowIndex" property
   * (i.e. also the current value of the "rowData" property) will be exposed.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  public void setVar(String var) {
    this.var = var;
  }

  /**
   * The count of rendered direct paging links in the sheet's footer.<br />
   * The <strong>default</strong> is 9.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Integer", defaultValue = "9")
  public void setDirectLinkCount(String directLinkCount) {
    this.directLinkCount = directLinkCount;
  }

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
  @UIComponentTagAttribute(defaultValue = "auto")
  public void setForceVerticalScrollbar(String forceVerticalScrollbar) {
    this.forceVerticalScrollbar = forceVerticalScrollbar;
  }

  /**
   * Flag indicating whether or not a range of direct paging links should be
   * rendered in the sheet's footer.<br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  public void setShowDirectLinks(String showDirectLinks) {
    this.showDirectLinks = showDirectLinks;
  }

  /**
   * Flag indicating whether and where the range pages should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index displayed page directly.<br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  public void setShowPageRange(String showPageRange) {
    this.showPageRange = showPageRange;
  }


  /**
   * Flag indicating whether or not the range of displayed rows should
   * rendered in the sheet's footer. Rendering this range also offers the
   * capability to enter the index of the start row directly. <br />
   * Valid values are <strong>left</strong>, <strong>center</strong>,
   * <strong>right</strong> and <strong>none</strong>.
   * The <strong>default</strong> is <code>none</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "none")
  public void setShowRowRange(String showRowRange) {
    this.showRowRange = showRowRange;
  }

  /**
   * Sheet state saving object.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "org.apache.myfaces.tobago.model.SheetState")
  public void setState(String state) {
    this.state = state;
  }

  /**
   * MethodBinding representing an stateChangeListener method that will be
   * notified when the state was changed by the user.
   * The expression must evaluate to a public method that takes an
   * StateChangeEvent parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setStateChangeListener(String stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }
}

