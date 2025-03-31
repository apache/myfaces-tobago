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

import org.apache.myfaces.tobago.component.SupportFieldId;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractUISelectOneList extends AbstractUISelectOneBase implements SupportFieldId {

  private transient Optional<UIComponent> abstractUIFilterOptional = null; //set "null" to detect if initialized
  private transient boolean filterUpdate;
  private transient List<SelectItem> itemList = null;

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "field";
  }

  public abstract String getFilter();

  public abstract boolean isExpanded();

  public abstract boolean isLocalMenu();

  public abstract String getFooter();

  public AbstractUIFilter getAbstractUIFilter() {
    if (abstractUIFilterOptional == null) {
      abstractUIFilterOptional = getChildren().stream()
          .filter(AbstractUIFilter.class::isInstance)
          .findFirst();
    }
    return (AbstractUIFilter) abstractUIFilterOptional.orElse(null);
  }

  public boolean isFilterUpdate() {
    return filterUpdate;
  }

  public void setFilterUpdate(boolean filterUpdate) {
    this.filterUpdate = filterUpdate;
  }

  /**
   * Selected elements must always be available. The server-side filtering reduces the available items and may filter
   * out selected items. For this reason, a UISelectItems component is added, which must contain all selected items that
   * are not available/filtered out.
   */
  public List<?> getMissingSelectedValues(FacesContext facesContext) {
    final String missingSelected = "missingSelected";

    Optional<UIComponent> missingSelectedSelectItemsOptional = getChildren().stream()
        .filter(child -> child.getAttributes().get(missingSelected) != null).findFirst();
    if (missingSelectedSelectItemsOptional.isPresent()) {
      return (List<?>) ((UISelectItems) missingSelectedSelectItemsOptional.get()).getValue();
    } else {
      UISelectItems firstSelectItems =
          (UISelectItems) getAbstractUIFilter().getChildren().stream()
              .filter(child -> child instanceof UISelectItems)
              .findFirst().orElse(getChildren().stream()
                  .filter(child -> child instanceof UISelectItems)
                  .findFirst().orElse(null));

      if (firstSelectItems != null) {
        final UISelectItems selectedItems = (UISelectItems) ComponentUtils.createComponent(
            facesContext, Tags.selectItems.componentType(), null, null);
        selectedItems.getAttributes().put(missingSelected, true);

        copyAttribute(firstSelectItems, selectedItems, "binding");
        copyAttribute(firstSelectItems, selectedItems, "itemDisabled");
        copyAttribute(firstSelectItems, selectedItems, "itemImage");
        copyAttribute(firstSelectItems, selectedItems, "itemLabel");
        copyAttribute(firstSelectItems, selectedItems, "itemValue");
        copyAttribute(firstSelectItems, selectedItems, "tip");
        copyAttribute(firstSelectItems, selectedItems, "var");
        getChildren().add(0, selectedItems);

        selectedItems.setValue(new ArrayList<>()); //list must be mutable
        return (List<?>) selectedItems.getValue();
      }
    }
    return null;
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

  public void updateMissingSelectedItems(FacesContext facesContext) {
    List<Object> missingItems = (List<Object>) getMissingSelectedValues(facesContext);
    final Object selectedValue = getValue();
    missingItems.clear();
    if (selectedValue != null) {
      missingItems.add(selectedValue);
    }
  }

  public List<SelectItem> getItemList(FacesContext facesContext) {
    final AbstractUIFilter abstractUIFilter = getAbstractUIFilter();
    if (abstractUIFilter != null) {
      if (itemList == null) {
        moveFilterSelectItemsToParent(abstractUIFilter);
        updateMissingSelectedItems(facesContext);

        itemList = SelectItemUtils.getItemList(facesContext, this);

        List<?> missingSelectedValues = getMissingSelectedValues(facesContext);
        List<SelectItem> removeSelectItems = new ArrayList<>();
        for (SelectItem selectItem : itemList.subList(missingSelectedValues.size(), itemList.size())) {
          if (missingSelectedValues.contains(selectItem.getValue())) {
            removeSelectItems.add(selectItem);
          }
        }
        for (SelectItem selectItem : removeSelectItems) {
          missingSelectedValues.remove(selectItem.getValue());
          itemList.remove(selectItem);
        }
      }

      return itemList;
    } else {
      return SelectItemUtils.getItemList(facesContext, this);
    }
  }

  private void moveFilterSelectItemsToParent(final AbstractUIFilter abstractUIFilter) {
    while (abstractUIFilter.getChildren().stream()
        .anyMatch(child -> child instanceof UISelectItems || child instanceof UISelectItem)) {
      UIComponent uiComponent = abstractUIFilter.getChildren().stream()
          .filter(child -> child instanceof UISelectItems || child instanceof UISelectItem)
          .findFirst().orElse(null);
      if (uiComponent != null) {
        getChildren().add(uiComponent);
      } else {
        break;
      }
    }
  }
}
