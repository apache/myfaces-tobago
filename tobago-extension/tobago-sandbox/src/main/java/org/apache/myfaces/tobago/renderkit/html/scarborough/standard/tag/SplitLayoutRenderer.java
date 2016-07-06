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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;
import org.apache.myfaces.tobago.renderkit.html.standard.standard.tag.GridLayoutRenderer;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SplitLayoutRenderer extends GridLayoutRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(SplitLayoutRenderer.class);
  private static final String POSITION_ID_POSTFIX = "_spLP";

  // todo: put as enum to DataAttributes, when moved from sandbox to core
  private static final MarkupLanguageAttributes SPLIT_LAYOUT = DataAttributes.dynamic("tobago-split-layout");

  // todo: put as enum to DataAttributes, when moved from sandbox to core
  private static final MarkupLanguageAttributes SPLIT_LAYOUT_CONTAINMENT
      = DataAttributes.dynamic("data-tobago-split-layout-containment");

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final String sourceId = facesContext.getExternalContext().getRequestParameterMap().get("javax.faces.source");
    final String clientId = component.getClientId();
    if (clientId.equals(sourceId)) {
      // only decode and update layout at resize request
      final Map<String, String> parameterMap = facesContext.getExternalContext().getRequestParameterMap();
      final String position = parameterMap.get(clientId + POSITION_ID_POSTFIX);
      ((AbstractUISplitLayout) component).updateLayout(Integer.parseInt(position));
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIComponent parent = component.getParent();
    if (parent.isRendered()) {
      final List<UIComponent> components = LayoutUtils.findLayoutChildren(parent);
      if (components.size() != 2) {
        LOG.warn("Illegal component count in splitLayout: {}", components.size());
      }
      RenderUtils.encode(facesContext, components.get(0));
      RenderUtils.encode(facesContext, components.get(1));
      if (components.get(0).isRendered() && components.get(1).isRendered()) {
        // only when both components are rendered
        encodeHandle(facesContext, (AbstractUISplitLayout) component);
      }
    }
  }

  protected void encodeHandle(final FacesContext facesContext, final AbstractUISplitLayout layout) throws IOException {
    final String id = layout.getClientId(facesContext);

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.startElement(HtmlElements.SPAN);
    writer.writeIdAttribute(id);
    writer.writeAttribute(SPLIT_LAYOUT, layout.getOrientation().name(), false);
    writer.writeAttribute(SPLIT_LAYOUT_CONTAINMENT, createDraggableContainment(layout), true);
/* XXX todo not implemented currently
    final Style style = calculateHandleStyle(layout);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(layout, layout.getOrientation().name()));

    final int position;
    if (layout.getOrientation() == Orientation.horizontal) {
      position = style.getLeft().getPixel();
    } else {
      position = style.getTop().getPixel();
    }
*/
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(id + POSITION_ID_POSTFIX);
    writer.writeNameAttribute(id + POSITION_ID_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
//    writer.writeAttribute(HtmlAttributes.value, Integer.toString(position), false);
    writer.endElement(HtmlElements.INPUT);
    writer.endElement(HtmlElements.SPAN);
  }

  private String createDraggableContainment(final AbstractUISplitLayout layout) {
/* XXX todo not implemented currently
    final UIComponent parent = layout.getParent();
    final LayoutContainer container = (LayoutContainer) parent;
    final List<UIComponent> components = LayoutUtils.findLayoutChildren(parent);
    final Style firstComponent = ((Visual) components.get(0)).getStyle();
    final Style secondComponent = ((Visual) components.get(1)).getStyle();

    Measure minimum;

    if (layout.getOrientation() == Orientation.horizontal) {
      minimum = firstComponent.getMinWidth();
      final int minimumSize1 = minimum != null ? minimum.getPixel() : 0;
      minimum = secondComponent.getMinWidth();
      final int minimumSize2 = minimum != null ? minimum.getPixel() : 0;
      final int totalSize = container.getWidth().getPixel();
      return "[" + minimumSize1 + ", 0, " + (totalSize - minimumSize2) + ", 0]";
    } else {
      minimum = firstComponent.getMinHeight();
      final int minimumSize1 = minimum != null ? minimum.getPixel() : 0;
      minimum = secondComponent.getMinHeight();
      final int minimumSize2 = minimum != null ? minimum.getPixel() : 0;
      final int totalSize = container.getHeight().getPixel();
      return "[0, " + minimumSize1 + ", 0, " + (totalSize - minimumSize2) + "]";
    }
*/
    return "";
  }

  private Style calculateHandleStyle(final AbstractUISplitLayout layout) {
/* XXX todo not implemented currently
    final UIComponent parent = layout.getParent();
    final LayoutContainer container = (LayoutContainer) parent;
    final LayoutComponent secondComponent = (LayoutComponent) LayoutUtils.findLayoutChildren(parent).get(1);
    final Style style = new Style();

    if (layout.getOrientation() == Orientation.horizontal) {
      style.setWidth(Measure.valueOf(5));
      style.setHeight(container.getCurrentHeight());
      style.setLeft(Measure.valueOf(secondComponent.getLeft().subtract(5)));
      style.setTop(Measure.valueOf(0));
    } else {
      style.setWidth(container.getCurrentWidth());
      style.setHeight(Measure.valueOf(5));
      style.setLeft(Measure.valueOf(0));
      style.setTop(Measure.valueOf(Measure.valueOf(secondComponent.getTop().subtract(5))));
    }
    // todo: use CSS class
    style.setDisplay(Display.block);
    style.setPosition(Position.absolute);
    return style;
*/
    return null;
  }
}
