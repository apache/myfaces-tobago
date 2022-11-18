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
import org.apache.myfaces.tobago.internal.component.AbstractUIButtons;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class ButtonsRenderer<T extends AbstractUIButtons> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final Markup markup = component.getMarkup();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final boolean autoSpacing = component.getAutoSpacing(facesContext);

    writer.startElement(HtmlElements.TOBAGO_BUTTONS);
    writer.writeIdAttribute(component.getClientId(facesContext));

    writer.writeClassAttribute(
        Orientation.vertical.equals(component.getOrientation())
            ? BootstrapClass.BTN_GROUP_VERTICAL : BootstrapClass.BTN_GROUP,
        autoSpacing ? TobagoClass.AUTO__SPACING : null,
        component.getCustomClass());
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.GROUP.toString(), false);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    final String tip = component.getTip();
    if (tip != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
    }

    insideBegin(facesContext, HtmlElements.TOBAGO_BUTTONS);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    insideEnd(facesContext, HtmlElements.TOBAGO_BUTTONS);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_BUTTONS);
  }
}
