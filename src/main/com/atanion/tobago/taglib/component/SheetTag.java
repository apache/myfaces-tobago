/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class SheetTag extends TobagoTag {

// ///////////////////////////////////////////// constant

  private static Log log = LogFactory.getLog(SheetTag.class);

// ///////////////////////////////////////////// attribute

  protected String var;
  protected boolean paging = true;
  protected boolean hideHeader;
  protected int pagingStart = 0;
  protected int pagingLength = 20;
  protected int border = 0;
  protected String columnLayout;
  protected String value;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {

    // todo: implement uidata with overridden processUpdates to store state
    return UIData.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    UIData data = (UIData) component;
    super.setProperties(data);
    if (data.getAttributes().get(TobagoConstants.ATTR_PAGING) == null) {
      data.getAttributes().put(TobagoConstants.ATTR_PAGING, new Boolean(paging));
    }
    setBooleanProperty(data,TobagoConstants.ATTR_HIDE_HEADER, hideHeader);
    data.setFirst(pagingStart);
//    if (data.getFirst() == null) {
//      data.getAttributes().put(
//          TobagoConstants.ATTR_FIRST, new Integer(pagingStart));
//    }
    data.setRows(pagingLength);
//    if (data.getAttributes().get(TobagoConstants.ATTR_ROWS) == null) {
//      data.getAttributes().put(
//          TobagoConstants.ATTR_ROWS, new Integer(pagingLength));
//    }

    if (log.isDebugEnabled()) {
      log.debug("innerWidth 1 ==== " + data.getAttributes().get(TobagoConstants.ATTR_INNER_WIDTH) + "   " + data);
    }
    setProperty(data, TobagoConstants.ATTR_INNER_WIDTH, new Integer(-1));
    if (log.isDebugEnabled()) {
      log.debug("innerWidth 2 ==== " + data.getAttributes().get(TobagoConstants.ATTR_INNER_WIDTH) + "   " + data);
    }
    setProperty(data, TobagoConstants.ATTR_COLUMN_LAYOUT, columnLayout);


    if (value != null) {
      if (isValueReference(value)) {
        ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
        data.setValueBinding("value", valueBinding);
      } else {
        data.setValue(value);
      }
    }

    data.setVar(var);
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setVar(String var) {
    this.var = var;
  }

  public void setPaging(boolean paging) {
    this.paging = paging;
  }

  public void setHideHeader(boolean hideHeader) {
    this.hideHeader = hideHeader;
  }

  public void setPagingStart(int pagingStart) {
    this.pagingStart = pagingStart;
  }

  public void setPagingLength(int pagingLength) {
    this.pagingLength = pagingLength;
  }

  public void setBorder(int border) {
    this.border = border;
  }

  public void setColumnLayout(String columnLayout) {
    this.columnLayout = columnLayout;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
