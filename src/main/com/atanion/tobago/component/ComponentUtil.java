/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 01.07.2003 10:07:23.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.ForEachTag;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.webapp.UIComponentTag;
import javax.faces.el.ValueBinding;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.component.UIColumn;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionListener;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// todo: java 1.5 use static import for TobagoConstants
public class ComponentUtil implements TobagoConstants {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ComponentUtil.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

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
  public static void findSubForms(List collect, UIComponent component) {
    List children = component.getChildren();
    for (int i = 0; i < children.size(); i++) {
      UIComponent child = (UIComponent) children.get(i);
      if (child instanceof UIForm) {
        collect.add(child);
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
      if (LOG.isDebugEnabled())  {
        LOG.debug("found clientId: '" + clientId + "'");
      }
      return clientId;
    }
    if (LOG.isDebugEnabled())  {
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
      bool = ((ValueBinding)bool).getValue(FacesContext.getCurrentInstance());
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
      value = ((ValueBinding)value).getValue(FacesContext.getCurrentInstance());
    }
    return value;
  }

  public static int getIntAttribute(UIComponent component, String name) {
    return getIntAttribute(component, name, 0);
  }

  public static int getIntAttribute(UIComponent component, String name,
      int defaultValue) {
    Object integer = component.getAttributes().get(name);
    if (integer instanceof Number) {
      return ((Number) integer).intValue();
    } else if (integer == null) {
      return defaultValue;
    } else {
      LOG.warn("Unknown type '" + integer.getClass().getName() +
          "' for integer attribute: " + name + " comp: " + component);
      return defaultValue;
    }
  }

  public static Character getCharakterAttribute(
      UIComponent component, String name) {
    Object charakter = component.getAttributes().get(name);
    if (charakter == null) {
      return null;
    } else if (charakter instanceof Character) {
      return ((Character) charakter);
    } else if (charakter instanceof String) {
      String asString = ((String)charakter);
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
  public static RendererBase getRenderer(
      UIComponent component, FacesContext facesContext) {
    String rendererType = component.getRendererType();
    if (rendererType == null) {
      return null;
    }
    RenderKitFactory rkFactory = (RenderKitFactory)
        FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = rkFactory.getRenderKit(
        facesContext, facesContext.getViewRoot().getRenderKitId());
    return (RendererBase)
        renderKit.getRenderer(component.getFamily(), rendererType);
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

  public static List getSelectItems(UIComponent component) {

    ArrayList list = new ArrayList();

    for (Iterator kids = component.getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (LOG.isDebugEnabled())  {
        LOG.debug("kid " + kid);
        LOG.debug("kid " + kid.getClass().getName());
      }
      if (kid instanceof UISelectItem) {
        Object value = ((UISelectItem) kid).getValue();
        if (value == null) {
          UISelectItem item = (UISelectItem) kid;
          list.add(new SelectItem(item.getItemValue(), item.getItemLabel(),
              item.getItemDescription()));
        } else if (value instanceof SelectItem) {
          list.add(value);
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
          if (LOG.isDebugEnabled())  {
            LOG.debug("value is null");
          }
          continue;
        } else if (value instanceof SelectItem) {
          list.add(value);
        } else if (value instanceof SelectItem[]) {
          SelectItem items[] = (SelectItem[]) value;
          for (int i = 0; i < items.length; i++) {
            list.add(items[i]);
          }
        } else if (value instanceof Collection) {
          for (Iterator elements = ((Collection) value).iterator();
              elements.hasNext(); list.add(elements.next())) {
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

  public static void debug(UIComponent component, int offset) {
    LOG.debug(spaces(offset) + debugComponent(component));
    Map facets = component.getFacets();
    if (facets.size() > 0) {
      for (Iterator iter = facets.keySet().iterator(); iter.hasNext();) {
        Object name = iter.next();
        LOG.debug(spaces(offset + 1) + "\"" + name + "\" = "
            + debugComponent((UIComponent) facets.get(name)));
      }
    }
    for (Iterator i = component.getChildren().iterator(); i.hasNext();) {
      debug((UIComponent) i.next(), offset + 1);
    }
  }

  private static String debugComponent(UIComponent component) {
    return component.getClass().getName()
        + '@' + Integer.toHexString(component.hashCode())
        + " " + component.getRendererType()
        + " " + component.getId();
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
    final Iterator iterator = component.getChildren().iterator();
    while (iterator.hasNext()) {
      UIComponent uiComponent = (UIComponent) iterator.next();
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
    final Iterator iterator = component.getChildren().iterator();
    while (iterator.hasNext()) {
      UIComponent uiComponent = (UIComponent) iterator.next();
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
        if (Boolean.valueOf(value).booleanValue()) {
          component.getAttributes().put(name, Boolean.TRUE);
        } else {
          component.getAttributes().remove(name);
        }
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

  public static UIComponent createComponent(String componentType, String rendererType) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType);
  }

  public static UIComponent createComponent(
      FacesContext facesContext, String componentType, String rendererType) {
    UIComponent component
        = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    return component;
  }
    
  public static UIColumn createTextColumn(
      String label, String sortable, String align, String value) {
    UIComponent text = createComponent(UIOutput.COMPONENT_TYPE, "Text");
    setStringProperty(text, ATTR_VALUE, value, null);
    return createColumn(label, sortable, align, text);
  }

  public static UIColumn createColumn(
      String label, String sortable, String align, UIComponent child) {
    UIColumn column = createColumn(label, sortable, align);
    column.getChildren().add(child);
    return column;
  }

  private static UIColumn createColumn(
      String label, String sortable, String align) {
    UIColumn column = (UIColumn) createComponent(UIColumn.COMPONENT_TYPE, null);
    setStringProperty(column, ATTR_LABEL, label, null);
    setBooleanProperty(column, ATTR_SORTABLE, sortable, null);
    setStringProperty(column, ATTR_ALIGN, align, null);
    return column;
  }

  public static int getIntValue(ValueBinding valueBinding) {
    return getAsInt(valueBinding.getValue(FacesContext.getCurrentInstance()));
  }

  private static int getAsInt(Object value) {
    int result;
    if (value instanceof Number) {
      result = ((Number)value).intValue();
    }
    else if (value instanceof String ) {
      result = Integer.parseInt((String) value);
    }
    else {
      throw new IllegalArgumentException("Can't convert " + value + " to int!");
    }
    return result;
  }
}
