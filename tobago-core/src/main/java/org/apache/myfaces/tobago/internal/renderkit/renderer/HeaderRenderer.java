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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIHeader;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class HeaderRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUIHeader header = (AbstractUIHeader) component;
    final Markup markup = header.getMarkup();
    writer.startElement(HtmlElements.TOBAGO_HEADER);
    writer.writeIdAttribute(component.getClientId(facesContext));
    // TBD: NAVBAR_DARK and BG_INVERSE should not be the default
    // TBD: how to configure it when it is needed, with customClass, or with markup?

    writer.writeClassAttribute(
        TobagoClass.HEADER,
        TobagoClass.HEADER.createMarkup(markup),
        header.isFixed() ? BootstrapClass.STICKY_TOP : null,
        header.getCustomClass());
// TBD: should NAVBAR class be in the LinksRenderer?
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.BANNER.toString(), false);
    writer.writeAttribute(HtmlAttributes.TITLE, header.getTip(), true);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, header);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_HEADER);
  }
}
