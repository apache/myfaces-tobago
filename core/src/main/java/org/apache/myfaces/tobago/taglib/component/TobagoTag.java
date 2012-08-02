/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HIDDEN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TITLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.TOBAGO_COMPONENT_CREATED;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.util.Deprecation;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;

public abstract class TobagoTag extends UIComponentTag
    implements TobagoTagDeclaration {

  private static final Log LOG = LogFactory.getLog(TobagoTag.class);

  private String label;
  private String title;
  private String width;
  private String height;
  private String hidden;
  private String readonly;
  private String disabled;
  private String inline;

  @Override
  public int doStartTag() throws JspException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("doStartTag() rendererType  " + getRendererType());
      LOG.debug("doStartTag() componentType " + getComponentType());
    }
    return super.doStartTag();
  }

  @Override
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

  @Override
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

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_LABEL, label);
    ComponentUtil.setStringProperty(component, ATTR_TITLE, title);

    ComponentUtil.setBooleanProperty(component, ATTR_DISABLED, disabled);
    ComponentUtil.setBooleanProperty(component, ATTR_READONLY, readonly);
    ComponentUtil.setBooleanProperty(component, ATTR_HIDDEN, hidden);
    ComponentUtil.setBooleanProperty(component, ATTR_INLINE, inline);

    if (width != null) {
      LOG.warn("the width attribute is deprecated, please use a layout manager. (" + getClass().getSimpleName() + ")");
    }
    ComponentUtil.setStringProperty(component, ATTR_WIDTH, width);
    if (height != null) {
      LOG.warn("the height attribute is deprecated, please use a layout manager. (" + getClass().getSimpleName() + ")");
    }
    ComponentUtil.setStringProperty(component, ATTR_HEIGHT, height);
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
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("Attribute 'height' is deprecated, "
          + "and will removed soon! Please use a layout manager instead.");
    }
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
    if (Deprecation.LOG.isWarnEnabled()) {
      Deprecation.LOG.warn("Attribute 'width' is deprecated, "
          + "and will removed soon! Please use a layout manager instead.");
    }
    this.width = width;
  }

  public int doEndTag() throws JspException {

    UIComponent component = getComponentInstance();
    if (component instanceof OnComponentCreated
        && component.getAttributes().get(TOBAGO_COMPONENT_CREATED) == null) {
      component.getAttributes().put(TOBAGO_COMPONENT_CREATED, Boolean.TRUE);
      ((OnComponentCreated) component).onComponentCreated();
    }
    return super.doEndTag();
  }
}
