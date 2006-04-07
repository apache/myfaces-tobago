package org.apache.myfaces.tobago.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SWITCH_TYPE;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UITabGroup extends UIPanel implements TabChangeSource, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

  public static final String RENDERED_INDEX
      = "org.apache.myfaces.tobago.component.UITabGroup.RENDERED_INDEX";

  private int activeIndex;
  private int renderedIndex;

  private MethodBinding tabChangeListener = null;
  public static final String SWITCH_TYPE_CLIENT = "client";
  public static final String SWITCH_TYPE_RELOAD_PAGE = "reloadPage";
  public static final String SWITCH_TYPE_RELOAD_TAB = "reloadTab";

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {

    ValueBinding stateBinding = getValueBinding(ATTR_STATE);
    Object state
        = stateBinding != null ? stateBinding.getValue(facesContext) : null;
    if (state instanceof Integer) {
      activeIndex = ((Integer) state).intValue();
    } else if (state != null) {
      LOG.warn("Illegal class in stateBinding: " + state.getClass().getName());
    }

    setRenderedIndex(activeIndex);
    super.encodeBegin(facesContext);
  }

  @Override
  public void encodeChildren(FacesContext context)
      throws IOException {
    // childeren are rendered by encodeEnd'jsp
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    resetTabLayout();
    super.encodeEnd(facesContext);
  }



  private void resetTabLayout() {
    for (UIPanel tab : getTabs()) {
        tab.getAttributes().remove(ATTR_LAYOUT_WIDTH);
        tab.getAttributes().remove(ATTR_LAYOUT_HEIGHT);
    }
  }

  public UIPanel[] getTabs() {
    List<UIPanel> tabs = new ArrayList<UIPanel>();
    for (Object o : getChildren()) {
      UIComponent kid = (UIComponent) o;
      if (kid instanceof UIPanel) {
        if (kid.isRendered()) {
          tabs.add((UIPanel) kid);
        }
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return tabs.toArray(new UIPanel[tabs.size()]);
  }

  public UIPanel getActiveTab() {
    return getTabs()[activeIndex];
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
      UIPanel renderedTab = getTabs()[getRenderedIndex()];
      renderedTab.processDecodes(context);
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
      UIPanel renderedTab = getTabs()[getRenderedIndex()];
      renderedTab.processValidators(context);
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
      UIPanel renderedTab = getTabs()[getRenderedIndex()];
      renderedTab.processUpdates(context);
      updateState(context);
    } else {
      super.processUpdates(context);
      updateState(context);
    }
  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);

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
  }

  public void setTabChangeListener(MethodBinding tabStateChangeListener) {
    this.tabChangeListener = tabStateChangeListener;
  }

  public MethodBinding getTabChangeListener() {
    return tabChangeListener;
  }

  public void updateState(FacesContext facesContext) {
    ValueBinding stateBinding = getValueBinding(ATTR_STATE);
    if (stateBinding != null) {
      stateBinding.setValue(facesContext, activeIndex);
    }
  }

  public void addTabChangeListener(TabChangeListener listener) {
    if (LOG.isWarnEnabled() && isClientType()) {
      LOG.warn("Adding TabChangeListener to Client side Tabgroup!");
    }
    addFacesListener(listener);
  }

  private boolean isClientType() {
    final String switchType
        = ComponentUtil.getStringAttribute(this, ATTR_SWITCH_TYPE);
    return (switchType == null || switchType.equals(SWITCH_TYPE_CLIENT));
  }

  public void removeTabChangeListener(TabChangeListener listener) {
    removeFacesListener(listener);
  }

  public TabChangeListener[] getTabChangeListeners() {
    return (TabChangeListener[]) getFacesListeners(TabChangeListener.class);
  }

  public Object saveState(FacesContext context) {
    Object[] state = new Object[4];
    state[0] = super.saveState(context);
    state[1] = new Integer(renderedIndex);
    state[2] = new Integer(activeIndex);
    state[3] = saveAttachedState(context, tabChangeListener);
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    renderedIndex = ((Integer) values[1]).intValue();
    activeIndex = ((Integer) values[2]).intValue();
    tabChangeListener = (MethodBinding) restoreAttachedState(context, values[3]);
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    if (activeIndex < 0 || !(activeIndex < getTabs().length)) {
      LOG.error("This should never occur! Problem in decoding?");
      ValueBinding stateBinding = getValueBinding(ATTR_STATE);
      Object state
          = stateBinding != null ? stateBinding.getValue(facesContext) : null;
      if (state instanceof Integer) {
        activeIndex = ((Integer) state).intValue();
      } else if (state != null) {
        LOG.warn("Illegal class in stateBinding: " + state.getClass().getName());
      }
    }

    setRenderedIndex(activeIndex);
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public int getActiveIndex() {
    return activeIndex;
  }

  public void setActiveIndex(int activeIndex) {
    this.activeIndex = activeIndex;
  }

  private void setRenderedIndex(int index) {
    renderedIndex = index;
  }

  public int getRenderedIndex() {
    return renderedIndex;
  }
}
