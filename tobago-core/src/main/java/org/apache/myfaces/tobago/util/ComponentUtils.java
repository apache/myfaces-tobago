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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.TransientStateHolder;
import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.internal.component.AbstractUIFormBase;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;
import org.apache.myfaces.tobago.internal.component.AbstractUIReload;
import org.apache.myfaces.tobago.internal.component.AbstractUISheet;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.FacesMessage;
import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
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
   *
   * @since 2.0.0
   */
  public static final String DATA_ATTRIBUTES_KEY = "javax.faces.component.DATA_ATTRIBUTES_KEY";

  private ComponentUtils() {
  }

  /**
   * @deprecated since 3.0.1
   */
  @Deprecated
  public static boolean hasErrorMessages(final FacesContext context) {
    for (final FacesMessage message : (Iterable<FacesMessage>) context::getMessages) {
      if (FacesMessage.SEVERITY_ERROR.compareTo(message.getSeverity()) <= 0) {
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

  /**
   * @deprecated since 3.0.1
   */
  @Deprecated
  public static boolean isInPopup(final UIComponent component) {
    UIComponent c = component;
    while (c != null) {
      if (c instanceof AbstractUIPopup) {
        return true;
      }
      c = c.getParent();
    }
    return false;
  }

  /**
   * @deprecated since 3.0.1
   */
  @Deprecated
  public static void resetPage(final FacesContext context) {
    final UIViewRoot view = context.getViewRoot();
    if (view != null) {
      view.getAttributes().remove(PAGE_KEY);
    }
  }

  /**
   * Tries to walk up the parents to find the UIViewRoot, if not found, then go to FaceletContext's FacesContext for the
   * view root.
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

  public static AbstractUIPage findPage(final UIComponent component) {
    UIComponent c = component;
    if (c instanceof UIViewRoot) {
      return findPageBreadthFirst(c);
    } else {
      while (c != null) {
        if (c instanceof AbstractUIPage) {
          return (AbstractUIPage) c;
        }
        c = c.getParent();
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

  public static AbstractUIFormBase findForm(final UIComponent component) {
    UIComponent c = component;
    while (c != null) {
      if (c instanceof AbstractUIFormBase) {
        return (AbstractUIFormBase) c;
      }
      c = c.getParent();
    }
    return null;
  }

  public static <T> T findAncestor(final UIComponent component, final Class<T> type) {
    UIComponent c = component;
    while (c != null) {
      if (type.isAssignableFrom(c.getClass())) {
        return (T) c;
      }
      c = c.getParent();
    }
    return null;
  }

  /**
   * Find all sub forms of a component, and collects it. It does not find sub forms of sub forms.
   */
  public static List<AbstractUIForm> findSubForms(final UIComponent component) {
    final List<AbstractUIForm> collect = new ArrayList<>();
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
      final UIComponent component, final Facets facet, final Class<T> type) {

    final UIComponent facetComponent = component.getFacet(facet.name());
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

    final List<T> result = new ArrayList<>();

    for (final UIComponent child : component.getChildren()) {
      if (type.isAssignableFrom(child.getClass())) {
        result.add((T) child);
      }
      result.addAll(findDescendantList(child, type));
    }
    return result;
  }

  /**
   * Looks for the attribute "for" in the component. If there is any search for the component which is referenced by the
   * "for" attribute, and return their clientId. If there is no "for" attribute, return the "clientId" of the parent (if
   * it has a parent). This is useful for labels.
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
    final String forValue = getStringAttribute(component, Attributes.forValue);
    if (forValue == null) {
      return component.getParent();
    }
    return ComponentUtils.findComponent(component, forValue);
  }

  /**
   * Looks for the attribute "for" of the component. In case that the value is equals to "@auto" the children of the
   * parent will be checked if they are of the type of the parameter clazz. The "id" of the first one will be used to
   * reset the "for" attribute of the component.
   */
  public static void evaluateAutoFor(final UIComponent component, final Class<? extends UIComponent> clazz) {
    final String forComponent = getStringAttribute(component, Attributes.forValue);
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
      ComponentUtils.setAttribute(component, Attributes.forValue, forComponent);
      return true;
    }
    return false;
  }

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  public static boolean isInActiveForm(final UIComponent component) {
    UIComponent c = component;
    while (c != null) {
      if (c instanceof AbstractUIFormBase) {
        final AbstractUIFormBase form = (AbstractUIFormBase) c;
        if (form.isSubmitted()) {
          return true;
        }
      }
      c = c.getParent();
    }
    return false;
  }

  public static FacesMessage.Severity getMaximumSeverity(final UIComponent component) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final List<FacesMessage> messages = facesContext.getMessageList(component.getClientId(facesContext));
    final FacesMessage.Severity maximumSeverity = getMaximumSeverity(messages);

    final boolean invalid = component instanceof UIInput && !((UIInput) component).isValid();

    return invalid
        && (maximumSeverity == null || FacesMessage.SEVERITY_ERROR.getOrdinal() > maximumSeverity.getOrdinal())
        ? FacesMessage.SEVERITY_ERROR : maximumSeverity;
  }

  public static FacesMessage.Severity getMaximumSeverity(final List<FacesMessage> messages) {
    FacesMessage.Severity max = null;
    for (final FacesMessage message : messages) {
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
    return getBooleanAttribute(component, Attributes.disabled)
        || getBooleanAttribute(component, Attributes.readonly);
  }

  public static Object getAttribute(final UIComponent component, final Attributes name) {
    return component.getAttributes().get(name.getName());
  }

  public static boolean getBooleanAttribute(final UIComponent component, final Attributes name) {
    return getBooleanAttribute(component, name, false);
  }

  public static boolean getBooleanAttribute(
      final UIComponent component, final Attributes name, final boolean defaultValue) {

    final Object bool = component.getAttributes().get(name.getName());
    if (bool == null) {
      return defaultValue;
    } else if (bool instanceof Boolean) {
      return (Boolean) bool;
    } else {
      return Boolean.valueOf(bool.toString());
    }
  }

  public static String getStringAttribute(final UIComponent component, final Attributes name) {
    return getStringAttribute(component, name, null);
  }

  public static String getStringAttribute(
      final UIComponent component, final Attributes name, final String defaultValue) {
    final String result = (String) getAttribute(component, name);
    return result != null ? result : defaultValue;
  }

  public static int getIntAttribute(final UIComponent component, final Attributes name) {
    return getIntAttribute(component, name, 0);
  }

  public static int getIntAttribute(final UIComponent component, final Attributes name, final int defaultValue) {
    final Object integer = component.getAttributes().get(name.getName());
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

  public static Character getCharacterAttribute(final UIComponent component, final Attributes name) {
    final Object character = getAttribute(component, name);
    if (character == null) {
      return null;
    } else if (character instanceof Character) {
      return (Character) character;
    } else if (character instanceof String) {
      final String asString = (String) character;
      return asString.length() > 0 ? asString.charAt(0) : null;
    } else {
      LOG.warn("Unknown type '" + character.getClass().getName()
          + "' for integer attribute: " + name + " comp: " + component);
      return null;
    }
  }

  public static void setAttribute(final UIComponent component, final Attributes name, final Object value) {
    component.getAttributes().put(name.getName(), value);
  }

  public static void removeAttribute(final UIComponent component, final Attributes name) {
    component.getAttributes().remove(name.getName());
  }

  public static UIComponent getFacet(final UIComponent component, final Facets facet) {
    return component.getFacet(facet.name());
  }

  public static void setFacet(final UIComponent component, final Facets facet, final UIComponent child) {
    component.getFacets().put(facet.name(), child);
  }

  public static void removeFacet(final UIComponent component, final Facets facet) {
    component.getFacets().remove(facet.name());
  }

  public static AbstractUIReload getReloadFacet(final UIComponent component) {
    final UIComponent facet = getFacet(component, Facets.reload);
    if (facet == null) {
      return null;
    } else if (facet instanceof AbstractUIReload) {
      return (AbstractUIReload) facet;
    } else {
      LOG.warn("Content of a reload facet must be {} but found {} in component with id '{}'",
          AbstractUIReload.class.getName(), facet.getClass().getName(), component.getClientId());
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

    final Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
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
   * <p>
   * The search depends on the number of prefixed colons in the relativeId:
   * </p>
   * <dl>
   * <dd>number of prefixed colons == 0</dd>
   * <dt>fully relative</dt>
   * <dd>number of prefixed colons == 1</dd>
   * <dt>absolute (still normal findComponent syntax)</dt>
   * <dd>number of prefixed colons == 2</dd>
   * <dt>search in the current naming container (same as 0 colons)</dt>
   * <dd>number of prefixed colons == 3</dd>
   * <dt>search in the parent naming container of the current naming container</dt>
   * <dd>number of prefixed colons &gt; 3</dd>
   * <dt>go to the next parent naming container for each additional colon</dt>
   * </dl>
   * <p>
   * If a literal is specified: to use more than one identifier the identifiers must be space delimited.
   * </p>
   */
  public static UIComponent findComponent(final UIComponent from, final String relativeId) {
    UIComponent from1 = from;
    String relativeId1 = relativeId;
    final int idLength = relativeId1.length();
    if (idLength > 0
        && relativeId1.charAt(0) == '@'
        && "@this".equals(relativeId1)) {
      return from1;
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
  public static String evaluateClientIds(
      final FacesContext context, final UIComponent component, final String[] componentIds) {
    final List<String> result = new ArrayList<>(componentIds.length);
    for (final String id : componentIds) {
      if (!StringUtils.isBlank(id)) {
        final String clientId = evaluateClientId(context, component, id);
        if (clientId != null) {
          result.add(clientId);
        }
      }
    }
    if (result.isEmpty()) {
      return null;
    } else {
      return StringUtils.join(result, ' ');
    }
  }

  /**
   * Resolves the real clientId.
   */
  public static String evaluateClientId(
      final FacesContext context, final UIComponent component, final String componentId) {
    final UIComponent partiallyComponent = ComponentUtils.findComponent(component, componentId);
    if (partiallyComponent != null) {
      final String clientId = partiallyComponent.getClientId(context);
      if (partiallyComponent instanceof AbstractUISheet) {
        final int rowIndex = ((AbstractUISheet) partiallyComponent).getRowIndex();
        if (rowIndex >= 0 && clientId.endsWith(Integer.toString(rowIndex))) {
          return clientId.substring(0, clientId.lastIndexOf(UINamingContainer.getSeparatorChar(context)));
        }
      }
      return clientId;
    }
    LOG.error("No component found for id='{}', search base component is '{}'",
        componentId, component != null ? component.getClientId(context) : "<null>");
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
          if (converted instanceof Object[]) {
            return ((Object[]) converted)[0];
          } else if (converted instanceof List) {
            return ((List) converted).get(0);
          } else if (converted instanceof Collection) {
            return ((Collection) converted).iterator().next();
          } else {
            return null;
          }
        } else {
          return renderer.getConvertedValue(facesContext, component, stringValue);
        }
      } else if (component instanceof ValueHolder) {
        Converter converter = ((ValueHolder) component).getConverter();
        if (converter == null) {
          //Try to find out by value expression
          final ValueExpression expression = component.getValueExpression("value");
          if (expression != null) {
            final Class valueType = expression.getType(facesContext.getELContext());
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

  public static FacesMessage.Severity getMaximumSeverityOfChildrenMessages(
      final FacesContext facesContext, final NamingContainer container) {
    if (container instanceof UIComponent) {
      final String clientId = ((UIComponent) container).getClientId(facesContext);
      FacesMessage.Severity max = null;
      for (final String id : (Iterable<String>) facesContext::getClientIdsWithMessages) {
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
   * Adding a data attribute to the component. The name must start with "data-", e. g. "data-tobago-foo" or "data-bar"
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
   * Adding a data attribute to the component. The name should not start with "data-", e. g. "tobago-foo" or "bar"
   */
  public static void putDataAttribute(final UIComponent component, final Object name, final Object value) {
    Map<Object, Object> map = getDataAttributes(component);
    if (map == null) {
      map = new HashMap<>();
      component.getAttributes().put(DATA_ATTRIBUTES_KEY, map);
    }
    if (map.containsKey(name)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Data attribute '{}' is already set for component '{}' (old value='{}', new value='{}')!",
            name, component.getClientId(), map.get(name), value);
      }
    }
    map.put(name, value);
  }

  @SuppressWarnings("unchecked")
  public static Map<Object, Object> getDataAttributes(final UIComponent component) {
    return (Map<Object, Object>) component.getAttributes().get(DATA_ATTRIBUTES_KEY);
  }

  public static Object getDataAttribute(final UIComponent component, final String name) {
    final Map<Object, Object> map = getDataAttributes(component);
    return map != null ? map.get(name) : null;
  }

  /**
   * May return null, if no converter can be find.
   */
  public static Converter getConverter(
      final FacesContext facesContext, final UIComponent component, final Object value) {

    Converter converter = null;
    if (component instanceof ValueHolder) {
      converter = ((ValueHolder) component).getConverter();
    }

    if (converter == null) {
      final ValueExpression valueExpression = component.getValueExpression("value");
      if (valueExpression != null) {
        Class converterType = null;
        try {
          converterType = valueExpression.getType(facesContext.getELContext());
        } catch (final Exception e) {
          // ignore, seems not to be possible, when EL is a function like #{bean.getName(item.id)}
        }
        if (converterType == null) {
          if (value != null) {
            converterType = value.getClass();
          }
        }
        if (converterType != null && converterType != Object.class) {
          converter = facesContext.getApplication().createConverter(converterType);
        }
      }
    }

    return converter;
  }

  public static String getFormattedValue(
      final FacesContext facesContext, final UIComponent component, final Object currentValue)
      throws ConverterException {

    if (currentValue == null) {
      return "";
    }

    final Converter converter = ComponentUtils.getConverter(facesContext, component, currentValue);
    if (converter != null) {
      return converter.getAsString(facesContext, component, currentValue);
    } else {
      return currentValue.toString();
    }
  }

  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType, final RendererTypes rendererType,
      final String clientId) {
    final UIComponent component = facesContext.getApplication().createComponent(componentType);
    if (rendererType != null) {
      component.setRendererType(rendererType.name());
    }
    component.setId(clientId);
    return component;
  }

  public static List<UIComponent> findLayoutChildren(final UIComponent container) {
    final List<UIComponent> result = new ArrayList<>();
    addLayoutChildren(container, result);
    return result;
  }

  private static void addLayoutChildren(final UIComponent component, final List<UIComponent> result) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof Visual && !((Visual) child).isPlain()
          || UIComponent.isCompositeComponent(child) && !child.isRendered()) {
        result.add(child);
      } else {
        // Child seems to be transparent for layout, like UIForm with "plain" set.
        // So we try to add the inner components.
        addLayoutChildren(child, result);
      }
    }

    final UIComponent child = component.getFacet(UIComponent.COMPOSITE_FACET_NAME);
    if (child instanceof Visual && !((Visual) child).isPlain()) {
      result.add(child);
    } else if (child != null) {
      // Child seems to be transparent for layout, like UIForm with "plain" set.
      // So we try to add the inner components.
      addLayoutChildren(child, result);
    }
  }

  /**
   * returns the "confirmation" attribute or the value of the "confirmation" facet of a component.
   *
   * @since Tobago 4.4.0
   */
  public static String getConfirmation(final UIComponent component) {
    final String confirmation = getStringAttribute(component, Attributes.confirmation, null);
    if (confirmation != null) {
      return confirmation;
    }
    final UIComponent facet = ComponentUtils.getFacet(component, Facets.confirmation);
    if (facet instanceof ValueHolder && ((UIComponent) facet).isRendered()) {
      final ValueHolder valueHolder = (ValueHolder) facet;
      return "" + valueHolder.getValue();
    } else if (facet != null && !(facet instanceof ValueHolder)) {
      LOG.warn("The content of a confirmation facet must be a ValueHolder. Use e. g. <tc:out>.");
    }
    return null;
  }
}
