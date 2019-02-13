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

import org.apache.myfaces.tobago.internal.component.AbstractUIMetaLink;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MetaLinkRenderer extends RendererBase {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIMetaLink metaLink = (AbstractUIMetaLink) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.LINK);
    writer.writeAttribute(HtmlAttributes.CHARSET, metaLink.getCharset(), true);
    writer.writeAttribute(HtmlAttributes.HREF, metaLink.getHref(), true);
    writer.writeAttribute(HtmlAttributes.HREFLANG, metaLink.getHreflang(), true);
    writer.writeAttribute(HtmlAttributes.TYPE, metaLink.getType(), true);
    writer.writeAttribute(HtmlAttributes.REL, metaLink.getRel(), true);
    writer.writeAttribute(HtmlAttributes.REV, metaLink.getRev(), true);
    writer.writeAttribute(HtmlAttributes.MEDIA, metaLink.getMedia(), true);
    writer.endElement(HtmlElements.LINK);
  }
}
