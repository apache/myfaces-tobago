/*
 * Copyright 2002-2005 atanion GmbH.
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
 * Created: Oct 1, 2002 1:21:21 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.taglib.decl.HasState;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.List;
import static org.apache.myfaces.tobago.TobagoConstants.*;

/**
 * 
 */
@Tag(name = "page")
public class PageTag extends TobagoBodyTag
    implements HasLabel, HasId, HasDimension, HasBinding, HasState
    {
// ------------------------------------------------------------------ constants

  public static final String PAGE_IN_REQUEST =
      "org.apache.myfaces.tobago.taglib.component.Page";

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
    UIPage page = (UIPage) getComponentInstance();
    List<UIPopup> popups = page.getPopups();
    int result = super.doEndTag();

    // clear popups;
    popups.clear();

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


  /**
   *  The charset to render.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setCharset(String charset) {
    this.charset = charset;
  }


  /**
   * values for doctype :
        'strict'   : HTML 4.01 Strict DTD
        'loose'    : HTML 4.01 Transitional DTD
        'frameset' : HTML 4.01 Frameset DTD
        all other values are ignored and no DOCTYPE is set.
        default value is 'loose'
   *
   * @param doctype
   */
  @TagAttribute
  @UIComponentTagAttribute( defaultValue = "loose")
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


  /**
   * Contains the id of the component witch should have the focus after
        loading the page.
        Set to emtpy string for disabling setting of focus.
        Default (null) enables the "auto focus" feature.
   * @param focusId
   */
  @TagAttribute
  @UIComponentTagAttribute()
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

