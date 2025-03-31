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
import org.apache.myfaces.tobago.component.SupportsAutoSpacing;
import org.apache.myfaces.tobago.component.SupportsFilter;
import org.apache.myfaces.tobago.component.SupportsHelp;
import org.apache.myfaces.tobago.component.SupportsLabelLayout;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.internal.taglib.component.SelectManyListTagDeclaration;
import org.apache.myfaces.tobago.internal.util.SelectItemUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * {@link SelectManyListTagDeclaration}
 */
public abstract class AbstractUISelectManyList extends AbstractUISelectManyBase
    implements SupportsAutoSpacing, Visual, SupportsLabelLayout, ClientBehaviorHolder, SupportsHelp, SupportFieldId,
    SupportsFilter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private transient boolean nextToRenderIsLabel;
  private transient Optional<UIComponent> abstractUIFilterOptional = null; //set "null" to detect if initialized
  private transient boolean filterUpdate;
  private transient List<SelectItem> itemList = null;

  @Override
  public Object[] getSelectedValues() {
    final Object value = getValue();
    if (value instanceof Collection) {
      return ((Collection) value).toArray();
    } else {
      return (Object[]) value;
    }
  }

  @Override
  public String getFieldId(final FacesContext facesContext) {
    return getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "field";
  }

  public abstract Integer getTabIndex();

  public abstract boolean isDisabled();

  public abstract boolean isExpanded();

  public boolean isError() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return !isValid() || !facesContext.getMessageList(getClientId(facesContext)).isEmpty();
  }

  public abstract boolean isFocus();

  public abstract String getFilter();

  public abstract boolean isLocalMenu();

  public abstract String getFooter();

  @Override
  public boolean isNextToRenderIsLabel() {
    return nextToRenderIsLabel;
  }

  @Override
  public void setNextToRenderIsLabel(final boolean nextToRenderIsLabel) {
    this.nextToRenderIsLabel = nextToRenderIsLabel;
  }

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
    Object[] selectedValues = getSelectedValues();
    missingItems.clear();
    if (selectedValues != null) {
      Collections.addAll(missingItems, selectedValues);
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
