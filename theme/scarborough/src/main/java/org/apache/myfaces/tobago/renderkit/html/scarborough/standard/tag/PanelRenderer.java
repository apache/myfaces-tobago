package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

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

/*
  * Created 28.04.2003 at 15:29:36.
  * $Id$
  */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import org.apache.myfaces.tobago.ajax.api.AjaxRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxResponseRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LAYOUT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_RELOAD;

public class PanelRenderer extends LayoutableRendererBase implements AjaxRenderer {

  private static final Log LOG = LogFactory.getLog(PanelRenderer.class);

  public boolean getRendersChildren() {
    return true;
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    // wenn hoehe gesetzt dann diese,
    // sonst wenn layout vorhanden dieses fragen:
    //       -> aus rowLayout berechnen
    // sonst Warnung ausgebenn und addition der children's fixedHeight

    int height =
        ComponentUtil.getIntAttribute(component, ATTR_HEIGHT, -1);

    if (height == -1) {
      height = getFixedHeightForPanel(component, facesContext);
    }
    return height;
  }

  public static int getFixedHeightForPanel(UIComponent component, FacesContext facesContext) {
    int height = -1;
    // first ask layoutManager
    UIComponent layout = component.getFacet(FACET_LAYOUT);
    if (layout != null) {
      LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, layout);
      height = renderer.getFixedHeight(facesContext, component);
    }
    if (height < 0) {

      if (component.getChildren().size() == 0) {
        height = 0;
      } else {

        if (LOG.isDebugEnabled()) {
          LOG.debug("Can't calculate fixedHeight! "
              + "using estimation by contained components. for "
              + component.getClientId(facesContext) + " = "
              + component.getClass().getName() + " "
              + component.getRendererType());
        }

        height = 0;
        for (Object o : component.getChildren()) {
          UIComponent child = (UIComponent) o;
          LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, child);
          if (renderer == null
              && child instanceof UINamingContainer
              && child.getChildren().size() > 0) {
            // this is a subview component ??
            renderer = ComponentUtil.getRenderer(facesContext, (UIComponent) child.getChildren().get(0));
          }
          if (renderer != null) {
            int h = renderer.getFixedHeight(facesContext, child);
            if (h > 0) {
              height += h;
            }
          }
        }
      }
    }
    return height;
  }

  public void encodeChildren(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent;
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      RenderUtil.encode(facesContext, child);
    }
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    String clientId = component.getClientId(facesContext);
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, component);
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);
    //writer.writeStyleAttribute();
    String tip = (String) component.getAttributes().get(TobagoConstants.ATTR_TIP);
    if (tip != null) {
      writer.writeAttribute(HtmlConstants.TITLE, tip, true);
    }
    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      writer.writeJavascript("Tobago.addAjaxComponent(\"" + clientId + "\")");
      Integer frequency = null;
      UIComponent facetReload = component.getFacet(FACET_RELOAD);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        UIReload update = (UIReload) facetReload;
        frequency = update.getFrequency();
      }
      if (frequency != null && frequency > 0) {

        final String[] cmds = {
           "new Tobago.Panel(\"" + clientId + "\", " + true + ", "+ frequency + ");"
        };

        HtmlRendererUtil.writeScriptLoader(facesContext, null, cmds);
      }
      checkForCommandFacet(component, facesContext, writer);
    }
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.endElement(HtmlConstants.DIV);
  }

  public void encodeAjax(FacesContext facesContext, UIComponent component)
      throws IOException {
    AjaxUtils.checkParamValidity(facesContext, component, UIPanel.class);
    boolean update = true;
    final String ajaxId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxId.equals(component.getClientId(facesContext))) {
      if (component.getFacet(FACET_RELOAD) != null && component.getFacet(FACET_RELOAD) instanceof UIReload
          && component.getFacet(FACET_RELOAD).isRendered()
          && !((UIReload) component.getFacet(FACET_RELOAD)).isImmediate()) {
        UIReload reload = (UIReload) component.getFacet(FACET_RELOAD);
        update = reload.getUpdate();
      }
    }
    if (update) {
      component.encodeChildren(facesContext);
    } else {
      facesContext.getResponseWriter().write(AjaxResponseRenderer.CODE_NOT_MODIFIED);
    }
  }

}

