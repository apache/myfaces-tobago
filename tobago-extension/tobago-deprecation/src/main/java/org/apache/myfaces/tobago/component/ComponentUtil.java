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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.event.SheetStateChangeEvent;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;

import javax.faces.application.Application;
import javax.faces.component.ActionSource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
 */
@Deprecated
public class ComponentUtil {

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static final Class[] ACTION_ARGS = {};
  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static final Class[] ACTION_LISTENER_ARGS = {ActionEvent.class};
  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static final Class[] VALUE_CHANGE_LISTENER_ARGS = {ValueChangeEvent.class};
  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static final Class[] VALIDATOR_ARGS = {FacesContext.class, UIComponent.class, Object.class};

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  private ComponentUtil() {
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean hasErrorMessages(final FacesContext context) {
    return ComponentUtils.hasErrorMessages(context);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean containsPopupActionListener(final javax.faces.component.UICommand command) {
    return ComponentUtils.containsPopupActionListener(command);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String getFacesMessageAsString(final FacesContext facesContext, final UIComponent component) {
    return ComponentUtils.getFacesMessageAsString(facesContext, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isInPopup(final UIComponent component) {
    return ComponentUtils.isInPopup(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void resetPage(final FacesContext context) {
    ComponentUtils.resetPage(context);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIPage findPage(final FacesContext context, final UIComponent component) {
    return (UIPage) ComponentUtils.findPage(context, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIPage findPage(final UIComponent component) {
    return (UIPage) ComponentUtils.findPage(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void addStyles(final UIComponent component, final String[] styles) {
    ((TobagoFacesContext) FacesContext.getCurrentInstance()).getStyleFiles().addAll(Arrays.asList(styles));
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void addScripts(final UIComponent component, final String[] scripts) {
    ((TobagoFacesContext) FacesContext.getCurrentInstance()).getScriptFiles().addAll(Arrays.asList(scripts));
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void addOnloadCommands(final UIComponent component, final String[] cmds) {
    ((TobagoFacesContext) FacesContext.getCurrentInstance()).getOnloadScripts().addAll(Arrays.asList(cmds));
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIPage findPage(final FacesContext facesContext) {
    return (UIPage) ComponentUtils.findPage(facesContext);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIForm findForm(final UIComponent component) {
    return (UIForm) ComponentUtils.findForm(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static List<UIForm> findSubForms(final UIComponent component) {
    return new ArrayList<UIForm>((List) ComponentUtils.findSubForms(component));
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static <T extends UIComponent> T findDescendant(final UIComponent component, final Class<T> type) {
    return ComponentUtils.findDescendant(component, type);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String findClientIdFor(final UIComponent component, final FacesContext facesContext) {
    return ComponentUtils.findClientIdFor(component, facesContext);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent findFor(final UIComponent component) {
    return ComponentUtils.findFor(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isInActiveForm(final UIComponent component) {
    return ComponentUtils.isInActiveForm(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isError(final javax.faces.component.UIInput uiInput) {
    return ComponentUtils.isError(uiInput);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isError(final UIComponent component) {
    return ComponentUtils.isError(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isOutputOnly(final UIComponent component) {
    return ComponentUtils.isOutputOnly(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean mayValidate(final UIComponent component) {
    return ComponentUtils.mayValidate(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean mayUpdateModel(final UIComponent component) {
    return ComponentUtils.mayUpdateModel(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean getBooleanAttribute(final UIComponent component, final String name) {
    return ComponentUtils.getBooleanAttribute(component, name);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setRenderedPartially(
      final org.apache.myfaces.tobago.component.UICommand command, final String renderers) {
    ((SupportsRenderedPartially) command).setRenderedPartially(new String[]{renderers});
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setStyleClasses(final UIComponent component, final String styleClasses) {
    ComponentUtils.setStyleClasses(component, styleClasses);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setMarkup(final UIComponent markupComponent, final String markup) {
    ComponentUtils.setMarkup(markupComponent, markup);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static Object getAttribute(final UIComponent component, final String name) {
    return ComponentUtils.getAttribute(component, name);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String getStringAttribute(final UIComponent component, final String name) {
    return ComponentUtils.getStringAttribute(component, name);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static int getIntAttribute(final UIComponent component, final String name) {
    return getIntAttribute(component, name, 0);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static int getIntAttribute(final UIComponent component, final String name, final int defaultValue) {
    return ComponentUtils.getIntAttribute(component, name, defaultValue);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static Character getCharacterAttribute(final UIComponent component, final String name) {
    return ComponentUtils.getCharacterAttribute(component, name);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isFacetOf(final UIComponent component, final UIComponent parent) {
    return ComponentUtils.isFacetOf(component, parent);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static LayoutComponentRendererBase getRenderer(final FacesContext facesContext, final UIComponent component) {
    return (LayoutComponentRendererBase) ComponentUtils.getRenderer(facesContext, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static LayoutComponentRendererBase getRenderer(
      final FacesContext facesContext, final String family, final String rendererType) {
    return (LayoutComponentRendererBase) ComponentUtils.getRenderer(facesContext, family, rendererType);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String currentValue(final UIComponent component) {
    return RenderUtils.currentValue(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static List<SelectItem> getSelectItems(final UIComponent component) {
    return RenderUtils.getSelectItems(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static Object findParameter(final UIComponent component, final String name) {
    return ComponentUtils.findParameter(component, name);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String toString(final UIComponent component, final int offset) {
    return DebugUtils.toString(component, offset);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static ActionListener createActionListener(final String type) throws JspException {
    return ComponentUtils.createActionListener(type);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIGraphic getFirstGraphicChild(final UIComponent component) {
    return ComponentUtils.getFirstGraphicChild(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean isHoverEnabled(final UIComponent component) {
    Deprecation.LOG.error("no longer supported");
    return ComponentUtils.getBooleanAttribute(component, Attributes.HOVER);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIOutput getFirstNonGraphicChild(final UIComponent component) {
    return ComponentUtils.getFirstNonGraphicChild(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setIntegerSizeProperty(final UIComponent component, final String name, final String value) {
    ComponentUtils.setIntegerSizeProperty(component, name, value);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String removePx(final String value) {
    return ComponentUtils.removePx(value);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setIntegerProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, new Integer(value));
      }
    }
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setBooleanProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, Boolean.valueOf(value));
      }
    }
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setStringProperty(final UIComponent component, final String name, final String value) {
    if (value != null) {
      if (UIComponentTag.isValueReference(value)) {
        component.setValueBinding(name, createValueBinding(value));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setValueForValueBinding(final String name, final Object value) {
    ComponentUtils.setValueForValueBinding(name, value);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static ValueBinding createValueBinding(final String value) {
    return ComponentUtils.createValueBinding(value);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String getValueFromEl(String script) {
    if (UIComponentTag.isValueReference(script)) {
      final ValueBinding valueBinding = ComponentUtils.createValueBinding(script);
      script = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
    }
    return script;
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent createComponent(final String componentType, final String rendererType, final String id) {
    return CreateComponentUtils.createComponent(componentType, rendererType, id);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType,
      final String rendererType, final String id) {
    return CreateComponentUtils.createComponent(facesContext, componentType, rendererType, id);
  }

  /**
   * Please use createComponent(FacesContext facesContext, String componentType, String rendererType, String id)
   *
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType, final String rendererType) {
    return createComponent(facesContext, componentType, rendererType, null);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIColumn createTextColumn(
      final String label, final String sortable, final String align, final String value, final String id) {
    return (UIColumn) CreateComponentUtils.createTextColumn(label, sortable, align, value, id);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIColumn createColumn(
      final String label, final String sortable, final String align, final UIComponent child) {
    return (UIColumn) CreateComponentUtils.createColumn(label, sortable, align, child);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIColumn createColumn(
      final String label, final String sortable, final String align, final UIComponent child, final String id) {
    return (UIColumn) CreateComponentUtils.createColumn(label, sortable, align, child, id);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIMenuSelectOne createUIMenuSelectOneFacet(
      final FacesContext facesContext, final UICommand command, final String id) {
    return CreateComponentUtils.createUIMenuSelectOneFacet(facesContext, command, id);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static boolean hasSelectedValue(final List<SelectItem> items, final Object value) {
    return ComponentUtils.hasSelectedValue(items, value);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent createUISelectBooleanFacet(
      final FacesContext facesContext, final UICommand command, final String id) {
    return CreateComponentUtils.createUISelectBooleanFacet(facesContext, command, id);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static int getIntValue(final ValueBinding valueBinding) {
    return ComponentUtils.getIntValue(valueBinding);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String createPickerId(
      final FacesContext facesContext, final UIComponent component, final String postfix) {
    return ComponentUtils.createPickerId(facesContext, component, postfix);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String getComponentId(final FacesContext facesContext, final UIComponent component) {
    return ComponentUtils.getComponentId(facesContext, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent provideLabel(final FacesContext facesContext, final UIComponent component) {
    return ComponentUtils.provideLabel(facesContext, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static List<SelectItem> getItemsToRender(final javax.faces.component.UISelectOne component) {
    return RenderUtils.getItemsToRender(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static List<SelectItem> getItemsToRender(final javax.faces.component.UISelectMany component) {
    return RenderUtils.getItemsToRender(component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setValidator(final EditableValueHolder editableValueHolder, final String validator) {
    ComponentUtils.setValidator(editableValueHolder, validator);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setConverter(final ValueHolder valueHolder, final String converterId) {
    ComponentUtils.setConverter(valueHolder, converterId);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setAction(final UICommand component, final String type, final String action) {
    ComponentUtils.setAction(component, action);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setSuggestMethodBinding(final UIIn component, final String suggestMethod) {
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

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setActionListener(final ActionSource command, final String actionListener) {
    ComponentUtils.setActionListener(command, actionListener);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setValueChangeListener(final EditableValueHolder valueHolder, final String valueChangeListener) {
    ComponentUtils.setValueChangeListener(valueHolder, valueChangeListener);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setValueBinding(final UIComponent component, final String name, final String state) {
    ComponentUtils.setValueBinding(component, name, state);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static void setStateChangeListener(final UISheet data, final String stateChangeListener) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();

    if (stateChangeListener != null) {
      if (UIComponentTag.isValueReference(stateChangeListener)) {
        final Class[] arguments = {SheetStateChangeEvent.class};
        final MethodBinding binding
            = application.createMethodBinding(stateChangeListener, arguments);
        data.setStateChangeListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + stateChangeListener);
      }
    }
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static String[] getMarkupBinding(final FacesContext facesContext, final SupportsMarkup component) {
    return ComponentUtils.getMarkupBinding(facesContext, component);
  }

  /**
   * @deprecated Since Tobago 1.5 please use {@link ComponentUtils}
   */
  @Deprecated
  public static UIComponent findComponent(final UIComponent from, final String relativeId) {
    return ComponentUtils.findComponent(from, relativeId);
  }
}
