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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.DeprecatedDimension;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxResponseRenderer;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.PageState;
import org.apache.myfaces.tobago.model.PageStateImpl;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.ProcessValidationsCallback;
import org.apache.myfaces.tobago.util.TobagoCallback;
import org.apache.myfaces.tobago.util.UpdateModelValuesCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractUIPage extends AbstractUIForm
    implements OnComponentPopulated, LayoutContainer, DeprecatedDimension {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final String FORM_ACCEPT_CHARSET = "utf-8";

  private static final TobagoCallback APPLY_REQUEST_VALUES_CALLBACK = new ApplyRequestValuesCallback();
  private static final ContextCallback PROCESS_VALIDATION_CALLBACK = new ProcessValidationsCallback();
  private static final ContextCallback UPDATE_MODEL_VALUES_CALLBACK = new UpdateModelValuesCallback();

  private String formId;

  private String actionId;

  private Box actionPosition;

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public String getFormId(final FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form";
    }
    return formId;
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    if (!AjaxUtils.isAjaxRequest(facesContext)) {
      super.encodeBegin(facesContext);
      ((AbstractUILayoutBase) getLayoutManager()).encodeBegin(facesContext);
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {
    if (AjaxUtils.isAjaxRequest(facesContext)) {
      new AjaxResponseRenderer().renderResponse(facesContext);
    } else {
      ((AbstractUILayoutBase) getLayoutManager()).encodeChildren(facesContext);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    if (!AjaxUtils.isAjaxRequest(facesContext)) {
      ((AbstractUILayoutBase) getLayoutManager()).encodeEnd(facesContext);
      super.encodeEnd(facesContext);
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace(DebugUtils.toString(this.getParent(), 0));
    }
  }

  private void processDecodes0(final FacesContext facesContext) {

    decode(facesContext);

    markSubmittedForm(facesContext);

    // invoke processDecodes() on children
    for (final Iterator kids = getFacetsAndChildren(); kids.hasNext();) {
      final UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }
  }

  @Override
  public void processDecodes(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.parseAndStoreComponents(context);
    if (ajaxComponents != null) {
      // first decode the page
      final AbstractUIPage page = ComponentUtils.findPage(context);
      page.decode(context);
      page.markSubmittedForm(context);
      FacesContextUtils.setAjax(context, true);

      // decode the action if actionComponent not inside one of the ajaxComponents
      // otherwise it is decoded there
      decodeActionComponent(context, page, ajaxComponents);

      // and all ajax components
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        FacesContextUtils.setAjaxComponentId(context, entry.getKey());
        invokeOnComponent(context, entry.getKey(), APPLY_REQUEST_VALUES_CALLBACK);
      }
    } else {
      processDecodes0(context);
    }
  }

  private void decodeActionComponent(
      final FacesContext facesContext, final AbstractUIPage page, final Map<String, UIComponent> ajaxComponents) {
    final String id = page.getActionId();
    UIComponent actionComponent = null;
    if (id != null) {
      actionComponent = findComponent(id);
      if (actionComponent == null && FacesVersion.supports20() && FacesVersion.isMyfaces()) {
        final String bugActionId = id.replaceAll(":\\d+:", ":");
        try {
          actionComponent = findComponent(bugActionId);
          //LOG.info("command = \"" + actionComponent + "\"", new Exception());
        } catch (final Exception e) {
          // ignore
        }
      }
    }
    if (actionComponent == null) {
      return;
    }
    for (final UIComponent ajaxComponent : ajaxComponents.values()) {
      UIComponent component = actionComponent;
      while (component != null) {
        if (component == ajaxComponent) {
          return;
        }
        component = component.getParent();
      }
    }
    invokeOnComponent(facesContext, id, APPLY_REQUEST_VALUES_CALLBACK);
  }


  @Override
  public void processValidators(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }

    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        FacesContextUtils.setAjaxComponentId(context, entry.getKey());
        invokeOnComponent(context, entry.getKey(), PROCESS_VALIDATION_CALLBACK);
      }
    } else {
      super.processValidators(context);
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        invokeOnComponent(context, entry.getKey(), UPDATE_MODEL_VALUES_CALLBACK);
      }
    } else {
      super.processUpdates(context);
    }
  }

  public void markSubmittedForm(final FacesContext facesContext) {
    // find the form of the action command and set submitted to it and all
    // children

    // reset old submitted state
    setSubmitted(false);

    String currentActionId = getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + currentActionId + "'");
    }

    final UIViewRoot viewRoot = facesContext.getViewRoot();
    UIComponent command = viewRoot.findComponent(currentActionId);

    if (command == null && currentActionId != null) {
      // If currentActionId component was inside a sheet the id contains the
      // rowIndex and is therefore not found here.
      // We do not need the row here because we want just to find the
      // related form, so removing the rowIndex will help here.
      currentActionId = cutIteratorFromId(currentActionId);
      try {
        command = viewRoot.findComponent(currentActionId);
      } catch (final Exception e) {
        // ignore
        if (LOG.isTraceEnabled()) {
          LOG.trace("sourceId='{}'", currentActionId);
          LOG.trace("Exception in findComponent", e);
        }
      }
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(currentActionId);
      LOG.trace("command:{}", command);
      LOG.trace(DebugUtils.toString(viewRoot, 0));
    }

    if (command != null) {
      final AbstractUIForm form = ComponentUtils.findForm(command);
      form.setSubmitted(true);

      if (LOG.isTraceEnabled()) {
        LOG.trace("form:{}", form);
        LOG.trace(form.getClientId(facesContext));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Illegal actionId! Render response...");
      }
      facesContext.renderResponse();
    }
  }

  // TODO: Remove this method if proven this never happens anymore
  // TODO: This workaround is stil needed for Mojarra
  // TODO: Otherwise actions in tree/sheet will not be detected
  protected String cutIteratorFromId(final String sourceId) {

    final char[] chars = sourceId.toCharArray();
    final int n = chars.length;
    final char colon = ':';
    final StringBuilder builder = new StringBuilder(n);
    char lastInBuilder = ' '; // any non-colon
    for (char c : chars) {
      if (c == colon) { // colon
        if (lastInBuilder != colon) {
          builder.append(c);
          lastInBuilder = c;
        }
      } else if (lastInBuilder == colon && '0' <= c && c <= '9') { // number

      } else { // any other
        builder.append(c);
        lastInBuilder = c;
      }
    }

    if (builder.length() == n) {
      return sourceId;
    } else if (lastInBuilder == colon) {
      builder.deleteCharAt(builder.length() - 1);
      return builder.toString();
    } else {
      return builder.toString();
    }
  }

  /**
   * @deprecated PageState is deprecated since 1.5.0
   */
  @Deprecated
  public void updatePageState(final FacesContext facesContext) {
  }

  /**
   * @deprecated PageState is deprecated since 1.5.0
   */
  @Deprecated
  public PageState getPageState(final FacesContext facesContext) {
    final ValueExpression expression = getValueExpression(Attributes.STATE);
    if (expression != null) {
      final ELContext elContext = facesContext.getELContext();
      PageState state = (PageState) expression.getValue(elContext);
      if (state == null) {
        state = new PageStateImpl();
        expression.setValue(elContext, state);
      }
      return state;
    } else {
      return null;
    }
  }

  public String getActionId() {
    return actionId;
  }

  public void setActionId(final String actionId) {
    this.actionId = actionId;
  }

  public Box getActionPosition() {
    return actionPosition;
  }

  public void setActionPosition(final Box actionPosition) {
    this.actionPosition = actionPosition;
  }

  /**
   * @deprecated since 1.5.7 and 2.0.0
   */
  @Deprecated
  public String getDefaultActionId() {
    Deprecation.LOG.error("The default action handling has been changed!");
    return null;
  }

  /**
   * @deprecated since 1.5.7 and 2.0.0
   */
  @Deprecated
  public void setDefaultActionId(final String defaultActionId) {
    Deprecation.LOG.error("The default action handling has been changed!");
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT, parent));
    }
  }

  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public LayoutManager getLayoutManager() {
    final UIComponent facet = getFacet(Facets.LAYOUT);
    if (facet == null) {
      return null;
    } else if (facet instanceof LayoutManager) {
      return (LayoutManager) facet;
    } else {
      return (LayoutManager) ComponentUtils.findChild(facet, AbstractUILayoutBase.class);
    }
  }

  public void setLayoutManager(final LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return isRendered();
  }

  public abstract Measure getWidth();

  public abstract Measure getHeight();
}
