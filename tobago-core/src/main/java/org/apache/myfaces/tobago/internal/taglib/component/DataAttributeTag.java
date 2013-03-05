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

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.apt.annotation.SimpleTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * PRELIMINARY - SUBJECT TO CHANGE
 * <p/>
 * Add an data attribute on the UIComponent
 * associated with the closest parent UIComponent custom action.
 * Data attributes will be passed through the renderers into the DOM of the user agent and
 * can be used by scripts.
 */
@Preliminary
@Tag(name = "dataAttribute", bodyContent = BodyContent.EMPTY)
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.DataAttributeHandler")
public abstract class DataAttributeTag extends TagSupport {

  private static final long serialVersionUID = 2L;

  private javax.el.ValueExpression name;
  private javax.el.ValueExpression value;

  @Override
  public void release() {
    super.release();
    name = null;
    value = null;
  }

  /**
   * @throws javax.servlet.jsp.JspException if a JSP error occurs
   */
  public int doStartTag() throws JspException {

    // Locate our parent UIComponentTag
    UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
    if (tag == null) {
      // TODO Message resource i18n
      throw new JspException("Not nested in faces tag");
    }

    if (!tag.getCreated()) {
      return (SKIP_BODY);
    }

    final UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }

    final ELContext elContext = FacesContext.getCurrentInstance().getELContext();

    final Object attributeName = name.getValue(elContext);
    final Object attributeValue = value.getValue(elContext);

    ComponentUtils.putDataAttribute(component, attributeName, attributeValue);

    return (SKIP_BODY);
  }

  /**
   * PRELIMINARY - SUBJECT TO CHANGE
   * <p/>
   * The name of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "name", type = "java.lang.String")
  public void setName(ValueExpression name) {
    this.name = name;
  }

  /**
   * PRELIMINARY - SUBJECT TO CHANGE
   * <p/>
   * The value of the attribute in the parent component.
   */
  @TagAttribute(required = true, name = "value", type = "java.lang.String")
  public void setValue(ValueExpression value) {
    this.value = value;
  }
}
