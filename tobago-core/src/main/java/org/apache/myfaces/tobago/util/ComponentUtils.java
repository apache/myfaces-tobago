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

package org.apache.myfaces.tobago.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.TransientStateHolder;
import org.apache.myfaces.tobago.el.ConstantMethodBinding;
import org.apache.myfaces.tobago.event.AbstractPopupActionListener;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.util.FindComponentUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectMany;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComponentUtils {

  private static final Logger LOG = LoggerFactory.getLogger(ComponentUtils.class);

  public static final String SUB_SEPARATOR = "::";
  
  private static final String RENDER_KEY_PREFIX
      = "org.apache.myfaces.tobago.util.ComponentUtils.RendererKeyPrefix_";

  private static final String PAGE_KEY = "org.apache.myfaces.tobago.Page.Key";

  public static final Class[] ACTION_ARGS = {};
  public static final Class[] ACTION_LISTENER_ARGS = {ActionEvent.class};
  public static final Class[] VALUE_CHANGE_LISTENER_ARGS = {ValueChangeEvent.class};
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};
  public static final String LIST_SEPARATOR_CHARS = ", ";

  /**
   * PRELIMINARY - SUBJECT TO CHANGE
   *
   * Name of the map for data attributes in components. New in JSF 2.2.
   * @since 1.6.0
   */
  public static final String DATA_ATTRIBUTES_KEY = "javax.faces.component.DATA_ATTRIBUTES_KEY";

  private ComponentUtils() {
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
      if (actionListener instanceof AbstractPopupActionListener) {
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
      if (component instanceof AbstractUIPopup) {
        return true;
      }
      component = component.getParent();
    }
    return false;
  }

  public static void resetPage(FacesContext context) {
    javax.faces.component.UIViewRoot view = context.getViewRoot();
    if (view != null) {
      view.getAttributes().remove(PAGE_KEY);
    }
  }

  @SuppressWarnings("unchecked")
  public static AbstractUIPage findPage(FacesContext context, UIComponent component) {
    javax.faces.component.UIViewRoot view = context.getViewRoot();
    if (view != null) {
      TransientStateHolder stateHolder = (TransientStateHolder) view.getAttributes().get(PAGE_KEY);
      if (stateHolder == null || stateHolder.isEmpty()) {
        AbstractUIPage page = findPage(component);
        stateHolder = new TransientStateHolder(page);
        context.getViewRoot().getAttributes().put(PAGE_KEY, stateHolder);
      }
      return (AbstractUIPage) stateHolder.get();
    } else {
      return findPage(component);
    }
  }

  public static AbstractUIPage findPage(UIComponent component) {
    while (component != null) {
      if (component instanceof AbstractUIPage) {
        return (AbstractUIPage) component;
      }
      component = component.getParent();
    }
    return null;
  }

  public static AbstractUIPage findPage(FacesContext facesContext) {
    return findPageBreadthFirst(facesContext.getViewRoot());
  }

  private static AbstractUIPage findPageBreadthFirst(UIComponent component) {
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      if (child instanceof AbstractUIPage) {
        return (AbstractUIPage) child;
      }
    }
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      AbstractUIPage result = findPageBreadthFirst(child);
      if (result != null) {
        return result;
      }
    }
    return null;
  }


  public static AbstractUIForm findForm(UIComponent component) {
    while (component != null) {
      if (component instanceof AbstractUIForm) {
        return (AbstractUIForm) component;
      }
      component = component.getParent();
    }
    return null;
  }

  public static <T> T findAncestor(UIComponent component, Class<T> type) {

    while (component != null) {
      if (type.isAssignableFrom(component.getClass())) {
        return (T) component;
      }
      component = component.getParent();
    }
    return null;
  }

  /**
   * Find all sub forms of a component, and collects it.
   * It does not find sub forms of sub forms.
   */
  public static List<AbstractUIForm> findSubForms(UIComponent component) {
    List<AbstractUIForm> collect = new ArrayList<AbstractUIForm>();
    findSubForms(collect, component);
    return collect;
  }

  @SuppressWarnings("unchecked")
  private static void findSubForms(List<AbstractUIForm> collect, UIComponent component) {
    Iterator<UIComponent> kids = component.getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent child = kids.next();
      if (child instanceof AbstractUIForm) {
        collect.add((AbstractUIForm) child);
      } else {
        findSubForms(collect, child);
      }
    }
  }

  /**
   * Searches the component tree beneath the component and return the first component matching the type.
   */
  public static <T extends UIComponent> T findDescendant(UIComponent component, Class<T> type) {

    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (type.isAssignableFrom(child.getClass())) {
        return (T) child;
      }
      final T descendant = findDescendant(child, type);
      if (descendant != null) {
        return descendant;
      }
    }
    return null;
  }

  /**
   * Looks for the attribute "for" in the component. If there is any
   * search for the component which is referenced by the "for" attribute,
   * and return their clientId.
   * If there is no "for" attribute, return the "clientId" of the parent
   * (if it has a parent). This is useful for labels.
   */
  public static String findClientIdFor(UIComponent component, FacesContext facesContext) {
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
    String forValue = (String) component.getAttributes().get(Attributes.FOR);
    if (forValue == null) {
      return component.getParent();
    }
    return ComponentUtils.findComponent(component, forValue);
  }

  /**
   * Looks for the attribute "for" of the component.
   * In case that the value is equals to "@auto" the children of the parent will be
   * checked if they are a UIInput. The "id" of the first one will be used to reset the "for"
   * attribute of the component.
   * @deprecated
   */
  @Deprecated
  public static void evaluateAutoFor(UIComponent component) {
    String forComponent = (String) component.getAttributes().get(Attributes.FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("for = '" + forComponent + "'");
    }
    if ("@auto".equals(forComponent)) {
      for (Object object : component.getParent().getChildren()) {
        if (setForToInput(component, (UIComponent) object, AbstractUIInput.class, false)) {
          break;
        }
      }
    }
  }

  /**
   * Looks for the attribute "for" of the component.
   * In case that the value is equals to "@auto" the children of the parent will be
   * checked if they are of the type of the parameter clazz. The "id" of the first one will be used to reset the "for"
   * attribute of the component.
   */
  public static void evaluateAutoFor(UIComponent component, Class<? extends UIComponent> clazz) {
    String forComponent = (String) component.getAttributes().get(Attributes.FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("for = '" + forComponent + "'");
    }
    if ("@auto".equals(forComponent)) {
      // parent
      for (Object object : component.getParent().getChildren()) {
        if (setForToInput(component, (UIComponent) object, clazz, component instanceof NamingContainer)) {
          return;
        }
      }
      // grand parent
      for (Object object : component.getParent().getParent().getChildren()) {
        if (setForToInput(component, (UIComponent) object, clazz, component.getParent() instanceof NamingContainer)) {
          return;
        }
      }
    }
  }

  private static boolean setForToInput(
      UIComponent component, UIComponent child, Class<? extends UIComponent> clazz, boolean namingContainer) {
    if (clazz.isAssignableFrom(child.getClass())) { // find the matching component
      final String forComponent;
      if (namingContainer) {
        forComponent = ":::" + child.getId();
      } else {
        forComponent = child.getId();
      }
      component.getAttributes().put(Attributes.FOR, forComponent);
      return true;
    }
    return false;
  }

  public static boolean isInActiveForm(UIComponent component) {
    while (component != null) {
      if (component instanceof AbstractUIForm) {
        AbstractUIForm form = (AbstractUIForm) component;
        if (form.isSubmitted()) {
          return true;
        }
      }
      component = component.getParent();
    }
    return false;
  }

  public static FacesMessage.Severity getMaximumSeverity(UIComponent component) {
    final boolean invalid = component instanceof javax.faces.component.UIInput
        && !((javax.faces.component.UIInput) component).isValid();
    FacesMessage.Severity max = invalid ? FacesMessage.SEVERITY_ERROR : null;
    FacesContext facesContext = FacesContext.getCurrentInstance();
    final Iterator messages = facesContext.getMessages(component.getClientId(facesContext));
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      if (max == null || message.getSeverity().getOrdinal() > max.getOrdinal()) {
        max = message.getSeverity();
      }
    }
    return max;
  }

  public static boolean isError(javax.faces.component.UIInput uiInput) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    return !uiInput.isValid()
        || facesContext.getMessages(uiInput.getClientId(facesContext)).hasNext();
  }

  public static boolean isError(UIComponent component) {
    if (component instanceof AbstractUIInput) {
      return isError((AbstractUIInput) component);
    }
    return false;
  }

  public static boolean isOutputOnly(UIComponent component) {
    return getBooleanAttribute(component, Attributes.DISABLED)
        || getBooleanAttribute(component, Attributes.READONLY);
  }

  public static boolean mayValidate(UIComponent component) {
    return !isOutputOnly(component)
        && ComponentUtils.isInActiveForm(component);
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
      LOG.warn("Searching for a boolean, but find a String. Should not happen. "
          + "attribute: '" + name + "' id: '" + component.getClientId(FacesContext.getCurrentInstance())
          + "' comp: '" + component + "'");
      return Boolean.valueOf((String) bool);
    } else {
      LOG.warn("Unknown type '" + bool.getClass().getName()
          + "' for boolean attribute: " + name + " id: " + component.getClientId(FacesContext.getCurrentInstance())
          + " comp: " + component);
      return false;
    }
  }



  public static ValueBinding createValueBinding(String value) {
    return FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
  }

  /**
   * @deprecated Please define a {@link Markup} and set it to the component with
   * {@link SupportsMarkup#setMarkup(Markup markup)} before the rendering phase.
   */
  @Deprecated
  public static void setStyleClasses(UIComponent component, String styleClasses) {
    if (styleClasses != null) {
      if (UIComponentTag.isValueReference(styleClasses)) {
        component.setValueBinding(Attributes.STYLE_CLASS, createValueBinding(styleClasses));
      } else {
        String[] classes = splitList(styleClasses);
        if (classes.length > 0) {
          StyleClasses styles = StyleClasses.ensureStyleClasses(component);
          for (String clazz : classes) {
            styles.addFullQualifiedClass(clazz);
          }
        }
      }
    }
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public static void setMarkup(UIComponent markupComponent, String markup) {
    if (markup != null) {
      if (markupComponent instanceof SupportsMarkup) {
        if (UIComponentTag.isValueReference(markup)) {
          markupComponent.setValueBinding(Attributes.MARKUP, createValueBinding(markup));
        } else {
          ((SupportsMarkup) markupComponent).setMarkup(Markup.valueOf(markup));
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

  public static Object getObjectAttribute(UIComponent component, String name) {
    return getAttribute(component, name);
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

  public static RendererBase getRenderer(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext, component.getFamily(), component.getRendererType());
  }

  public static RendererBase getRenderer(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    Map<String, Object> requestMap = (Map<String, Object>) facesContext.getExternalContext().getRequestMap();
    StringBuilder key = new StringBuilder(RENDER_KEY_PREFIX);
    key.append(rendererType);
    RendererBase renderer = (RendererBase) requestMap.get(key.toString());

    if (renderer == null) {
      Renderer myRenderer = getRendererInternal(facesContext, family, rendererType);
      if (myRenderer instanceof RendererBase) {
        requestMap.put(key.toString(), myRenderer);
        renderer = (RendererBase) myRenderer;
      } else {
        return null;
      }
    }
    return renderer;
  }


  private static Renderer getRendererInternal(FacesContext facesContext, String family, String rendererType) {
    RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    RenderKit renderKit = rkFactory.getRenderKit(facesContext, facesContext.getViewRoot().getRenderKitId());
    Renderer myRenderer = renderKit.getRenderer(family, rendererType);
    return myRenderer;
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
    return ComponentUtils.getBooleanAttribute(component, Attributes.HOVER);
  }

  public static UIOutput getFirstNonGraphicChild(UIComponent component) {
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof UIOutput) {
        return (UIOutput) child;
      }
    }
    return null;
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public static void setIntegerSizeProperty(UIComponent component, String name, String value) {
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

  public static void setValueForValueBinding(String name, Object value) {
    FacesContext context = FacesContext.getCurrentInstance();
    ValueBinding valueBinding = context.getApplication().createValueBinding(name);
    valueBinding.setValue(context, value);
  }


  public static boolean hasSelectedValue(List<SelectItem> items, Object value) {
    for (SelectItem item : items) {
      if (item.getValue().equals(value)) {
        return true;
      }
    }
    return false;
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

  /**
   * Checks if the Component has a label facet and if not creates one with the label attribute.
   *
   * Todo: check if this method should be set to deprecated. 
   */
  public static UIComponent provideLabel(FacesContext facesContext, UIComponent component) {
    UIComponent label = component.getFacet(Facets.LABEL);


    if (label == null) {
      final Map attributes = component.getAttributes();
      Object labelText = component.getValueBinding(Attributes.LABEL);
      if (labelText == null) {
        labelText = attributes.get(Attributes.LABEL);
      }

      if (labelText != null) {
        Application application = FacesContext.getCurrentInstance().getApplication();
        label = application.createComponent(UIOutput.COMPONENT_TYPE);
        label.setRendererType(RendererTypes.LABEL);
        String idprefix = ComponentUtils.getComponentId(facesContext, component);
        label.setId(idprefix + "_" + Facets.LABEL);
        label.setRendered(true);

        if (labelText instanceof ValueBinding) {
          label.setValueBinding(Attributes.VALUE, (ValueBinding) labelText);
        } else {
          label.getAttributes().put(Attributes.VALUE, labelText);
        }

        component.getFacets().put(Facets.LABEL, label);
      }
    }
    return label;
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
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
   * @deprecated since 1.5.0
   */
  @Deprecated
  public static void setConverter(ValueHolder valueHolder, String converterId) {
    if (converterId != null && valueHolder.getConverter() == null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final Application application = facesContext.getApplication();
      if (UIComponentTag.isValueReference(converterId)) {
        ValueBinding valueBinding = application.createValueBinding(converterId);
        if (valueHolder instanceof UIComponent) {
          ((UIComponent) valueHolder).setValueBinding(Attributes.CONVERTER, valueBinding);
        }
      } else {
        Converter converter = application.createConverter(converterId);
        valueHolder.setConverter(converter);
      }
    }
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public static void setAction(ActionSource component, String action) {
    if (action != null) {
      if (UIComponentTag.isValueReference(action)) {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Application application = facesContext.getApplication();
        MethodBinding binding = application.createMethodBinding(action, null);
        component.setAction(binding);
      } else {
        component.setAction(new ConstantMethodBinding(action));
      }
    }
  }

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
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

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
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

  /**
   * @deprecated since 1.5.0
   */
  @Deprecated
  public static void setValueBinding(UIComponent component, String name, String state) {
    // TODO: check, if it is an writeable object
    if (state != null && UIComponentTag.isValueReference(state)) {
      ValueBinding valueBinding = createValueBinding(state);
      component.setValueBinding(name, valueBinding);
    }
  }

  /**
   * @deprecated since 1.5
   */
  @Deprecated
  public static String[] getMarkupBinding(FacesContext facesContext, SupportsMarkup component) {
    ValueBinding vb = ((UIComponent) component).getValueBinding(Attributes.MARKUP);
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
        return ArrayUtils.EMPTY_STRING_ARRAY;
      } else {
        return new String[]{markups.toString()};
      }
    }

    return ArrayUtils.EMPTY_STRING_ARRAY;
  }

  /**
   * The search depends on the number of colons in the relativeId:
   * <dl>
   *   <dd>colonCount == 0</dd>
   *   <dt>fully relative</dt>
   *   <dd>colonCount == 1</dd>
   *   <dt>absolute (still normal findComponent syntax)</dt>
   *   <dd>colonCount > 1</dd>
   *   <dt>for each extra colon after 1, go up a naming container</dt>
   * </dl>
   * (to the view root, if naming containers run out)
   */
  public static UIComponent findComponent(UIComponent from, String relativeId) {
    return FindComponentUtils.findComponent(from, relativeId);
  }

  public static String[] splitList(String renderers) {
    return StringUtils.split(renderers, LIST_SEPARATOR_CHARS);
  }

  public static Object getConvertedValue(
      FacesContext facesContext, UIComponent component, String stringValue) {
    try {
      Renderer renderer = getRenderer(facesContext, component);
      if (renderer != null) {
        if (component instanceof UISelectMany) {
          final Object converted = renderer.getConvertedValue(facesContext, component, new String[]{stringValue});
          return converted instanceof List ? ((List) converted).get(0) : ((Object[]) converted)[0];
        } else {
          return renderer.getConvertedValue(facesContext, component, stringValue);
        }
      } else if (component instanceof ValueHolder) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
          //Try to find out by value binding
          ValueBinding vb = component.getValueBinding("value");
          if (vb != null) {
            Class valueType = vb.getType(facesContext);
            if (valueType != null) {
              converter = facesContext.getApplication().createConverter(valueType);
            }
          }
        }
        if (converter != null) {
          converter.getAsObject(facesContext, component, stringValue);
        }
      }
    } catch (Exception e) {
      LOG.warn("Can't convert string value '" + stringValue + "'", e);
    }
    return stringValue;
  }

  public static Markup updateMarkup(UIComponent component, Markup markup) {
    if (markup == null) {
      markup = Markup.NULL;
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      markup = markup.add(Markup.DISABLED);
    }
    if (ComponentUtils.getBooleanAttribute(component, Attributes.READONLY)) {
      markup = markup.add(Markup.READONLY);
    }
    if (component instanceof UIInput) {
      UIInput input = (UIInput) component;

      final FacesMessage.Severity maximumSeverity = ComponentUtils.getMaximumSeverity(input);
      markup = markup.add(markupOfSeverity(maximumSeverity));

      if (input.isRequired()) {
        markup = markup.add(Markup.REQUIRED);
      }
    }
    return markup;
  }

  public static Markup markupOfSeverity(FacesMessage.Severity maximumSeverity) {
    if (FacesMessage.SEVERITY_FATAL.equals(maximumSeverity)) {
      return Markup.FATAL;
    } else if (FacesMessage.SEVERITY_ERROR.equals(maximumSeverity)) {
      return Markup.ERROR;
    } else if (FacesMessage.SEVERITY_WARN.equals(maximumSeverity)) {
      return Markup.WARN;
    } else if (FacesMessage.SEVERITY_INFO.equals(maximumSeverity)) {
      return Markup.INFO;
    }
    return null;
  }

  public static void addCurrentMarkup(SupportsMarkup component, Markup markup) {
    component.setCurrentMarkup(markup.add(component.getCurrentMarkup()));
  }

  public static boolean hasChildrenWithMessages(FacesContext facesContext, NamingContainer  container) {
    if (container instanceof UIComponent) {
      String clientId = ((UIComponent) container).getClientId(facesContext);
      for (Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        String id = (String) ids.next();
        if (id.startsWith(clientId)) {
          return true;
        }
      }
    }
    return false;
  }

  public static FacesMessage.Severity getMaximumSeverityOfChildrenMessages(
      FacesContext facesContext, NamingContainer container) {
    if (container instanceof UIComponent) {
      String clientId = ((UIComponent) container).getClientId(facesContext);
      FacesMessage.Severity max = null;
      for (Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        String id = (String) ids.next();
        if (id != null && id.startsWith(clientId)) {
          final Iterator messages = facesContext.getMessages(id);
          while (messages.hasNext()) {
            FacesMessage message = (FacesMessage) messages.next();
            if (max == null || message.getSeverity().getOrdinal() > max.getOrdinal()) {
              max = message.getSeverity();
            }
          }
        }
      }
      return max;
    }
    return null;
  }

  public static String[] getChildrenWithMessages(FacesContext facesContext, NamingContainer container) {
    if (container instanceof UIComponent) {
      List<String> clientIds = new ArrayList<String>();
      String clientId = ((UIComponent) container).getClientId(facesContext);
      for (Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        String id = (String) ids.next();
        if (id.startsWith(clientId)) {
          clientIds.add(id);
        }
      }
      return clientIds.toArray(new String[clientIds.size()]);
    }
    return ArrayUtils.EMPTY_STRING_ARRAY;
  }

  /**
   * PRELIMINARY - SUBJECT TO CHANGE
   *
   * Adding a data attribute to the component.
   * The name must start with "data-", e. g. "data-tobago-foo" or "data-bar"
   */
  public static void putDataAttributeWithPrefix(UIComponent component, String name, Object value) {
    if (name.startsWith("data-")) {
      putDataAttribute(component, name.substring(5), value);
    } else {
      LOG.error("The name must start with 'data-' but it doesn't: '" + name + "'");
    }
  }

  /**
   * PRELIMINARY - SUBJECT TO CHANGE
   *
   * Adding a data attribute to the component.
   * The name should not start with "data-", e. g. "tobago-foo" or "bar"
   */
  public static void putDataAttribute(UIComponent component, Object name, Object value) {
    Map<Object, Object> map = (Map<Object, Object>) component.getAttributes().get(DATA_ATTRIBUTES_KEY);
    if (map == null) {
      map = new HashMap<Object, Object>();
      component.getAttributes().put(DATA_ATTRIBUTES_KEY, map);
    }
    map.put(name, value);
  }

  public static Map<Object, Object> getDataAttributes(UIComponent component) {
    return (Map<Object, Object>) component.getAttributes().get(DATA_ATTRIBUTES_KEY);
  }
}
