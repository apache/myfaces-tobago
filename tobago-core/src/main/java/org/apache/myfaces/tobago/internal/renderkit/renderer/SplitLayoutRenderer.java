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
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * WARNING: This component is preliminary and may be changed without a major release.
 * </p>
 */
@Preliminary
public class SplitLayoutRenderer extends RendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SplitLayoutRenderer.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId();
    if (clientId.equals(sourceId)) {
      // only decode and update layout at resize request
//      final Map<String, String> parameterMap = facesContext.getExternalContext().getRequestParameterMap();
//      final String position = parameterMap.get(clientId + POSITION_ID_POSTFIX);
      LOG.warn("todo update layout");
      //      ((AbstractUISplitLayout) component).updateLayout(Integer.parseInt(position));
    }
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUISplitLayout splitLayout = (AbstractUISplitLayout) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = splitLayout.getMarkup();

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(splitLayout.getClientId(facesContext));
    writer.writeAttribute(DataAttributes.MARKUP, JsonUtils.encode(splitLayout.getMarkup()), false);
    writer.writeClassAttribute(
        BootstrapClass.D_FLEX, // tbd: SPLIT_LAYOUT
        splitLayout.isHorizontal() ? BootstrapClass.FLEX_ROW : BootstrapClass.FLEX_COLUMN,
        markup != null && markup.contains(Markup.SPREAD) ? TobagoClass.SPREAD : null);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final AbstractUISplitLayout splitLayout = (AbstractUISplitLayout) component;
    final List<UIComponent> components = ComponentUtils.findLayoutChildren(splitLayout);
    if (components.size() != 2) {
      LOG.warn("Illegal component count in splitLayout: {}", components.size());
    }

    components.get(0).encodeAll(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(
        splitLayout.isHorizontal() ? TobagoClass.SPLIT_LAYOUT__HORIZONTAL : TobagoClass.SPLIT_LAYOUT__VERTICAL);
    writer.endElement(HtmlElements.DIV);

    components.get(1).encodeAll(facesContext);

    if (components.get(0).isRendered() && components.get(1).isRendered()) {
      // only when both components are rendered
      encodeHandle(facesContext, splitLayout);
    }
  }

  private void encodeHandle(final FacesContext facesContext, final AbstractUISplitLayout layout) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.SPAN);
/*
    writer.startElement(HtmlElements.INPUT);
    final String id = layout.getClientId(facesContext);
    writer.writeIdAttribute(id + POSITION_ID_POSTFIX);
    writer.writeNameAttribute(id + POSITION_ID_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
//    writer.writeAttribute(HtmlAttributes.value, Integer.toString(position), false);
    writer.endElement(HtmlElements.INPUT);
*/
    writer.endElement(HtmlElements.SPAN);
  }
}
