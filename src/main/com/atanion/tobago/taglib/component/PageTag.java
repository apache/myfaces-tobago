/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class PageTag extends TobagoBodyTag {

// ------------------------------------------------------------------ constants

  public static final String PAGE_IN_REQUEST =
      "com.atanion.tobago.taglib.component.Page";

// ----------------------------------------------------------------- attributes

  private String charset;

  private String doctype = "loose";

  private String method = "POST";

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    pageContext.removeAttribute(PAGE_IN_REQUEST, PageContext.REQUEST_SCOPE);
    int result = super.doEndTag();
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
    sb.append(
        ClientProperties.getInstance(facesContext.getViewRoot())
        .getContentType());
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

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_METHOD, method);
    setStringProperty(component, ATTR_CHARSET, charset);
    setStringProperty(component, ATTR_DOCTYPE, doctype);
  }

  public void release() {
    super.release();
    charset = null;
    doctype = "loose";
    method = "POST";
  }
// ------------------------------------------------------------ getter + setter

  public String getCharset() {
    return charset;
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public String getDoctype() {
    return doctype;
  }

  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }
}

