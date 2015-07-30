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

import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UISuggest;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestRenderer extends InputRendererBase {

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UISuggest suggest = (UISuggest) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final String id  = suggest.getClientId(facesContext);
    final UIIn in = (UIIn) suggest.getParent();
    final MethodExpression suggestMethodExpression = suggest.getSuggestMethodExpression();
    final AutoSuggestItems items
        = createAutoSuggestItems(suggestMethodExpression.invoke(facesContext.getELContext(), new Object[]{in}));
    // todo: declare unused/unsupported stuff deprecated

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(suggest));
    writer.writeIdAttribute(id);
    writer.writeAttribute(DataAttributes.FOR, in.getClientId(facesContext), false);
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
    writer.startElement(HtmlElements.LI, null);
    writer.startElement(HtmlElements.A, null);
    writer.writeAttribute(HtmlAttributes.HREF, "#", false);
    writer.writeAttribute(HtmlAttributes.TABINDEX, -1);
    writer.endElement(HtmlElements.A);

    writer.startElement(HtmlElements.OL, null);
    for (final AutoSuggestItem item : items.getItems()) {
      writer.startElement(HtmlElements.LI, null);
      writer.startElement(HtmlElements.A, null);
      writer.writeAttribute(HtmlAttributes.HREF, "#", false);
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

  private AutoSuggestItems createAutoSuggestItems(final Object object) {
    if (object instanceof AutoSuggestItems) {
      return (AutoSuggestItems) object;
    }
    final AutoSuggestItems autoSuggestItems = new AutoSuggestItems();
    if (object instanceof List && !((List) object).isEmpty()) {
      if (((List) object).get(0) instanceof AutoSuggestItem) {
        //noinspection unchecked
        autoSuggestItems.setItems((List<AutoSuggestItem>) object);
      } else if (((List) object).get(0) instanceof String) {
        final List<AutoSuggestItem> items = new ArrayList<AutoSuggestItem>(((List) object).size());
        for (int i = 0; i < ((List) object).size(); i++) {
          final AutoSuggestItem item = new AutoSuggestItem();
          item.setLabel((String) ((List) object).get(i));
          item.setValue((String) ((List) object).get(i));
          items.add(item);
        }
        autoSuggestItems.setItems(items);
      } else {
        throw new ClassCastException("Can't create AutoSuggestItems from '" + object + "'. "
            + "Elements needs to be " + String.class.getName() + " or " + AutoSuggestItem.class.getName());
      }
    } else {
      autoSuggestItems.setItems(Collections.<AutoSuggestItem>emptyList());
    }
    return autoSuggestItems;
  }

}
