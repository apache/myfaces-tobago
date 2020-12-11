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

package org.apache.myfaces.tobago.internal.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.component.InputSuggest2;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UISelectManyBox;
import org.apache.myfaces.tobago.component.UISelectOneChoice;
import org.apache.myfaces.tobago.model.AutoSuggestItem;
import org.apache.myfaces.tobago.model.AutoSuggestItems;
import org.apache.myfaces.tobago.model.SuggestFilter;

import javax.el.MethodExpression;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractUISuggest
    extends UIComponentBase implements SupportsMarkup, InputSuggest2 {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUISuggest.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Suggest";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Suggest";

  private static final String ITEM_MAP_KEY
      = "org.apache.myfaces.tobago.internal.component.AbstractUISuggest.ITEM_MAP_KEY";

  @Override
  public String getFamily() {
    return COMPONENT_FAMILY;
  }

  public AutoSuggestItems getSuggestItems(FacesContext facesContext) {
    final MethodExpression suggestMethodExpression = getSuggestMethodExpression();

    UIInput in;
    if (isSelect2()) {
      String search = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
          .get(getClientId());
      LOG.trace(" search = \"{}\"", search);
      in = new UIInput();
      in.setSubmittedValue(search);
    } else {
      in = (UIInput) getParent();
    }
    AutoSuggestItems suggestItems
        = createAutoSuggestItems(suggestMethodExpression.invoke(facesContext.getELContext(), new Object[]{in}));
    if (!suggestItems.getItems().isEmpty()) {
      //noinspection unchecked
      Map<String, AutoSuggestItem> itemMap = (Map<String, AutoSuggestItem>) getStateHelper().get(ITEM_MAP_KEY);
      if (itemMap == null) {
        itemMap = new HashMap<String, AutoSuggestItem>();
        getStateHelper().put(ITEM_MAP_KEY, itemMap);
      }
      for (AutoSuggestItem item : suggestItems.getItems()) {
        if (!itemMap.containsKey(item.getValue())) {
          itemMap.put(item.getValue(), item);
        }
      }
    }
    return suggestItems;
  }

  public boolean isSelect2() {
    return getParent() instanceof UISelectManyBox || getParent() instanceof UISelectOneChoice;
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

  public SelectItem getSelectItem(FacesContext facesContext, Object value, Converter converter) {
    //noinspection unchecked
    Map<String, AutoSuggestItem> itemMap = (Map<String, AutoSuggestItem>) getStateHelper().get(ITEM_MAP_KEY);
    if (itemMap != null) {
      String id = converter != null ? converter.getAsString(facesContext, getParent(), value) : (String) value;
      AutoSuggestItem autoSuggestItem = itemMap.get(id);
      if (autoSuggestItem != null) {
        return new SelectItem(value, autoSuggestItem.getLabel());
      }
    }
    return null;
  }

  public abstract void setDelay(Integer delay);

  public abstract void setMinimumCharacters(Integer minimumCharacters);

  public abstract void setFilter(SuggestFilter filter);

  public abstract Integer getMinimumCharacters();
}
