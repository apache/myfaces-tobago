package org.apache.myfaces.tobago.internal.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractUITabGroup extends UIPanelBase
    implements TabChangeSource, ActionSource, LayoutContainer, LayoutComponent, OnComponentPopulated {

  private static final Log LOG = LogFactory.getLog(AbstractUITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

  public static final String SWITCH_TYPE_CLIENT = "client";
  public static final String SWITCH_TYPE_RELOAD_PAGE = "reloadPage";
  public static final String SWITCH_TYPE_RELOAD_TAB = "reloadTab";

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {

    super.encodeBegin(facesContext);

    ((UILayoutBase) getLayoutManager()).encodeBegin(facesContext);
  }

  @Override
  public void encodeChildren(FacesContext facesContext) throws IOException {

//    ((UILayoutBase) getLayoutManager()).encodeChildren(facesContext);
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {

    ((UILayoutBase) getLayoutManager()).encodeEnd(facesContext);

    resetTabLayout();
    super.encodeEnd(facesContext);
    setRenderedIndex(getSelectedIndex());
  }


  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public void queueEvent(FacesEvent event) {
    if (this == event.getSource()) {
      if (isImmediate()) {
        event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        event.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
    super.queueEvent(event);
  }

  private void resetTabLayout() {
    for (UIComponent component : (List<UIComponent>) getChildren()) {
      component.getAttributes().remove(Attributes.LAYOUT_WIDTH);
      component.getAttributes().remove(Attributes.LAYOUT_HEIGHT);
    }
  }

  public UIPanelBase[] getTabs() {
    List<UIPanelBase> tabs = new ArrayList<UIPanelBase>();
    for (Object o : getChildren()) {
      UIComponent kid = (UIComponent) o;
      if (kid instanceof UIPanelBase) {
        //if (kid.isRendered()) {
        tabs.add((UIPanelBase) kid);
        //}
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return tabs.toArray(new UIPanelBase[tabs.size()]);
  }

  public UIPanelBase getActiveTab() {
    return getTab(getSelectedIndex());
  }


  @Override
  public void processDecodes(FacesContext context) {
    if (!isClientType()) {

      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      UIPanelBase renderedTab = getRenderedTab();
      renderedTab.processDecodes(context);
      for (UIComponent facet : (Collection<UIComponent>) getFacets().values()) {
        facet.processDecodes(context);
      }
      try {
        decode(context);
      } catch (RuntimeException e) {
        context.renderResponse();
        throw e;
      }
    } else {
      super.processDecodes(context);
    }
  }

  @Override
  public void processValidators(FacesContext context) {
    if (!isClientType()) {
      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      UIPanelBase renderedTab = getRenderedTab();
      renderedTab.processValidators(context);
      for (UIComponent facet : (Collection<UIComponent>) getFacets().values()) {
        facet.processValidators(context);
      }
    } else {
      super.processValidators(context);
    }
  }

  @Override
  public void processUpdates(FacesContext context) {
    if (!isClientType()) {
      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      UIPanelBase renderedTab = getRenderedTab();
      renderedTab.processUpdates(context);
      for (UIComponent facet : (Collection<UIComponent>) getFacets().values()) {
        facet.processUpdates(context);
      }

    } else {
      super.processUpdates(context);
    }
  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof TabChangeEvent && facesEvent.getComponent() == this) {
      Integer index = ((TabChangeEvent) facesEvent).getNewTabIndex();
      if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.SELECTED_INDEX)) {
        FacesUtils.setValueOfBindingOrExpression(getFacesContext(), index, this, Attributes.SELECTED_INDEX);
      } else {
        setSelectedIndex(index);
      }
      MethodBinding tabChangeListenerBinding = getTabChangeListener();
      if (tabChangeListenerBinding != null) {
        try {
          tabChangeListenerBinding.invoke(getFacesContext(), new Object[]{facesEvent});
        } catch (EvaluationException e) {
          Throwable cause = e.getCause();
          if (cause != null && cause instanceof AbortProcessingException) {
            throw (AbortProcessingException) cause;
          } else {
            throw e;
          }
        }
      }
      MethodBinding actionListenerBinding = getActionListener();
      if (actionListenerBinding != null) {
        try {
          actionListenerBinding.invoke(getFacesContext(), new Object[]{facesEvent});
        } catch (EvaluationException e) {
          Throwable cause = e.getCause();
          if (cause != null && cause instanceof AbortProcessingException) {
            throw (AbortProcessingException) cause;
          } else {
            throw e;
          }
        }
      }

      ActionListener defaultActionListener
          = getFacesContext().getApplication().getActionListener();
      if (defaultActionListener != null) {
        defaultActionListener.processAction((ActionEvent) facesEvent);
      }
    }
  }

  public void addTabChangeListener(TabChangeListener listener) {
    if (LOG.isWarnEnabled() && isClientType()) {
      LOG.warn("Adding TabChangeListener to Client side Tabgroup!");
    }
    addFacesListener(listener);
  }

  private boolean isClientType() {
    String switchType = getSwitchType();
    return (switchType == null || switchType.equals(SWITCH_TYPE_CLIENT));
  }

  public void removeTabChangeListener(TabChangeListener listener) {
    removeFacesListener(listener);
  }

  public TabChangeListener[] getTabChangeListeners() {
    return (TabChangeListener[]) getFacesListeners(TabChangeListener.class);
  }

  public abstract Integer getRenderedIndex();

  public abstract void setRenderedIndex(Integer index);

  public abstract Integer getSelectedIndex();

  public abstract void setSelectedIndex(Integer index);

  public abstract String getSwitchType();

  private UIPanelBase getTab(int index) {
    int i = 0;
    for (UIComponent component : (List<UIComponent>) getChildren()) {
      if (component instanceof UIPanelBase) {
        if (i == index) {
          return (UIPanelBase) component;
        }
        i++;
      } else {
        LOG.error("Invalid component in UITabGroup: " + component);
      }
    }
    LOG.error("Found no component with index: " + index + " childCount: " + getChildCount());
    return null;
  }

  private UIPanelBase getRenderedTab() {
    return getTab(getRenderedIndex());
  }


  /**
   * @since 1.5.0
   */
  public void addActionListener(ActionListener listener) {
    addFacesListener(listener);
  }

  /**
   * @since 1.5.0
   */
  public ActionListener[] getActionListeners() {
    return (ActionListener[]) getFacesListeners(ActionListener.class);
  }

  /**
   * @since 1.5.0
   */
  public void removeActionListener(ActionListener listener) {
    removeFacesListener(listener);
  }

  // LAYOUT Begin
  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.TAB_GROUP_LAYOUT, RendererTypes.TAB_GROUP_LAYOUT, parent));
    }
  }
  
  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (UILayoutBase) layoutManager);
  }

// LAYOUT End
}
