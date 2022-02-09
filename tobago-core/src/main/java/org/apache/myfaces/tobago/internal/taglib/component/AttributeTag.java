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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Add an attribute on the UIComponent associated with the closest parent UIComponent custom action.
 */
@Tag(name = "attribute")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.AttributeHandler")
public abstract class AttributeTag extends TagSupport {

  private static final long serialVersionUID = 2L;

  private static final Logger LOG = LoggerFactory.getLogger(AttributeTag.class);

  private ValueExpression name;
  private ValueExpression value;

  /**
   * @throws javax.servlet.jsp.JspException if a JSP error occurs
   */
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    final UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return SKIP_BODY;
    }

    final UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }

    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();

    final String attributeName = (String) name.getValue(elContext);

    if (!value.isLiteralText()) {
      component.setValueExpression(attributeName, value);
    } else if (Attributes.STYLE_CLASS.equals(attributeName)) {
      ComponentUtils.setStyleClasses(component, (String) value.getValue(elContext));
    } else if (Attributes.RENDERED_PARTIALLY.equals(attributeName)
        && component instanceof SupportsRenderedPartially) {
      final String[] components = ComponentUtils.splitList((String) value.getValue(elContext));
      ((SupportsRenderedPartially) component).setRenderedPartially(components);
    } else {
      component.getAttributes().put(attributeName, value.getValue(elContext));
    }
    return SKIP_BODY;
  }

  @Override
  public void release() {
    super.release();
    name = null;
    value = null;
  }

  /**
   * The name of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "name", type = "java.lang.String")
  public void setName(final ValueExpression name) {
    this.name = name;
  }

  /**
   * The value of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "value", type = "java.lang.String")
  public void setValue(final ValueExpression value) {
    this.value = value;
  }

  /**
   * Warning: The mode is only available when using Facelets. Allowed values are "action", "actionListener",
   * "actionFromValue", "isNotSet", "isSet", "valueIfSet". <br/> "action" (method binding) evaluate the expression to
   * find the method binding which is referenced with the template. <br/> "actionListener" same as "action" but for the
   * method signature of ActionListeners. <br/> "isSet" (boolean) checks, if the expression is set from the composition
   * caller. <br/> "isNotSet" (boolean) negation of "isSet" <br/> "actionFromValue" Evaluates the ValueBinding to get an
   * outcome set directly (no action method) <br/> "valueIfSet" set the attribute only if the value is set.
   */
  @TagAttribute(name = "mode")
  public void setMode(final ValueExpression mode) {
    LOG.error("The mode is only available when using Facelets, not with JSP.");
  }

}
