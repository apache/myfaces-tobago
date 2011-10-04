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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIReload;
import org.apache.myfaces.tobago.internal.component.AbstractUIPanel;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
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
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    HtmlRendererUtils.renderDojoDndSource(facesContext, component);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
    UIPanel component = (UIPanel) uiComponent;
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      RenderUtils.encode(facesContext, child);
    }
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    
    // UIPanel or UICell (deprecated)
    AbstractUIPanel panel = (AbstractUIPanel) component;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    String clientId = panel.getClientId(facesContext);
    writer.startElement(HtmlElements.DIV, panel);
    HtmlRendererUtils.renderDojoDndItem(panel, writer, true);
    writer.writeIdAttribute(clientId);
    writer.writeClassAttribute(Classes.create(panel));
    Style style = new Style(facesContext, panel);
    // XXX hotfix for panels in sheets
    if (style.getPosition() == null) {
      style.setPosition(Position.RELATIVE);
    }
    writer.writeStyleAttribute(style);
    if (panel instanceof UIPanel && ((UIPanel) panel).getTip() != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, ((UIPanel) panel).getTip(), true);
    }

    HtmlRendererUtils.encodeContextMenu(facesContext, writer, panel);

    // TODO check ajax id
    if (FacesContextUtils.isAjax(facesContext)) {
      Integer frequency = null;
      UIComponent facetReload = panel.getFacet(Facets.RELOAD);
      if (facetReload != null && facetReload instanceof UIReload && facetReload.isRendered()) {
        UIReload update = (UIReload) facetReload;
        frequency = update.getFrequency();
      }
      if (frequency == null) {
        frequency = 0;
      }
      final String[] cmds = {
          "new Tobago.Panel(\"" + clientId + "\", " + true + ", " + frequency + ");"
      };
      HtmlRendererUtils.writeScriptLoader(facesContext, null, cmds);
    }
    HtmlRendererUtils.checkForCommandFacet(panel, facesContext, writer);

    final Measure borderLeft = panel.getBorderLeft();
    final Measure borderRight = panel.getBorderRight();
    final Measure borderTop = panel.getBorderTop();
    final Measure borderBottom = panel.getBorderBottom();

    if (borderLeft.greaterThan(Measure.ZERO) || borderRight.greaterThan(Measure.ZERO)
        || borderTop.greaterThan(Measure.ZERO) || borderBottom.greaterThan(Measure.ZERO)) {
      writer.startElement(HtmlElements.DIV, panel);
      writer.writeClassAttribute(Classes.create(panel, "content")); // needed to be scrollable inside of the panel
      final Style inner = new Style(facesContext, panel);
      // Todo: FIXME (be null may occur in sheets)
      if (inner.getWidth() != null) {
        inner.setWidth(inner.getWidth().subtract(borderLeft).subtract(borderRight));
      }
      // Todo: FIXME (be null may occur in sheets)
      if (inner.getHeight() != null) {
        inner.setHeight(inner.getHeight().subtract(borderTop).subtract(borderBottom));
      }
      inner.setLeft(borderLeft);
      inner.setTop(borderTop);
      writer.writeStyleAttribute(inner);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    AbstractUIPanel panel = (AbstractUIPanel) component;
    
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
