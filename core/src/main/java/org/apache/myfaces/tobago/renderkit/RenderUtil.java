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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILayout;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.io.IOException;

public class RenderUtil {

  private static final Log LOG = LogFactory.getLog(RenderUtil.class);

  public static final String COMPONENT_IN_REQUEST = "org.apache.myfaces.tobago.component";

  public static boolean contains(Object[] list, Object value) {
    if (list == null) {
      return false;
    }
    for (Object aList : list) {
      if (aList != null && aList.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static void encodeChildren(FacesContext facesContext,
      UIComponent panel)
      throws IOException {
//    UIComponent layout = panel.getFacet("layout");
    UILayout layout = UILayout.getLayout(panel);
    if (layout != null) {
      layout.encodeChildrenOfComponent(facesContext, panel);
    } else {
      for (Object o : panel.getChildren()) {
        UIComponent child = (UIComponent) o;
        encode(facesContext, child);
      }
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }

      LayoutRenderer layoutRenderer = (LayoutRenderer)
          ComponentUtil.getRenderer(facesContext, UILayout.getLayout(component));
      layoutRenderer.prepareRender(facesContext, component);

      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        for (Object o : component.getChildren()) {
          UIComponent kid = (UIComponent) o;
          encode(facesContext, kid);
        }
      }
      component.encodeEnd(facesContext);
    }
  }


  public static String addMenuCheckToggle(String clientId, String onClick) {
    if (onClick != null) {
      onClick = " ; " + onClick;
    } else {
      onClick = "";
    }

    onClick = "menuCheckToggle('" + clientId + "')" + onClick;

    return onClick;
  }

  public static String getFormattedValue(
      FacesContext facesContext, UIComponent component) {
    Object value = null;
    if (component instanceof ValueHolder) {
      value = ((ValueHolder) component).getLocalValue();
      if (value == null) {
        value = ((ValueHolder) component).getValue();
      }
    }
    return getFormattedValue(facesContext, component, value);
  }

  public static String getFormattedValue(
      FacesContext context, UIComponent component, Object currentValue)
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

  public static int calculateStringWidth2(FacesContext facesContext, UIComponent component, String text) {
    int width = 0;
    int defaultCharWidth = 0;
    try {
      defaultCharWidth = ThemeConfig.getValue(facesContext, component, "fontWidth");
    } catch (NullPointerException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("no value for \"fontWidth\" found in theme-config");
      }
    }

    String fontWidths = ResourceManagerUtil.getProperty(facesContext, "tobago", "tobago.font2.widths");

    for (char c : text.toCharArray()) {
      int charWidth;
      if (c >= 32 && c < 128) {
        int begin = (c - 32) * 2;
        charWidth = Integer.parseInt(fontWidths.substring(begin, begin + 2), 16);
      } else {
        charWidth = defaultCharWidth;
      }
      width += charWidth;
    }

    return width;
  }

  public static int calculateStringWidth(FacesContext facesContext, UIComponent component, String text) {
    int width = 0;
    int defaultCharWidth = 0;
    try {
      defaultCharWidth = ThemeConfig.getValue(facesContext, component, "fontWidth");
    } catch (NullPointerException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("no value for \"fontWidth\" found in theme-config");
      }
    }

    String fontWidths = ResourceManagerUtil.getProperty(facesContext, "tobago", "tobago.font.widths");

    for (char c : text.toCharArray()) {
      int charWidth;
      if (c >= 32 && c < 128) {
        int begin = (c - 32) * 2;
        charWidth = Integer.parseInt(fontWidths.substring(begin, begin + 2), 16);
      } else {
        charWidth = defaultCharWidth;
      }
      width += charWidth;
    }

    return width;
  }
}
