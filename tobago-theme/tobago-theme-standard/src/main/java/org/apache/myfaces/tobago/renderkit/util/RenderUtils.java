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

package org.apache.myfaces.tobago.renderkit.util;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIData;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.ExpandedState;
import org.apache.myfaces.tobago.model.SelectedState;
import org.apache.myfaces.tobago.model.TreePath;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RenderUtils {

  private static final Logger LOG = LoggerFactory.getLogger(RenderUtils.class);

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static final String COMPONENT_IN_REQUEST = "org.apache.myfaces.tobago.component";

  private RenderUtils() {
    // to prevent instantiation
  }

  public static boolean contains(Object[] list, Object value) {
    if (list == null) {
      return false;
    }
    for (Object aList : list) {
      if (aList != null && aList.equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static void encodeChildren(FacesContext facesContext, UIComponent panel) throws IOException {
    for (UIComponent child : panel.getChildren()) {
      encode(facesContext, child);
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    encode(facesContext, component, null);
  }

  public static void encode(
      FacesContext facesContext, UIComponent component, List<? extends Class<? extends UIComponent>> only)
      throws IOException {

    if (only != null && !matchFilter(component, only)) {
      return;
    }

    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }
      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        for (UIComponent child : component.getChildren()) {
          encode(facesContext, child, only);
        }
      }
      component.encodeEnd(facesContext);
    }
  }

  private static boolean matchFilter(UIComponent component, List<? extends Class<? extends UIComponent>> only) {
    for (Class<? extends UIComponent> clazz : only) {
      if (clazz.isAssignableFrom(component.getClass())) {
        return true;
      }
    }
    return false;
  }

  public static void prepareRendererAll(FacesContext facesContext, UIComponent component) throws IOException {
    if (!component.isRendered()) {
      return;
    }
    RendererBase renderer = ComponentUtils.getRenderer(facesContext, component);
    boolean prepareRendersChildren = false;
    if (renderer != null) {
      renderer.prepareRender(facesContext, component);
      prepareRendersChildren = renderer.getPrepareRendersChildren();
    }
    if (prepareRendersChildren) {
      renderer.prepareRendersChildren(facesContext, component);
    } else {
      Iterator it = component.getFacetsAndChildren();
      while (it.hasNext()) {
        UIComponent child = (UIComponent) it.next();
        prepareRendererAll(facesContext, child);
      }
    }
  }

  public static String getFormattedValue(
      FacesContext facesContext, UIComponent component) {
    Object value = null;
    if (component instanceof ValueHolder) {
      value = ((ValueHolder) component).getLocalValue();
      if (value == null) {
        value = ((ValueHolder) component).getValue();
      }
    }
    return getFormattedValue(facesContext, component, value);
  }

  // Copy from RendererBase
  public static String getFormattedValue(
      FacesContext context, UIComponent component, Object currentValue)
      throws ConverterException {

    if (currentValue == null) {
      return "";
    }

    if (!(component instanceof ValueHolder)) {
      return currentValue.toString();
    }

    Converter converter = ((ValueHolder) component).getConverter();

    if (converter == null) {
      if (currentValue instanceof String) {
        return (String) currentValue;
      }
      Class converterType = currentValue.getClass();
      converter = context.getApplication().createConverter(converterType);
    }

    if (converter == null) {
      return currentValue.toString();
    } else {
      return converter.getAsString(context, component, currentValue);
    }
  }

  public static Measure calculateStringWidth(FacesContext facesContext, UIComponent component, String text) {
    return calculateStringWidth(facesContext, (Configurable) component, text, "tobago.font.widths");
  }

  public static Measure calculateStringWidth2(FacesContext facesContext, UIComponent component, String text) {
    return calculateStringWidth(facesContext, (Configurable) component, text, "tobago.font2.widths");
  }

  private static Measure calculateStringWidth(
      FacesContext facesContext, Configurable component, String text, String type) {
    if (text == null) {
      return Measure.ZERO;
    }
    int width = 0;
    int defaultCharWidth = 10;
    try {
      defaultCharWidth = ResourceManagerUtils.getThemeMeasure(facesContext, component, "fontWidth").getPixel();
    } catch (NullPointerException e) {
      LOG.warn("no value for 'fontWidth' for type '" + component.getRendererType() + "' found in theme-config");
    }

    String fontWidths = ResourceManagerUtils.getProperty(facesContext, "tobago", type);

    for (char c : text.toCharArray()) {
      if (c >= 32 && c < 128) { // "normal" char in precomputed range
        int begin = (c - 32) * 2;
        width += Integer.parseInt(fontWidths.substring(begin, begin + 2), 16);
      } else {
        width += defaultCharWidth;
      }
    }

    width += width / 10 + 1; // fixes the problem, that sometime some browsers add some pixels

    return Measure.valueOf(width);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectOne component) {
    return getItems(component);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectMany component) {
    return getItems(component);
  }

  public static List<SelectItem> getItems(javax.faces.component.UIInput component) {

    List<SelectItem> selectItems = getSelectItems(component);

    String renderRange = (String) component.getAttributes().get(Attributes.RENDER_RANGE_EXTERN);
    if (renderRange == null) {
      renderRange = (String) component.getAttributes().get(Attributes.RENDER_RANGE);
    }
    if (renderRange == null) {
      return selectItems;
    }

    int[] indices = StringUtils.getIndices(renderRange);
    List<SelectItem> items = new ArrayList<SelectItem>(indices.length);

    if (selectItems.size() != 0) {
      for (int indice : indices) {
        items.add(selectItems.get(indice));
      }
    } else {
      LOG.warn("No items found! rendering dummies instead!");
      for (int i = 0; i < indices.length; i++) {
        items.add(new SelectItem(Integer.toString(i), "Item " + i, ""));
      }
    }
    return items;
  }

  public static String currentValue(UIComponent component) {
    String currentValue = null;
    if (component instanceof ValueHolder) {
      Object value;
      if (component instanceof EditableValueHolder) {
        value = ((EditableValueHolder) component).getSubmittedValue();
        if (value != null) {
          return (String) value;
        }
      }

      value = ((ValueHolder) component).getValue();
      if (value != null) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
          FacesContext context = FacesContext.getCurrentInstance();
          converter = context.getApplication().createConverter(value.getClass());
        }
        if (converter != null) {
          currentValue =
              converter.getAsString(FacesContext.getCurrentInstance(),
                  component, value);
        } else {
          currentValue = value.toString();
        }
      }
    }
    return currentValue;
  }

  public static List<SelectItem> getSelectItems(UIComponent component) {

    ArrayList<SelectItem> list = new ArrayList<SelectItem>();

    for (UIComponent child : component.getChildren()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("kid " + child);
        LOG.debug("kid " + child.getClass().getName());
      }
      if (child instanceof UISelectItem) {
        Object value = ((UISelectItem) child).getValue();
        if (value == null) {
          UISelectItem item = (UISelectItem) child;
          if (child instanceof org.apache.myfaces.tobago.component.UISelectItem) {
            list.add(getSelectItem(
                (org.apache.myfaces.tobago.component.UISelectItem) child));
          } else {
            list.add(new SelectItem(item.getItemValue() == null ? "" : item.getItemValue(),
                item.getItemLabel() != null ? item.getItemLabel() : item.getItemValue().toString(),
                item.getItemDescription()));
          }
        } else if (value instanceof SelectItem) {
          list.add((SelectItem) value);
        } else {
          final String message
              = "TYPE ERROR: value NOT instanceof SelectItem. type="
              + value.getClass().getName() + " value=" + value;
          LOG.error(message);
          DebugUtils.addDevelopmentMessage(FacesContext.getCurrentInstance(), message);
        }
      } else if (child instanceof UISelectItems) {
        Object value = ((UISelectItems) child).getValue();
        if (LOG.isDebugEnabled()) {
          LOG.debug("value " + value);
          if (value != null) {
            LOG.debug("value " + value.getClass().getName());
          }
        }
        if (value == null) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("value is null");
          }
        } else if (value instanceof SelectItem) {
          list.add((SelectItem) value);
        } else if (value instanceof SelectItem[]) {
          SelectItem[] items = (SelectItem[]) value;
          list.addAll(Arrays.asList(items));
        } else if (value instanceof Collection) {
          for (Object o : ((Collection) value)) {
            list.add((SelectItem) o);
          }
        } else if (value instanceof Map) {
          for (Object key : ((Map) value).keySet()) {
            if (key != null) {
              Object val = ((Map) value).get(key);
              if (val != null) {
                list.add(new SelectItem(val.toString(), key.toString(), null));
              }
            }
          }
        } else {
          final String message
              = "TYPE ERROR: value NOT instanceof SelectItem, SelectItem[], Collection, Map. type="
              + value.getClass().getName() + " value=" + value;
          LOG.error(message);
          DebugUtils.addDevelopmentMessage(FacesContext.getCurrentInstance(), message);
        }
      }
    }

    return list;
  }

  private static SelectItem getSelectItem(org.apache.myfaces.tobago.component.UISelectItem component) {
    return
        new org.apache.myfaces.tobago.model.SelectItem(component.getItemValue() == null ? "" : component.getItemValue(),
            component.getItemLabel(), component.getItemDescription(),
            component.isItemDisabled(), component.getItemImage(), component.getMarkup());
  }


  public static void decodedStateOfTreeData(FacesContext facesContext, AbstractUIData data) {

    if (!data.isTreeModel()) {
      return;
    }

    // selected
    final List<Integer> selectedIndices = decodeIndices(facesContext, data, AbstractUIData.SUFFIX_SELECTED);

    // expanded
    final List<Integer> expandedIndices = decodeIndices(facesContext, data, AbstractUIData.SUFFIX_EXPANDED);

    final int last = data.isRowsUnlimited() ? Integer.MAX_VALUE : data.getFirst() + data.getRows();
    for (int rowIndex = data.getFirst(); rowIndex < last; rowIndex++) {
      data.setRowIndex(rowIndex);
      if (!data.isRowAvailable()) {
        break;
      }

      final TreePath path = data.getPath();

      // selected
        final SelectedState selectedState = data.getSelectedState();
        final boolean oldSelected = selectedState.isSelected(path);
        final boolean newSelected = selectedIndices.contains(rowIndex);
        if (newSelected != oldSelected) {
          if (newSelected) {
            selectedState.select(path);
          } else {
            selectedState.unselect(path);
          }
        }

      // expanded
      if (expandedIndices != null) {
        final ExpandedState expandedState = data.getExpandedState();
        final boolean oldExpanded = expandedState.isExpanded(path);
        final boolean newExpanded = expandedIndices.contains(rowIndex);
        if (newExpanded != oldExpanded) {
          if (newExpanded) {
            expandedState.expand(path);
          } else {
            expandedState.collapse(path);
          }
        }
      }

    }
    data.setRowIndex(-1);
  }

  private static List<Integer> decodeIndices(FacesContext facesContext, AbstractUIData data, String suffix) {
    String string = null;
    final String key = data.getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + suffix;
    try {
      string = facesContext.getExternalContext().getRequestParameterMap().get(key);
      if (string != null) {
        return StringUtils.parseIntegerList(string);
      }
    } catch (Exception e) {
      // should not happen
      LOG.warn("Can't parse " + suffix + ": '" + string + "' from parameter '" + key + "'", e);
    }
    return null;
  }

  public static String generateUrl(FacesContext facesContext, AbstractUICommandBase component) {

    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();
    final ExternalContext externalContext = facesContext.getExternalContext();

    String url = null;

    if (component.getResource() != null) {
      boolean jsfResource = component.isJsfResource();
      url = ResourceManagerUtils.getPageWithoutContextPath(facesContext, component.getResource());
      if (url != null) {
        if (jsfResource) {
          url = viewHandler.getActionURL(facesContext, url);
          url = externalContext.encodeActionURL(url);
        } else {
          url = viewHandler.getResourceURL(facesContext, url);
          url = externalContext.encodeResourceURL(url);
        }
      } else {
        url = "";
      }
    } else if (component.getLink() != null) {

      String link = component.getLink();
      if (link.startsWith("/")) { // internal absolute link
        url = viewHandler.getActionURL(facesContext, link);
        url = externalContext.encodeActionURL(url);
      } else if (link.contains(":")) { // external link
        url = link;
      } else { // internal relative link
        url = externalContext.encodeResourceURL(link);
      }

      StringBuilder builder = new StringBuilder(url);
      boolean firstParameter = !url.contains("?");
      for (UIComponent child : component.getChildren()) {
        if (child instanceof UIParameter) {
          UIParameter parameter = (UIParameter) child;
          if (firstParameter) {
            builder.append("?");
            firstParameter = false;
          } else {
            builder.append("&");
          }
          builder.append(parameter.getName());
          builder.append("=");
          Object value = parameter.getValue();
          // TODO encoding
          builder.append(value != null ? URLDecoder.decode(value.toString()) : null);
        }
      }
      url = builder.toString();
    }

    return url;
  }

}
