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

import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.FacesException;
import java.util.Locale;

/**
 * Date: Apr 21, 2007
 * Time: 8:04:25 PM
 */
public class RendererBase extends Renderer {
  protected static final Log LOG = LogFactory.getLog(LayoutableRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    // nothing to do

    // FIXME later:
    if (component instanceof UIInput) {
      LOG.warn("decode() should be overwritten! Renderer: "
          + this.getClass().getName());
    }
  }

  public String getRendererName(String rendererType) {
    String name;
    if (LOG.isDebugEnabled()) {
      LOG.debug("rendererType = '" + rendererType + "'");
    }
    /* if ("javax.faces.Text".equals(rendererType)) { // TODO: find a better way
    name = RENDERER_TYPE_OUT;
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
      currentValue = RenderUtil.getFormattedValue(facesContext, component, currentObj);
    }
    return currentValue;
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
}
