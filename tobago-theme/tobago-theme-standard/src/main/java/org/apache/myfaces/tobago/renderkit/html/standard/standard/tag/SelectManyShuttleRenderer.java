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

import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.SelectManyRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;

public class SelectManyShuttleRenderer extends SelectManyRendererBase {

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final UISelectManyShuttle select = (UISelectManyShuttle) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlElements.DIV, select);
    final Style style = new Style(facesContext, select);
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select));
    final String clientId = select.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, select);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, select);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    final boolean hasLabel = select.hasLabel();
    // TODO get buttonWidth and label Height from theme
    final Measure buttonWidth = Measure.valueOf(50);

    final Measure labelHeight = Measure.valueOf(18);
    style.setTop(Measure.valueOf(0));
    style.setLeft(Measure.valueOf(0));
    final Measure width = style.getWidth();
    final Measure selectWidth = width.subtract(buttonWidth).divide(2);
    style.setWidth(selectWidth);
    Style labelStyle = null;
    if (hasLabel) {
      labelStyle = new Style(style);
      labelStyle.setHeight(labelHeight);
      style.setHeight(style.getHeight().subtract(labelHeight));
      style.setTop(style.getTop().add(labelHeight));
    }
    Iterable<SelectItem> items = SelectItemUtils.getItemIterator(facesContext, select);
    final boolean disabled = !items.iterator().hasNext() || select.isDisabled() || select.isReadonly();

    final String unselectedLabel = select.getUnselectedLabel();
    if (unselectedLabel != null) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeStyleAttribute(labelStyle);
      writer.writeClassAttribute(Classes.create(select, "unselectedLabel"));
      writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
      writer.write(unselectedLabel);
      writer.endElement(HtmlElements.DIV);
    }
    writer.startElement(HtmlElements.SELECT, null);
    final String unselectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "unselected";
    writer.writeIdAttribute(unselectedClientId);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);

    // TODO tabIndex
    final Integer tabIndex = select.getTabIndex();
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }

    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select, "unselected"));

    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);

    final Object[] values = select.getSelectedValues();
    HtmlRendererUtils.renderSelectItems(select, items, values, false, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.DIV, null);
    style.setLeft(style.getLeft().add(selectWidth));
    style.setWidth(buttonWidth);
    writer.writeStyleAttribute(style);
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(select, "toolBar"));
    createButton(facesContext, component, writer, disabled, "image/selectManyShuttleAddAll", "addAll");
    createButton(facesContext, component, writer, disabled, "image/selectManyShuttleAdd", "add");
    createButton(facesContext, component, writer, disabled, "image/selectManyShuttleRemove", "remove");
    createButton(facesContext, component, writer, disabled, "image/selectManyShuttleRemoveAll", "removeAll");
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
    final String selectedLabel = select.getSelectedLabel();
    if (selectedLabel != null) {
      writer.startElement(HtmlElements.DIV, null);
      labelStyle.setLeft(labelStyle.getLeft().add(selectWidth).add(buttonWidth));
      writer.writeStyleAttribute(labelStyle);
      writer.writeClassAttribute(Classes.create(select, "selectedLabel"));
      writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
      writer.write(selectedLabel);
      writer.endElement(HtmlElements.DIV);
    }

    writer.startElement(HtmlElements.SELECT, select);
    final String selectedClientId = clientId + ComponentUtils.SUB_SEPARATOR + "selected";
    writer.writeIdAttribute(selectedClientId);

    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    style.setWidth(selectWidth);
    style.setLeft(style.getLeft().add(buttonWidth));
    writer.writeStyleAttribute(style);
    writer.writeClassAttribute(Classes.create(select, "selected"));
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    items = SelectItemUtils.getItemIterator(facesContext, select);
    HtmlRendererUtils.renderSelectItems(select, items, values, true, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);
    writer.startElement(HtmlElements.SELECT, select);
    writer.writeClassAttribute(Classes.create(component, "hidden"));
    final String hiddenClientId = clientId + ComponentUtils.SUB_SEPARATOR + "hidden";
    writer.writeIdAttribute(hiddenClientId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.MULTIPLE, HtmlAttributes.MULTIPLE, false);
    writer.writeAttribute(HtmlAttributes.REQUIRED, select.isRequired());
    HtmlRendererUtils.renderCommandFacet(select, facesContext, writer);
    items = SelectItemUtils.getItemIterator(facesContext, select);
    HtmlRendererUtils.renderSelectItems(select, items, values, writer, facesContext);

    writer.endElement(HtmlElements.SELECT);

    writer.endElement(HtmlElements.DIV);
  }

  private void createButton(final FacesContext context, final UIComponent component, final TobagoResponseWriter writer,
        final boolean disabled, final String image, final String sub) throws IOException {
    writer.startElement(HtmlElements.BUTTON, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON, false);
    writer.writeClassAttribute(Classes.create(component, sub));
    writer.writeIdAttribute(component.getClientId(context) + ComponentUtils.SUB_SEPARATOR + sub);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    final String imagePath = ResourceManagerUtils.getImageOrDisabledImage(context, image, disabled);
    writer.startElement(HtmlElements.IMG, null);
    writer.writeAttribute(HtmlAttributes.SRC, imagePath, true);
    writer.endElement(HtmlElements.IMG);
    writer.endElement(HtmlElements.BUTTON);
  }
}
