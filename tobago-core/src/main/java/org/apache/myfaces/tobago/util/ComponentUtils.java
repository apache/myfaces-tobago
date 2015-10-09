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

import org.apache.commons.collections.CollectionUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UISheet;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.TransientStateHolder;
import org.apache.myfaces.tobago.event.AbstractPopupActionListener;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.ObjectUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.NamingContainer;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIOutput;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.FaceletContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class ComponentUtils {

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
   * Name of the map for data attributes in components. New in JSF 2.2.
   * @since 2.0.0
   */
  public static final String DATA_ATTRIBUTES_KEY = "javax.faces.component.DATA_ATTRIBUTES_KEY";

  private ComponentUtils() {
  }

  public static boolean hasErrorMessages(final FacesContext context) {
    for (final Iterator iter = context.getMessages(); iter.hasNext();) {
      final FacesMessage message = (FacesMessage) iter.next();
      if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
        return true;
      }
    }
    return false;
  }

  public static boolean containsPopupActionListener(final UICommand command) {
    final ActionListener[] actionListeners = command.getActionListeners();
    for (final ActionListener actionListener : actionListeners) {
      if (actionListener instanceof AbstractPopupActionListener) {
        return true;
      }
    }
    return false;
  }

  public static String getFacesMessageAsString(final FacesContext facesContext, final UIComponent component) {
    final Iterator messages = facesContext.getMessages(
        component.getClientId(facesContext));
    final StringBuilder stringBuffer = new StringBuilder();
    while (messages.hasNext()) {
      final FacesMessage message = (FacesMessage) messages.next();
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

  public static void resetPage(final FacesContext context) {
    final UIViewRoot view = context.getViewRoot();
    if (view != null) {
      view.getAttributes().remove(PAGE_KEY);
    }
  }

  /**
   * Tries to walk up the parents to find the UIViewRoot, if not found, then go to FaceletContext's FacesContext for
   * the view root.
   */
  public static UIViewRoot findViewRoot(final FaceletContext faceletContext, final UIComponent component) {
    final UIViewRoot viewRoot = findAncestor(component, UIViewRoot.class);
    if (viewRoot != null) {
      return viewRoot;
    } else {
      return faceletContext.getFacesContext().getViewRoot();
    }
  }

  public static AbstractUIPage findPage(final FacesContext context, final UIComponent component) {
    final UIViewRoot view = context.getViewRoot();
    if (view != null) {
      TransientStateHolder stateHolder = (TransientStateHolder) view.getAttributes().get(PAGE_KEY);
      if (stateHolder == null || stateHolder.isEmpty()) {
        final AbstractUIPage page = findPage(component);
        stateHolder = new TransientStateHolder(page);
        context.getViewRoot().getAttributes().put(PAGE_KEY, stateHolder);
      }
      return (AbstractUIPage) stateHolder.get();
    } else {
      return findPage(component);
    }
  }

  public static AbstractUIPage findPage(UIComponent component) {
    if (component instanceof UIViewRoot) {
      return findPageBreadthFirst(component);
    } else {
      while (component != null) {
        if (component instanceof AbstractUIPage) {
          return (AbstractUIPage) component;
        }
        component = component.getParent();
      }
      return null;
    }
  }

  public static AbstractUIPage findPage(final FacesContext facesContext) {
    return findPageBreadthFirst(facesContext.getViewRoot());
  }

  private static AbstractUIPage findPageBreadthFirst(final UIComponent component) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof AbstractUIPage) {
        return (AbstractUIPage) child;
      }
    }
    for (final UIComponent child : component.getChildren()) {
      final AbstractUIPage result = findPageBreadthFirst(child);
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

  public static <T> T findAncestor(UIComponent component, final Class<T> type) {

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
  public static List<AbstractUIForm> findSubForms(final UIComponent component) {
    final List<AbstractUIForm> collect = new ArrayList<AbstractUIForm>();
    findSubForms(collect, component);
    return collect;
  }

  @SuppressWarnings("unchecked")
  private static void findSubForms(final List<AbstractUIForm> collect, final UIComponent component) {
    final Iterator<UIComponent> kids = component.getFacetsAndChildren();
    while (kids.hasNext()) {
      final UIComponent child = kids.next();
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
  public static <T extends UIComponent> T findDescendant(final UIComponent component, final Class<T> type) {

    for (final UIComponent child : component.getChildren()) {
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
   * Searches the component tree beneath the component and return the first component matching the type.
   */
  public static <T extends UIComponent> T findFacetDescendant(
      final UIComponent component, final String facet, final Class<T> type) {

    final UIComponent facetComponent = component.getFacet(facet);
    if (facetComponent != null) {
      if (type.isAssignableFrom(facetComponent.getClass())) {
        return (T) facetComponent;
      } else {
        return findDescendant(facetComponent, type);
      }
    } else {
      return null;
    }
  }

  /**
   * Searches the children beneath the component and return the first component matching the type.
   */
  public static <T extends UIComponent> T findChild(final UIComponent component, final Class<T> type) {

    for (final UIComponent child : component.getChildren()) {
      if (type.isAssignableFrom(child.getClass())) {
        return (T) child;
      }
    }
    return null;
  }

  /**
   * Searches the component tree beneath the component and return all component matching the type.
   */
  public static <T extends UIComponent> List<T> findDescendantList(final UIComponent component, final Class<T> type) {

    final List<T> result = new ArrayList<T>();
    
    for (final UIComponent child : component.getChildren()) {
      if (type.isAssignableFrom(child.getClass())) {
        result.add((T) child);
      }
      result.addAll(findDescendantList(child, type));
    }
    return result;
  }

  /**
   * Looks for the attribute "for" in the component. If there is any
   * search for the component which is referenced by the "for" attribute,
   * and return their clientId.
   * If there is no "for" attribute, return the "clientId" of the parent
   * (if it has a parent). This is useful for labels.
   */
  public static String findClientIdFor(final UIComponent component, final FacesContext facesContext) {
    final UIComponent forComponent = findFor(component);
    if (forComponent != null) {
      final String clientId = forComponent.getClientId(facesContext);
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

  public static UIComponent findFor(final UIComponent component) {
    final String forValue = (String) component.getAttributes().get(Attributes.FOR);
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
  public static void evaluateAutoFor(final UIComponent component) {
    final String forComponent = (String) component.getAttributes().get(Attributes.FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("for = '" + forComponent + "'");
    }
    if ("@auto".equals(forComponent)) {
      for (final UIComponent child : component.getParent().getChildren()) {
        if (setForToInput(component, child, AbstractUIInput.class, false)) {
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
  public static void evaluateAutoFor(final UIComponent component, final Class<? extends UIComponent> clazz) {
    final String forComponent = (String) component.getAttributes().get(Attributes.FOR);
    if (LOG.isDebugEnabled()) {
      LOG.debug("for = '" + forComponent + "'");
    }
    if ("@auto".equals(forComponent)) {
      // parent
      for (final UIComponent child : component.getParent().getChildren()) {
        if (setForToInput(component, child, clazz, component instanceof NamingContainer)) {
          return;
        }
      }
      // grand parent
      for (final UIComponent child : component.getParent().getParent().getChildren()) {
        if (setForToInput(component, child, clazz, component.getParent() instanceof NamingContainer)) {
          return;
        }
      }
    }
  }

  private static boolean setForToInput(
      final UIComponent component, final UIComponent child, final Class<? extends UIComponent> clazz,
      final boolean namingContainer) {
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
        final AbstractUIForm form = (AbstractUIForm) component;
        if (form.isSubmitted()) {
          return true;
        }
      }
      component = component.getParent();
    }
    return false;
  }

  public static FacesMessage.Severity getMaximumSeverity(final UIComponent component) {
    final boolean invalid = component instanceof UIInput && !((UIInput) component).isValid();
    FacesMessage.Severity max = invalid ? FacesMessage.SEVERITY_ERROR : null;
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Iterator messages = facesContext.getMessages(component.getClientId(facesContext));
    while (messages.hasNext()) {
      final FacesMessage message = (FacesMessage) messages.next();
      if (max == null || message.getSeverity().getOrdinal() > max.getOrdinal()) {
        max = message.getSeverity();
      }
    }
    return max;
  }

  public static boolean isError(final UIInput uiInput) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return !uiInput.isValid()
        || facesContext.getMessages(uiInput.getClientId(facesContext)).hasNext();
  }

  public static boolean isError(final UIComponent component) {
    if (component instanceof AbstractUIInput) {
      return isError((AbstractUIInput) component);
    }
    return false;
  }

  public static boolean isOutputOnly(final UIComponent component) {
    return getBooleanAttribute(component, Attributes.DISABLED)
        || getBooleanAttribute(component, Attributes.READONLY);
  }

  public static boolean mayValidate(final UIComponent component) {
    return !isOutputOnly(component)
        && ComponentUtils.isInActiveForm(component);
  }

  public static boolean mayUpdateModel(final UIComponent component) {
    return mayValidate(component);
  }

  public static boolean getBooleanAttribute(final UIComponent component, final String name) {

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

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static ValueBinding createValueBinding(final String value) {
    return FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
  }

  public static Object getAttribute(final UIComponent component, final String name) {
    Object value = component.getAttributes().get(name);
    if (value instanceof ValueBinding) {
      value = ((ValueBinding) value).getValue(FacesContext.getCurrentInstance());
    }
    return value;
  }

  public static Object getObjectAttribute(final UIComponent component, final String name) {
    return getAttribute(component, name);
  }

  public static String getStringAttribute(final UIComponent component, final String name) {
    return (String) getAttribute(component, name);
  }

  public static int getIntAttribute(final UIComponent component, final String name) {
    return getIntAttribute(component, name, 0);
  }

  public static int getIntAttribute(final UIComponent component, final String name,
      final int defaultValue) {
    final Object integer = component.getAttributes().get(name);
    if (integer instanceof Number) {
      return ((Number) integer).intValue();
    } else if (integer instanceof String) {
      try {
        return Integer.parseInt((String) integer);
      } catch (final NumberFormatException e) {
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

  public static Character getCharacterAttribute(final UIComponent component, final String name) {
    final Object character = component.getAttributes().get(name);
    if (character == null) {
      return null;
    } else if (character instanceof Character) {
      return ((Character) character);
    } else if (character instanceof String) {
      final String asString = ((String) character);
      return asString.length() > 0 ? asString.charAt(0) : null;
    } else {
      LOG.warn("Unknown type '" + character.getClass().getName()
          + "' for integer attribute: " + name + " comp: " + component);
      return null;
    }
  }

  public static boolean isFacetOf(final UIComponent component, final UIComponent parent) {
    for (final Object o : parent.getFacets().keySet()) {
      final UIComponent facet = parent.getFacet((String) o);
      if (component.equals(facet)) {
        return true;
      }
    }
    return false;
  }

  public static RendererBase getRenderer(final FacesContext facesContext, final UIComponent component) {
    return getRenderer(facesContext, component.getFamily(), component.getRendererType());
  }

  public static RendererBase getRenderer(final FacesContext facesContext, final String family,
                                         final String rendererType) {
    if (rendererType == null) {
      return null;
    }

    final Map<String, Object> requestMap = (Map<String, Object>) facesContext.getExternalContext().getRequestMap();
    final StringBuilder key = new StringBuilder(RENDER_KEY_PREFIX);
    key.append(rendererType);
    RendererBase renderer = (RendererBase) requestMap.get(key.toString());

    if (renderer == null) {
      final Renderer myRenderer = getRendererInternal(facesContext, family, rendererType);
      if (myRenderer instanceof RendererBase) {
        requestMap.put(key.toString(), myRenderer);
        renderer = (RendererBase) myRenderer;
      } else {
        return null;
      }
    }
    return renderer;
  }


  private static Renderer getRendererInternal(
      final FacesContext facesContext, final String family, final String rendererType) {
    final RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    final RenderKit renderKit = rkFactory.getRenderKit(facesContext, facesContext.getViewRoot().getRenderKitId());
    final Renderer myRenderer = renderKit.getRenderer(family, rendererType);
    return myRenderer;
  }

  public static Object findParameter(final UIComponent component, final String name) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof UIParameter) {
        final UIParameter parameter = (UIParameter) child;
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

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static ActionListener createActionListener(final String type) {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        classLoader = type.getClass().getClassLoader();
      }
      final Class clazz = classLoader.loadClass(type);
      return (ActionListener) clazz.newInstance();
    } catch (final Exception e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("type=" + type, e);
      }
      throw new RuntimeException(e);
    }
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static UIGraphic getFirstGraphicChild(final UIComponent component) {
    UIGraphic graphic = null;
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof UIGraphic) {
        graphic = (UIGraphic) child;
        break;
      }
    }
    return graphic;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static boolean isHoverEnabled(final UIComponent component) {
    return ComponentUtils.getBooleanAttribute(component, Attributes.HOVER);
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static UIOutput getFirstNonGraphicChild(final UIComponent component) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof UIOutput) {
        return (UIOutput) child;
      }
    }
    return null;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String removePx(String value) {
    if (value != null && value.endsWith("px")) {
      value = value.substring(0, value.length() - 2);
    }
    return value;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static void setValueForValueBinding(final String name, final Object value) {
    final FacesContext context = FacesContext.getCurrentInstance();
    final ValueBinding valueBinding = context.getApplication().createValueBinding(name);
    valueBinding.setValue(context, value);
  }

  /**
   * @deprecated Since Tobago 2.0
   */
  @Deprecated
  public static boolean hasSelectedValue(final List<SelectItem> items, final Object value) {
    for (final SelectItem item : items) {
      if (ObjectUtils.equals(item.getValue(), value)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static int getIntValue(final ValueBinding valueBinding) {
    return getAsInt(valueBinding.getValue(FacesContext.getCurrentInstance()));
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  private static int getAsInt(final Object value) {
    final int result;
    if (value instanceof Number) {
      result = ((Number) value).intValue();
    } else if (value instanceof String) {
      result = Integer.parseInt((String) value);
    } else {
      throw new IllegalArgumentException("Can't convert " + value + " to int!");
    }
    return result;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String createPickerId(
      final FacesContext facesContext, final UIComponent component, final String postfix) {
    //String id = component.getId();
    final String id = getComponentId(facesContext, component);
    return id + "_picker" + postfix;
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String getComponentId(final FacesContext facesContext, final UIComponent component) {
    final String id = component.getId();
    //if (id == null) {
    // XXX What is this?
    //  id = component.getClientId(facesContext).substring(id.lastIndexOf('_'));
    //}
    return id;
  }

  /**
   * Checks if the Component has a label facet and if not creates one with the label attribute.
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static UIComponent provideLabel(final FacesContext facesContext, final UIComponent component) {
    UIComponent label = component.getFacet(Facets.LABEL);

    if (label == null) {
      final Map attributes = component.getAttributes();
      Object labelText = component.getValueBinding(Attributes.LABEL);
      if (labelText == null) {
        labelText = attributes.get(Attributes.LABEL);
      }

      if (labelText != null) {
        final Application application = FacesContext.getCurrentInstance().getApplication();
        label = application.createComponent(UIOutput.COMPONENT_TYPE);
        label.setRendererType(RendererTypes.LABEL);
        final String idprefix = ComponentUtils.getComponentId(facesContext, component);
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
   * <p>
   * The search depends on the number of prefixed colons in the relativeId:
   * <dl>
   *   <dd>number of prefixed colons == 0</dd>
   *   <dt>fully relative</dt>
   *   <dd>number of prefixed colons == 1</dd>
   *   <dt>absolute (still normal findComponent syntax)</dt>
   *   <dd>number of prefixed colons == 2</dd>
   *   <dt>search in the current naming container (same as 0 colons)</dt>
   *   <dd>number of prefixed colons == 3</dd>
   *   <dt>search in the parent naming container of the current naming container</dt>
   *   <dd>number of prefixed colons > 3</dd>
   *   <dt>go to the next parent naming container for each additional colon</dt>
   * </dl>
   * </p>
   * <p>
   * If a literal is specified: to use more than one identifier the identifiers must be space delimited.
   * </p>
   */
  public static UIComponent findComponent(final UIComponent from, final String relativeId) {
    UIComponent from1 = from;
    String relativeId1 = relativeId;
    final int idLength = relativeId1.length();
    if (idLength > 0 && relativeId1.charAt(0) == '@') {
      if (relativeId1.equals("@this")) {
        return from1;
      }
    }

    // Figure out how many colons
    int colonCount = 0;
    while (colonCount < idLength) {
      if (relativeId1.charAt(colonCount) != UINamingContainer.getSeparatorChar(FacesContext.getCurrentInstance())) {
        break;
      }
      colonCount++;
    }

    // colonCount == 0: fully relative
    // colonCount == 1: absolute (still normal findComponent syntax)
    // colonCount > 1: for each extra colon after 1, go up a naming container
    // (to the view root, if naming containers run out)
    if (colonCount > 1) {
      relativeId1 = relativeId1.substring(colonCount);
      for (int j = 1; j < colonCount; j++) {
        while (from1.getParent() != null) {
          from1 = from1.getParent();
          if (from1 instanceof NamingContainer) {
            break;
          }
        }
      }
    }
    return from1.findComponent(relativeId1);
  }

  /**
   * Resolves the real clientIds.
   */
  public static String[] evaluateClientIds(
      final FacesContext context, final UIComponent component, final String[] componentIds) {
    final List<String> result = new ArrayList<String>(componentIds.length);
    for (final String id : componentIds) {
      if (!StringUtils.isBlank(id)) {
        final String clientId = evaluateClientId(context, component, id);
        if (clientId != null) {
          result.add(clientId);
        }
      }
    }
    return (String[]) result.toArray(new String[result.size()]);
  }

  /**
   * Resolves the real clientId.
   */
  public static String evaluateClientId(
      final FacesContext context, final UIComponent component, final String componentId) {
    final UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      final String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof UISheet) {
        final int rowIndex = ((UISheet) partiallyComponent).getRowIndex();
        if (rowIndex >= 0 && clientId.endsWith(Integer.toString(rowIndex))) {
          return clientId.substring(0, clientId.lastIndexOf(UINamingContainer.getSeparatorChar(context)));
        }
      }
      return clientId;
    }
    LOG.error("No component found for id='" + componentId + "', "
        + "search base component is '" + component.getClientId(context) + "'");
    return null;
  }

  public static String[] splitList(final String renderers) {
    return StringUtils.split(renderers, LIST_SEPARATOR_CHARS);
  }

  public static Object getConvertedValue(
      final FacesContext facesContext, final UIComponent component, final String stringValue) {
    try {
      final Renderer renderer = getRenderer(facesContext, component);
      if (renderer != null) {
        if (component instanceof UISelectMany) {
          final Object converted = renderer.getConvertedValue(facesContext, component, new String[]{stringValue});
          return converted instanceof Collection ? CollectionUtils.get(converted, 0) : ((Object[]) converted)[0];
        } else {
          return renderer.getConvertedValue(facesContext, component, stringValue);
        }
      } else if (component instanceof ValueHolder) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
          //Try to find out by value binding
          final ValueBinding vb = component.getValueBinding("value");
          if (vb != null) {
            final Class valueType = vb.getType(facesContext);
            if (valueType != null) {
              converter = facesContext.getApplication().createConverter(valueType);
            }
          }
        }
        if (converter != null) {
          converter.getAsObject(facesContext, component, stringValue);
        }
      }
    } catch (final Exception e) {
      LOG.warn("Can't convert string value '" + stringValue + "'", e);
    }
    return stringValue;
  }

  public static Markup updateMarkup(final UIComponent component, Markup markup) {
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
      final UIInput input = (UIInput) component;

      final FacesMessage.Severity maximumSeverity = ComponentUtils.getMaximumSeverity(input);
      markup = markup.add(markupOfSeverity(maximumSeverity));

      if (input.isRequired()) {
        markup = markup.add(Markup.REQUIRED);
      }
    }
    return markup;
  }

  public static Markup markupOfSeverity(final FacesMessage.Severity maximumSeverity) {
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

  public static void addCurrentMarkup(final Visual component, final Markup markup) {
    component.setCurrentMarkup(markup.add(component.getCurrentMarkup()));
  }

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static boolean hasChildrenWithMessages(final FacesContext facesContext, final NamingContainer  container) {
    if (container instanceof UIComponent) {
      final String clientId = ((UIComponent) container).getClientId(facesContext);
      for (final Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        final String id = (String) ids.next();
        if (id.startsWith(clientId)) {
          return true;
        }
      }
    }
    return false;
  }

  public static FacesMessage.Severity getMaximumSeverityOfChildrenMessages(
      final FacesContext facesContext, final NamingContainer container) {
    if (container instanceof UIComponent) {
      final String clientId = ((UIComponent) container).getClientId(facesContext);
      FacesMessage.Severity max = null;
      for (final Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        final String id = (String) ids.next();
        if (id != null && id.startsWith(clientId)) {
          final Iterator messages = facesContext.getMessages(id);
          while (messages.hasNext()) {
            final FacesMessage message = (FacesMessage) messages.next();
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

  /**
   * @deprecated since 2.0.0
   */
  @Deprecated
  public static String[] getChildrenWithMessages(final FacesContext facesContext, final NamingContainer container) {
    if (container instanceof UIComponent) {
      final List<String> clientIds = new ArrayList<String>();
      final String clientId = ((UIComponent) container).getClientId(facesContext);
      for (final Iterator ids = facesContext.getClientIdsWithMessages(); ids.hasNext();) {
        final String id = (String) ids.next();
        if (id.startsWith(clientId)) {
          clientIds.add(id);
        }
      }
      return clientIds.toArray(new String[clientIds.size()]);
    }
    return ArrayUtils.EMPTY_STRING_ARRAY;
  }

  /**
   * Adding a data attribute to the component. 
   * The name must start with "data-", e. g. "data-tobago-foo" or "data-bar"
   */
  public static void putDataAttributeWithPrefix(
      final UIComponent component, final DataAttributes name, final Object value) {
    if (name.getValue().startsWith("data-")) {
      putDataAttribute(component, name.getValue().substring(5), value);
    } else {
      LOG.error("The name must start with 'data-' but it doesn't: '" + name + "'");
    }
  }

  /**
   * Adding a data attribute to the component.
   * The name should not start with "data-", e. g. "tobago-foo" or "bar"
   */
  public static void putDataAttribute(final UIComponent component, final Object name, final Object value) {
    Map<Object, Object> map = getDataAttributes(component);
    if (map == null) {
      map = new HashMap<Object, Object>();
      component.getAttributes().put(DATA_ATTRIBUTES_KEY, map);
    }
    map.put(name, value);
  }

  @SuppressWarnings("unchecked")
  public static Map<Object, Object> getDataAttributes(final UIComponent component) {
    return (Map<Object, Object>) component.getAttributes().get(DATA_ATTRIBUTES_KEY);
  }

  public static Object getDataAttribute(final UIComponent component, final String name) {
    Map<Object, Object> map = getDataAttributes(component);
    return map != null ? map.get(name) : null;
  }

  /**
   * @deprecated since 2.0.0, please use
   * {@link javax.faces.component.UIComponent#invokeOnComponent(javax.faces.context.FacesContext, java.lang.String,
      javax.faces.component.ContextCallback) }
   */
  public static boolean invokeOnComponent(
      final FacesContext context, final UIComponent component, final String clientId, final ContextCallback callback) {
    return component.invokeOnComponent(context, clientId, callback);
  }
}
