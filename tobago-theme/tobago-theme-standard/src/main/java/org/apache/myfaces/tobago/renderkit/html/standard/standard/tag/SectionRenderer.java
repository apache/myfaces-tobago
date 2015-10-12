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
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SectionRenderer extends RendererBase {

    public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

        final UISection section = (UISection) component;
        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

        writer.startElement(HtmlElements.DIV);
        writer.writeIdAttribute(section.getClientId(facesContext));
        writer.writeClassAttribute(Classes.create(section), section.getCustomClass());
        HtmlRendererUtils.writeDataAttributes(facesContext, writer, section);

        String label = section.getLabelToRender();
        CssItem clazz = null;
        final HtmlElements tag;
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

        writer.startElement(tag);
        if (clazz != null) {
            writer.writeClassAttribute(clazz);
        }
        final String image = section.getImage();
        HtmlRendererUtils.encodeIconWithLabel(writer, image, label);
        writer.endElement(tag);
    }

    public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

        final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        writer.endElement(HtmlElements.DIV);
    }
}
