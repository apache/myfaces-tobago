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

public abstract class TobagoTag extends UIComponentTag
    implements TobagoConstants {
  // todo: in java 1.5 use: import static com.atanion.tobago.TobagoConstants.*;

// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(TobagoTag.class);

// ----------------------------------------------------------------- attributes

  /**
   * If the control is i18n it is need to be localized.
   * The value will be converted via the
   * internationalization mechanism.
   */
  protected String label;

  private String title;

  private String width;

  private String height;

  private String styleClass;

  private String hidden;

  private String readonly;

  private String disabled;

  private String i18n;

  private String inline;

  private String stateBinding;

  private String themeClass;

// ----------------------------------------------------------- business methods

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

  public void release() {
    super.release();
    hidden = null;
    readonly = null;
    disabled = null;
    i18n = null;
    inline = null;
    label = null;
    title = null;
    width = null;
    height = null;
    styleClass = null;
    stateBinding = null;
    themeClass = null;
  }

  protected final void setIntegerProperty(
      UIComponent component, String name, String value) {
    if (value != null) {
      if (isValueReference(value)) {
        component.setValueBinding(name,
            FacesContext.getCurrentInstance().getApplication()
            .createValueBinding(value));
      } else {
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    Application application = FacesContext.getCurrentInstance().getApplication();

    if (label != null) {
      provideLabel(component);
    }

    setStringProperty(component, ATTR_TITLE, title);

    setBooleanProperty(component, ATTR_DISABLED, disabled);
    setBooleanProperty(component, ATTR_READONLY, readonly);
    setBooleanProperty(component, ATTR_HIDDEN, hidden);
    setBooleanProperty(component, ATTR_I18N, i18n);
    setBooleanProperty(component, ATTR_INLINE, inline);

    setStringProperty(component, ATTR_WIDTH, width);
    setStringProperty(component, ATTR_HEIGHT, height);

    // todo: check, if it is an writeable object
    if (stateBinding != null && isValueReference(stateBinding)) {
      ValueBinding valueBinding = application.createValueBinding(stateBinding);
      component.setValueBinding(ATTR_STATE_BINDING, valueBinding);
    }

    setStringProperty(component, ATTR_STYLE_CLASS, styleClass);
    setStringProperty(component, ATTR_THEME_CLASS, themeClass);
  }

  // fixme: this is not nice!
  protected void provideLabel(UIComponent component) {
    Application application = getFacesContext().getApplication();
    UIOutput uiLabel
        = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
//   7 uiLabel.setRendererType("Text"); // fixme
    uiLabel.setRendererType("Label");
    uiLabel.setRendered(true);
    setStringProperty(uiLabel, "value", label);
    component.getFacets().put("label", uiLabel);
  }

  protected final void setBooleanProperty(
      UIComponent component, String name, String value) {
    if (value != null) {
      if (isValueReference(value)) {
        component.setValueBinding(name,
            FacesContext.getCurrentInstance().getApplication()
            .createValueBinding(value));
      } else {
        if (Boolean.valueOf(value).booleanValue()) {
          component.getAttributes().put(name, Boolean.TRUE);
        } else {
          component.getAttributes().remove(name);
        }
      }
    }
  }

  protected final void setStringProperty(
      UIComponent component, String name, String value) {
    if (value != null) {
      if (isValueReference(value)) {
        component.setValueBinding(name,
            FacesContext.getCurrentInstance().getApplication()
            .createValueBinding(value));
      } else {
        LOG.info("component.getAttributes().put(" +  name + ", " + value +")");
        component.getAttributes().put(name, value);
      }
    }
  }

// ------------------------------------------------------------ getter + setter

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getHidden() {
    return hidden;
  }

  public void setHidden(String hidden) {
    this.hidden = hidden;
  }

  public String getI18n() {
    return i18n;
  }

  public void setI18n(String i18n) {
    this.i18n = i18n;
  }

  public String getInline() {
    return inline;
  }

  public void setInline(String inline) {
    this.inline = inline;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getReadonly() {
    return readonly;
  }

  public void setReadonly(String readonly) {
    this.readonly = readonly;
  }

  public String getStateBinding() {
    return stateBinding;
  }

  public void setStateBinding(String stateBinding) {
    this.stateBinding = stateBinding;
  }

  public String getStyleClass() {
    return styleClass;
  }

  public void setStyleClass(String styleClass) {
    this.styleClass = styleClass;
  }

  public String getThemeClass() {
    return themeClass;
  }

  public void setThemeClass(String themeClass) {
    this.themeClass = themeClass;
  }

  public String getTitle() {
    return title;
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
}

