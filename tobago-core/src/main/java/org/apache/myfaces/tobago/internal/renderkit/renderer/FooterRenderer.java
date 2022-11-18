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

import org.apache.myfaces.tobago.internal.component.AbstractUIFooter;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class FooterRenderer<T extends AbstractUIFooter> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    insideBegin(facesContext, HtmlElements.TOBAGO_FOOTER);
    writer.startElement(HtmlElements.TOBAGO_FOOTER);
    writer.writeIdAttribute(component.getClientId(facesContext));

    writer.writeClassAttribute(
        component.isFixed() ? BootstrapClass.FIXED_BOTTOM : null,
        component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.CONTENTINFO.toString(), false);
    writer.writeAttribute(HtmlAttributes.TITLE, component.getTip(), true);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_FOOTER);
    insideEnd(facesContext, HtmlElements.TOBAGO_FOOTER);
  }
}
