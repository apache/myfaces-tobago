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

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.component.AbstractUILinks;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class LinksRenderer<T extends AbstractUILinks> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.UL);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
        TobagoClass.LINKS,
        getExtraCssItem(),
        Orientation.vertical.equals(component.getOrientation()) ? BootstrapClass.FLEX_COLUMN : null,
        component.getCustomClass());
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildrenInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    for (final UIComponent child : component.getChildren()) {
      if (child.isRendered()) {
        if (child instanceof AbstractUILink) {
          child.setRendererType(RendererTypes.LinkInsideLinks.name());
          child.encodeAll(facesContext);
        } else {
          writer.startElement(HtmlElements.LI);
          writer.writeClassAttribute(BootstrapClass.NAV_ITEM);
          child.encodeAll(facesContext);
          writer.endElement(HtmlElements.LI);
        }
      }
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.UL);
  }

  protected CssItem getExtraCssItem() {
    return BootstrapClass.NAV;
  }
}
