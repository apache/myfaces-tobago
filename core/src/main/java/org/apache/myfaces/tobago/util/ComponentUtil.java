package org.apache.myfaces.tobago.util;

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

/*
 * Created 01.07.2003 10:07:23.
 * $Id$
 */

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CONVERTER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HOVER;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MARKUP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_READONLY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_LABEL;
import org.apache.myfaces.tobago.component.AbstractUIForm;
import org.apache.myfaces.tobago.component.AbstractUIPage;
import org.apache.myfaces.tobago.component.AbstractUIPopup;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.component.UIInputBase;
import org.apache.myfaces.tobago.context.TransientStateHolder;
import org.apache.myfaces.tobago.el.ConstantMethodBinding;
import org.apache.myfaces.tobago.event.PopupActionListener;
import org.apache.myfaces.tobago.renderkit.LayoutRenderer;
import org.apache.myfaces.tobago.renderkit.LayoutableRenderer;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ComponentUtil {

  private static final Log LOG = LogFactory.getLog(ComponentUtil.class);

  private static final String RENDER_KEY_PREFIX
      = "org.apache.myfaces.tobago.util.ComponentUtil.RendererKeyPrefix_";

  private static final String PAGE_KEY = "org.apache.myfaces.tobago.Page.Key";

  public static final Class[] ACTION_ARGS = {};
  public static final Class[] ACTION_LISTENER_ARGS = {ActionEvent.class};
  public static final Class[] VALUE_CHANGE_LISTENER_ARGS = {ValueChangeEvent.class};
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};
  public static final String LIST_SEPARATOR_CHARS = ", ";

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

  /**
   * Find all subforms of a component, and collects it.
   * It does not find subforms of subforms.
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
      if (component instanceof AbstractUIForm) {
        AbstractUIForm form = (AbstractUIForm) component;
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
    if (component instanceof UIInputBase) {
      return isError((UIInputBase) component);
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

  public static void setStringProperty(UIComponent component, String name, String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  public static void setStyleClasses(UIComponent component, String styleClasses) {
    if (styleClasses != null) {
      if (UIComponentTag.isValueReference(styleClasses)) {
        component.setValueBinding(ATTR_STYLE_CLASS, createValueBinding(styleClasses));
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

  public static void setMarkup(UIComponent markupComponent, String markup) {
    if (markup != null) {
      if (markupComponent instanceof SupportsMarkup) {
        if (UIComponentTag.isValueReference(markup)) {
          markupComponent.setValueBinding(ATTR_MARKUP, createValueBinding(markup));
        } else {
          String[] markups = splitList(markup);
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
  public static LayoutableRenderer getRenderer(FacesContext facesContext, UIComponent component) {
    return getRenderer(facesContext, component.getFamily(), component.getRendererType());

  }

  public static LayoutRenderer getLayoutRenderer(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    Map requestMap = facesContext.getExternalContext().getRequestMap();
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
    if (renderer instanceof LayoutRenderer) {
      return (LayoutRenderer) renderer;
    }
    return null;
  }


  public static LayoutableRenderer getRenderer(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    Map requestMap = facesContext.getExternalContext().getRequestMap();
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
    if (renderer instanceof LayoutableRenderer) {
      return (LayoutableRenderer) renderer;
    }
    return null;
  }

  public static RendererBase getRendererBase(FacesContext facesContext, UIComponent component) {
    return getRendererBase(facesContext, component.getFamily(), component.getRendererType());
  }

   public static RendererBase getRendererBase(FacesContext facesContext, String family, String rendererType) {
    if (rendererType == null) {
      return null;
    }

    Map requestMap = facesContext.getExternalContext().getRequestMap();
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

  public static UIComponent provideLabel(FacesContext facesContext, UIComponent component) {
    UIComponent label = component.getFacet(Facets.LABEL);


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
        label.setId(idprefix + "_" + Facets.LABEL);
        label.setRendered(true);

        if (labelText instanceof ValueBinding) {
          label.setValueBinding(ATTR_VALUE, (ValueBinding) labelText);
        } else {
          label.getAttributes().put(ATTR_VALUE, labelText);
        }

        component.getFacets().put(Facets.LABEL, label);
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

  public static void setValueBinding(UIComponent component, String name, String state) {
    // TODO: check, if it is an writeable object
    if (state != null && UIComponentTag.isValueReference(state)) {
      ValueBinding valueBinding = createValueBinding(state);
      component.setValueBinding(name, valueBinding);
    }
  }


  public static String[] getMarkupBinding(FacesContext facesContext, SupportsMarkup component) {
    ValueBinding vb = ((UIComponent) component).getValueBinding(ATTR_MARKUP);
    if (vb != null) {
      Object markups = vb.getValue(facesContext);
      if (markups instanceof String[]) {
        return (String[]) markups;
      } else if (markups instanceof String) {
        String[] strings = ((String) markups).split("[, ]");
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

  public static String[] splitList(String renderers) {
    return StringUtils.split(renderers, LIST_SEPARATOR_CHARS);
  }

}
