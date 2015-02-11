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
import org.apache.myfaces.tobago.component.SupportsCss;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.renderkit.BoxRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Css;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {

    private static final Logger LOG = LoggerFactory.getLogger(BoxRenderer.class);

    @Override
    public void prepareRender(
            final FacesContext facesContext, final UIComponent component) throws IOException {
        super.prepareRender(facesContext, component);

        final Css css = ((SupportsCss) component).getCurrentCss();
        css.add("panel");
        css.add("panel-default");
    }

    @Override
    public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

        final UIBox box = (UIBox) component;
        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

        final UIComponent label = box.getFacet(Facets.LABEL);
        final String labelString = box.getLabel();

        final UIPanel toolbar = (UIPanel) box.getFacet(Facets.TOOL_BAR); //XXX todo
        if (toolbar != null) {
            LOG.error("XXX toolbar for bootstrap not yet implemented");
        }

        writer.startElement(HtmlElements.DIV, box);
        writer.writeClassAttribute(Classes.create(box));
        writer.writeIdAttribute(box.getClientId(facesContext));
        final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, box);
        if (title != null) {
            writer.writeAttribute(HtmlAttributes.TITLE, title, true);
        }
        HtmlRendererUtils.writeDataAttributes(facesContext, writer, box);

        if (label != null || labelString != null || toolbar != null) {
            writer.startElement(HtmlElements.DIV, box);
            writer.writeClassAttribute(BootstrapClass.PANEL_HEADING);

            if (label != null) {
                RenderUtils.encode(facesContext, label);
            } else {
                writer.writeText(labelString);
            }

            writer.endElement(HtmlElements.DIV);
        }

// XXX todo
        final UIMenuBar menuBar = ComponentUtils.findFacetDescendant(box, Facets.MENUBAR, UIMenuBar.class);
        if (menuBar != null) {
            LOG.error("XXX menuBar for bootstrap not yet implemented");
//            RenderUtils.encode(facesContext, menuBar);
        }

        writer.startElement(HtmlElements.DIV, box);
        writer.writeClassAttribute(BootstrapClass.PANEL_BODY);
    }

    @Override
    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
        final ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HtmlElements.DIV);
        writer.endElement(HtmlElements.DIV);
    }

}
