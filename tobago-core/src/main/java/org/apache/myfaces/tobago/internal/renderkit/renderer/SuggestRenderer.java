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

import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUISuggest;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SuggestRenderer<T extends AbstractUISuggest> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    final String clientId = component.getClientId(facesContext);
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(clientId)) {
      final String query = requestParameterMap.get(clientId);
      if (LOG.isDebugEnabled()) {
        LOG.debug("suggest query='{}'", query);
      }
      // XXX this is for the old way: for "suggestMethod"
      final AbstractUIInput input = ComponentUtils.findAncestor(component, AbstractUIInput.class);
      if (input != null) {
        input.setSubmittedValue(query);
      }
      // this is the new way: for select items
      component.setQuery(query);
    }
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final AbstractUIInput input = ComponentUtils.findAncestor(component, AbstractUIInput.class);

    int totalCount = component.getTotalCount();
    final String[] array;

    final List<SelectItem> items = SelectItemUtils.getItemList(facesContext, component);

    if (totalCount == -1 || items.size() < totalCount) {
      totalCount = items.size();
    }

    array = new String[totalCount];
    for (int i = 0; i < totalCount; i++) {
      array[i] = items.get(i).getLabel();
    }

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.TOBAGO_SUGGEST);
    final String clientId = component.getClientId(facesContext);
    writer.writeIdAttribute(clientId);
    if (input != null) {
      writer.writeAttribute(HtmlAttributes.FOR, input.getFieldId(facesContext), false);
    } else {
      LOG.error("No ancestor with type AbstractUIInput found for suggest id={}", clientId);
    }

    writer.writeAttribute(CustomAttributes.MIN_CHARS, component.getMinimumCharacters());
    writer.writeAttribute(CustomAttributes.DELAY, component.getDelay());
    writer.writeAttribute(CustomAttributes.MAX_ITEMS, component.getMaximumItems());
    writer.writeAttribute(CustomAttributes.UPDATE, component.isUpdate());
    writer.writeAttribute(CustomAttributes.TOTAL_COUNT, totalCount);
    writer.writeAttribute(CustomAttributes.LOCAL_MENU, component.isLocalMenu());
    writer.writeAttribute(CustomAttributes.FILTER, component.getFilter().getValue(), false);
    writer.writeAttribute(CustomAttributes.ITEMS, JsonUtils.encode(array), true);

    if (LOG.isDebugEnabled()) {
      LOG.debug("suggest list: {}", JsonUtils.encode(array));
    }

    writer.startElement(HtmlElements.INPUT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.NAME, clientId, false);
    writer.endElement(HtmlElements.INPUT);

    writer.endElement(HtmlElements.TOBAGO_SUGGEST);
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
        final List<AutoSuggestItem> items = new ArrayList<>(((List) object).size());
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
      autoSuggestItems.setItems(Collections.emptyList());
    }
    return autoSuggestItems;
  }

}
