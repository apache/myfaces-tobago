/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.el.ElUtil;
import com.atanion.tobago.taglib.core.SubviewTag;
import com.atanion.tobago.taglib.core.ViewTag;
import com.atanion.util.ObjectUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Stack;

public abstract class TobagoTag extends UIComponentTag {

// /////////////////////////////////////////// constants

  private static Log LOG = LogFactory.getLog(TobagoTag.class);

  private static String TAG_STACK
      = "com.atanion.tobago.taglib.component.TobagoTag.TAG_STACK";

// /////////////////////////////////////////// attributes

  private boolean hidden = false;

  private boolean readonly = false;

  private String disabled;

  /**
   * If the control is i18n it is need to be localized.
   * The value will be converted via the
   * internationalization mechanism.
   */
  protected boolean i18n = false;

  protected boolean inline = false;

  protected String label;

  protected String title;

  protected String width;

  protected String height;

  protected String styleClass;

  private String stateBinding;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getRendererType() {

    String name = getClass().getName();
    int beginIndex = name.lastIndexOf('.');
    if (beginIndex < 0) {
      beginIndex = 0;
    } else {
      beginIndex++;
    }
    int endIndex = name.length() - 3; // 3 = "Tag".length()
    return name.substring(beginIndex, endIndex);
  }

  protected final void setProperty(
      UIComponent component, String key, Object value) {
    if (value != null) {
      component.getAttributes().put(key, value);
    }
  }

  protected final void setProperty(
      UIComponent component, String key, boolean value) {
    if (value) {
      component.getAttributes().put(key, Boolean.TRUE);
    } else {
      component.getAttributes().remove(key);
    }
  }

