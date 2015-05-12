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

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Register an Converter instance on the UIComponent
 * associated with the closest parent UIComponent.
 */
@Tag(name = "converter")
@SimpleTag(
    faceletHandler = "org.apache.myfaces.tobago.facelets.ConverterHandler")
public abstract class ConverterTag extends TagSupport {

  private static final long serialVersionUID = 2L;

  private ValueExpression binding;
  private ValueExpression converterId;

  /**
   * Create a new instance of the specified {@link javax.faces.convert.Converter}
   * class, and register it with the {@link javax.faces.component.UIComponent} instance associated
   * with our most immediately surrounding {@link javax.faces.webapp.UIComponentELTag} instance, if
   * the {@link javax.faces.component.UIComponent} instance was created by this execution of the
   * containing JSP page.
   *
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
      return (SKIP_BODY);
    }

    final UIComponent component = tag.getComponentInstance();
    if (component == null) {
      // TODO Message resource i18n
      throw new JspException("Component Instance is null");
    }
    if (!(component instanceof ValueHolder)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof ValueHolder");
    }
    final ValueHolder valueHolder = (ValueHolder) component;

    Converter converter = null;

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ELContext elContext = facesContext.getELContext();

    if (binding != null && !binding.isLiteralText()) {
      converter = (Converter) binding.getValue(elContext);
    }

    if (converter == null && converterId != null) {
      final String localConverterId = (String) converterId.getValue(elContext);
      // evaluate any VB expression that we were passed
      converter = facesContext.getApplication().createConverter(localConverterId);
    }

    if (converter != null) {
      if (!binding.isLiteralText()) {
        final ValueExpression ve = component.getValueExpression(Attributes.CONVERTER);
        if (ve != null) {
          ve.setValue(elContext, converter);
        }
      } else {
        valueHolder.setConverter(converter);
      }
    }

    return (SKIP_BODY);
  }

  @Override
  public void release() {
    super.release();
    binding = null;
    converterId = null;
  }

  /**
   * The converterId of a registered converter.
   */
  @TagAttribute(name = "converterId", type = "java.lang.String")
  public void setConverterId(final ValueExpression converterId) {
    this.converterId = converterId;
  }

  /**
   * The value binding expression to a converter.
   */
  @TagAttribute(name = "binding", type = "javax.faces.convert.Converter")
  public void setBinding(final ValueExpression binding) {
    this.binding = binding;
  }
}
