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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManager;
import org.apache.myfaces.tobago.internal.context.ResourceManagerFactory;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Locale;

public class RendererBase extends Renderer {

  private static final Logger LOG = LoggerFactory.getLogger(RendererBase.class);

  private ResourceManager resourceManager;

  /**
   * Hook to e. g. register resources, etc.
   */
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {

    if (component instanceof SupportsMarkup) {
      final SupportsMarkup supportsMarkup = (SupportsMarkup) component;
      Markup markup = ComponentUtils.updateMarkup(component, supportsMarkup.getMarkup());
      supportsMarkup.setCurrentMarkup(markup);
    }
  }

  public boolean getPrepareRendersChildren() {
    return false;
  }

  public void prepareRendersChildren(FacesContext context, UIComponent component) throws IOException {
  }

  /**
   * @deprecated todo: should be done in the StyleClasses class.
   */
  @Deprecated
  protected String getRendererName(String rendererType) {
    return rendererType.substring(0, 1).toLowerCase(Locale.ENGLISH) + rendererType.substring(1);
  }

  /**
   * @deprecated since 1.5.0, please use getResourceManager().getThemeMeasure()
   */
  @Deprecated
  public int getConfiguredValue(FacesContext facesContext, UIComponent component, String key) {
    return getResourceManager().getThemeMeasure(facesContext, (Configurable) component, key).getPixel();
  }

  protected Object getCurrentValueAsObject(UIInput input) {
    Object submittedValue = input.getSubmittedValue();
    if (submittedValue != null) {
      return submittedValue;
    }
    return getValue(input);
  }

  protected String getCurrentValue(FacesContext facesContext, UIComponent component) {

    if (component instanceof EditableValueHolder) {
      EditableValueHolder editableValueHolder = (EditableValueHolder) component;
      Object submittedValue = editableValueHolder.getSubmittedValue();
      if (submittedValue != null || !editableValueHolder.isValid()) {
        return (String) submittedValue;
      }
    }
    String currentValue = null;
    Object currentObj = getValue(component);
    if (currentObj != null) {
      currentValue = getFormattedValue(facesContext, component, currentObj);
    }
    return currentValue;
  }

  protected String getFormattedValue(FacesContext context, UIComponent component, Object currentValue)
      throws ConverterException {

    if (currentValue == null) {
      return "";
    }

    if (!(component instanceof ValueHolder)) {
      return currentValue.toString();
    }

    Converter converter = ((ValueHolder) component).getConverter();

    if (converter == null) {
      if (currentValue instanceof String) {
        return (String) currentValue;
      }
      Class converterType = currentValue.getClass();
      converter = context.getApplication().createConverter(converterType);
    }

    if (converter == null) {
      return currentValue.toString();
    } else {
      return converter.getAsString(context, component, currentValue);
    }
  }

  protected Object getValue(UIComponent component) {
    if (component instanceof ValueHolder) {
      return ((ValueHolder) component).getValue();
    } else {
      return null;
    }
  }

  public Converter getConverter(FacesContext context, UIComponent component) {
    Converter converter = null;
    if (component instanceof ValueHolder) {
      converter = ((ValueHolder) component).getConverter();
    }
    if (converter == null) {
      ValueBinding valueBinding = component.getValueBinding("value");
      if (valueBinding != null) {
        Class converterType = valueBinding.getType(context);
        if (converterType == null || converterType == String.class
            || converterType == Object.class) {
          return null;
        }
        try {
          converter = context.getApplication().createConverter(converterType);
        } catch (FacesException e) {
          LOG.error("No Converter found for type " + converterType);
        }
      }
    }
    return converter;
  }

  @Override
  public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue)
      throws ConverterException {
    if (!(submittedValue instanceof String)) {
      return submittedValue;
    }
    Converter converter = getConverter(context, component);
    if (converter != null) {
      return converter.getAsObject(context, component, (String) submittedValue);
    } else {
      return submittedValue;
    }
  }

  public void onComponentCreated(FacesContext facesContext, UIComponent component, UIComponent parent) {
  }
  
  protected synchronized ResourceManager getResourceManager() {
    if (resourceManager == null) {
      resourceManager = ResourceManagerFactory.getResourceManager(FacesContext.getCurrentInstance());
    }
    return resourceManager;
  }
}
