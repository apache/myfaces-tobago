package org.apache.myfaces.tobago.renderkit;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.config.ThemeConfig;

import javax.faces.FacesException;
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

/**
 * Date: Apr 21, 2007
 * Time: 8:04:25 PM
 */
public class RendererBase extends Renderer {

  private static final Log LOG = LogFactory.getLog(LayoutableRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    // nothing to do

    // FIXME later:
    if (component instanceof UIInput) {
      LOG.warn("decode() should be overwritten! Renderer: "
          + this.getClass().getName());
    }
  }

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    
  }

  public boolean getPrepareRendersChildren() {
    return false;
  }

  public void prepareRendersChildren(FacesContext context, UIComponent component) {
    
  }

  public String getRendererName(String rendererType) {
    String name;
    if (LOG.isDebugEnabled()) {
      LOG.debug("rendererType = '" + rendererType + "'");
    }
    /* if ("javax.faces.Text".equals(rendererType)) { // TODO: find a better way
    name = OUT;
  } else {*/
    name = rendererType;
    /*}
    if (name.startsWith("javax.faces.")) { // FIXME: this is a hotfix from jsf1.0beta to jsf1.0fr
      LOG.warn("patching renderer from " + name);
      name = name.substring("javax.faces.".length());
      LOG.warn("patching renderer to   " + name);
    }*/
    name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
    return name;
  }

  public int getConfiguredValue(FacesContext facesContext,
      UIComponent component, String key) {
    try {
      return ThemeConfig.getValue(facesContext, component, key);
    } catch (Exception e) {
      LOG.error("Can't take '" + key + "' for " + getClass().getName()
          + " from config-file: " + e.getMessage(), e);
    }
    return 0;
  }

  protected Object getCurrentValueAsObject(UIInput input) {
    Object submittedValue = input.getSubmittedValue();
    if (submittedValue != null) {
      return submittedValue;
    }
    return getValue(input);
  }

  protected String getCurrentValue(
      FacesContext facesContext, UIComponent component) {

    if (component instanceof UIInput) {
      Object submittedValue = ((UIInput) component).getSubmittedValue();
      if (submittedValue != null) {
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
      Object value = ((ValueHolder) component).getValue();
      if (LOG.isDebugEnabled()) {
        LOG.debug("component.getValue() returned " + value);
      }
      return value;
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

  public Object getConvertedValue(FacesContext context,
      UIComponent component, Object submittedValue)
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

  public void onComponentCreated(FacesContext context, UIComponent component) {
    
  }
}
