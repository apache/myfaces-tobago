/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIData;

import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;

public class SheetTag extends TobagoTag {

// ----------------------------------------------------------------- attributes

  private String var;
  private String showRowRange = "none";
  private String showPageRange = "none";
  private String showDirectLinks = "none";
  private String directLinkCount = "9";
  private String hideHeader;
  private String pagingStart = "0";
  private String pagingLength = "100";
  private String columns;
  private String value;
  private String forceVerticalScrollbar;
  private String stateBinding;

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
    hideHeader = null;
    pagingStart = "0";
    pagingLength = "100";
    columns = null;
    value = null;
    forceVerticalScrollbar = null;
    stateBinding = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_SHOW_ROW_RANGE,
        showRowRange, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_SHOW_PAGE_RANGE,
        showPageRange, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_SHOW_DIRECT_LINKS,
        showDirectLinks, getIterationHelper());
    ComponentUtil.setIntegerProperty(component, ATTR_DIRECT_LINK_COUNT,
        directLinkCount, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_HIDE_HEADER, hideHeader,
        getIterationHelper());
    ComponentUtil.setIntegerProperty(component, ATTR_FIRST, pagingStart,
        getIterationHelper());
    ComponentUtil.setIntegerProperty(component, ATTR_ROWS, pagingLength,
        getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COLUMNS, columns,
        getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value,
        getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_FORCE_VERTICAL_SCROLLBAR,
        forceVerticalScrollbar, getIterationHelper());

//   todo: works this? or use that: component.setVar(var);
    ComponentUtil.setStringProperty(component, ATTR_VAR, var,
        getIterationHelper());

    component.getAttributes().put(ATTR_INNER_WIDTH, new Integer(-1));

    // todo: check, if it is an writeable object
    if (stateBinding != null && isValueReference(stateBinding)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(
          stateBinding, getIterationHelper());
      component.setValueBinding(ATTR_STATE_BINDING, valueBinding);
    }
  }

// ------------------------------------------------------------ getter + setter

  public String getColumns() {
    return columns;
  }

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getHideHeader() {
    return hideHeader;
  }

  public void setHideHeader(String hideHeader) {
    this.hideHeader = hideHeader;
  }

  public String getPagingLength() {
    return pagingLength;
  }

  public void setPagingLength(String pagingLength) {
    this.pagingLength = pagingLength;
  }

  public String getPagingStart() {
    return pagingStart;
  }

  public void setPagingStart(String pagingStart) {
    this.pagingStart = pagingStart;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  public void setDirectLinkCount(String directLinkCount) {
    this.directLinkCount = directLinkCount;
  }

  public void setForceVerticalScrollbar(String forceVerticalScrollbar) {
    this.forceVerticalScrollbar = forceVerticalScrollbar;
  }

  public void setShowDirectLinks(String showDirectLinks) {
    this.showDirectLinks = showDirectLinks;
  }

  public void setShowPageRange(String showPageRange) {
    this.showPageRange = showPageRange;
  }

  public void setShowRowRange(String showRowRange) {
    this.showRowRange = showRowRange;
  }

  public void setStateBinding(String stateBinding) {
    this.stateBinding = stateBinding;
  }
}

