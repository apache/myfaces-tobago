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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UISuggest;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class SuggestRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SuggestRenderer.class);

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UISuggest suggest = (UISuggest) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    // todo: declare unused/unsupported stuff deprecated

    if (suggest.getParent() instanceof UIIn) {
      writeInSuggestElements(facesContext, writer, suggest);
    } else if (suggest.isSelect2()) {
      writeSelect2SuggestElements(facesContext, writer, suggest);
    } else {
    }
  }

  public void writeInSuggestElements(FacesContext facesContext, TobagoResponseWriter writer, UISuggest suggest)
      throws IOException {
    final UIIn in = (UIIn) suggest.getParent();
    final String inClientId = in.getClientId(facesContext);

    final AutoSuggestItems items = suggest.getSuggestItems(facesContext);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(suggest));
    writer.writeIdAttribute(suggest.getClientId(facesContext));
    writer.writeAttribute(DataAttributes.FOR, inClientId, false);
    writer.writeAttribute(DataAttributes.SUGGEST_MIN_CHARS, suggest.getMinimumCharacters());
    writer.writeAttribute(DataAttributes.SUGGEST_DELAY, suggest.getDelay());
    writer.writeAttribute(DataAttributes.SUGGEST_MAX_ITEMS, suggest.getMaximumItems());
    writer.writeAttribute(DataAttributes.SUGGEST_UPDATE, Boolean.toString(suggest.isUpdate()), false);
    int totalCount = suggest.getTotalCount();
    if (totalCount == -1) {
      totalCount = items.getItems().size();
    }
    writer.writeAttribute(DataAttributes.SUGGEST_TOTAL_COUNT, totalCount);

    writer.startElement(HtmlElements.OL, null);
    writer.writeClassAttribute("tobago-menuBar");
    writer.startElement(HtmlElements.LI, null);
    writer.writeClassAttribute("tobago-menu tobago-menu-markup-top");
    writer.startElement(HtmlElements.A, null);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.endElement(HtmlElements.A);

    writer.startElement(HtmlElements.OL, null);
    for (final AutoSuggestItem item : items.getItems()) {
      writer.startElement(HtmlElements.LI, null);
      writer.startElement(HtmlElements.A, null);
      writer.writeAttribute(HtmlAttributes.HREF, "#", false);
      writer.writeAttribute(DataAttributes.SUGGEST_ITEM_FOR, inClientId, false);
      writer.writeText(item.getLabel());
      writer.endElement(HtmlElements.A);
      writer.endElement(HtmlElements.LI);
    }
    writer.startElement(HtmlElements.LI, null);
    writer.writeAttribute(HtmlAttributes.DISABLED, true);
    final String title
        = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "tobago.in.inputSuggest.moreElements");
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.startElement(HtmlElements.A, null);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeText("...");
    writer.endElement(HtmlElements.A);
    writer.endElement(HtmlElements.LI);

    writer.endElement(HtmlElements.OL);

    writer.endElement(HtmlElements.LI);
    writer.endElement(HtmlElements.OL);

    writer.endElement(HtmlElements.DIV);
  }

  public void writeSelect2SuggestElements(
      FacesContext facesContext, TobagoResponseWriter writer, UISuggest suggest)
      throws IOException {
    final UIComponent selectManyBox = (UIComponent) suggest.getParent();
    final String inClientId = selectManyBox.getClientId(facesContext);
    String id = suggest.getClientId(facesContext);

    final AutoSuggestItems items = suggest.getSuggestItems(facesContext);

    writer.startElement(HtmlElements.INPUT, null);
    writer.writeClassAttribute(Classes.create(suggest));
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    writer.writeAttribute(DataAttributes.FOR, inClientId, false);
    writer.writeAttribute(DataAttributes.SUGGEST_MIN_CHARS, suggest.getMinimumCharacters());
    writer.writeAttribute(DataAttributes.SUGGEST_DELAY, suggest.getDelay());
    writer.writeAttribute(DataAttributes.SUGGEST_MAX_ITEMS, suggest.getMaximumItems());
    writer.writeAttribute(DataAttributes.SUGGEST_UPDATE, Boolean.toString(suggest.isUpdate()), false);
    int totalCount = suggest.getTotalCount();
    if (totalCount == -1) {
      totalCount = items.getItems().size();
    }
    writer.writeAttribute(DataAttributes.SUGGEST_TOTAL_COUNT, totalCount);

    StringBuilder builder = new StringBuilder("{\"results\":[");
    for (final AutoSuggestItem item : items.getItems()) {
      builder.append("{");

      builder.append("\"id\":\"").append(encode(item.getValue())).append("\",");
      builder.append("\"text\":\"").append(encode(item.getLabel())).append("\"");

      builder.append("},");
    }
    if (builder.toString().endsWith(",")) {
      builder.setLength(builder.length() - 1);
    }
    builder.append("]}");
    writer.writeAttribute(DataAttributes.SUGGEST_RESPONSE_DATA, builder.toString(), true);

    writer.endElement(HtmlElements.INPUT);
  }

  private String encode(String value) {
    return value.replace("\\", "\\\\").replace("\"", "\\\"");
  }

}
