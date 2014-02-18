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
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.layout.BankHead;
import org.apache.myfaces.tobago.internal.layout.Cell;
import org.apache.myfaces.tobago.internal.layout.Grid;
import org.apache.myfaces.tobago.internal.layout.OriginCell;
import org.apache.myfaces.tobago.layout.AutoLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.apache.myfaces.tobago.renderkit.MarginValues;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.SpacingValues;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlRoleValues;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class GridLayoutRenderer extends RendererBase implements SpacingValues, MarginValues {

  private static final Logger LOG = LoggerFactory.getLogger(GridLayoutRenderer.class);

  // todo: may use: http://www.w3.org/TR/css3-grid-layout/ (currently only IE 10 and higher)

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIGridLayout gridLayout = (AbstractUIGridLayout) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

//    writer.startElement(HtmlElements.DIV, gridLayout);
    writer.startElement(HtmlElements.TABLE, gridLayout);
    writer.writeAttribute(HtmlAttributes.ROLE, HtmlRoleValues.PRESENTATION.toString(), false);
    writer.writeClassAttribute(Classes.create(gridLayout));

    final StringBuilder builder = new StringBuilder();

    builder.append("{\"columns\":");
    jsonLayout(gridLayout.getGrid().getBankHeads(Orientation.HORIZONTAL), builder);
    builder.append(",");

    builder.append("\"rows\":");
    jsonLayout(gridLayout.getGrid().getBankHeads(Orientation.VERTICAL), builder);
    builder.append("}");

    writer.writeAttribute("data-tobago-layout", builder.toString(), true);


    writer.startElement(HtmlElements.COLGROUP, gridLayout);
    final BankHead[] horizontalBankHeads = gridLayout.getGrid().getBankHeads(Orientation.HORIZONTAL);
    for (BankHead horizontalBankHead : horizontalBankHeads) {
      if (horizontalBankHead.isRendered()) {
        writer.startElement(HtmlElements.COL, gridLayout);
        writer.endElement(HtmlElements.COL);
      }
    }
    writer.endElement(HtmlElements.COLGROUP);
    writer.startElement(HtmlElements.TBODY, gridLayout);
  }

  private void jsonLayout(final BankHead[] bankHeads, final StringBuilder builder) {
    builder.append("[");
    for (int i  = 0; i < bankHeads.length; i++) {
      if (bankHeads[i].isRendered()) {
        final LayoutToken token = bankHeads[i].getToken();
        if (token instanceof RelativeLayoutToken) {
          final int factor = ((RelativeLayoutToken) token).getFactor();
          builder.append(factor);
        } else if (token instanceof AutoLayoutToken) {
          builder.append("\"auto\"");
        } else if (token instanceof PixelLayoutToken) {
          builder.append("{\"pixel\":");
          builder.append(((PixelLayoutToken) token).getPixel());
          builder.append("}");
        } else {
          LOG.warn("Not supported: " + token);
        }
        builder.append(',');
      }
    }
    if (builder.charAt(builder.length() - 1) == ',') {
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append("]");
  }

  @Override
  public void encodeChildren(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIGridLayout gridLayout = (AbstractUIGridLayout) component;
/*
    final UIComponent container = gridLayout.getParent();
    if (container instanceof LayoutContainer && !((LayoutContainer) container).isLayoutChildren()) {
      return;
    }
    RenderUtils.encodeChildren(facesContext, container);
*/

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final Grid grid = gridLayout.getGrid();
    final BankHead[] horizontalBankHeads = grid.getBankHeads(Orientation.HORIZONTAL);
    final BankHead[] verticalBankHeads = grid.getBankHeads(Orientation.VERTICAL);

    for (int i = 0; i < verticalBankHeads.length; i++) {
      if (verticalBankHeads[i].isRendered()) {
        writer.startElement(HtmlElements.TR, gridLayout);
        for (int j = 0; j < horizontalBankHeads.length; j++) {
          if (horizontalBankHeads[j].isRendered()) {
            final Cell cell = grid.getCell(j, i);
            if (cell instanceof OriginCell) {
              writer.startElement(HtmlElements.TD, gridLayout);
              final int columnSpan = cell.getColumnSpan();
              if (columnSpan > 1) {
                writer.writeAttribute(HtmlAttributes.COLSPAN, columnSpan);
              }
              final int rowSpan = cell.getRowSpan();
              if (rowSpan > 1) {
                writer.writeAttribute(HtmlAttributes.ROWSPAN, rowSpan);
              }


              final LayoutComponent element = cell.getComponent();
              StringBuilder builder = new StringBuilder();
              builder.append("{");
              final Measure width = element.getWidth();
              if (width != null) {
                builder.append("\"width\":");
                builder.append("{\"pixel\":");
                builder.append(width.getPixel());
                builder.append("}");
                builder.append(",");
              }
              final Measure height = element.getHeight();
              if (height != null) {
                builder.append("\"height\":");
                builder.append("{\"pixel\":");
                builder.append(height.getPixel());
                builder.append("}");
                builder.append(",");
              }
              if (builder.length() > 1) {
                builder.deleteCharAt(builder.length() - 1);
              }
              // todo: add more attributes
              builder.append("}");

              final UIComponent child = (UIComponent) element;
              if (builder.length() > 2) { // empty is not need to render
                ComponentUtils.putDataAttribute(child, "tobago-layout", builder.toString());
              }
              RenderUtils.encode(facesContext, child);

              writer.endElement(HtmlElements.TD);
            }
          }
        }
        writer.endElement(HtmlElements.TR);
      }
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.endElement(HtmlElements.TBODY);
    writer.endElement(HtmlElements.TABLE);
//    writer.endElement(HtmlElements.DIV);
  }

  public Measure getColumnSpacing(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.COLUMN_SPACING);
  }

  public Measure getRowSpacing(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.ROW_SPACING);
  }

  public Measure getMarginLeft(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_LEFT);
  }

  public Measure getMarginRight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_RIGHT);
  }

  public Measure getMarginTop(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_TOP);
  }

  public Measure getMarginBottom(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_BOTTOM);
  }
}
