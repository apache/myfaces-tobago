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

import org.apache.myfaces.tobago.apt.annotation.Preliminary;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

/**
 * <p>
 * WARNING: This component is preliminary and may be changed without a major release.
 * </p>
 */
@Preliminary
public class SplitLayoutRenderer<T extends AbstractUISplitLayout> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String SUFFIX_SIZES = ComponentUtils.SUB_SEPARATOR + "sizes";

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("jakarta.faces.source");
    final String clientId = component.getClientId() + SUFFIX_SIZES;
    if (clientId.equals(sourceId)) {
      // only decode and update layout at resize request
//      final Map<String, String> parameterMap = facesContext.getExternalContext().getRequestParameterMap();
//      final String position = parameterMap.get(clientId + SUFFIX_SIZES);
      LOG.warn("todo update layout");
      //      ((AbstractUISplitLayout) component).updateLayout(Integer.parseInt(position));
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = component.getMarkup();

    writer.startElement(HtmlElements.TOBAGO_SPLIT_LAYOUT);
    writer.writeIdAttribute(component.getClientId(facesContext));
    writer.writeClassAttribute(
        component.isHorizontal() ? BootstrapClass.FLEX_ROW : BootstrapClass.FLEX_COLUMN,
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
    writer.writeAttribute(CustomAttributes.ORIENTATION,
        component.isHorizontal() ? Orientation.HORIZONTAL : Orientation.VERTICAL, false);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.INPUT);
    writer.writeNameAttribute(component.getClientId(facesContext) + SUFFIX_SIZES);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
//    writer.writeAttribute(HtmlAttributes.VALUE, sizes);
    writer.endElement(HtmlElements.INPUT);

    writer.endElement(HtmlElements.TOBAGO_SPLIT_LAYOUT);
  }
}
