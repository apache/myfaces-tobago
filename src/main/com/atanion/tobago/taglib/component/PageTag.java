/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.List;

public class PageTag extends TobagoBodyTag {
// ------------------------------------------------------------------ constants

  public static final String PAGE_IN_REQUEST =
      "com.atanion.tobago.taglib.component.Page";

// ----------------------------------------------------------------- attributes

  private String charset;

  private String doctype = "loose";

  private String method = "POST";

  private String stateBinding;

  private String focusId;

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    pageContext.removeAttribute(PAGE_IN_REQUEST, PageContext.REQUEST_SCOPE);
    List popups = (List) getComponentInstance().getAttributes().get(ATTR_POPUP_LIST);
    int result = super.doEndTag();

    // clear popups;
    if (popups != null) {
      popups.clear();
    }

    // reseting doctype and charset
    doctype = "loose";
    charset = null;
    return result;
  }

  public int doStartTag() throws JspException {
    pageContext.getResponse().setContentType(generateContentType(charset));
    pageContext.setAttribute(PAGE_IN_REQUEST, this, PageContext.REQUEST_SCOPE);
    final int result = super.doStartTag();
    return result;
  }

  public static String generateContentType(String charset) {
    StringBuffer sb = new StringBuffer("text/");
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ClientProperties clientProperties
        = ClientProperties.getInstance(facesContext.getViewRoot());
    sb.append(clientProperties.getContentType());
    if (charset == null) {
      charset = "UTF-8";
    }
    sb.append("; charset=");
    sb.append(charset);
    return sb.toString();
  }

  public static PageTag findPageTag(PageContext pageContext) {
    return (PageTag) pageContext.getAttribute(PAGE_IN_REQUEST,
        PageContext.REQUEST_SCOPE);
  }

  public String getComponentType() {
    return UIPage.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    charset = null;
    doctype = "loose";
    method = "POST";
    stateBinding = null;
    focusId = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_METHOD, method, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_CHARSET, charset, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_DOCTYPE, doctype, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_FOCUS_ID, focusId, getIterationHelper());

    // todo: check, if it is an writeable object
    if (stateBinding != null && isValueReference(stateBinding)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(stateBinding, getIterationHelper());
      component.setValueBinding(ATTR_STATE_BINDING, valueBinding);
    }
  }

// ------------------------------------------------------------ getter + setter

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public void setStateBinding(String stateBinding) {
    this.stateBinding = stateBinding;
  }

  public String getFocusId() {
    return focusId;
  }

  public void setFocusId(String focusId) {
    this.focusId = focusId;
  }
}

