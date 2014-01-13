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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UIFlowLayout;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.MarginValues;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.SpacingValues;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class FlowLayoutRenderer extends RendererBase implements SpacingValues, MarginValues {

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final UIFlowLayout layout = (UIFlowLayout) component;
    writer.startElement(HtmlElements.DIV, layout);
    writer.writeClassAttribute(Classes.create(layout));
    final Style style = new Style();
    style.setMarginLeft(getMarginLeft(facesContext, layout));
    style.setMarginRight(getMarginRight(facesContext, layout));
    style.setMarginTop(getMarginTop(facesContext, layout));
    style.setMarginBottom(getMarginBottom(facesContext, layout));
    style.setTextAlign(layout.getTextAlign());
    writer.writeStyleAttribute(style);
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UIComponent container = component.getParent();
    if (container instanceof LayoutContainer && !((LayoutContainer) container).isLayoutChildren()) {
      return;
    }
    RenderUtils.encodeChildren(facesContext, container);
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  public Measure getColumnSpacing(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.COLUMN_SPACING);
  }

  public Measure getRowSpacing(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.ROW_SPACING);
  }

  public Measure getMarginLeft(final FacesContext facesContext, final Configurable component) {
    final Configurable parent = (Configurable) ((UIComponent) component).getParent();
    return getResourceManager().getThemeMeasure(facesContext, parent, Attributes.MARGIN_LEFT);
  }

  public Measure getMarginRight(final FacesContext facesContext, final Configurable component) {
    final Configurable parent = (Configurable) ((UIComponent) component).getParent();
    return getResourceManager().getThemeMeasure(facesContext, parent, Attributes.MARGIN_RIGHT);
  }

  public Measure getMarginTop(final FacesContext facesContext, final Configurable component) {
    final Configurable parent = (Configurable) ((UIComponent) component).getParent();
    return getResourceManager().getThemeMeasure(facesContext, parent, Attributes.MARGIN_TOP);
  }

  public Measure getMarginBottom(final FacesContext facesContext, final Configurable component) {
    final Configurable parent = (Configurable) ((UIComponent) component).getParent();
    return getResourceManager().getThemeMeasure(facesContext, parent, Attributes.MARGIN_BOTTOM);
  }

}
