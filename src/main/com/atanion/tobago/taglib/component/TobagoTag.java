/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

public abstract class TobagoTag extends UIComponentTag {

// /////////////////////////////////////////// constants

  private static Log LOG = LogFactory.getLog(TobagoTag.class);

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

  private String themeClass;

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

  protected final void setBooleanProperty(
      UIComponent component, String key, boolean value) {
    if (value) {
      component.getAttributes().put(key, Boolean.TRUE);
    } else {
      component.getAttributes().remove(key);
    }
  }

  protected final void setProperty(
      UIComponent component, String attrName, String vbName, String value) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("attrName = '" + attrName + "'");
      LOG.debug("vbName   = '" + vbName + "'");
    }
    if (value != null) {
      if (isValueReference(value)) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ValueBinding valueBinding = app.createValueBinding(value);
        component.setValueBinding(vbName, valueBinding);
      } else {
        setProperty(component, attrName, value);
      }
    }
  }

  protected final void setBooleanProperty(
      UIComponent component, String attrName, String vbName, String value) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("attrName = '" + attrName + "'");
      LOG.debug("vbName   = '" + vbName + "'");
    }
    if (value != null) {
      if (isValueReference(value)) {
        Application app = FacesContext.getCurrentInstance().getApplication();
        ValueBinding valueBinding = app.createValueBinding(value);
        component.setValueBinding(vbName, valueBinding);
      } else {
        boolean booleanValue = Boolean.valueOf(value).booleanValue();
        setBooleanProperty(component, attrName, booleanValue);
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
        setBooleanProperty(component, TobagoConstants.ATTR_DISABLED, disabledBoolean);
      }
    }

    if (label != null) {
      provideLabel(component);
    }

    setProperty(component, TobagoConstants.ATTR_TITLE,  "title", title);

    setBooleanProperty(component, TobagoConstants.ATTR_READONLY, readonly);
    setBooleanProperty(component, TobagoConstants.ATTR_HIDDEN, hidden);
    setBooleanProperty(component, TobagoConstants.ATTR_I18N, i18n);
    setBooleanProperty(component, TobagoConstants.ATTR_INLINE, inline);

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

    setProperty(component, TobagoConstants.ATTR_THEME_CLASS,
        TobagoConstants.ATTR_THEME_CLASS, themeClass);
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
    if (isValueReference(label)) {
      ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(label);
      uiLabel.setValueBinding("value", valueBinding);
    } else {
      uiLabel.setValue(label);
    }
    component.getFacets().put("label", uiLabel);
  }

  public int doStartTag() throws JspException {

    if (LOG.isDebugEnabled()) {
      LOG.debug("doStartTag() rendererType  " + getRendererType());
      LOG.debug("doStartTag() componentType " + getComponentType());
    }

    final UIComponentTag parent = getParentUIComponentTag(pageContext);
    if (parent instanceof TobagoBodyTag) {
      ((TobagoBodyTag) parent).handleBodyContent();
    } else {
      // plain-text/html will not collected in this case.
    }

    return super.doStartTag();
  }

  public void release() {
    super.release();
    this.hidden = false;
    this.readonly = false;
    this.disabled = null;
    this.inline = false;
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

  public void setThemeClass(String themeClass) {
    this.themeClass = themeClass;
  }
}
