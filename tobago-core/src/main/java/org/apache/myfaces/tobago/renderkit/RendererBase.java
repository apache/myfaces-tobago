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
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Locale;

public class RendererBase extends Renderer {

  private static final Logger LOG = LoggerFactory.getLogger(RendererBase.class);

  private ResourceManager resourceManager;

  /**
   * Hook to e. g. register resources, etc.
   */
  public void prepareRender(final FacesContext facesContext, final UIComponent component) throws IOException {

    if (component instanceof SupportsMarkup) {
      final SupportsMarkup supportsMarkup = (SupportsMarkup) component;
      final Markup markup = ComponentUtils.updateMarkup(component, supportsMarkup.getMarkup());
      supportsMarkup.setCurrentMarkup(markup);
    }
  }

  public boolean getPrepareRendersChildren() {
    return false;
  }

  public void prepareRendersChildren(final FacesContext context, final UIComponent component) throws IOException {
  }

  /**
   * @deprecated todo: should be done in the StyleClasses class.
   */
  @Deprecated
  protected String getRendererName(final String rendererType) {
    return rendererType.substring(0, 1).toLowerCase(Locale.ENGLISH) + rendererType.substring(1);
  }

  /**
   * @deprecated since 1.5.0, please use getResourceManager().getThemeMeasure()
   */
  @Deprecated
  public int getConfiguredValue(final FacesContext facesContext, final UIComponent component, final String key) {
    return getResourceManager().getThemeMeasure(facesContext, (Configurable) component, key).getPixel();
  }

  protected Object getCurrentValueAsObject(final UIInput input) {
    final Object submittedValue = input.getSubmittedValue();
    if (submittedValue != null) {
      return submittedValue;
    }
    return getValue(input);
  }

  protected String getCurrentValue(final FacesContext facesContext, final UIComponent component) {

    if (component instanceof EditableValueHolder) {
      final EditableValueHolder editableValueHolder = (EditableValueHolder) component;
      final Object submittedValue = editableValueHolder.getSubmittedValue();
      if (submittedValue != null || !editableValueHolder.isValid()) {
        return (String) submittedValue;
      }
    }
    String currentValue = null;
    final Object currentObj = getValue(component);
    if (currentObj != null) {
      currentValue = getFormattedValue(facesContext, component, currentObj);
    }
    return currentValue;
  }

  protected String getFormattedValue(final FacesContext context, final UIComponent component, final Object currentValue)
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
      if (currentValue instanceof Measure) {
        return ((Measure) currentValue).serialize();
      }
      final Class converterType = currentValue.getClass();
      converter = context.getApplication().createConverter(converterType);
    }

    if (converter == null) {
      return currentValue.toString();
    } else {
      return converter.getAsString(context, component, currentValue);
    }
  }

  protected Object getValue(final UIComponent component) {
    if (component instanceof ValueHolder) {
      return ((ValueHolder) component).getValue();
    } else {
      return null;
    }
  }

  public Converter getConverter(final FacesContext context, final UIComponent component) {
    Converter converter = null;
    if (component instanceof ValueHolder) {
      converter = ((ValueHolder) component).getConverter();
    }
    if (converter == null) {
      final ValueExpression valueExpression = component.getValueExpression("value");
      if (valueExpression != null) {
        final Class converterType = valueExpression.getType(context.getELContext());
        if (converterType == null || converterType == String.class
            || converterType == Object.class) {
          return null;
        }
        try {
          converter = context.getApplication().createConverter(converterType);
        } catch (final FacesException e) {
          LOG.error("No Converter found for type " + converterType);
        }
      }
    }
    return converter;
  }

  @Override
  public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
      throws ConverterException {
    if (!(submittedValue instanceof String)) {
      return submittedValue;
    }
    final Converter converter = getConverter(context, component);
    if (converter != null) {
      return converter.getAsObject(context, component, (String) submittedValue);
    } else {
      return submittedValue;
    }
  }

  public void onComponentCreated(
      final FacesContext facesContext, final UIComponent component, final UIComponent parent) {
  }
  
  protected synchronized ResourceManager getResourceManager() {
    if (resourceManager == null) {
      resourceManager = ResourceManagerFactory.getResourceManager(FacesContext.getCurrentInstance());
    }
    return resourceManager;
  }
}
