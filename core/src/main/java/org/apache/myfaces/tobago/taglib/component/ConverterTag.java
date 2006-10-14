package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Oct 13, 2006
 * Time: 6:01:59 PM
 */
/**
 * Register an Converter instance on the UIComponent
 * associated with the closest parent UIComponent.
 */
@Tag(name = "converter", bodyContent = BodyContent.EMPTY)
public class ConverterTag extends TagSupport {


  /**
   * <p>The converterId of the {@link javax.faces.convert.Converter}</p>
   */
  private String converterId;
  private String binding;
  /**
   * The converterId of a registered Converter.
   * @param converterId  A valid converterId
   */
  @TagAttribute(required = true)
  public void setConverterId(String converterId) {
    this.converterId = converterId;
  }

  /**
   * The value binding expression to a Converter.
   * @param binding A valid binding
   */
  @TagAttribute
  public void setBinding(String binding) {
    this.binding = binding;
  }



  /**
   * <p>Create a new instance of the specified {@link javax.faces.convert.Converter}
   * class, and register it with the {@link javax.faces.component.UIComponent} instance associated
   * with our most immediately surrounding {@link javax.faces.webapp.UIComponentTag} instance, if
   * the {@link javax.faces.component.UIComponent} instance was created by this execution of the
   * containing JSP page.</p>
   *
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
    if (!(component instanceof ValueHolder)) {
      // TODO Message resource i18n
      throw new JspException("Component "+ component.getClass().getName() + " is not instanceof ValueHolder");
    }
    ValueHolder valueHolder = (ValueHolder) component;

    Converter converter = null;

    if (binding != null && UIComponentTag.isValueReference(binding)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(binding);
      if (valueBinding != null) {
        Object obj = valueBinding.getValue(FacesContext.getCurrentInstance());
        if (obj != null && obj instanceof Converter) {
          converter = (Converter) obj;
        }
      }
    }

    if (converter == null && converterId != null) {
      String localConverterId;
      // evaluate any VB expression that we were passed
      if (UIComponentTag.isValueReference(converterId)) {
        ValueBinding typeValueBinding = ComponentUtil.createValueBinding(converterId);
        localConverterId = (String) typeValueBinding.getValue(FacesContext.getCurrentInstance());
      } else {
        localConverterId = converterId;
      }
      converter = FacesContext.getCurrentInstance().getApplication().createConverter(localConverterId);
      if (converter != null && binding != null) {
        ComponentUtil.setValueForValueBinding(binding, converter);
      }
    }
    if (converter != null) {
      valueHolder.setConverter(converter);
    }
    // TODO else LOG.warn?
    return (SKIP_BODY);
  }


  /**
   * <p>Release references to any acquired resources.
   */
  public void release() {
    this.converterId = null;
    this.binding = null;
  }


}
