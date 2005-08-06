/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

public abstract class TobagoTag extends UIComponentTag
    implements com.atanion.tobago.taglib.decl.TobagoTag {
  // todo: in java 1.5 use: import static com.atanion.tobago.TobagoConstants.*;

// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(TobagoTag.class);

// ----------------------------------------------------------------- attributes

  protected String label;

  private String title;

  private String width;

  private String height;

  private String hidden;

  private String readonly;

  private String disabled;

  private String inline;

  private String localBinding;

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

    ForEachTag.IterationHelper iterationHelper = getIterationHelper();
    if (iterationHelper != null && localBinding != null) { // todo: ask Volker is this is correct
      setBinding(iterationHelper.replace(localBinding));
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
    inline = null;
    label = null;
    title = null;
    width = null;
    height = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TITLE, title, getIterationHelper());

    ComponentUtil.setBooleanProperty(component, ATTR_DISABLED, disabled, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_READONLY, readonly, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_HIDDEN, hidden, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_INLINE, inline, getIterationHelper());

    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height, getIterationHelper());

//    provideLabel(component);
  }


  protected ForEachTag.IterationHelper getIterationHelper() {
    ForEachTag.IterationHelper iterator = null;
    Tag parent = getParent();

    while (parent != null && ! (parent instanceof ForEachTag)) {
      parent = parent.getParent();
    }
    if (parent != null) {
      iterator = ((ForEachTag)parent).getIterationHelper();
    }
    return iterator;
  }

// ------------------------------------------------------------ getter + setter

  public void setBinding(String binding) throws JspException {
    super.setBinding(binding);
    localBinding = binding;
  }

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

