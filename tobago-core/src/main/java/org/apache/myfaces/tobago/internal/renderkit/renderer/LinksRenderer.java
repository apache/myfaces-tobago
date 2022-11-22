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

import org.apache.myfaces.tobago.internal.component.AbstractUILinks;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class LinksRenderer<T extends AbstractUILinks> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

//    final boolean insideBar = ComponentUtils.findAncestor(component, AbstractUIBar.class) != null;
//    final boolean insideBar = facesContext.getAttributes().get("inside-bar") != null;
    final boolean insideBar = isInside(facesContext, HtmlElements.TOBAGO_BAR);
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final boolean autoSpacing = component.getAutoSpacing(facesContext);

    writer.startElement(HtmlElements.TOBAGO_LINKS);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
        autoSpacing ? TobagoClass.AUTO__SPACING : null,
        component.getCustomClass());
    writer.startElement(HtmlElements.UL);
    writer.writeClassAttribute(
        insideBar ? BootstrapClass.NAVBAR_NAV : BootstrapClass.NAV,
        Orientation.vertical.equals(component.getOrientation()) ? BootstrapClass.FLEX_COLUMN : null);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildrenInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    insideBegin(facesContext, HtmlElements.TOBAGO_LINKS);
    for (final UIComponent child : component.getChildren()) {
      if (child.isRendered()) {
        writer.startElement(HtmlElements.LI);
        writer.writeClassAttribute(BootstrapClass.NAV_ITEM);
        child.encodeAll(facesContext);
        writer.endElement(HtmlElements.LI);
      }
    }
    insideEnd(facesContext, HtmlElements.TOBAGO_LINKS);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.UL);
    writer.endElement(HtmlElements.TOBAGO_LINKS);
  }
}
