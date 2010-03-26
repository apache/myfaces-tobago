package org.apache.myfaces.tobago.component;

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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.context.TransientStateHolder;
import org.apache.myfaces.tobago.el.ConstantMethodBinding;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.renderkit.LayoutableRendererBase;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.Callback;
import org.apache.myfaces.tobago.util.RangeParser;
import org.apache.myfaces.tobago.util.TobagoCallback;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_LINK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ALIGN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CONVERTER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CREATE_SPAN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ESCAPE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HOVER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARKUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDERED_PARTIALLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE_EXTERN;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SORTABLE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.COMMAND_TYPE_NAVIGATE;
import static org.apache.myfaces.tobago.TobagoConstants.COMMAND_TYPE_RESET;
import static org.apache.myfaces.tobago.TobagoConstants.COMMAND_TYPE_SCRIPT;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_SELECT_BOOLEAN_CHECKBOX;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_SELECT_ONE_RADIO;

public class ComponentUtil {

  private static final Log LOG = LogFactory.getLog(ComponentUtil.class);

  private static final String RENDER_KEY_PREFIX
      = "org.apache.myfaces.tobago.component.ComponentUtil.RendererKeyPrefix_";

  public static final Class[] ACTION_ARGS = {};
  public static final Class[] ACTION_LISTENER_ARGS = {ActionEvent.class};
  public static final Class[] VALUE_CHANGE_LISTENER_ARGS = {ValueChangeEvent.class};
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};

  private ComponentUtil() {
  }


  public static boolean hasErrorMessages(FacesContext context) {
    for (Iterator iter = context.getMessages(); iter.hasNext();) {
      FacesMessage message = (FacesMessage) iter.next();
      if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
        return true;
      }
    }
    return false;
  }

  public static boolean containsPopupActionListener(UICommand command) {
    ActionListener[] actionListeners = command.getActionListeners();
    for (ActionListener actionListener : actionListeners) {
      if (actionListener instanceof PopupActionListener) {
        return true;
      }
    }
    return false;
  }

  public static String getFacesMessageAsString(FacesContext facesContext, UIComponent component) {
    Iterator messages = facesContext.getMessages(
        component.getClientId(facesContext));
    StringBuilder stringBuffer = new StringBuilder();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      stringBuffer.append(message.getDetail());
    }
    if (stringBuffer.length() > 0) {
      return stringBuffer.toString();
    } else {
      return null;
    }
  }

  public static boolean isInPopup(UIComponent component) {
    while (component != null) {
      if (component instanceof UIPopup) {
        return true;
      }
      component = component.getParent();
    }
    return false;
  }

  public static void resetPage(FacesContext context) {
    javax.faces.component.UIViewRoot view = context.getViewRoot();
    if (view != null) {
      view.getAttributes().remove(UIPage.COMPONENT_TYPE);
    }
  }

  @SuppressWarnings(value = "unchecked")
  public static UIPage findPage(FacesContext context, UIComponent component) {
    javax.faces.component.UIViewRoot view = context.getViewRoot();
    if (view != null) {
      TransientStateHolder stateHolder = (TransientStateHolder) view.getAttributes().get(UIPage.COMPONENT_TYPE);
      if (stateHolder == null || stateHolder.isEmpty()) {
        UIPage page = findPage(component);
        stateHolder = new TransientStateHolder(page);
        context.getViewRoot().getAttributes().put(UIPage.COMPONENT_TYPE, stateHolder);
      }
      return (UIPage) stateHolder.get();
    } else {
      return findPage(component);
    }
  }

  public static UIPage findPage(UIComponent component) {
    while (component != null) {
      if (component instanceof UIPage) {
        return (UIPage) component;
      }
      component = component.getParent();
    }
    return null;
  }

  public static void addStyles(UIComponent component, String[] styles) {
    UIPage uiPage = ComponentUtil.findPage(component);
    uiPage.getStyleFiles().addAll(Arrays.asList(styles));
  }

  public static void addScripts(UIComponent component, String[] scripts) {
    UIPage uiPage = ComponentUtil.findPage(component);
    uiPage.getScriptFiles().addAll(Arrays.asList(scripts));
  }

  public static void addOnloadCommands(UIComponent component, String[] cmds) {
    UIPage uiPage = ComponentUtil.findPage(component);
    uiPage.getOnloadScripts().addAll(Arrays.asList(cmds));
  }

  public static UIPage findPage(FacesContext facesContext) {
    return findPageBreadthFirst(facesContext.getViewRoot());
  }

  private static UIPage findPageBreadthFirst(UIComponent component) {
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      if (child instanceof UIPage) {
        return (UIPage) child;
      }
    }
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      UIPage result = findPageBreadthFirst(child);
      if (result != null) {
        return result;
      }
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

  @SuppressWarnings(value = "unchecked")
  private static void findSubForms(List<UIForm> collect, UIComponent component) {
    Iterator<UIComponent> kids = component.getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent child = kids.next();
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
    String forValue = (String) component.getAttributes().get(ATTR_FOR);
    if (forValue == null) {
      return component.getParent();
    }
    return component.findComponent(forValue);
  }

  public static boolean isInActiveForm(UIComponent component) {
    while (component != null) {
      //log.debug("compoent= " + component.getClientId(FacesContext.getCurrentInstance())
      // + " " + component.getRendererType());
      if (component instanceof UIForm) {
        UIForm form = (UIForm) component;
        if (form.isSubmitted()) {
          //log.debug("in active form = " + form.getClientId(FacesContext.getCurrentInstance()));
          return true;
        } /*else {
          log.debug("form found but not active = " + form.getClientId(FacesContext.getCurrentInstance()));
        } */
      }
      component = component.getParent();
    }
    //log.debug("not in an active form");
    return false;
  }

  public static boolean isError(javax.faces.component.UIInput uiInput) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return !uiInput.isValid()
        || facesContext.getMessages(uiInput.getClientId(facesContext)).hasNext();
  }

  public static boolean isError(UIComponent component) {
    if (component instanceof UIInput) {
      return isError((UIInput) component);
    }
    return false;
  }

  public static boolean isOutputOnly(UIComponent component) {
    return getBooleanAttribute(component, ATTR_DISABLED)
        || getBooleanAttribute(component, ATTR_READONLY);
  }

  public static boolean mayValidate(UIComponent component) {
    return !isOutputOnly(component)
        && ComponentUtil.isInActiveForm(component);
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
      return (Boolean) bool;
    } else if (bool instanceof String) {
      if (LOG.isInfoEnabled()) {
        LOG.info("Searching for a boolean, but find a String. Should not happen. "
            + "attribute: '" + name + "' id: '" + component.getClientId(FacesContext.getCurrentInstance())
            + "' comp: '" + component + "'");
      }
      return Boolean.valueOf((String) bool);
    } else {
      LOG.warn("Unknown type '" + bool.getClass().getName()
          + "' for boolean attribute: " + name + " id: " + component.getClientId(FacesContext.getCurrentInstance())
          + " comp: " + component);
      return false;
    }
  }

  public static void setRenderedPartially(org.apache.myfaces.tobago.component.UICommand command,
      String renderers) {
    if (renderers != null) {
      if (UIComponentTag.isValueReference(renderers)) {
        command.setValueBinding(ATTR_RENDERED_PARTIALLY, createValueBinding(renderers));
      } else {
        String[] components = StringUtils.split(renderers, ",");
        command.setRenderedPartially(components);
      }
    }
  }

  public static void setStyleClasses(UIComponent component, String styleClasses) {
    if (styleClasses != null) {
      if (UIComponentTag.isValueReference(styleClasses)) {
        component.setValueBinding(ATTR_STYLE_CLASS, createValueBinding(styleClasses));
      } else {
        String[] classes = StringUtils.split(styleClasses, ", ");
        if (classes.length > 0) {
          StyleClasses styles = StyleClasses.ensureStyleClasses(component);
          for (String clazz : classes) {
            styles.addFullQualifiedClass(clazz);
          }
        }
      }
    }
  }

  public static void setMarkup(UIComponent markupComponent, String markup) {
    if (markup != null) {
      if (markupComponent instanceof SupportsMarkup) {
        if (UIComponentTag.isValueReference(markup)) {
          markupComponent.setValueBinding(ATTR_MARKUP, createValueBinding(markup));
        } else {
          String[] markups = StringUtils.split(markup, ",");
          ((SupportsMarkup) markupComponent).setMarkup(markups);
        }
      } else {
        LOG.error("Component did not support Markup " + markupComponent.getClass().getName());
      }
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
      LOG.warn("Unknown type '" + integer.getClass().getName()
          + "' for integer attribute: " + name + " comp: " + component);
      return defaultValue;
    }
  }

  /**
   * @param component
   * @param name
   * @deprecated please use the  method {@link #getCharacterAttribute(javax.faces.component.UIComponent, String)}
   */
  @Deprecated
  public static Character getCharakterAttribute(UIComponent component, String name) {
    return getCharacterAttribute(component, name);
  }

  public static Character getCharacterAttribute(UIComponent component, String name) {
    Object character = component.getAttributes().get(name);
    if (character == null) {
      return null;
    } else if (character instanceof Character) {
      return ((Character) character);
    } else if (character instanceof String) {
      String asString = ((String) character);
      return asString.length() > 0 ? asString.charAt(0) : null;
    } else {
      LOG.warn("Unknown type '" + character.getClass().getName()
          + "' for integer attribute: " + name + " comp: " + component);
      return null;
    }
  }

  public static boolean isFacetOf(UIComponent component, UIComponent parent) {
    for (Object o : parent.getFacets().keySet()) {
      UIComponent facet = parent.getFacet((String) o);
      if (component.equals(facet)) {
        return true;
      }
    }
    return false;
  }

  // TODO This should not be neseccary, but UIComponentBase.getRenderer() is protected
  public static LayoutableRendererBase getRenderer(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext, component.getFamily(), component.getRendererType());

  }

  public static LayoutableRendererBase getRenderer(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    LayoutableRendererBase renderer;

    Map requestMap = facesContext.getExternalContext().getRequestMap();
    renderer = (LayoutableRendererBase) requestMap.get(RENDER_KEY_PREFIX + rendererType);

    if (renderer == null) {
      RenderKitFactory rkFactory = (RenderKitFactory)
          FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
      RenderKit renderKit = rkFactory.getRenderKit(facesContext, facesContext.getViewRoot().getRenderKitId());
      Renderer myRenderer = renderKit.getRenderer(family, rendererType);
      if (myRenderer instanceof LayoutableRendererBase) {
        requestMap.put(RENDER_KEY_PREFIX + rendererType, myRenderer);
        renderer = (LayoutableRendererBase) myRenderer;
      } else {
        return null;
      }
    }
    return renderer;
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
            list.add(new org.apache.myfaces.tobago.model.SelectItem(
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

  public static Object findParameter(UIComponent component, String name) {
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
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
    StringBuilder result = new StringBuilder();
    if (component == null) {
      result.append("null");
    } else {
      result.append('\n');
      if (!asFacet) {
        result.append(spaces(offset));
        result.append(toString(component));
      }
      Map facets = component.getFacets();
      if (facets.size() > 0) {
        for (Map.Entry<String, UIComponent> entry : (Set<Map.Entry<String, UIComponent>>) facets.entrySet()) {
          UIComponent facet = entry.getValue();
          result.append('\n');
          result.append(spaces(offset + 1));
          result.append('\"');
          result.append(entry.getKey());
          result.append("\" = ");
          result.append(toString(facet));
          result.append(toString(facet, offset + 1, true));
        }
      }
      for (Object o : component.getChildren()) {
        result.append(toString((UIComponent) o, offset + 1, false));
      }
    }
    return result.toString();
  }

  private static String toString(UIComponent component) {
    StringBuilder buf = new StringBuilder(component.getClass().getName());
    buf.append('@');
    buf.append(Integer.toHexString(component.hashCode()));
    buf.append(" ");
    buf.append(component.getRendererType());
    buf.append(" ");
    if (component instanceof javax.faces.component.UIViewRoot) {
      buf.append(((javax.faces.component.UIViewRoot) component).getViewId());
    } else {
      buf.append(component.getId());
      buf.append(" ");
      buf.append(component.getClientId(FacesContext.getCurrentInstance()));
    }
    return buf.toString();
  }

  private static String spaces(int n) {
    StringBuilder buffer = new StringBuilder();
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
      if ((uiComponent instanceof UIOutput)
          && !(uiComponent instanceof UIGraphic)) {
        output = (UIOutput) uiComponent;
        break;
      }
    }
    return output;
  }

  public static void setIntegerSizeProperty(UIComponent component,
      String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        value = removePx(value);
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  public static String removePx(String value) {
    if (value != null && value.endsWith("px")) {
      value = value.substring(0, value.length() - 2);
    }
    return value;
  }

  public static void setIntegerProperty(UIComponent component,
      String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  public static void setBooleanProperty(UIComponent component,
      String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, Boolean.valueOf(value));
      }
    }
  }

  public static void setStringProperty(UIComponent component, String name,
      String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  public static void setValueForValueBinding(String name, Object value) {
    FacesContext context = FacesContext.getCurrentInstance();
    ValueBinding valueBinding = context.getApplication().createValueBinding(name);
    valueBinding.setValue(context, value);
  }

  public static ValueBinding createValueBinding(String value) {
    return FacesContext.getCurrentInstance().getApplication()
        .createValueBinding(value);
  }

  public static String getValueFromEl(String script) {
    if (UIComponentTag.isValueReference(script)) {
      ValueBinding valueBinding = ComponentUtil.createValueBinding(script);
      script = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
    }
    return script;
  }

  /**
   * Please use createComponent(String componentType, String rendererType, String id)
   *
   * @deprecated
   */
  @Deprecated
  public static UIComponent createComponent(String componentType, String rendererType) {
    return createComponent(componentType, rendererType,  null);
  }

  public static UIComponent createComponent(String componentType, String rendererType, String id) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType, id);
  }

  /**
   * Please use createComponent(FacesContext facesContext, String componentType, String rendererType, String id)
   *
   * @deprecated
   */
  @Deprecated
  public static UIComponent createComponent(FacesContext facesContext, String componentType, String rendererType) {
    return createComponent(facesContext, componentType, rendererType, null);
  }

  public static UIComponent createComponent(
      FacesContext facesContext, String componentType, String rendererType, String id) {
    UIComponent component
        = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    component.setId(id);
    return component;
  }

  /**
  * Please use createTextColumn(String label, String sortable, String align, String value, String id)
  *
  * @deprecated
  */
 @Deprecated
  public static UIColumn createTextColumn(String label, String sortable, String align, String value) {
    return createTextColumn(label, sortable, align, value, null);
  }

  public static UIColumn createTextColumn(String label, String sortable, String align, String value, String id) {
    UIComponent text = createComponent(UIOutput.COMPONENT_TYPE, RENDERER_TYPE_OUT, id + "_t");
    setStringProperty(text, ATTR_VALUE, value);
    setBooleanProperty(text, ATTR_CREATE_SPAN, "false");
    setBooleanProperty(text, ATTR_ESCAPE, "false");
    return createColumn(label, sortable, align, text, id);
  }

  /**
  * Please use createColumn(String label, String sortable, String align, UIComponent child)
  *
  * @deprecated
  */
 @Deprecated
  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child) {
    return createColumn(label, sortable, align, child, null);
  }

  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child, String id) {
    UIColumn column = createColumn(label, sortable, align, id);
    column.getChildren().add(child);
    return column;
  }

  private static UIColumn createColumn(String label, String sortable, String align, String id) {
    UIColumn column = (UIColumn) createComponent(UIColumn.COMPONENT_TYPE, null, id);
    setStringProperty(column, ATTR_LABEL, label);
    setBooleanProperty(column, ATTR_SORTABLE, sortable);
    setStringProperty(column, ATTR_ALIGN, align);
    return column;
  }

  /**
  * Please use createUIMenuSelectOneFacet(FacesContext facesContext, UICommand command, String id)
  *
  * @deprecated
  */
 @Deprecated
  public static UIMenuSelectOne createUIMenuSelectOneFacet(FacesContext facesContext, UICommand command) {
    return createUIMenuSelectOneFacet(facesContext, command, null);
  }

  public static UIMenuSelectOne createUIMenuSelectOneFacet(FacesContext facesContext, UICommand command, String id) {
    UIMenuSelectOne radio = null;
    final ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
    if (valueBinding != null) {
      radio = (UIMenuSelectOne) createComponent(facesContext,
          UIMenuSelectOne.COMPONENT_TYPE, RENDERER_TYPE_SELECT_ONE_RADIO, id);
      command.getFacets().put(FACET_ITEMS, radio);
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

  /**
  * Please use createUISelectBooleanFacet(FacesContext facesContext, UICommand command, String id)
  *
  * @deprecated
  */
 @Deprecated
  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command) {
    return createUISelectBooleanFacet(facesContext, command, null);
  }

  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command, String id) {
    UIComponent checkbox
        = createComponent(facesContext, UISelectBoolean.COMPONENT_TYPE, RENDERER_TYPE_SELECT_BOOLEAN_CHECKBOX, id);
    command.getFacets().put(FACET_ITEMS, checkbox);
    ValueBinding valueBinding = command.getValueBinding(ATTR_VALUE);
    if (valueBinding != null) {
      checkbox.setValueBinding(ATTR_VALUE, valueBinding);
    } else {
      checkbox.getAttributes().put(ATTR_VALUE, command.getAttributes().get(ATTR_VALUE));
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
    //String id = component.getId();
    String id = getComponentId(facesContext, component);
    return id + "_picker" + postfix;
  }

  public static String getComponentId(FacesContext facesContext, UIComponent component) {
    String id = component.getId();
    //if (id == null) {
    // XXX What is this?
    //  id = component.getClientId(facesContext).substring(id.lastIndexOf('_'));
    //}
    return id;
  }

  public static UIComponent provideLabel(FacesContext facesContext, UIComponent component) {
    UIComponent label = component.getFacet(FACET_LABEL);


    if (label == null) {
      final Map attributes = component.getAttributes();
      Object labelText = component.getValueBinding(ATTR_LABEL);
      if (labelText == null) {
        labelText = attributes.get(ATTR_LABEL);
      }

      if (labelText != null) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        label = application.createComponent(UIOutput.COMPONENT_TYPE);
        label.setRendererType(RENDERER_TYPE_LABEL);
        String idprefix = ComponentUtil.getComponentId(facesContext, component);
        label.setId(idprefix + "_" + FACET_LABEL);
        label.setRendered(true);

        if (labelText instanceof ValueBinding) {
          label.setValueBinding(ATTR_VALUE, (ValueBinding) labelText);
        } else {
          label.getAttributes().put(ATTR_VALUE, labelText);
        }

        component.getFacets().put(FACET_LABEL, label);
      }
    }
    return label;
  }

  /*public static void debug(UIComponent component) {
      LOG.error("###############################");
      LOG.error("ID " + component.getId());
      LOG.error("ClassName " + component.getClass().getName());
      if (component instanceof EditableValueHolder) {
        EditableValueHolder editableValueHolder = (EditableValueHolder) component;
        LOG.error("Valid " + editableValueHolder.isValid());
        LOG.error("SubmittedValue " + editableValueHolder.getSubmittedValue());
      }
    for (Iterator it = component.getFacetsAndChildren(); it.hasNext(); ) {
      debug((UIComponent)it.next());
    }
  } */


  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectOne component) {
    return getItems(component);
  }

  public static List<SelectItem> getItemsToRender(javax.faces.component.UISelectMany component) {
    return getItems(component);
  }

  private static List<SelectItem> getItems(javax.faces.component.UIInput component) {

    List<SelectItem> selectItems = ComponentUtil.getSelectItems(component);

    String renderRange = (String)
        component.getAttributes().get(ATTR_RENDER_RANGE_EXTERN);
    if (renderRange == null) {
      renderRange = (String)
          component.getAttributes().get(ATTR_RENDER_RANGE);
    }
    if (renderRange == null) {
      return selectItems;
    }

    int[] indices = RangeParser.getIndices(renderRange);
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

  public static void setValidator(EditableValueHolder editableValueHolder, String validator) {
    if (validator != null && editableValueHolder.getValidator() == null) {
      if (UIComponentTag.isValueReference(validator)) {
        MethodBinding methodBinding =
            FacesContext.getCurrentInstance().getApplication().createMethodBinding(validator, VALIDATOR_ARGS);
        editableValueHolder.setValidator(methodBinding);
      }
    }
  }

  /**
   * @param component
   * @param converterId
   * @deprecated please use the typesave method {@link #setConverter(javax.faces.component.ValueHolder, String)}
   */
  @Deprecated
  public static void setConverter(UIComponent component, String converterId) {
    if (component instanceof ValueHolder) {
      setConverter((ValueHolder) component, converterId);
    }
  }

  public static void setConverter(ValueHolder valueHolder, String converterId) {
    if (converterId != null && valueHolder.getConverter() == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final Application application = facesContext.getApplication();
      if (UIComponentTag.isValueReference(converterId)) {
        ValueBinding valueBinding = application.createValueBinding(converterId);
        if (valueHolder instanceof UIComponent) {
          ((UIComponent) valueHolder).setValueBinding(ATTR_CONVERTER, valueBinding);
        }
      } else {
        Converter converter = application.createConverter(converterId);
        valueHolder.setConverter(converter);
      }
    }
  }

  /**
   * @param component
   * @param type
   * @param action
   * @deprecated please use the typesave method {@link #setAction(javax.faces.component.UICommand, String, String)}
   */
  @Deprecated
  public static void setAction(UIComponent component, String type, String action) {
    if (component instanceof UICommand) {
      setAction((UICommand) component, type, action);
    }
  }

  public static void setAction(UICommand component, String type, String action) {
    String commandType;
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    if (type != null && UIComponentTag.isValueReference(type)) {
      commandType = (String) application.createValueBinding(type).getValue(facesContext);
    } else {
      commandType = type;
    }
    if (commandType != null
        && (commandType.equals(COMMAND_TYPE_NAVIGATE)
        || commandType.equals(COMMAND_TYPE_RESET)
        || commandType.equals(COMMAND_TYPE_SCRIPT))) {
      if (commandType.equals(COMMAND_TYPE_NAVIGATE)) {
        setStringProperty(component, ATTR_ACTION_LINK, action);
      } else if (commandType.equals(COMMAND_TYPE_SCRIPT)) {
        setStringProperty(component, ATTR_ACTION_ONCLICK, action);
      } else {
        LOG.warn("Type reset is not supported");
      }
    } else {
      if (action != null) {
        if (UIComponentTag.isValueReference(action)) {
          MethodBinding binding = application.createMethodBinding(action, null);
          component.setAction(binding);
        } else {
          component.setAction(new ConstantMethodBinding(action));
        }
      }
    }

  }

  /**
   * @param component
   * @param suggestMethod
   * @deprecated please use the typesave method {@link #setSuggestMethodBinding(UIInput, String)}
   */
  @Deprecated
  public static void setSuggestMethodBinding(UIComponent component, String suggestMethod) {
    if (component instanceof UIInput) {
      setSuggestMethodBinding((UIInput) component, suggestMethod);
    }
  }

  public static void setSuggestMethodBinding(UIInput component, String suggestMethod) {
    if (suggestMethod != null) {
      if (UIComponentTag.isValueReference(suggestMethod)) {
        final MethodBinding methodBinding = FacesContext.getCurrentInstance().getApplication()
            .createMethodBinding(suggestMethod, new Class[]{String.class});
        component.setSuggestMethod(methodBinding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (suggestMethod): " + suggestMethod);
      }
    }
  }

  public static void setActionListener(ActionSource command, String actionListener) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    if (actionListener != null) {
      if (UIComponentTag.isValueReference(actionListener)) {
        MethodBinding binding
            = application.createMethodBinding(actionListener, ACTION_LISTENER_ARGS);
        command.setActionListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + actionListener);
      }
    }
  }

  public static void setValueChangeListener(EditableValueHolder valueHolder, String valueChangeListener) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    if (valueChangeListener != null) {
      if (UIComponentTag.isValueReference(valueChangeListener)) {
        MethodBinding binding
            = application.createMethodBinding(valueChangeListener, VALUE_CHANGE_LISTENER_ARGS);
        valueHolder.setValueChangeListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (valueChangeListener): " + valueChangeListener);
      }
    }
  }


  public static void setSortActionListener(UIData data, String actionListener) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    if (actionListener != null) {
      if (UIComponentTag.isValueReference(actionListener)) {
        MethodBinding binding = application.createMethodBinding(
            actionListener, ACTION_LISTENER_ARGS);
        data.setSortActionListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (sortActionListener): " + actionListener);
      }
    }
  }

  public static void setValueBinding(UIComponent component, String name, String state) {
    // TODO: check, if it is an writeable object
    if (state != null && UIComponentTag.isValueReference(state)) {
      ValueBinding valueBinding = createValueBinding(state);
      component.setValueBinding(name, valueBinding);
    }
  }

  public static void setStateChangeListener(UIData data, String stateChangeListener) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();

    if (stateChangeListener != null) {
      if (UIComponentTag.isValueReference(stateChangeListener)) {
        Class[] arguments = {SheetStateChangeEvent.class};
        MethodBinding binding
            = application.createMethodBinding(stateChangeListener, arguments);
        data.setStateChangeListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + stateChangeListener);
      }
    }
  }


  public static String[] getMarkupBinding(FacesContext facesContext, SupportsMarkup component) {
    ValueBinding vb = ((UIComponent) component).getValueBinding(ATTR_MARKUP);
    if (vb != null) {
      Object markups = vb.getValue(facesContext);
      if (markups instanceof String[]) {
        return (String[]) markups;
      } else if (markups instanceof String) {
        String[] strings = StringUtils.split((String) markups, ", ");
        List<String> result = new ArrayList<String>(strings.length);
        for (String string : strings) {
          if (string.trim().length() != 0) {
            result.add(string.trim());
          }
        }
        return result.toArray(new String[result.size()]);
      } else if (markups == null) {
        return new String[0];
      } else {
        return new String[]{markups.toString()};
      }
    }

    return new String[0];
  }

  /**
   * colonCount == 0: fully relative
   * colonCount == 1: absolute (still normal findComponent syntax)
   * colonCount > 1: for each extra colon after 1, go up a naming container
   * (to the view root, if naming containers run out)
   */

  public static UIComponent findComponent(UIComponent from, String relativeId) {
    int idLength = relativeId.length();
    // Figure out how many colons
    int colonCount = 0;
    while (colonCount < idLength) {
      if (relativeId.charAt(colonCount) != NamingContainer.SEPARATOR_CHAR) {
        break;
      }
      colonCount++;
    }

    // colonCount == 0: fully relative
    // colonCount == 1: absolute (still normal findComponent syntax)
    // colonCount > 1: for each extra colon after 1, go up a naming container
    // (to the view root, if naming containers run out)
    if (colonCount > 1) {
      relativeId = relativeId.substring(colonCount);
      for (int j = 1; j < colonCount; j++) {
        while (from.getParent() != null) {
          from = from.getParent();
          if (from instanceof NamingContainer) {
            break;
          }
        }
      }
    }
    return from.findComponent(relativeId);
  }

  public static void invokeOnComponent(FacesContext facesContext, String clientId, UIComponent component,
      Callback callback) {
    List<UIComponent> list = new ArrayList<UIComponent>();
    while (component != null) {
      list.add(component);
      component = component.getParent();
    }
    Collections.reverse(list);
    invokeOrPrepare(facesContext, list, clientId, callback);
    facesContext.getExternalContext().getRequestMap().remove(TobagoConstants.ATTR_ZINDEX);
  }

  private static void invokeOrPrepare(FacesContext facesContext, List<UIComponent> list, String clientId,
      Callback callback) {
    if (list.size() == 1) {
      callback.execute(facesContext, list.get(0));
    } else if (list.get(0) instanceof UIData) {
      prepareOnUIData(facesContext, list, clientId, callback);
    } else if (list.get(0) instanceof UIForm) {
      prepareOnUIForm(facesContext, list, clientId, callback);
    } else if (list.get(0) instanceof UIPopup) {
      prepareOnUIPopup(facesContext, list, clientId, callback);
    } else {
      prepareOnUIComponent(facesContext, list, clientId, callback);
    }
  }

  @SuppressWarnings(value = "unchecked")
  private static void prepareOnUIForm(FacesContext facesContext, List<UIComponent> list, String clientId,
      Callback callback) {
    UIComponent currentComponent = list.remove(0);
    if (!(currentComponent instanceof UIForm)) {
      throw new IllegalStateException(currentComponent.getClass().getName());
    }
    // TODO is this needed?
    if (callback instanceof TobagoCallback) {
      if (PhaseId.APPLY_REQUEST_VALUES.equals(((TobagoCallback) callback).getPhaseId())) {
        currentComponent.decode(facesContext);
      }
    }
    UIForm uiForm = (UIForm) currentComponent;
    facesContext.getExternalContext().getRequestMap().put(UIForm.SUBMITTED_MARKER, uiForm.isSubmitted());
    invokeOrPrepare(facesContext, list, clientId, callback);

  }

  private static void prepareOnUIComponent(
      FacesContext facesContext, List<UIComponent> list, String clientId, Callback callback) {
    list.remove(0);
    invokeOrPrepare(facesContext, list, clientId, callback);
  }

  private static void prepareOnUIPopup(
      FacesContext facesContext, List<UIComponent> list, String clientId, Callback callback) {
    if (callback instanceof TobagoCallback
        && PhaseId.RENDER_RESPONSE.equals(((TobagoCallback) callback).getPhaseId())) {
      Integer zIndex = (Integer) facesContext.getExternalContext().getRequestMap().get(TobagoConstants.ATTR_ZINDEX);
      if (zIndex == null) {
        zIndex = 0;
      } else {
        zIndex += 10;
      }
      facesContext.getExternalContext().getRequestMap().put(TobagoConstants.ATTR_ZINDEX, zIndex);
    }
    list.remove(0);
    invokeOrPrepare(facesContext, list, clientId, callback);
  }

  private static void prepareOnUIData(FacesContext facesContext, List<UIComponent> list, String clientId,
      Callback callback) {
    UIComponent currentComponent = list.remove(0);
    if (!(currentComponent instanceof UIData)) {
      throw new IllegalStateException(currentComponent.getClass().getName());
    }

    // we may need setRowIndex on UIData
    javax.faces.component.UIData uiData = (javax.faces.component.UIData) currentComponent;
    int oldRowIndex = uiData.getRowIndex();
    String sheetId = uiData.getClientId(facesContext);
    String idRemainder = clientId.substring(sheetId.length());
    if (LOG.isInfoEnabled()) {
      LOG.info("idRemainder = \"" + idRemainder + "\"");
    }
    if (idRemainder.startsWith(String.valueOf(NamingContainer.SEPARATOR_CHAR))) {
      idRemainder = idRemainder.substring(1);
      int idx = idRemainder.indexOf(NamingContainer.SEPARATOR_CHAR);
      if (idx > 0) {
        String firstPart = idRemainder.substring(0, idx);
        if (NumberUtils.isDigits(firstPart)) {
          try {
            int rowIndex = Integer.parseInt(firstPart);
            if (LOG.isInfoEnabled()) {
              LOG.info("set rowIndex = \"" + rowIndex + "\"");
            }
            uiData.setRowIndex(rowIndex);
          } catch (NumberFormatException e) {
            LOG.error("idRemainder = \"" + idRemainder + "\"", e);
          }
        }
      }
    } else {
      if (LOG.isInfoEnabled()) {
        LOG.info("no match for \"^:\\d+:.*\"");
      }
    }

    invokeOrPrepare(facesContext, list, clientId, callback);

    // we should reset rowIndex on UIData
    uiData.setRowIndex(oldRowIndex);
  }
}
