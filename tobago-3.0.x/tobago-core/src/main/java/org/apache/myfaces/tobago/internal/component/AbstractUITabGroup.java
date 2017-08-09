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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UITab;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource2;
import org.apache.myfaces.tobago.model.SwitchType;
import org.apache.myfaces.tobago.util.FacesELUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.component.ActionSource2;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUITabGroup extends AbstractUIPanelBase
    implements TabChangeSource2, ActionSource2, ClientBehaviorHolder, Visual {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUITabGroup.class);

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    super.encodeEnd(facesContext);
    setRenderedIndex(getSelectedIndex());
  }


  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void queueEvent(final FacesEvent event) {
    if (this == event.getSource()) {
      if (isImmediate() || getSwitchType() == SwitchType.client) {
        // if switch type client event is always immediate
        event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        event.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
    super.queueEvent(event);
  }

  public AbstractUITab[] getTabs() {
    final List<AbstractUITab> tabs = new ArrayList<AbstractUITab>();
    for (final UIComponent kid : getChildren()) {
      if (kid instanceof AbstractUITab) {
        //if (kid.isRendered()) {
        tabs.add((AbstractUITab) kid);
        //}
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return tabs.toArray(new AbstractUITab[tabs.size()]);
  }

  public AbstractUITab getActiveTab() {
    return getTab(getSelectedIndex());
  }


  @Override
  public void processDecodes(final FacesContext context) {
    if (!(getSwitchType() == SwitchType.client)) {

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
    if (!(getSwitchType() == SwitchType.client)) {
      if (!isRendered()) {
        return;
      }
      final AbstractUITab renderedTab = getRenderedTab();
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
    if (!(getSwitchType() == SwitchType.client)) {
      if (!isRendered()) {
        return;
      }
      final AbstractUITab renderedTab = getRenderedTab();
      renderedTab.processUpdates(context);
      for (final UIComponent facet : getFacets().values()) {
        facet.processUpdates(context);
      }

    } else {
      super.processUpdates(context);
    }
  }

  @Override
  public void broadcast(final FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof TabChangeEvent && facesEvent.getComponent() == this) {
      final TabChangeEvent event = (TabChangeEvent) facesEvent;

      final MethodExpression methodExpression = getTabChangeListenerExpression();
      if (methodExpression != null) {
        FacesELUtils.invokeMethodExpression(FacesContext.getCurrentInstance(), methodExpression, facesEvent);
      }

// switched off, because this is already called in super.broadcast()
//      final ActionListener[] actionListeners = getActionListeners();
//      for (ActionListener listener : actionListeners) {
//        listener.processAction(event);
//      }

      // XXX is this needed?
      if (!(getSwitchType() == SwitchType.client)) {
        final ActionListener defaultActionListener = getFacesContext().getApplication().getActionListener();
        if (defaultActionListener != null) {
          defaultActionListener.processAction(event);
        }
      }
      final Integer index = event.getNewTabIndex();
      final ValueExpression expression = getValueExpression(Attributes.selectedIndex.getName());
      if (expression != null) {
        expression.setValue(getFacesContext().getELContext(), index);
      } else {
        setSelectedIndex(index);
      }
    }
  }

  @Override
  public void addTabChangeListener(final TabChangeListener listener) {
    if (LOG.isWarnEnabled() && getSwitchType() == SwitchType.client) {
      LOG.warn("Adding TabChangeListener to client side TabGroup!");
    }
    addFacesListener(listener);
  }

  @Override
  public void removeTabChangeListener(final TabChangeListener listener) {
    removeFacesListener(listener);
  }

  @Override
  public TabChangeListener[] getTabChangeListeners() {
    return (TabChangeListener[]) getFacesListeners(TabChangeListener.class);
  }

  public abstract Integer getRenderedIndex();

  public abstract void setRenderedIndex(Integer index);

  public abstract Integer getSelectedIndex();

  public abstract void setSelectedIndex(Integer index);

  public abstract SwitchType getSwitchType();

  private AbstractUITab getTab(final int index) {
    int i = 0;
    for (final UIComponent component : getChildren()) {
      if (component instanceof AbstractUITab) {
        if (i == index) {
          return (AbstractUITab) component;
        }
        i++;
      } else {
        LOG.error("Invalid component in UITabGroup: " + component);
      }
    }
    LOG.error("Found no component with index: " + index + " childCount: " + getChildCount());
    return null;
  }

  private AbstractUITab getRenderedTab() {
    return getTab(getRenderedIndex());
  }


  /**
   * @since 1.5.0
   */
  @Override
  public void addActionListener(final ActionListener listener) {
    addFacesListener(listener);
  }

  /**
   * @since 1.5.0
   */
  @Override
  public ActionListener[] getActionListeners() {
    return (ActionListener[]) getFacesListeners(ActionListener.class);
  }

  /**
   * @since 1.5.0
   */
  @Override
  public void removeActionListener(final ActionListener listener) {
    removeFacesListener(listener);
  }
}
