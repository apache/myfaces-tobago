/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Dec 2, 2002 at 5:23:53 PM.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIData;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import java.util.Collections;
import java.util.List;

public class SheetTag extends TobagoTag {

  private static final Log LOG = LogFactory.getLog(SheetTag.class);

// ----------------------------------------------------------------- attributes

  private String var;
  private String paging = "true";
  private String hideHeader;
  private String pagingStart = "0";
  private String pagingLength = "20";
  private String columns;
  private String value;
  private String forceVerticalScrollbar;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    // todo: implement uidata with overridden processUpdates to store state
    return UIData.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {
    try {
      UIData component = (UIData) getComponentInstance();
      Object value = component.getValue();
      if (value == null) {
        LOG.warn("Found 'null' as sheet's value! using empty List instead!");
        component.setValue(Collections.EMPTY_LIST);
      } else if (value instanceof Object[] || value instanceof List) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("value = Object[] || List");
        }
      }else  {
        LOG.error("Found illegal type as scheet's value");
        throw new JspException("Illegal value type for sheet: "
            + value.getClass().getName(),
            new IllegalArgumentException(value.getClass().getName()));
      }
    } catch (Throwable e) {
      LOG.error("#+#+#+#+#", e);
    }
    return super.doEndTag();
  }

  public void release() {
    super.release();
    var = null;
    paging = "true";
    hideHeader = null;
    pagingStart = "0";
    pagingLength = "20";
    columns = null;
    value = null;
    forceVerticalScrollbar = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setBooleanProperty(component, ATTR_PAGING, paging);
   ComponentUtil.setBooleanProperty(component, ATTR_HIDE_HEADER, hideHeader);
   ComponentUtil.setIntegerProperty(component, ATTR_FIRST, pagingStart);
   ComponentUtil.setIntegerProperty(component, ATTR_ROWS, pagingLength);
   ComponentUtil.setStringProperty(component, ATTR_COLUMNS, columns);
   ComponentUtil.setStringProperty(component, ATTR_VALUE, value);
   ComponentUtil.setStringProperty(component, ATTR_SCROLLBARS, forceVerticalScrollbar);

//   todo: works this? or use that: component.setVar(var);
   ComponentUtil.setStringProperty(component, ATTR_VAR, var);

    component.getAttributes().put(ATTR_INNER_WIDTH, new Integer(-1));
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

  public String getPaging() {
    return paging;
  }

  public void setPaging(String paging) {
    this.paging = paging;
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

  public void setForceVerticalScrollbar(String forceVerticalScrollbar) {
    this.forceVerticalScrollbar = forceVerticalScrollbar;
  }
}

