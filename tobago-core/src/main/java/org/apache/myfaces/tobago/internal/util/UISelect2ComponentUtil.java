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

package org.apache.myfaces.tobago.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBox;
import org.apache.myfaces.tobago.internal.component.AbstractUISuggest;
import org.apache.myfaces.tobago.internal.component.UISelect2Component;
import org.apache.myfaces.tobago.model.SelectItem;
import org.apache.myfaces.tobago.model.SubmittedItem;
import org.apache.myfaces.tobago.model.UICustomItemContainer;
import org.apache.myfaces.tobago.util.SelectItemUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UISelect2ComponentUtil {

  private static final Logger LOG = LoggerFactory.getLogger(UISelect2ComponentUtil.class);

  private static final String VALID_CUSTOMITEM_MAP
      = "org.apache.myfaces.tobago.internal.util.UISelect2ComponentUtil.valid_CustomItem_Map";

  private UISelect2ComponentUtil() {
  }

  public static Object ensureCustomValue(FacesContext facesContext, UISelect2Component component, Object value) {
    if (value != null && !"".equals(value)) {
      AbstractUISuggest suggest = component.getSuggest();
      if (component.isAllowCustom() || suggest != null) {
        ensureCustomValue(facesContext, component, value, suggest);
      }
    }
    return value;
  }

  public static Object ensureCustomValues(FacesContext facesContext, UISelect2Component component, Object values) {
    if (values != null) {
      AbstractUISuggest suggest = component.getSuggest();
      if (component.isAllowCustom() || suggest != null) {
        if (values instanceof Object[]) {
          for (Object value : (Object[]) values) {
            ensureCustomValue(facesContext, component, value, suggest);
          }
        } else if (values instanceof Collection) {
          for (Object value : (Collection) values) {
            ensureCustomValue(facesContext, component, value, suggest);
          }
        }
      }
    }
    return values;
  }

  public static void ensureCustomValue(
      FacesContext facesContext, UISelect2Component component, Object value, AbstractUISuggest suggest) {

    if (!isInItemList(facesContext, component, value)) {
      javax.faces.model.SelectItem item = null;
      if (suggest != null) {
        item = suggest.getSelectItem(facesContext, value, component.getConverter());
      }
      if (item == null && component.isAllowCustom()) {
        item = new SelectItem(value);
      }
      if (item != null) {
        LOG.trace("ADD item = \"{}\"", item.getValue());
        getValidCustomItemMap(component).add(item);
      }
    }
  }

  private static boolean isInItemList(FacesContext facesContext, UISelect2Component component, Object value) {
    LOG.trace("check for value = \"{}\"", value);
    Iterable<javax.faces.model.SelectItem> items
        = SelectItemUtils.getItemIterator(facesContext, (UIComponent) component);
    for (javax.faces.model.SelectItem item : items) {
      LOG.trace("check item value = \"{}\"", item.getValue());
      if (item.getValue() != null && item.getValue().equals(value)) {
        LOG.trace("TRUE");
        return true;
      }
    }
    LOG.trace("false");
    return false;
  }


  public static Set<javax.faces.model.SelectItem> getValidCustomItemMap(UISelect2Component component) {
    Object o = component.getComponentStateHelper().get(VALID_CUSTOMITEM_MAP);
    //noinspection unchecked
    HashSet<javax.faces.model.SelectItem> set = (HashSet<javax.faces.model.SelectItem>) o;
    if (set == null) {
      set = new HashSet<javax.faces.model.SelectItem>();
      component.getComponentStateHelper().put(VALID_CUSTOMITEM_MAP, set);
    }
    return set;
  }

  public static void ensureCustomItemsContainer(FacesContext facesContext, UISelect2Component component) {
    Set<javax.faces.model.SelectItem> validCustomItemMap = getValidCustomItemMap(component);
    boolean done = false;
    for (UIComponent child : ((UIComponent) component).getChildren()) {
      if (child instanceof UICustomItemContainer) {
        ((UICustomItemContainer) child).setValue(validCustomItemMap);
        done = true;
        break;
      }
    }
    if (!done) {
      ((UIComponent) component).getChildren().add(new UICustomItemContainer(validCustomItemMap));
    }

    if (component instanceof AbstractUISelectManyBox) {
      // remove duplicates
      Set<javax.faces.model.SelectItem> set = new HashSet<javax.faces.model.SelectItem>(validCustomItemMap);
      validCustomItemMap.clear();
      for (javax.faces.model.SelectItem selectItem : set) {
        LOG.trace("cleanup check = \"{}\"", selectItem.getValue());
        if (!isInItemList(facesContext, component, selectItem.getValue())) {
          LOG.trace("cleanup readd = \"{}\"", selectItem.getValue());
          validCustomItemMap.add(selectItem);
        }
      }
    }
  }

  public static Iterable<javax.faces.model.SelectItem> ensureSubmittedValues(FacesContext facesContext,
      UIInput component, Iterable<javax.faces.model.SelectItem> items, String[] submittedValues) {
    if (submittedValues == null || !(component instanceof UISelect2Component)) {
      return items;
    }
    Converter converter = component.getConverter();
    List<javax.faces.model.SelectItem> itemsToRender = new ArrayList<javax.faces.model.SelectItem>();
    Map<String, javax.faces.model.SelectItem> optionValues = new HashMap<String, javax.faces.model.SelectItem>();
    for (javax.faces.model.SelectItem item : items) {
      itemsToRender.add(item);
      Object itemValue = item.getValue();
      if (converter != null) {
        optionValues.put(converter.getAsString(facesContext, component, itemValue), item);
      } else {
        optionValues.put(itemValue != null ? itemValue.toString() : null, item);
      }
    }

    for (String submittedValue : submittedValues) {
      if (!optionValues.keySet().contains(submittedValue)) {
        itemsToRender.add(new SubmittedItem(submittedValue));
      }
    }

    return itemsToRender;
  }
}
