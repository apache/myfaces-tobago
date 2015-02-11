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

import org.apache.myfaces.tobago.component.UISection;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SectionRenderer extends LayoutComponentRendererBase {

    public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

        final UISection section = (UISection) component;
        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

        writer.startElement(HtmlElements.DIV, component);
        writer.writeIdAttribute(section.getClientId(facesContext));
        writer.writeClassAttribute(Classes.create(component));
        HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

        String label = section.getLabelToRender();
        CssItem clazz = null;
        final String tag;
        switch (section.getLevel()) {
            case 1:
                tag = HtmlElements.H1;
                clazz = BootstrapClass.PAGE_HEADER;
                break;
            case 2:
                tag = HtmlElements.H2;
                break;
            case 3:
                tag = HtmlElements.H3;
                break;
            case 4:
                tag = HtmlElements.H4;
                break;
            case 5:
                tag = HtmlElements.H5;
                break;
            default:
                tag = HtmlElements.H6;
        }

        writer.startElement(tag, component);
        if (clazz != null) {
            writer.writeClassAttribute(clazz);
        }
        final String image = section.getImage();
        if (image != null && image.startsWith("glyphicon-")) { // XXX hack: should be integrated in the resource manager
            writer.startElement(HtmlElements.SPAN);
            writer.writeClassAttribute(BootstrapClass.GLYPHICON, BootstrapClass.glyphicon(image));
            writer.endElement(HtmlElements.SPAN);

        }
        if (image != null && label != null) {
            writer.writeText(" ");
        }
        if (image == null && label == null) { // needed, otherwise the look is broken (bootstrap 3.3.1)
            writer.writeText(HtmlUtils.CHAR_NON_BEAKING_SPACE);
        }
        if (label != null) {
            writer.writeText(label);
        }
        writer.endElement(tag);
    }

    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        writer.endElement(HtmlElements.DIV);
    }
}