  protected final void setProperty(
      UIComponent component, String attrName, String vbName, String string) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("attrName = '" + attrName + "'");
      LOG.debug("vbName   = '" + vbName + "'");
    }
    if (string != null) {
      if (isValueReference(string)) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ValueBinding valueBinding = app.createValueBinding(string);
        component.setValueBinding(vbName, valueBinding);
      } else {
        boolean disabledBoolean = Boolean.valueOf(string).booleanValue();
        setProperty(component, attrName, disabledBoolean);
      }
    }
  }

  protected void setProperties(UIComponent component) {

    super.setProperties(component);

    Application application = FacesContext.getCurrentInstance().getApplication();
    if (disabled != null) {
      if (isValueReference(disabled)) {
        ValueBinding valueBinding = application.createValueBinding(disabled);
        component.setValueBinding(TobagoConstants.VB_DISABLED, valueBinding);
      } else {
        boolean disabledBoolean = Boolean.valueOf(disabled).booleanValue();
        setProperty(component, TobagoConstants.ATTR_DISABLED, disabledBoolean);
      }
    }

    if (label != null) {
      provideLabel(component);
    }

    if (title != null) {
      if (isValueReference(title)) {
        ValueBinding valueBinding = application.createValueBinding(title);
        component.setValueBinding("title", valueBinding);
      } else {
        component.getAttributes().put(TobagoConstants.ATTR_TITLE, title);
      }
    }

    setProperty(component, TobagoConstants.ATTR_READONLY, readonly);
    setProperty(component, TobagoConstants.ATTR_HIDDEN, hidden);
    setProperty(component, TobagoConstants.ATTR_I18N, i18n);
    setProperty(component, TobagoConstants.ATTR_INLINE, inline);

    if (width != null) {
      if (isValueReference(width)) {
        ValueBinding valueBinding = application.createValueBinding(width);
        component.setValueBinding("width", valueBinding);
      } else {
        component.getAttributes().put(TobagoConstants.ATTR_WIDTH, width);
      }
    }

    if (height != null) {
      if (isValueReference(height)) {
        ValueBinding valueBinding = application.createValueBinding(height);
        component.setValueBinding("height", valueBinding);
      } else {
        component.getAttributes().put(TobagoConstants.ATTR_HEIGHT, height);
      }
    }

    if (stateBinding != null && isValueReference(stateBinding)) {
      ValueBinding valueBinding = application.createValueBinding(stateBinding);
      component.setValueBinding(TobagoConstants.ATTR_STATE_BINDING, valueBinding);
    }

    setProperty(component, TobagoConstants.ATTR_STYLE_CLASS, styleClass);
  }



  protected void provideAttribute(UIComponent component, String name, String key) {
    if (isValueReference(name)) {
      ValueBinding valueBinding =
          FacesContext.getCurrentInstance().getApplication().createValueBinding(name);
      component.setValueBinding(key, valueBinding);
    } else {
      component.getAttributes().put(key, name);
    }
  }

  // fixme: this is not nice!
  protected void provideLabel(UIComponent component) {
    Application application = getFacesContext().getApplication();
    UIOutput uiLabel
        = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
//    uiLabel.setRendererType("Text"); // fixme
    uiLabel.setRendererType("Label");
    uiLabel.setRendered(true);
    uiLabel.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    if (isValueReference(label)) {
      ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(label);
      uiLabel.setValueBinding("value", valueBinding);
    } else {
      uiLabel.setValue(label);
    }
    component.getFacets().put("label", uiLabel);
  }

  public int doStartTag() throws JspException {

    Stack tagStack = ensureTagStack();
    tagStack.push(this);

    LOG.debug("doStartTag() rendererType  " + getRendererType());
    LOG.debug("doStartTag() componentType " + getComponentType());

//    Log.debug("##########################################");
//    Log.debug("### # " + getClass().getName());
//    for (Tag tag = getParent(); tag != null; tag = tag.getParent()) {
//      Log.debug("### . " + tag.getClass().getName());
//    }
//    Log.debug("##########################################");

    final TobagoTag parent = getTobagoParent(tagStack);
    LOG.debug("parent is: " + parent);
    if (parent instanceof TobagoBodyTag) {
      ((TobagoBodyTag) parent).handleBodyContent();
    } else {
//      log.warn("Found parent with is not TobagoBodyTag. id = '" + id + "'");
      // plain-text/html will not collected in this case.
    }

    int result = super.doStartTag();
//    LOG.debug("isSuppressed=" + isSuppressed() + "  Tag=" + this);
    LOG.debug("doStartTag() component     " + getComponentInstance());
    return result;
  }

  public int doEndTag() throws JspException {

//    Log.debug("doEndTag() rendererType  " + getRendererType());

    UIComponent thisComponent = getComponentInstance();
    LOG.debug(
        "ATTR_SUPPRESSED " + (thisComponent != null) + " " + isSuppressed()
        + " " + getId() + " " + this);
//    if (thisComponent != null && isSuppressed()) {
    if (isSuppressed()) {
      getComponentInstance().getAttributes().put(
          TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    }
    int result = super.doEndTag();
    ensureTagStack().pop();
    return result;
  }

  protected boolean isSuppressed() {
    if (getFacetName() != null) {
      return true;
    }
    if (!getComponentInstance().isRendered()) {
      return true;
    }
    for (UIComponent component = getComponentInstance().getParent();
        component != null; component = component.getParent()) {
      if (!component.isRendered()) {
        return true;
      }
      if (component.getRendersChildren()) {
        return true;
      }
    }
    return false;
  }

  public void release() {
    super.release();
    this.hidden = false;
    this.readonly = false;
    this.disabled = null;
    this.inline = false;
  }

  protected Stack ensureTagStack() {
    Stack tagStack = (Stack) pageContext.getAttribute(
        TAG_STACK, PageContext.REQUEST_SCOPE);
    if (tagStack == null) {
      if (!(this instanceof ViewTag || this instanceof SubviewTag)) {
        String text = "ViewTag or SubviewTag must be the outermost UIComponentTag";
        LOG.error(text);
        LOG.error(this);
        LOG.error(this.getClass().getName());
        LOG.error(ObjectUtils.toString(this, 2));
        throw new IllegalStateException(text);
      }
      tagStack = new Stack();
      pageContext.setAttribute(TAG_STACK, tagStack, PageContext.REQUEST_SCOPE);
    }
    return tagStack;
  }

  protected TobagoTag getTobagoParent(Stack tagStack) {
    for (int i = tagStack.size() - 1; i >= 1; i--) {
      if (this.equals(tagStack.get(i))) {
        return (TobagoTag) tagStack.get(i - 1);
      }
    }
    return null;
  }

// /////////////////////////////////////////// bean getter + setter

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public void setI18n(boolean i18n) {
    this.i18n = i18n;
  }

  public void setInline(boolean inline) {
    this.inline = inline;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }

  public void setStateBinding(String stateBinding) {
    this.stateBinding = stateBinding;
  }
}
