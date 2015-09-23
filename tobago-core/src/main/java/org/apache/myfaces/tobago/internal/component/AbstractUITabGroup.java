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

import org.apache.myfaces.tobago.compat.FacesUtilsEL;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUITabGroup extends AbstractUIPanel
    implements TabChangeSource2, ActionSource2, OnComponentPopulated, SupportsRenderedPartially, Visual {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

  public static final String SWITCH_TYPE_CLIENT = "client";
  public static final String SWITCH_TYPE_RELOAD_PAGE = "reloadPage";
  public static final String SWITCH_TYPE_RELOAD_TAB = "reloadTab";

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    resetTabLayout();
    super.encodeEnd(facesContext);
    setRenderedIndex(getSelectedIndex());
  }


  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public void queueEvent(final FacesEvent event) {
    if (this == event.getSource()) {
      if (isImmediate() || isSwitchTypeClient()) {
        // if switch type client event is always immediate
        event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        event.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
    super.queueEvent(event);
  }

  private void resetTabLayout() {
    for (final UIComponent component : getChildren()) {
      component.getAttributes().remove(Attributes.LAYOUT_WIDTH);
      component.getAttributes().remove(Attributes.LAYOUT_HEIGHT);
    }
  }

  public AbstractUIPanel[] getTabs() {
    final List<AbstractUIPanel> tabs = new ArrayList<AbstractUIPanel>();
    for (final UIComponent kid : getChildren()) {
      if (kid instanceof AbstractUIPanel) {
        //if (kid.isRendered()) {
        tabs.add((AbstractUIPanel) kid);
        //}
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return tabs.toArray(new AbstractUIPanel[tabs.size()]);
  }

  public AbstractUIPanel getActiveTab() {
    return getTab(getSelectedIndex());
  }


  @Override
  public void processDecodes(final FacesContext context) {
    if (!isSwitchTypeClient()) {

      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      int index = 0;
      for (final UIComponent child : getChildren()) {
        if (child instanceof UITab) {
          final UITab tab = (UITab) child;
          if (tab.isRendered()) {
            if (getRenderedIndex() == index) {
              tab.processDecodes(context);
            } else {
              UIComponent facet = tab.getFacet(Facets.TOOL_BAR);
              if (facet != null) {
                facet.processDecodes(context);
              }
            }
          }
          index++;
        }
      }
//      final AbstractUIPanelBase renderedTab = getRenderedTab();
//      renderedTab.processDecodes(context);
      for (final UIComponent facet : getFacets().values()) {
        facet.processDecodes(context);
      }
      try {
        decode(context);
      } catch (final RuntimeException e) {
        context.renderResponse();
        throw e;
      }
    } else {
      super.processDecodes(context);
    }
  }

  @Override
  public void processValidators(final FacesContext context) {
    if (!isSwitchTypeClient()) {
      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      final AbstractUIPanel renderedTab = getRenderedTab();
      renderedTab.processValidators(context);
      for (final UIComponent facet : getFacets().values()) {
        facet.processValidators(context);
      }
    } else {
      super.processValidators(context);
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    if (!isSwitchTypeClient()) {
      if (context == null) {
        throw new NullPointerException("context");
      }
      if (!isRendered()) {
        return;
      }
      final AbstractUIPanel renderedTab = getRenderedTab();
      renderedTab.processUpdates(context);
      for (final UIComponent facet : getFacets().values()) {
        facet.processUpdates(context);
      }

    } else {
      super.processUpdates(context);
    }
  }

  public void broadcast(final FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof TabChangeEvent && facesEvent.getComponent() == this) {
      final TabChangeEvent event = (TabChangeEvent) facesEvent;

      final MethodExpression methodExpression = getTabChangeListenerExpression();
      if (methodExpression != null) {
        FacesUtilsEL.invokeMethodExpression(FacesContext.getCurrentInstance(), methodExpression, facesEvent);
      }

// switched off, because this is already called in super.broadcast()
//      final ActionListener[] actionListeners = getActionListeners();
//      for (ActionListener listener : actionListeners) {
//        listener.processAction(event);
//      }

      // XXX is this needed?
      if (!isSwitchTypeClient()) {
        final ActionListener defaultActionListener = getFacesContext().getApplication().getActionListener();
        if (defaultActionListener != null) {
          defaultActionListener.processAction(event);
        }
      }
      final Integer index = event.getNewTabIndex();
      final ValueExpression expression = getValueExpression(Attributes.SELECTED_INDEX);
      if (expression != null) {
        expression.setValue(getFacesContext().getELContext(), index);
      } else {
        setSelectedIndex(index);
      }
    }
  }

  public void addTabChangeListener(final TabChangeListener listener) {
    if (LOG.isWarnEnabled() && isSwitchTypeClient()) {
      LOG.warn("Adding TabChangeListener to client side TabGroup!");
    }
    addFacesListener(listener);
  }

  public boolean isSwitchTypeClient() {
    final String switchType = getSwitchType();
    return (switchType == null || switchType.equals(SWITCH_TYPE_CLIENT));
  }

  public void removeTabChangeListener(final TabChangeListener listener) {
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

  private AbstractUIPanel getTab(final int index) {
    int i = 0;
    for (final UIComponent component : getChildren()) {
      if (component instanceof AbstractUIPanel) {
        if (i == index) {
          return (AbstractUIPanel) component;
        }
        i++;
      } else {
        LOG.error("Invalid component in UITabGroup: " + component);
      }
    }
    LOG.error("Found no component with index: " + index + " childCount: " + getChildCount());
    return null;
  }

  private AbstractUIPanel getRenderedTab() {
    return getTab(getRenderedIndex());
  }


  /**
   * @since 1.5.0
   */
  public void addActionListener(final ActionListener listener) {
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
  public void removeActionListener(final ActionListener listener) {
    removeFacesListener(listener);
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
    super.onComponentPopulated(facesContext, parent);
  }
}
