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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.config.ThemeConfig;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import javax.faces.render.Renderer;
import javax.faces.FacesException;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.Locale;

public abstract class RendererBase
    extends Renderer implements TobagoRenderer {

  private static final Log LOG = LogFactory.getLog(RendererBase.class);

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
    if ("javax.faces.Text".equals(rendererType)) { // TODO: find a better way
      name = RENDERER_TYPE_OUT;
    } else {
      name = rendererType;
    }
    if (name.startsWith("javax.faces.")) { // FIXME: this is a hotfix from jsf1.0beta to jsf1.0fr
      LOG.warn("patching renderer from " + name);
      name = name.substring("javax.faces.".length());
      LOG.warn("patching renderer to   " + name);
    }
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

  public int getHeaderHeight(
      FacesContext facesContext, UIComponent component) {
    int height = getConfiguredValue(facesContext, component, "headerHeight");
    final UIComponent menubar = component.getFacet(FACET_MENUBAR);
    if (menubar != null) {
      height += getConfiguredValue(facesContext, menubar, "headerHeight");
    }
    return height;
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingWidth");
  }

  public int getPaddingHeight(
      FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingHeight");
  }

  public int getComponentExtraWidth(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraWidth");
  }

  public int getComponentExtraHeight(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraHeight");
  }

  public Dimension getMinimumSize(
      FacesContext facesContext, UIComponent component) {
    int width = getConfiguredValue(facesContext, component, "minimumWidth");
    if (width == -1) {
      width = getConfiguredValue(facesContext, component, "fixedWidth");
    }
    int height = getConfiguredValue(facesContext, component, "minimumHeight");
    if (height == -1) {
      height = getConfiguredValue(facesContext, component, "fixedHeight");
    }
    return new Dimension(width, height);
  }

  public int getFixedWidth(FacesContext facesContext, UIComponent component) {
    return getFixedSpace(facesContext, component, true);
  }
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    return getFixedSpace(facesContext, component, false);
  }
  public int getFixedSpace(FacesContext facesContext, UIComponent component, boolean width) {

    int fixedSpace = 0;

    if (component instanceof UICell) {
      List children = LayoutUtil.addChildren(new ArrayList(), component);
      for (Object aChildren : children) {
        UIComponent child = (UIComponent) aChildren;

        RendererBase renderer = ComponentUtil.getRenderer(facesContext, child);
        if (renderer != null) {
          if (width) {
            fixedSpace = Math.max(fixedSpace, renderer.getFixedWidth(facesContext, child));
          } else {
            fixedSpace = Math.max(fixedSpace, renderer.getFixedHeight(facesContext, child));
          }
        }
      }
    } else {
      if (width) {
        fixedSpace = getFixedSpace(facesContext, component, ATTR_WIDTH, "fixedWidth");
      } else {
        fixedSpace = getFixedSpace(facesContext, component, ATTR_HEIGHT, "fixedHeight");
      }
    }
    return fixedSpace;
  }

  private int getFixedSpace(FacesContext facesContext, UIComponent component,
                            String attr, String attrFixed) {
    int intSpace = -1;
    String space = null;
    if (component != null) {
      space = ComponentUtil.getStringAttribute(component, attr);
    }
    if (space != null) {
      try {
        intSpace = Integer.parseInt(space.replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
        LOG.error("Catched: " + e.getMessage(), e);
      }
    }
    if (intSpace == -1) {
      return getConfiguredValue(facesContext, component, attrFixed);
    } else {
      return intSpace;
    }
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
      currentValue   = RenderUtil.getFormattedValue(facesContext, component, currentObj);
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

  protected void checkForCommandFacet(UIComponent component, FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)) , facesContext, writer);
  }

  protected void checkForCommandFacet(UIComponent component, List<String> clientIds, FacesContext facesContext,
      TobagoResponseWriter writer) throws IOException {
    Map<String, UIComponent> facets = component.getFacets();
    for (Map.Entry<String, UIComponent> entry: facets.entrySet()) {
      if (entry.getValue() instanceof UICommand) {
        addCommandFacet(clientIds, entry, facesContext, writer);
      }
    }
  }

  // TODO create HtmlRendererBase
  private void addCommandFacet(List<String> clientIds, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer) throws
      IOException {
    for (String clientId: clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

  private void writeScriptForClientId(String clientId, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer) throws IOException {
    if (facetEntry.getValue() instanceof UICommand && ((UICommand) facetEntry.getValue()).getRenderedPartially().length > 0) {
      String script =
          "var element = Tobago.element(\"" + clientId  + "\");\n"
              + "if (element) {\n"
              + "   Tobago.addEventListener(element, \"" + facetEntry.getKey()
              + "\", function(){Tobago.reloadComponent('"
              + HtmlRendererUtil.getComponentId(facesContext, facetEntry.getValue(), 
              ((UICommand) facetEntry.getValue()).getRenderedPartially()[0]) + "','"
              + facetEntry.getValue().getClientId(facesContext)  + "', {})});\n"
              + "}";
      HtmlRendererUtil.writeJavascript(writer, script);
    } else {
      String script =
          "var element = Tobago.element(\"" + clientId  + "\");\n"
              + "if (element) {\n"
              + "   Tobago.addEventListener(element, \"" + facetEntry.getKey() + "\", function(){Tobago.submitAction('"
              + facetEntry.getValue().getClientId(facesContext) + "')});\n"
              + "}";
      HtmlRendererUtil.writeJavascript(writer, script);
    }
  }
}
