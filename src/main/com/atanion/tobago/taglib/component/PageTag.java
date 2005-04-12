/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.taglib.decl.HasCharset;
import com.atanion.tobago.taglib.decl.HasDoctype;
import com.atanion.tobago.taglib.decl.HasFocusId;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasState;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.List;
@Tag(name = "page")
public class PageTag extends TobagoBodyTag
    implements HasLabel, HasDoctype, HasCharset, HasId, HasWidth, HasHeight,
               HasBinding, HasState, HasFocusId
    {
// ------------------------------------------------------------------ constants

  public static final String PAGE_IN_REQUEST =
      "com.atanion.tobago.taglib.component.Page";

// ----------------------------------------------------------------- attributes

  private String charset;

  private String doctype = "loose";

  private String method = "POST";

  private String state;

  private String focusId;

  private String label;

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
    state = null;
    focusId = null;
    label = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_METHOD, method, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_CHARSET, charset, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_DOCTYPE, doctype, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_FOCUS_ID, focusId, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());

    // todo: check, if it is an writeable object
    if (state != null && isValueReference(state)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(state, getIterationHelper());
      component.setValueBinding(ATTR_STATE, valueBinding);
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

  public void setState(String state) {
    this.state = state;
  }

  public String getFocusId() {
    return focusId;
  }

  public void setFocusId(String focusId) {
    this.focusId = focusId;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }
}

