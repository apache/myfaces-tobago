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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Position;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SplitLayoutRenderer extends GridLayoutRenderer {
  
  private static final Logger LOG = LoggerFactory.getLogger(SplitLayoutRenderer.class);
  private static final String POSITION_ID_POSTFIX = "_spLP";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    final String clientId = component.getClientId();
    if (clientId.equals(ComponentUtils.findPage(facesContext).getActionId())) {
      // only decode and update layout at resize request
      final Map<String, String> parameterMap = facesContext.getExternalContext().getRequestParameterMap();
      final String position = parameterMap.get(clientId + POSITION_ID_POSTFIX);
      ((AbstractUISplitLayout) component).updateLayout(Integer.parseInt(position));
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final LayoutContainer container = (LayoutContainer) ((AbstractUISplitLayout) component).getParent();
    if (!((LayoutContainer) container).isLayoutChildren()) {
      return;
    } else {
      final List<LayoutComponent> components = container.getComponents();
      if (components.size() != 2) {
        LOG.warn("Illegal component count in splitLayout: {}", components.size());
      }
      RenderUtils.encode(facesContext, (UIComponent) components.get(0));
      RenderUtils.encode(facesContext, (UIComponent) components.get(1));
      if (((UIComponent) components.get(0)).isRendered() && ((UIComponent) components.get(1)).isRendered()) {
        // only when both components are rendered
        encodeHandle(facesContext, (AbstractUISplitLayout) component);
      }
    }
  }

  protected void encodeHandle(final FacesContext facesContext, final AbstractUISplitLayout layout) throws IOException {
    final String id = layout.getClientId(facesContext);
    
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.SPAN, layout);
    writer.writeIdAttribute(id);
    writer.writeAttribute("data-tobago-split-layout", layout.getOrientation().toLowerCase(), true);
    writer.writeAttribute("data-tobago-split-layout-containment", createDraggableContainment(layout), true);
    final Style style = calculateHandleStyle(layout);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(layout, layout.getOrientation().toLowerCase()));

    final int position;
    if (AbstractUISplitLayout.HORIZONTAL.equals(layout.getOrientation())) {
      position = style.getLeft().getPixel();
    } else {
      position = style.getTop().getPixel();
    }
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeIdAttribute(id + POSITION_ID_POSTFIX);
    writer.writeNameAttribute(id + POSITION_ID_POSTFIX);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeAttribute(HtmlAttributes.VALUE, Integer.toString(position), false);
    writer.endElement(HtmlElements.INPUT);


    writer.endElement(HtmlElements.SPAN);

  }

  private String createDraggableContainment(final AbstractUISplitLayout layout) {
    final LayoutContainer container = (LayoutContainer) ((AbstractUISplitLayout) layout).getParent();
    final LayoutComponent firstComponent = container.getComponents().get(0);
    final LayoutComponent secondComponent = container.getComponents().get(1);

    if (AbstractUISplitLayout.HORIZONTAL.equals(layout.getOrientation())) {
      final int minimumSize1 = firstComponent.getMinimumWidth().getPixel();
      final int minimumSize2 = secondComponent.getMinimumWidth().getPixel();
      final int totalSize = container.getCurrentWidth().getPixel();
      return new StringBuilder("[").append(minimumSize1).append(", 0, ").append(totalSize-minimumSize2).append(", 0]")
          .toString();
    } else {
      final int minimumSize1 = firstComponent.getMinimumHeight().getPixel();
      final int minimumSize2 = secondComponent.getMinimumHeight().getPixel();
      final int totalSize = container.getCurrentHeight().getPixel();
      return new StringBuilder("[0, ").append(minimumSize1).append(", 0, ").append(totalSize-minimumSize2).append("]")
          .toString();
    }
  }

  private Style calculateHandleStyle(final AbstractUISplitLayout layout) {
    final LayoutContainer container = (LayoutContainer) ((AbstractUISplitLayout) layout).getParent();
    final LayoutComponent secondComponent = container.getComponents().get(1);
    final Style style = new Style();
    if (AbstractUISplitLayout.HORIZONTAL.equals(layout.getOrientation())) {
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
    style.setDisplay(Display.BLOCK);
    style.setPosition(Position.ABSOLUTE);
    return style;
  }
}
