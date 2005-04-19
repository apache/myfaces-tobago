/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIData;
import com.atanion.tobago.event.SheetStateChangeEvent;
import com.atanion.tobago.model.SheetState;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasColumnLayout;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasVar;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.jstl.sql.Result;
import java.util.List;
import java.sql.ResultSet;

@Tag(name="sheet")
public class SheetTag extends TobagoTag implements HasIdBindingAndRendered
    {

// ----------------------------------------------------------------- attributes

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
    directLinkCount = null;
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

    ComponentUtil.setStringProperty(data, ATTR_SHOW_ROW_RANGE,
        showRowRange, getIterationHelper());
    ComponentUtil.setStringProperty(data, ATTR_SHOW_PAGE_RANGE,
        showPageRange, getIterationHelper());
    ComponentUtil.setStringProperty(data, ATTR_SHOW_DIRECT_LINKS,
        showDirectLinks, getIterationHelper());
    ComponentUtil.setIntegerProperty(data, ATTR_DIRECT_LINK_COUNT,
        directLinkCount, getIterationHelper());
    ComponentUtil.setBooleanProperty(data, ATTR_SHOW_HEADER, showHeader,
        getIterationHelper());
    ComponentUtil.setIntegerProperty(data, ATTR_FIRST, pagingStart,
        getIterationHelper());
    ComponentUtil.setIntegerProperty(data, ATTR_ROWS, pagingLength,
        getIterationHelper());
    ComponentUtil.setStringProperty(data, ATTR_COLUMNS, columns,
        getIterationHelper());
    ComponentUtil.setStringProperty(data, ATTR_VALUE, value,
        getIterationHelper());
    ComponentUtil.setStringProperty(data, ATTR_FORCE_VERTICAL_SCROLLBAR,
        forceVerticalScrollbar, getIterationHelper());

//   todo: works this? or use that: data.setVar(var);
    ComponentUtil.setStringProperty(data, ATTR_VAR, var,
        getIterationHelper());

    data.getAttributes().put(ATTR_INNER_WIDTH, new Integer(-1));

    // todo: check, if it is an writeable object
    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(
          state, getIterationHelper());
      data.setValueBinding(ATTR_STATE, valueBinding);
    }

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();

    if (stateChangeListener != null) {
      if (isValueReference(stateChangeListener)) {
        Class arguments[] = {SheetStateChangeEvent.class};
        MethodBinding binding
            = application.createMethodBinding(stateChangeListener, arguments);
        data.setStateChangeListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + stateChangeListener);
      }
    }

  }

// ------------------------------------------------------------ getter + setter

  public String getColumns() {
    return columns;
  }


  /**
   *  <![CDATA[
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('<x>*', '<x>px' or '<x>%').
   *    ]]>
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getShowHeader() {
    return showHeader;
  }

  /**
   *  <![CDATA[
   *    Flag indicating the header should rendered.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Boolean.class, defaultValue="true")
  public void setShowHeader(String showHeader) {
    this.showHeader = showHeader;
  }

  public String getPagingLength() {
    return pagingLength;
  }

  /**
   *  <![CDATA[
   *   The number of rows to display, starting with the one identified by the
   *   "pageingStart" property.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="100")
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
   *  <![CDATA[
   *   Zero-relative row number of the first row to be displayed.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="0")
  public void setPagingStart(String pagingStart) {
    this.pagingStart = pagingStart;
  }

  public String getValue() {
    return value;
  }

  /**
   *  The sheet's data.
   *  
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type={Object[].class, List.class, Result.class,
                                 ResultSet.class, Object.class})
  public void setValue(String value) {
    this.value = value;
  }

  public String getVar() {
    return var;
  }

   /**
   *  <![CDATA[
   * Name of a request-scope attribute under which the model data for the row
   * selected by the current value of the "rowIndex" property
   * (i.e. also the current value of the "rowData" property) will be exposed.
   *    ]]>
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setVar(String var) {
    this.var = var;
  }

   /**
   *  <![CDATA[
   *   The count of rendered direct paging links in the sheet's footer.<br />
   *    The <strong>default</strong> is 9.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Integer.class, defaultValue="9")
  public void setDirectLinkCount(String directLinkCount) {
    this.directLinkCount = directLinkCount;
  }

  /**
   *  <![CDATA[
   * Flag indicating whether or not this sheet should reserve space for
   *      vertical toolbar when calculating column width's.<br>
   *      Possible values are: <pre>
   *      'auto'  : sheet try to estimate the need of scrollbar,
   *                this is the default.
   *      'true'  : space for scroolbar is reserved.
   *      'false' : no space is reserved.
   *      </pre>
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="auto")
  public void setForceVerticalScrollbar(String forceVerticalScrollbar) {
    this.forceVerticalScrollbar = forceVerticalScrollbar;
  }

  /**
   *  <![CDATA[
   *   Flag indicating whether or not a range of direct paging links should be
   *   rendered in the sheet's footer.<br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="none")
  public void setShowDirectLinks(String showDirectLinks) {
    this.showDirectLinks = showDirectLinks;
  }

   /**
   *  <![CDATA[
   *   Flag indicating whether and where the range pages should
   *    rendered in the sheet's footer. Rendering this range also offers the
   *    capability to enter the index displayed page directly.<br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="none")
  public void setShowPageRange(String showPageRange) {
    this.showPageRange = showPageRange;
  }


  /**
   *  <![CDATA[
   *    Flag indicating whether or not the range of displayed rows should
   *   rendered in the sheet's footer. Rendering this range also offers the
   *    capability to enter the index of the start row directly. <br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="none")
  public void setShowRowRange(String showRowRange) {
    this.showRowRange = showRowRange;
  }

  /**
   * <![CDATA[
   * Sheet state saving object.
   *    ]]>
   */
  @TagAttribute
  @UIComponentTagAttribute(type=SheetState.class)
  public void setState(String state) {
    this.state = state;
  }

   /**
   *  <![CDATA[
   * MethodBinding representing an stateChangeListener method that will be
   * notified when the state was changed by the user.
   * The expression must evaluate to a public method that takes an
   * StateChangeEvent parameter, with a return type of void.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)public void setStateChangeListener(String stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }
}

