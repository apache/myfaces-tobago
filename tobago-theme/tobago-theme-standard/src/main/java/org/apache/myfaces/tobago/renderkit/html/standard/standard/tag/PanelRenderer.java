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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanel;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PanelRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(PanelRenderer.class);

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent uiComponent) throws IOException {
    final UIPanel component = (UIPanel) uiComponent;
    for (final UIComponent child : component.getChildren()) {
      RenderUtils.encode(facesContext, child);
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    
    // UIPanel or UICell (deprecated)
    final AbstractUIPanel panel = (AbstractUIPanel) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    final String clientId = panel.getClientId(facesContext);
    writer.startElement(HtmlElements.DIV, panel);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(panel).getStringValue() + " " + BootstrapClass.ROW.getName());
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, panel);
    if (panel instanceof UIPanel && ((UIPanel) panel).getTip() != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, ((UIPanel) panel).getTip(), true);
    }

    // TODO check ajax id?
    if (!FacesContextUtils.isAjax(facesContext)) {
      final UIComponent facetReload = panel.getFacet(Facets.RELOAD);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        final UIReload update = (UIReload) facetReload;
        writer.writeAttribute(DataAttributes.RELOAD, Integer.toString(update.getFrequency()), false);
      }
    }
    HtmlRendererUtils.renderCommandFacet(panel, facesContext, writer);
    HtmlRendererUtils.encodeContextMenu(facesContext, writer, panel);

    //HtmlRendererUtils.checkForCommandFacet(panel, facesContext, writer);

    final Measure borderLeft = panel.getBorderLeft();
    final Measure borderRight = panel.getBorderRight();
    final Measure borderTop = panel.getBorderTop();
    final Measure borderBottom = panel.getBorderBottom();

    if (borderLeft.greaterThan(Measure.ZERO) || borderRight.greaterThan(Measure.ZERO)
        || borderTop.greaterThan(Measure.ZERO) || borderBottom.greaterThan(Measure.ZERO)) {
      writer.startElement(HtmlElements.DIV, panel);
      writer.writeClassAttribute(Classes.create(panel, "content")); // needed to be scrollable inside of the panel
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final ResponseWriter writer = facesContext.getResponseWriter();
    final AbstractUIPanel panel = (AbstractUIPanel) component;
    
    final Measure borderLeft = panel.getBorderLeft();
    final Measure borderRight = panel.getBorderRight();
    final Measure borderTop = panel.getBorderTop();
    final Measure borderBottom = panel.getBorderBottom();

    if (borderLeft.greaterThan(Measure.ZERO) || borderRight.greaterThan(Measure.ZERO)
        || borderTop.greaterThan(Measure.ZERO) || borderBottom.greaterThan(Measure.ZERO)) {
    writer.endElement(HtmlElements.DIV);
    }
    writer.endElement(HtmlElements.DIV);
  }
}
