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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.ValueHolder;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * Date: Oct 14, 2006
 * Time: 1:47:13 PM
 */

/**
 * Add an attribute on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "attribute", bodyContent = BodyContent.EMPTY)
public class AttributeTag extends TagSupport {

  private static final long serialVersionUID = 6231531736083277631L;

  /**
   * <p>The name of the attribute</p>
   */
  private String name;

  /**
   * <p>The value of the attribute</p>
   */
  private String value;

  /**
   * The name of a attribute.
   *
   * @param name A attribute name
   */
  @TagAttribute(required = true)
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The value of a attribute
   *
   * @param value A attribute value
   */
  @TagAttribute(required = true)
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * @throws javax.servlet.jsp.JspException if a JSP error occurs
   */
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    UIComponentTag tag =
        UIComponentTag.getParentUIComponentTag(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return (SKIP_BODY);
    }

    UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }
    String attributeName = name;

    if (UIComponentTag.isValueReference(name)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(name);
      if (valueBinding != null) {
        attributeName = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
      } else {
        // TODO Message resource i18n
        throw new JspException("Can not get ValueBinding for attribute name " + name);
      }
    }
    if (component instanceof EditableValueHolder
        && TobagoConstants.ATTR_VALIDATOR.equals(attributeName)) {
      ComponentUtil.setValidator((EditableValueHolder) component, value);
    } else if (component instanceof ValueHolder
        && TobagoConstants.ATTR_CONVERTER.equals(attributeName)) {
      ComponentUtil.setConverter((ValueHolder) component, value);
    } else if (TobagoConstants.ATTR_STYLE_CLASS.equals(attributeName)) {
      ComponentUtil.setStyleClasses(component, value);
    } else if (TobagoConstants.ATTR_RENDERED_PARTIALLY.equals(attributeName)
        && component instanceof UICommand) {
      ComponentUtil.setRenderedPartially((UICommand) component, value);
    } else if (UIComponentTag.isValueReference(value)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(value);
      if (valueBinding != null) {
        component.setValueBinding(name, valueBinding);
      } else {
        // TODO Message resource i18n
        throw new JspException("Can not get ValueBinding for attribute value " + value);
      }
    } else {
      component.getAttributes().put(attributeName, value);
    }

    return (SKIP_BODY);
  }


  /**
   * <p>Release references to any acquired resources.
   */
  public void release() {
    super.release();
    this.name = null;
    this.value = null;
  }
}
