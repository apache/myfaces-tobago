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
import com.atanion.tobago.taglib.decl.HasDirectLinkCount;
import com.atanion.tobago.taglib.decl.HasForceVerticalScrollbar;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasPagingLength;
import com.atanion.tobago.taglib.decl.HasPagingStart;
import com.atanion.tobago.taglib.decl.HasShowDirectLinks;
import com.atanion.tobago.taglib.decl.HasShowPageRange;
import com.atanion.tobago.taglib.decl.HasShowRowRange;
import com.atanion.tobago.taglib.decl.HasState;
import com.atanion.tobago.taglib.decl.HasStateChangeListener;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.HasVar;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.IsShowHeader;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;

@Tag(name="sheet")
public class SheetTag extends TobagoTag
    implements HasId, HasValue, HasShowRowRange, HasShowPageRange,
               HasShowDirectLinks, HasDirectLinkCount, IsShowHeader,
               HasPagingStart, HasPagingLength, HasColumnLayout, HasVar,
               HasForceVerticalScrollbar, IsRendered, HasBinding,
               HasStateChangeListener
    // todo: don' implement HasState, use annotations at setter
               , HasState
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

  public void setColumns(String columns) {
    this.columns = columns;
  }

  public String getShowHeader() {
    return showHeader;
  }

  public void setShowHeader(String showHeader) {
    this.showHeader = showHeader;
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

  public String getStateChangeListener() {
    return stateChangeListener;
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

  @TagAttribute @UIComponentTagAttribute(type=SheetState.class)
  public void setState(String state) {
    this.state = state;
  }

  public void setStateChangeListener(String stateChangeListener) {
    this.stateChangeListener = stateChangeListener;
  }
}

