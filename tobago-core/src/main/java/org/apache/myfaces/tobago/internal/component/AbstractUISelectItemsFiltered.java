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

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.internal.taglib.component.SelectItemsFilteredTagDeclaration;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link SelectItemsFilteredTagDeclaration}
 */
public abstract class AbstractUISelectItemsFiltered extends UISelectItems {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private transient String query;
  private transient List<SelectItem> itemList = null;

  public String getQuery() {
    final ValueExpression expression = this.getValueExpression("query");
    if (expression != null) {
      try {
        return (String) expression.getValue(FacesContext.getCurrentInstance().getELContext());
      } catch (final Exception e) {
        LOG.error("", e);
        return null;
      }
    } else {
      return query;
    }
  }

  public void setQuery(final String query) {
    final ValueExpression expression = this.getValueExpression("query");
    if (expression != null) {
      try {
        expression.setValue(FacesContext.getCurrentInstance().getELContext(), query);
      } catch (final Exception e) {
        LOG.error("query='{}'", query, e);
      }
    } else {
      this.query = query;
    }
  }

  public abstract Integer getDelay();

  public abstract Integer getMinimumCharacters();

  /**
   * There must be at least two UISelectItems components in the SelectItemsFiltered parent.
   * <ul>
   *   <li>The deferred SelectItems component. It contains all selectItems, which are not available in the filtered
   *   list.</li>
   *   <li>The filtered SelectItems component. It contains selectItems that can be filtered using the "query"
   *   attribute.</li>
   * </ul>
   * <p>
   * All selected elements must always be available.
   * </p>
   * <p>
   * This method creates the deferred SelectItems component if not already added to the parent.
   * </p>
   *
   * @param parent must be the parent from the SelectItemsFiltered component
   * @return deferred SelectItems values
   */
  private List<?> getDeferredSelectedValues(FacesContext facesContext, UIComponent parent) {
    final String deferredSelectedKey = "deferredSelectedClientId";
    String clientId = (String) getAttributes().get(deferredSelectedKey);
    UISelectItems selectItems = clientId != null ? (UISelectItems) parent.findComponent(clientId) : null;

    if (selectItems != null) {
      return (List<?>) selectItems.getValue();
    } else {
      final UISelectItems selectedItems = (UISelectItems) ComponentUtils.createComponent(
          facesContext, Tags.selectItems.componentType(), null, null);
      getAttributes().put(deferredSelectedKey, selectedItems.getClientId(facesContext));

      copyAttribute(this, selectedItems, "binding");
      copyAttribute(this, selectedItems, "itemDisabled");
      copyAttribute(this, selectedItems, "itemLabel");
      copyAttribute(this, selectedItems, "itemValue");
      copyAttribute(this, selectedItems, "var");
      parent.getChildren().add(0, selectedItems);

      selectedItems.setValue(new ArrayList<>()); //list must be mutable
      return (List<?>) selectedItems.getValue();
    }
  }

  private void copyAttribute(UISelectItems from, UISelectItems to, String name) {
    ValueExpression fromValueExpression = from.getValueExpression(name);
    if (fromValueExpression != null) {
      to.setValueExpression(name, fromValueExpression);
    } else {
      Object value = from.getAttributes().get(name);
      if (value != null) {
        to.getAttributes().put(name, value);
      }
    }
  }

  public void updateDeferredSelectedItems(FacesContext facesContext, UIComponent parent, final Object selectedValue) {
    List<Object> deferredSelectedValues = (List<Object>) getDeferredSelectedValues(facesContext, parent);
    deferredSelectedValues.clear();
    if (selectedValue != null) {
      deferredSelectedValues.add(selectedValue);
    }
  }

  public void updateDeferredSelectedItems(
      FacesContext facesContext, UIComponent parent, final Object[] selectedValues) {
    List<Object> deferredSelectedValues = (List<Object>) getDeferredSelectedValues(facesContext, parent);
    deferredSelectedValues.clear();
    if (selectedValues != null) {
      Collections.addAll(deferredSelectedValues, selectedValues);
    }
  }

  /**
   * There must be at least two UISelectItems components in the SelectItemsFiltered parent.
   * <ul>
   *   <li>The deferred SelectItems component. It contains all selectItems, which are not available in the filtered
   *   list.</li>
   *   <li>The filtered SelectItems component. It contains selectItems that can be filtered using the "query"
   *   attribute.</li>
   * </ul>
   * <p>
   * All selected elements must always be available.
   * </p>
   *
   * @param parent must be the parent from the SelectItemsFiltered component
   * @return both deferred and filtered SelectItems
   */
  public List<SelectItem> getItemList(FacesContext facesContext, UIComponent parent) {
    if (itemList == null) {
      itemList = SelectItemUtils.getItemList(facesContext, parent);

      List<?> deferredSelectedValues = getDeferredSelectedValues(facesContext, parent);
      List<SelectItem> duplicateSelectItems = new ArrayList<>(); //remove duplicates
      for (SelectItem selectItem : itemList.subList(deferredSelectedValues.size(), itemList.size())) {
        if (deferredSelectedValues.contains(selectItem.getValue())) {
          duplicateSelectItems.add(selectItem);
        }
      }
      for (SelectItem duplicateSelectItem : duplicateSelectItems) {
        deferredSelectedValues.remove(duplicateSelectItem.getValue());

        for (SelectItem selectItem : itemList) {
          if (selectItem.getValue().equals(duplicateSelectItem.getValue())) {
            itemList.remove(selectItem); //remove first one from deferredSelectedItem list to keep results in order
            break;
          }
        }
      }
    }

    return itemList;
  }

  /**
   * There must be at least two UISelectItems components in the SelectItemsFiltered parent.
   * <ul>
   *   <li>The deferred SelectItems component. It contains all selectItems, which are not available in the filtered
   *   list.</li>
   *   <li>The filtered SelectItems component. It contains selectItems that can be filtered using the "query"
   *   attribute.</li>
   * </ul>
   * <p>
   * All selected elements must always be available.
   * </p>
   *
   * @param parent must be the parent from the SelectItemsFiltered component
   * @return filtered SelectItems
   */
  public List<SelectItem> getFilteredItemList(FacesContext facesContext, UIComponent parent) {
    final List<?> deferredSelectedValues = getDeferredSelectedValues(facesContext, parent);
    final List<SelectItem> allSelectItems = getItemList(facesContext, parent);
    return allSelectItems.subList(deferredSelectedValues.size(), allSelectItems.size());
  }
}
