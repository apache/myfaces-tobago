package org.apache.myfaces.tobago.renderkit.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @deprecated please use {@link RenderUtils}
 */
@Deprecated
public class RenderUtil {

  private static final Logger LOG = LoggerFactory.getLogger(RenderUtil.class);

  public static final String COMPONENT_IN_REQUEST = "org.apache.myfaces.tobago.component";

  private RenderUtil() {
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

  public static void encodeChildren(FacesContext facesContext,
      UIComponent panel)
      throws IOException {
//    UIComponent layout = panel.getFacet("layout");

    encodeChildrenWithoutLayout(facesContext, panel);
    // XXX is the following coded needed?

//    UILayoutBase layout = UILayoutBase.getLayout(panel);
//    if (layout != null) {
//      layout.encodeChildrenOfComponent(facesContext, panel);
//    } else {
//      for (Object o : panel.getChildren()) {
//        UIComponent child = (UIComponent) o;
//        encode(facesContext, child);
//      }
//    }
  }

  public static void encodeChildrenWithoutLayout(FacesContext facesContext, UIComponent container) throws IOException {
    for (UIComponent child : (List<UIComponent>) container.getChildren()) {
      encode(facesContext, child);
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }
      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        for (Object o : component.getChildren()) {
          UIComponent kid = (UIComponent) o;
          encode(facesContext, kid);
        }
      }
      component.encodeEnd(facesContext);
    }
  }

  public static void prepareRendererAll(FacesContext facesContext, UIComponent component) throws IOException {
    if (!component.isRendered()) {
      return;
    }
    RendererBase renderer = ComponentUtils.getRenderer(facesContext,  component);
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

  public static String addMenuCheckToggle(String clientId, String onClick) {
    Deprecation.LOG.error("No longer supported");
    return "alert('Using deprecated API. Please check log file.');";
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
    int width = 0;
    int defaultCharWidth = 0;
    try {
      // todo: use Measure instead of int
      defaultCharWidth = ResourceManagerUtils.getThemeMeasure(facesContext, component, "fontWidth").getPixel();
    } catch (NullPointerException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("no value for \"fontWidth\" found in theme-config");
      }
    }

    String fontWidths = ResourceManagerUtils.getProperty(facesContext, "tobago", type);

    for (char c : text.toCharArray()) {
      int charWidth;
      if (c >= 32 && c < 128) {
        int begin = (c - 32) * 2;
        charWidth = Integer.parseInt(fontWidths.substring(begin, begin + 2), 16);
      } else {
        charWidth = defaultCharWidth;
      }
      width += charWidth;
    }

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

    String renderRange = (String)
        component.getAttributes().get(Attributes.RENDER_RANGE_EXTERN);
    if (renderRange == null) {
      renderRange = (String)
          component.getAttributes().get(Attributes.RENDER_RANGE);
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
      LOG.warn("No items found! rendering dummys instead!");
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

    for (Object o1 : component.getChildren()) {
      UIComponent kid = (UIComponent) o1;
      if (LOG.isDebugEnabled()) {
        LOG.debug("kid " + kid);
        LOG.debug("kid " + kid.getClass().getName());
      }
      if (kid instanceof UISelectItem) {
        Object value = ((UISelectItem) kid).getValue();
        if (value == null) {
          UISelectItem item = (UISelectItem) kid;
          if (kid instanceof org.apache.myfaces.tobago.component.UISelectItem) {
            list.add(getSelectItem(
                (org.apache.myfaces.tobago.component.UISelectItem) kid));
          } else {
            list.add(new SelectItem(item.getItemValue() == null ? "" : item.getItemValue(),
                item.getItemLabel() != null ? item.getItemLabel() : item.getItemValue().toString(),
                item.getItemDescription()));
          }
        } else if (value instanceof SelectItem) {
          list.add((SelectItem) value);
        } else {
          throw new IllegalArgumentException("TYPE ERROR: value NOT instanceof SelectItem. type="
              + value.getClass().getName());
        }
      } else if (kid instanceof UISelectItems) {
        Object value = ((UISelectItems) kid).getValue();
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
          throw new IllegalArgumentException("TYPE ERROR: value NOT instanceof "
              + "SelectItem, SelectItem[], Collection, Map. type="
              + value.getClass().getName());
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

}
