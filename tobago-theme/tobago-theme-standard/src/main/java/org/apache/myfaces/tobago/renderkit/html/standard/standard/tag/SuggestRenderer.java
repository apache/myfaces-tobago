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
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.MethodExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SuggestRenderer extends RendererBase {

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {

    final UISuggest suggest = (UISuggest) component;
    final UIIn in = (UIIn) suggest.getParent();
    final MethodExpression suggestMethodExpression = suggest.getSuggestMethodExpression();
    final AutoSuggestItems items
        = createAutoSuggestItems(suggestMethodExpression.invoke(facesContext.getELContext(), new Object[]{in}));
    int totalCount = suggest.getTotalCount();
    final List<AutoSuggestItem> list = items.getItems();
    if (totalCount == -1) {
      totalCount = list.size();
    }

    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_MIN_CHARS.getValue()), suggest.getMinimumCharacters());
    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_DELAY.getValue()), suggest.getDelay());
    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_MAX_ITEMS.getValue()), suggest.getMaximumItems());
    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_UPDATE.getValue()), suggest.isUpdate());
    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_TOTAL_COUNT.getValue()), totalCount);

// tbd    final String title
// tbd       = ResourceManagerUtils.getPropertyNotNull(facesContext, "tobago", "tobago.in.inputSuggest.moreElements");

    String array[] = new String[list.size()];
    for (int i = 0; i < totalCount; i++) {
      array[i] = list.get(i).getLabel();
    }

    ComponentUtils.putDataAttribute(in, cut(DataAttributes.SUGGEST_DATA.getValue()), JsonUtils.encode(array));

    super.prepareRender(facesContext, component);
  }

  /// todo: make DataAttribute to a enum and add this util to the enum class
  private String cut(String name) {
    if (name.startsWith("data-")) {
      return name.substring(5);
    } else {
      return name;
    }
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
