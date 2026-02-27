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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UISelectItem;
import jakarta.faces.component.UISelectItems;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Based on code from MyFaces core.
 */
public class SelectItemUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * Creates a list of SelectItems to use for rendering.
   */
  public static Iterable<SelectItem> getItemIterator(final FacesContext facesContext, final UIComponent selector) {
    if (selector.getChildCount() == 0) {
      return Collections.emptyList();
    } else {
      return new Iterable<SelectItem>() {

        private SelectItemsIterator iterator;

        @Override
        public Iterator<SelectItem> iterator() {
          if (iterator == null) {
            iterator = new SelectItemsIterator(facesContext, selector);
          }
          return iterator;
        }
      };
    }
  }

  /**
   * Creates a list of SelectItems to use for rendering. You should only use this method (which returns a list), when
   * you need a list. Otherwise, please use {@link #getItemIterator(jakarta.faces.context.FacesContext,
   * jakarta.faces.component.UIComponent)}
   */
  public static List<SelectItem> getItemList(final FacesContext facesContext, final UIComponent selector) {
    if (selector.getChildCount() == 0) {
      return Collections.emptyList();
    } else {
      final Iterable<SelectItem> iterator = getItemIterator(facesContext, selector);
      final List<SelectItem> result = new ArrayList<>();
      for (final SelectItem selectItem : iterator) {
        result.add(selectItem);
      }
      return result;
    }
  }

  private static class SelectItemsIterator implements Iterator<SelectItem> {

    private final FacesContext facesContext;
    private final Iterator<UIComponent> children;
    private Iterator<?> nestedItems;
    private SelectItem nextItem;
    private UISelectItems currentUISelectItems;

    private SelectItemsIterator(final FacesContext facesContext, final UIComponent selector) {
      this.children = selector.getChildren().iterator();
      this.facesContext = facesContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasNext() {
      if (nextItem != null) {
        return true;
      }
      if (nestedItems != null) {
        if (nestedItems.hasNext()) {
          return true;
        }
        nestedItems = null;
      }

      UIComponent child = null;
      while (children.hasNext()) {
        final UIComponent c = children.next();
        // When there is other components nested that does
        // not extends from UISelectItem or UISelectItems
        // the behavior for this iterator is just skip this
        // element(s) until an element that extends from these
        // classes are found. If there is no more elements
        // that conform this condition, just return false.
        if (c instanceof UISelectItem || c instanceof UISelectItems) {
          child = c;
          break;
        }
      }
      if (child == null) {
        return false;
      }

      if (child instanceof UISelectItem) {
        final UISelectItem uiSelectItem = (UISelectItem) child;
        Object item = uiSelectItem.getValue();
        if (item == null) {
          // no value attribute --> create the SelectItem out of the other attributes
          final Object itemValue = uiSelectItem.getItemValue();
          String label = uiSelectItem.getItemLabel();
          final String description = uiSelectItem.getItemDescription();
          final boolean disabled = uiSelectItem.isItemDisabled();
          boolean escape = uiSelectItem.isItemEscaped();
          boolean noSelectionOption = uiSelectItem.isNoSelectionOption();
          if (label == null && itemValue != null) {
            label = itemValue.toString();
          }
          String image = null;
          Markup markup = null;
          if (uiSelectItem instanceof org.apache.myfaces.tobago.component.UISelectItem) {
            final org.apache.myfaces.tobago.component.UISelectItem tobagoSelectItem
                = (org.apache.myfaces.tobago.component.UISelectItem) uiSelectItem;
            image = tobagoSelectItem.getItemImage();
            markup = tobagoSelectItem.getMarkup();
          }
          item = new org.apache.myfaces.tobago.model.SelectItem(itemValue, label, description, disabled,
              escape, noSelectionOption, image, markup);
        } else if (!(item instanceof SelectItem)) {
          final ValueExpression expression = uiSelectItem.getValueExpression("value");
          throw new IllegalArgumentException("ValueExpression '"
              + (expression == null ? null : expression.getExpressionString()) + "' of UISelectItem : "
              + child + " does not reference an Object of type SelectItem");
        }
        nextItem = (SelectItem) item;
        return true;
      } else { // UISelectItems
        currentUISelectItems = (UISelectItems) child;
        final Object value = currentUISelectItems.getValue();

        if (value instanceof SelectItem) {
          nextItem = (SelectItem) value;
          return true;
        } else if (value != null && value.getClass().isArray()) {
          // value is any kind of array (primitive or non-primitive)
          // --> we have to use class Array to get the values
          final int length = Array.getLength(value);
          final Collection<Object> items = new ArrayList<>(length);
          for (int i = 0; i < length; i++) {
            items.add(Array.get(value, i));
          }
          nestedItems = items.iterator();
          return hasNext();
        } else if (value instanceof Iterable) {
          // value is Iterable --> Collection, DataModel,...
          nestedItems = ((Iterable<?>) value).iterator();
          return hasNext();
        } else if (value instanceof Map) {
          final Map<Object, Object> map = (Map<Object, Object>) value;
          final Collection<SelectItem> items = new ArrayList<>(map.size());
          for (final Map.Entry<Object, Object> entry : map.entrySet()) {
            items.add(new org.apache.myfaces.tobago.model.SelectItem(entry.getValue(), entry.getKey().toString()));
          }
          nestedItems = items.iterator();
          return hasNext();
        }
      }
      return false;
    }

    @Override
    public SelectItem next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (nextItem != null) {
        final SelectItem value = nextItem;
        nextItem = null;
        return value;
      }
      if (nestedItems != null) {
        Object item = nestedItems.next();

        if (!(item instanceof SelectItem)) {
          // check new params of SelectItems (since 2.0): itemValue, itemLabel, itemDescription,...
          // Note that according to the spec UISelectItems does not provide Getter and Setter
          // methods for this values, so we have to use the attribute map
          final Map<String, Object> attributeMap = currentUISelectItems.getAttributes();

          // write the current item into the request map under the key listed in var, if available
          boolean wroteRequestMapVarValue = false;
          Object oldRequestMapVarValue = null;
          final String var = ComponentUtils.getStringAttribute(currentUISelectItems, Attributes.var);
          if (var != null && !"".equals(var)) {
            // save the current value of the key listed in var from the request map
            oldRequestMapVarValue = facesContext.getExternalContext().getRequestMap().put(var, item);
            wroteRequestMapVarValue = true;
          }

          // check the itemValue attribute
          Object itemValue = ComponentUtils.getAttribute(currentUISelectItems, Attributes.itemValue);
          if (itemValue == null) {
            // the itemValue attribute was not provided
            // --> use the current item as the itemValue
            itemValue = item;
          }

          // Spec: When iterating over the select items, toString()
          // must be called on the string rendered attribute values
          final Object itemLabelObject = ComponentUtils.getAttribute(currentUISelectItems, Attributes.itemLabel);
          final String itemLabel;
          if (itemLabelObject != null) {
            itemLabel = itemLabelObject.toString();
          } else if (itemValue != null) {
            itemLabel = itemValue.toString();
          } else {
            LOG.warn("Label string can't be created!");
            itemLabel = "???";
          }
          Object itemDescription = ComponentUtils.getAttribute(currentUISelectItems, Attributes.itemDescription);
          if (itemDescription != null) {
            itemDescription = itemDescription.toString();
          }
          final boolean itemDisabled
              = ComponentUtils.getBooleanAttribute(currentUISelectItems, Attributes.itemDisabled, false);
          final String itemImage = ComponentUtils.getStringAttribute(currentUISelectItems, Attributes.itemImage);
          final Markup markup;
          if (currentUISelectItems instanceof Visual) {
            markup = ((Visual) currentUISelectItems).getMarkup();
          } else {
            markup = Markup.NULL;
          }
// TBD: should this be possible?
//        Boolean itemLabelEscaped = getBooleanAttribute(currentUISelectItems, ITEM_LABEL_ESCAPED_PROP, true);
// TBD ?
//        Object noSelectionValue = attributeMap.get(NO_SELECTION_VALUE_PROP);
          item = new org.apache.myfaces.tobago.model.SelectItem(
              itemValue, itemLabel, (String) itemDescription, itemDisabled, itemImage, markup);

          // remove the value with the key from var from the request map, if previously written
          if (wroteRequestMapVarValue) {
            // If there was a previous value stored with the key from var in the request map, restore it
            if (oldRequestMapVarValue != null) {
              facesContext.getExternalContext().getRequestMap().put(var, oldRequestMapVarValue);
            } else {
              facesContext.getExternalContext().getRequestMap().remove(var);
            }
          }
        }
        return (SelectItem) item;
      }
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

}
