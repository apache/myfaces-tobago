/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class PageTag extends TobagoBodyTag {

  public static final String PAGE_IN_REQUEST =
      "com.atanion.tobago.taglib.component.Page";

  private String charset;

  private String doctype = "loose";

  private String focusId;

  private String method = "POST";

//  private String enctype;


  public String getComponentType() {
    return UIPage.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    UIPage page = (UIPage) component;
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_METHOD, method);

    setProperty(component, TobagoConstants.ATTR_CHARSET, charset);
    setProperty(component, TobagoConstants.ATTR_DOCTYPE, doctype);
    if (focusId != null) {
      page.setFocusId(focusId);
    }
//    setBooleanProperty(component, TobagoConstants.ATTR_FOCUS_ID, focusId);

//    if (null == component.getAttributes().get(TobagoConstants.ATTR_ENCTYPE)) {
//      component.setAttribute(TobagoConstants.ATTR_ENCTYPE, enctype);
//    }
  }

  public int doStartTag() throws JspException {
    pageContext.getResponse().setContentType(generateContentType(charset));
    pageContext.setAttribute(PAGE_IN_REQUEST, this, PageContext.REQUEST_SCOPE);
    final int result = super.doStartTag();
//    ((UIPage)getComponentInstance()).getOnloadScripts().clear();
    return result;
  }

  public int doEndTag() throws JspException {
    pageContext.removeAttribute(PAGE_IN_REQUEST, PageContext.REQUEST_SCOPE);
    int result = super.doEndTag();
    // reseting doctype and charset
    doctype = "loose";
    charset = null;
    focusId = null;
    return result;
  }

  public static String generateContentType(String charset) {
    StringBuffer sb = new StringBuffer("text/");
    FacesContext facesContext = FacesContext.getCurrentInstance();
    sb.append(ClientProperties.getInstance(facesContext.getViewRoot()).getContentType());
    if (charset == null) {
      charset = "UTF-8";
    }
    sb.append("; charset=");
    sb.append(charset);
    return sb.toString();
  }

  public void setCharset(String charset) {
    this.charset = charset;
  }

  public void setDoctype(String doctype) {
    this.doctype = doctype;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public static PageTag findPageTag(PageContext pageContext) {
    return (PageTag) pageContext.getAttribute(PAGE_IN_REQUEST,
        PageContext.REQUEST_SCOPE);
  }
}

