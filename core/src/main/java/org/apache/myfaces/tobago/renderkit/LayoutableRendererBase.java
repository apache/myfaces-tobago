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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_MENUBAR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TRANSITION;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.LayoutUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class LayoutableRendererBase
    extends RendererBase implements LayoutInformationProvider {

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

        LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, child);
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
        intSpace = Integer.parseInt(LayoutUtil.stripNonNumericChars(space));
      } catch (NumberFormatException e) {
        LOG.error("Caught: " + e.getMessage(), e);
      }
    }
    if (intSpace == -1) {
      return getConfiguredValue(facesContext, component, attrFixed);
    } else {
      return intSpace;
    }
  }

  protected void checkForCommandFacet(UIComponent component, FacesContext facesContext, TobagoResponseWriter writer)
      throws IOException {
    checkForCommandFacet(component, Arrays.asList(component.getClientId(facesContext)), facesContext, writer);
  }

  protected void checkForCommandFacet(UIComponent component, List<String> clientIds, FacesContext facesContext,
      TobagoResponseWriter writer) throws IOException {
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)
        || ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      // skip if readonly
      return;
    }
    Map<String, UIComponent> facets = component.getFacets();
    for (Map.Entry<String, UIComponent> entry : facets.entrySet()) {
      if (entry.getValue() instanceof UICommand) {
        addCommandFacet(clientIds, entry, facesContext, writer);
      }
    }
  }

  // TODO create HtmlRendererBase
  private void addCommandFacet(List<String> clientIds, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer) throws
      IOException {
    for (String clientId : clientIds) {
      writeScriptForClientId(clientId, facetEntry, facesContext, writer);
    }
  }

  private void writeScriptForClientId(String clientId, Map.Entry<String, UIComponent> facetEntry,
      FacesContext facesContext, TobagoResponseWriter writer) throws IOException {
    if (facetEntry.getValue() instanceof UICommand
        && ((UICommand) facetEntry.getValue()).getRenderedPartially().length > 0) {
      String script =
          "var element = Tobago.element(\"" + clientId + "\");\n"
              + "if (element) {\n"
              + "   Tobago.addEventListener(element, \"" + facetEntry.getKey()
              + "\", function(){Tobago.reloadComponent2(this, '"
              + HtmlRendererUtil.getComponentId(facesContext, facetEntry.getValue(),
              ((UICommand) facetEntry.getValue()).getRenderedPartially()[0]) + "','"
              + facetEntry.getValue().getClientId(facesContext) + "', {})});\n"
              + "}";
      writer.writeJavascript(script);
    } else {
      UIComponent facetComponent = facetEntry.getValue();
      String facetAction = (String) facetComponent.getAttributes().get(ATTR_ONCLICK);
      if (facetAction == null) {
        facetAction = "Tobago.submitAction2(this, '" + facetComponent.getClientId(facesContext) + "', "
            + ComponentUtil.getBooleanAttribute(facetComponent, ATTR_TRANSITION) + ", null, '" + clientId +"')";
      } else {
         // Replace @autoId
        facetAction = StringUtils.replace(facetAction, "@autoId", facetComponent.getClientId(facesContext));
      }
      String script =
          "var element = Tobago.element(\"" + clientId + "\");\n"
              + "if (element) {\n"
              + "   Tobago.addEventListener(element, \"" + facetEntry.getKey() + "\", function(){"
              + facetAction + "});\n}";
      writer.writeJavascript(script);
    }
  }
}
