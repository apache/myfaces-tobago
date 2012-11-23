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
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.webapp.UIComponentClassicTagBase;
import javax.faces.webapp.UIComponentELTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/*
 * Date: Oct 13, 2006
 * Time: 6:01:59 PM
 */
/**
 * Register an Converter instance on the UIComponent
 * associated with the closest parent UIComponent.
 */
@Tag(name = "converter", bodyContent = BodyContent.EMPTY)
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.ConverterTag")
public abstract class ConverterTag extends TagSupport {

  private static final long serialVersionUID = 1L;

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
    UIComponentClassicTagBase tag =
        UIComponentELTag.getParentUIComponentClassicTagBase(pageContext);
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
    if (!(component instanceof ValueHolder)) {
      // TODO Message resource i18n
      throw new JspException("Component " + component.getClass().getName() + " is not instanceof ValueHolder");
    }
    ValueHolder valueHolder = (ValueHolder) component;

    Converter converter = null;

    if (isBindingSet() && !isBindingLiteral()) {
      Object valueBinding = getBindingAsBindingOrExpression();
      if (valueBinding != null) {
        Object obj = FacesUtils.getValueFromBindingOrExpression(valueBinding);
        if (obj != null && obj instanceof Converter) {
          converter = (Converter) obj;
        }
      }
    }

    if (converter == null && isConverterIdSet()) {
      String localConverterId;
      // evaluate any VB expression that we were passed
      if (!isConverterIdLiteral()) {
        Object typeValueBinding = getConverterIdAsBindingOrExpression();
        localConverterId = (String) FacesUtils.getValueFromBindingOrExpression(typeValueBinding);
      } else {
        localConverterId = getConverterIdValue();
      }
      converter = FacesContext.getCurrentInstance().getApplication().createConverter(localConverterId);
      if (converter != null && isBindingSet()) {
        Object valueBinding = getBindingAsBindingOrExpression();
        FacesUtils.setValueOfBindingOrExpression(FacesContext.getCurrentInstance(), converter, valueBinding);
      }
    }
    if (converter != null) {
      if (!isBindingLiteral()) {
        FacesUtils.setValueOfBindingOrExpression(
            FacesContext.getCurrentInstance(), converter, component, Attributes.CONVERTER);
      } else {
        valueHolder.setConverter(converter);
      }
    }
    // TODO else LOG.warn?
    return (SKIP_BODY);
  }

  /**
   * The converterId of a registered converter.
   *
   */
  @TagAttribute(name = "converterId", type = "java.lang.String")
  public abstract void setConverterId(ValueExpression converterId);

  public abstract String getConverterIdValue();

  public abstract boolean isConverterIdSet();

  public abstract boolean isConverterIdLiteral();

  public abstract Object getConverterIdAsBindingOrExpression();

  /**
   * The value binding expression to a converter.
   *
   */
  @TagAttribute(name = "binding", type = "javax.faces.convert.Converter")
  public abstract void setBinding(ValueExpression binding);

  public abstract Converter getBindingValue();

  public abstract boolean isBindingSet();

  public abstract boolean isBindingLiteral();

  public abstract Object getBindingAsBindingOrExpression();

}
