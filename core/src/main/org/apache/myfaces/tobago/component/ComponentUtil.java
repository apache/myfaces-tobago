/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 01.07.2003 10:07:23.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.TobagoConstants;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.taglib.component.ForEachTag;
import org.apache.myfaces.tobago.util.RangeParser;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComponentUtil {

  private static final Log LOG = LogFactory.getLog(ComponentUtil.class);

  private static final String RENDER_KEY_PREFIX
      = "org.apache.myfaces.tobago.component.ComponentUtil.RendererKeyPrefix_";

  public static UIPage findPage(UIComponent component) {
    while (component != null) {
      if (component instanceof UIPage) {
        return (UIPage) component;
      }
      component = component.getParent();
    }
    return null;
  }

  public static UIForm findForm(UIComponent component) {
    while (component != null) {
      if (component instanceof UIForm) {
        return (UIForm) component;
      }
      component = component.getParent();
    }
    return null;
  }

  /**
   * Find all subforms of a component, and collects it.
   * It does not find subforms of subforms.
   */
  public static List<UIForm> findSubForms(UIComponent component) {
    List<UIForm> collect = new ArrayList<UIForm>();
    findSubForms(collect, component);
    return collect;
  }

  private static void findSubForms(List<UIForm> collect, UIComponent component) {
    //noinspection unchecked
    @SuppressWarnings(value = "unchecked")
    List<UIComponent> children = component.getChildren();
    for (UIComponent child : children) {
      if (child instanceof UIForm) {
        collect.add((UIForm) child);
      } else {
        findSubForms(collect, child);
      }
    }
  }

  /**
   * Looks for the attribute "for" in the component. If there is any
   * search for the component which is referenced by the "for" attribute,
   * and return their clientId.
   * If there is no "for" attribute, return the "clientId" of the parent
   * (if it has a parent). This is useful for labels.
   */
  public static String findClientIdFor(UIComponent component,
      FacesContext facesContext) {
    UIComponent forComponent = findFor(component);
    if (forComponent != null) {
      String clientId = forComponent.getClientId(facesContext);
      if (LOG.isDebugEnabled()) {
        LOG.debug("found clientId: '" + clientId + "'");
      }
      return clientId;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("found no clientId");
    }
    return null;
  }

  public static UIComponent findFor(UIComponent component) {
    String forValue
        = (String) component.getAttributes().get(ATTR_FOR);
    if (forValue == null) {
      UIComponent parent = component.getParent();
      if (parent != null) {
        return parent;
      } else {
        return null;
      }
    }
    return component.findComponent(forValue);
  }

  public static boolean isInActiveForm(UIComponent component) {
    while (component != null) {
//      log.debug("compoent= " + component.getClientId(FacesContext.getCurrentInstance()) + " " + component.getRendererType());
      if (component instanceof UIForm) {
        UIForm form = (UIForm) component;
        if (form.isSubmitted()) {
//          log.debug("in active form = " + form.getClientId(FacesContext.getCurrentInstance()));
          return true;
        } else {
//          log.debug("form found but not active = " + form.getClientId(FacesContext.getCurrentInstance()));
        }
      }
      component = component.getParent();
    }
//    log.debug("not in an active form");
    return false;
  }

  public static boolean isError(UIComponent component) {
    if (component instanceof EditableValueHolder) {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      Iterator messages
          = facesContext.getMessages(component.getClientId(facesContext));
      return !((EditableValueHolder) component).isValid() ||
          messages.hasNext();
    }
    return false;
  }

  public static boolean isOutputOnly(UIComponent component) {
    return getBooleanAttribute(component, ATTR_DISABLED)
        || getBooleanAttribute(component, ATTR_READONLY);
  }

  public static boolean mayValidate(UIComponent component) {
    return
        !isOutputOnly(component) &&
        ComponentUtil.isInActiveForm(component);
  }

  public static boolean mayUpdateModel(UIComponent component) {
    return mayValidate(component);
  }

  public static boolean getBooleanAttribute(UIComponent component, String name) {

    Object bool = component.getAttributes().get(name);
    if (bool == null) {
      return false;
    }
    if (bool instanceof ValueBinding) {
      bool = ((ValueBinding) bool).getValue(FacesContext.getCurrentInstance());
    }
    if (bool instanceof Boolean) {
      return ((Boolean) bool).booleanValue();
    } else if (bool instanceof String) {
      LOG.warn("Searching for a boolean, but find a String. Should not happen. "
          + "attribute: '" + name + "' comp: '" + component + "'");
      return Boolean.getBoolean((String) bool);
    } else {
      LOG.warn("Unknown type '" + bool.getClass().getName() +
          "' for boolean attribute: " + name + " comp: " + component);
      return false;
    }
  }

  public static Object getAttribute(UIComponent component, String name) {
    Object value = component.getAttributes().get(name);
    if (value instanceof ValueBinding) {
      value = ((ValueBinding) value).getValue(FacesContext.getCurrentInstance());
    }
    return value;
  }

  public static String getStringAttribute(UIComponent component, String name) {
    return (String) getAttribute(component, name);
  }

  public static int getIntAttribute(UIComponent component, String name) {
    return getIntAttribute(component, name, 0);
  }

  public static int getIntAttribute(UIComponent component, String name,
      int defaultValue) {
    Object integer = component.getAttributes().get(name);
    if (integer instanceof Number) {
      return ((Number) integer).intValue();
    } else if (integer instanceof String) {
      try {
        return Integer.parseInt((String) integer);
      } catch (NumberFormatException e) {
        LOG.warn("Can't parse number from string : \"" + integer + "\"!");
        return defaultValue;
      }
    } else if (integer == null) {
      return defaultValue;
    } else {
      LOG.warn("Unknown type '" + integer.getClass().getName() +
          "' for integer attribute: " + name + " comp: " + component);
      return defaultValue;
    }
  }

  public static Character getCharakterAttribute(UIComponent component, String name) {
    Object charakter = component.getAttributes().get(name);
    if (charakter == null) {
      return null;
    } else if (charakter instanceof Character) {
      return ((Character) charakter);
    } else if (charakter instanceof String) {
      String asString = ((String) charakter);
      return asString.length() > 0 ? new Character(asString.charAt(0)) : null;
    } else {
      LOG.warn("Unknown type '" + charakter.getClass().getName() +
          "' for integer attribute: " + name + " comp: " + component);
      return null;
    }
  }

  public static boolean isFacetOf(UIComponent component, UIComponent parent) {
    for (Iterator i = parent.getFacets().keySet().iterator(); i.hasNext();) {
      UIComponent facet = parent.getFacet((String) i.next());
      if (component.equals(facet)) {
        return true;
      }
    }
    return false;
  }

  // todo This should not be neseccary, but UIComponentBase.getRenderer() is protected
  public static RendererBase getRenderer(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext, component.getFamily(), component.getRendererType());

  }

  public static RendererBase getRenderer(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    RendererBase renderer;

    Map requestMap = facesContext.getExternalContext().getRequestMap();
    renderer = (RendererBase) requestMap.get(RENDER_KEY_PREFIX + rendererType);

    if (renderer == null) {
      RenderKitFactory rkFactory = (RenderKitFactory)
          FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
      RenderKit renderKit = rkFactory.getRenderKit(facesContext, facesContext.getViewRoot().getRenderKitId());
      renderer = (RendererBase) renderKit.getRenderer(family, rendererType);
      requestMap.put(RENDER_KEY_PREFIX + rendererType, renderer);
    }
    return renderer;
  }

  public static String currentValue(UIComponent component) {
    String currentValue = null;
    if (component instanceof ValueHolder) {
      Object value = ((ValueHolder) component).getValue();
      if (value != null) {
        Converter converter = ((ValueHolder) component).getConverter();
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

    for (Iterator kids = component.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("kid " + kid);
        LOG.debug("kid " + kid.getClass().getName());
      }
      if (kid instanceof UISelectItem) {
        Object value = ((UISelectItem) kid).getValue();
        if (value == null) {
          UISelectItem item = (UISelectItem) kid;
          if (kid instanceof org.apache.myfaces.tobago.component.UISelectItem) {
            list.add(new org.apache.myfaces.tobago.model.SelectItem((org.apache.myfaces.tobago.component.UISelectItem) kid));
          } else {
            list.add(new SelectItem(item.getItemValue() == null ? "" : item.getItemValue(),
                item.getItemLabel(),
                item.getItemDescription()));
          }
        } else if (value instanceof SelectItem) {
          list.add((SelectItem) value);
        } else {
          throw new IllegalArgumentException("TYPE ERROR: value NOT instanceof SelectItem. type=" +
              value.getClass().getName());
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
          SelectItem items[] = (SelectItem[]) value;
          for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
          }
        } else if (value instanceof Collection) {
          for (Iterator elements = ((Collection) value).iterator();
              elements.hasNext(); list.add((SelectItem) elements.next())) {
          }
        } else if (value instanceof Map) {
          for (Iterator keys = ((Map) value).keySet().iterator();
              keys.hasNext();) {
            Object key = keys.next();
            if (key != null) {
              Object val = ((Map) value).get(key);
              if (val != null) {
                list.add(new SelectItem(val.toString(), key.toString(), null));
              }
            }
          }
        } else {
          throw new IllegalArgumentException("TYPE ERROR: value NOT instanceof " +
              "SelectItem, SelectItem[], Collection, Map. type=" +
              value.getClass().getName());
        }
      }
    }

    return list;
  }

  public static Object findParameter(UIComponent component, String name) {
    for (Iterator i = component.getChildren().iterator(); i.hasNext();) {
      UIComponent child = (UIComponent) i.next();
      if (child instanceof UIParameter) {
        UIParameter parameter = (UIParameter) child;
        if (LOG.isDebugEnabled()) {
          LOG.debug("Select name='" + parameter.getName() + "'");
          LOG.debug("Select value='" + parameter.getValue() + "'");
        }
        if (name.equals(parameter.getName())) {
          return parameter.getValue();
        }
      }
    }
    return null;
  }

  public static String toString(UIComponent component, int offset) {
    return toString(component, offset, false);
  }

  private static String toString(UIComponent component, int offset, boolean asFacet) {
    StringBuffer result = new StringBuffer();
    if (component == null) {
      result.append("null");
    } else {
      result.append('\n');
      if (!asFacet) {
        result.append(spaces(offset) + toString(component));
      }
      Map facets = component.getFacets();
      if (facets.size() > 0) {
        for (Iterator iter = facets.keySet().iterator(); iter.hasNext();) {
          Object name = iter.next();
          UIComponent facet = (UIComponent) facets.get(name);
          result.append('\n');
          result.append(spaces(offset + 1));
          result.append('\"');
          result.append(name);
          result.append("\" = ");
          result.append(toString(facet));
          result.append(toString(facet, offset + 1, true));
        }
      }
      for (Iterator i = component.getChildren().iterator(); i.hasNext();) {
        result.append(toString((UIComponent) i.next(), offset + 1, false));
      }
    }
    return result.toString();
  }

  private static String toString(UIComponent component) {
    return component.getClass().getName()
        + '@' + Integer.toHexString(component.hashCode())
        + " " + component.getRendererType()
        + " " + component.getId()
        + " " + component.getClientId(FacesContext.getCurrentInstance());
  }

  private static String spaces(int n) {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < n; i++) {
      buffer.append("  ");
    }
    return buffer.toString();
  }

  public static ActionListener createActionListener(String type)
      throws JspException {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        classLoader = type.getClass().getClassLoader();
      }
      Class clazz = classLoader.loadClass(type);
      return (ActionListener) clazz.newInstance();
    } catch (Exception e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("type=" + type, e);
      }
      throw new JspException(e);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public static UIGraphic getFirstGraphicChild(UIComponent component) {
    UIGraphic graphic = null;
    for (Object o : component.getChildren()) {
      UIComponent uiComponent = (UIComponent) o;
      if (uiComponent instanceof UIGraphic) {
        graphic = (UIGraphic) uiComponent;
        break;
      }
    }
    return graphic;
  }

  public static boolean isHoverEnabled(UIComponent component) {
    return ComponentUtil.getBooleanAttribute(component, ATTR_HOVER);
  }

  public static UIOutput getFirstNonGraphicChild(UIComponent component) {
    UIOutput output = null;
    for (Object o : component.getChildren()) {
      UIComponent uiComponent = (UIComponent) o;
      if ((uiComponent instanceof UIOutput) &&
          !(uiComponent instanceof UIGraphic)) {
        output = (UIOutput) uiComponent;
        break;
      }
    }
    return output;
  }


  public static final void setIntegerProperty(UIComponent component,
      String name, String value, ForEachTag.IterationHelper iterator) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value, iterator));
      } else {
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  public static final void setBooleanProperty(UIComponent component,
      String name, String value, ForEachTag.IterationHelper iterator) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value, iterator));
      } else {
        component.getAttributes().put(name, Boolean.valueOf(value));
      }
    }
  }

  public static final void setStringProperty(UIComponent component, String name,
      String value, ForEachTag.IterationHelper iterator) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value, iterator));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  public static ValueBinding createValueBinding(String value,
      ForEachTag.IterationHelper iterator) {
    if (iterator != null) {
      value = iterator.replace(value);
    }
    return FacesContext.getCurrentInstance().getApplication()
        .createValueBinding(value);
  }      

  public static String getValueFromEl(String script) {
    if (UIComponentTag.isValueReference(script)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(script, null);
      script = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
    }
    return script;
  }

  public static UIComponent createLabeledInputLayoutComponent() {
    return createComponent(UILabeledInputLayout.COMPONENT_TYPE,
        RENDERER_TYPE_LABELED_INPUT_LAYOUT);
  }

  public static UIComponent createComponent(String componentType, String rendererType) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType);
  }

  public static UIComponent createComponent(FacesContext facesContext, String componentType, String rendererType) {
    UIComponent component
        = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    return component;
  }

  public static UIColumn createTextColumn(String label, String sortable, String align, String value) {
    UIComponent text = createComponent(UIOutput.COMPONENT_TYPE, RENDERER_TYPE_OUT);
    setStringProperty(text, ATTR_VALUE, value, null);
    return createColumn(label, sortable, align, text);
  }

  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child) {
    UIColumn column = createColumn(label, sortable, align);
    column.getChildren().add(child);
    return column;
  }

  private static UIColumn createColumn(String label, String sortable, String align) {
    UIColumn column = (UIColumn) createComponent(UIColumn.COMPONENT_TYPE, null);
    setStringProperty(column, ATTR_LABEL, label, null);
    setBooleanProperty(column, ATTR_SORTABLE, sortable, null);
    setStringProperty(column, ATTR_ALIGN, align, null);
    return column;
  }

  public static UISelectOne createUISelectOneFacet(FacesContext facesContext, UICommand command) {
    UISelectOne radio = null;
    final ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
    if (valueBinding != null) {
      radio = (UISelectOne) createComponent(facesContext,
          UISelectOne.COMPONENT_TYPE, RENDERER_TYPE_SELECT_ONE_RADIO);
      command.getFacets().put(FACET_RADIO, radio);
      radio.setValueBinding(ATTR_VALUE, valueBinding);
    }
    return radio;
  }


  public static boolean hasSelectedValue(List<SelectItem> items, Object value) {
    for (SelectItem item : items) {
      if (item.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command) {
    UIComponent checkbox = null;
    final ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
    if (valueBinding != null) {
      checkbox = createComponent(facesContext, UISelectBoolean.COMPONENT_TYPE,
          RENDERER_TYPE_SELECT_BOOLEAN_CHECKBOX);
      command.getFacets().put(FACET_CHECKBOX, checkbox);
      checkbox.setValueBinding(ATTR_VALUE, valueBinding);
    }
    return checkbox;
  }

  public static int getIntValue(ValueBinding valueBinding) {
    return getAsInt(valueBinding.getValue(FacesContext.getCurrentInstance()));
  }

  private static int getAsInt(Object value) {
    int result;
    if (value instanceof Number) {
      result = ((Number) value).intValue();
    } else if (value instanceof String) {
      result = Integer.parseInt((String) value);
    } else {
      throw new IllegalArgumentException("Can't convert " + value + " to int!");
    }
    return result;
  }


  public static String createPickerId(FacesContext facesContext, UIComponent component, String postfix) {
    String id = component.getId();
    id = getComponentId(facesContext, component);
    return id + "_picker" + postfix;
  }

  public static String getComponentId(FacesContext facesContext, UIComponent component) {
    String id = component.getId();
    if (id == null) {
      id = component.getClientId(facesContext).substring(id.lastIndexOf('_'));
    }
    return id;
  }

  public static UIComponent provideLabel(FacesContext facesContext, UIComponent component) {
    UIComponent label = component.getFacet(FACET_LABEL);


    if (label == null) {
      final Map attributes = component.getAttributes();
      Object labelText = component.getValueBinding(ATTR_LABEL);
      if (labelText ==null) {
        labelText = attributes.get(ATTR_LABEL);
      }

      Object labelWithAccessKey = component.getValueBinding(ATTR_LABEL_WITH_ACCESS_KEY);
      if (labelWithAccessKey == null) {
        labelWithAccessKey = attributes.get(ATTR_LABEL_WITH_ACCESS_KEY);
      }

      Object accessKey = component.getValueBinding(ATTR_ACCESS_KEY);
      if (accessKey == null) {
        accessKey = attributes.get(ATTR_ACCESS_KEY);
      }

      if (labelText != null || labelWithAccessKey != null || accessKey != null) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        label = (UIOutput) application.createComponent(UIOutput.COMPONENT_TYPE);
        label.setRendererType("Label");
        String idprefix = ComponentUtil.getComponentId(facesContext, component);
        label.setId(idprefix + "_" + FACET_LABEL);
        label.setRendered(true);

        if (labelText instanceof ValueBinding) {
          label.setValueBinding(ATTR_VALUE, (ValueBinding) labelText);
        } else if (labelText != null) {
          label.getAttributes().put(ATTR_VALUE, labelText);
        }
        if (labelWithAccessKey instanceof ValueBinding) {
          label.setValueBinding(ATTR_LABEL_WITH_ACCESS_KEY, (ValueBinding) labelWithAccessKey);
        } else if (labelWithAccessKey != null) {
          label.getAttributes().put(ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
        }
        if (accessKey instanceof ValueBinding) {
          label.setValueBinding(ATTR_ACCESS_KEY, (ValueBinding) accessKey);
        } else if (accessKey != null) {
          label.getAttributes().put(ATTR_ACCESS_KEY, accessKey);
        }

        component.getFacets().put(FACET_LABEL, label);
      }
    }
    return label;
  }
  

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectOne component) {
    return getItems(component);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectMany component) {
    return getItems(component);
  }

  private static List<SelectItem> getItems(javax.faces.component.UIInput component) {

    List<SelectItem> selectItems = ComponentUtil.getSelectItems(component);

    String renderRange = (String)
        component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE_EXTERN);
    if (renderRange == null) {
      renderRange = (String)
          component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE);
    }
    if (renderRange == null) {
      return selectItems;
    }

    int[] indices = RangeParser.getIndices(renderRange);
    List<SelectItem> items = new ArrayList<SelectItem>(indices.length);

    if (selectItems.size() != 0) {
      for (int i = 0; i < indices.length; i++) {
        items.add(selectItems.get(indices[i]));
      }
    } else {
      LOG.warn("No items found! rendering dummys instead!");
      for (int i = 0; i < indices.length; i++) {
        items.add(new SelectItem(Integer.toString(i), "Item " + i, ""));
      }
    }
    return items;
  }
}
