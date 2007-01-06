package org.apache.myfaces.tobago.component;

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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMMEDIATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SWITCH_TYPE;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.TabChangeSource;
import org.apache.myfaces.tobago.event.TabChangeEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UITabGroup extends UIPanel implements TabChangeSource, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

  private int activeIndex;
  private int renderedIndex;
  private String switchType;
  private Boolean immediate;
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
      activeIndex = (Integer) state;
    } else if (state != null) {
      LOG.warn("Illegal class in stateBinding: " + state.getClass().getName());
    }

    setRenderedIndex(activeIndex);
    super.encodeBegin(facesContext);
  }

  public void setImmediate(boolean immediate) {
    this.immediate = immediate;
  }

   public boolean isImmediate() {
    if (immediate != null) {
      return immediate;
    }
    ValueBinding vb = getValueBinding(ATTR_IMMEDIATE);
    if (vb != null) {
      return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
      return false;
    }
  }

  public void queueEvent(FacesEvent event) {
    if (isImmediate()) {
      event.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
    } else {
      event.setPhaseId(PhaseId.INVOKE_APPLICATION);
    }
    super.queueEvent(event);
  }

  @Override
  public void encodeChildren(FacesContext context)
      throws IOException {
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    resetTabLayout();
    super.encodeEnd(facesContext);
  }

  private void resetTabLayout() {
    for (UIComponent component : (List<UIComponent>)getChildren()) {
      component.getAttributes().remove(ATTR_LAYOUT_WIDTH);
      component.getAttributes().remove(ATTR_LAYOUT_HEIGHT);
    }
  }

  public UIPanel[] getTabs() {
    List<UIPanel> tabs = new ArrayList<UIPanel>();
    for (Object o : getChildren()) {
      UIComponent kid = (UIComponent) o;
      if (kid instanceof UIPanel) {
        //if (kid.isRendered()) {
          tabs.add((UIPanel) kid);
        //}
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return tabs.toArray(new UIPanel[tabs.size()]);
  }

  public UIPanel getActiveTab() {
    return getTab(getActiveIndex());
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
      UIPanel renderedTab = getRenderedTab();
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
      UIPanel renderedTab = getRenderedTab();
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
      UIPanel renderedTab = getRenderedTab();
      renderedTab.processUpdates(context);
      updateState(context);
    } else {
      super.processUpdates(context);
      updateState(context);
    }
  }

  public void broadcast(FacesEvent facesEvent) throws AbortProcessingException {
    super.broadcast(facesEvent);
    if (facesEvent instanceof TabChangeEvent) {
      setActiveIndex(((TabChangeEvent)facesEvent).getNewTabIndex());
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
    return (switchType == null || switchType.equals(SWITCH_TYPE_CLIENT));
  }

  public void removeTabChangeListener(TabChangeListener listener) {
    removeFacesListener(listener);
  }

  public TabChangeListener[] getTabChangeListeners() {
    return (TabChangeListener[]) getFacesListeners(TabChangeListener.class);
  }

  public Object saveState(FacesContext context) {
    Object[] state = new Object[6];
    state[0] = super.saveState(context);
    state[1] = renderedIndex;
    state[2] = activeIndex;
    state[3] = saveAttachedState(context, tabChangeListener);
    state[4] = switchType;
    state[5] = immediate;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    renderedIndex = (Integer) values[1];
    activeIndex = (Integer) values[2];
    tabChangeListener = (MethodBinding) restoreAttachedState(context, values[3]);
    switchType = (String) values[4];
    immediate = (Boolean) values[5];
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    if (activeIndex < 0 || !(activeIndex < getChildCount())) {
      LOG.error("This should never occur! Problem in decoding?");
      ValueBinding stateBinding = getValueBinding(ATTR_STATE);
      Object state
          = stateBinding != null ? stateBinding.getValue(facesContext) : null;
      if (state instanceof Integer) {
        activeIndex = (Integer) state;
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

  public String getSwitchType() {
    String value = null;
    if (switchType != null) {
      value = switchType;
    } else {
      ValueBinding vb = getValueBinding(ATTR_SWITCH_TYPE);
      if (vb != null) {
        value = (String) vb.getValue(FacesContext.getCurrentInstance());
      }
    }

    if (SWITCH_TYPE_CLIENT.equals(value)
        || SWITCH_TYPE_RELOAD_PAGE.equals(value)
        || SWITCH_TYPE_RELOAD_TAB.equals(value)) {
      return value;
    } else if (value == null){
      // return default
      return SWITCH_TYPE_CLIENT;
    } else {
      LOG.warn("Illegal value for attribute switchtype : " + switchType
          + " Using default value " + SWITCH_TYPE_CLIENT);
      return SWITCH_TYPE_CLIENT;
    }
  }

  public void setSwitchType(String switchType) {
    this.switchType = switchType;
  }

  private UIPanel getTab(int index) {
    int i = 0;
    for (UIComponent component : (List<UIComponent>)getChildren()) {
      if (component instanceof UIPanel) {
        if (i == index) {
          return (UIPanel) component;
        }
        i++;
      } else {
        LOG.error("Invalid component in UITabGroup: " + component);
      }
    }
    System.err.println("Found no component with "+ index + " " + getChildCount());
    return null;
  }

  private UIPanel getRenderedTab() {
    return getTab(getRenderedIndex());
  }
}
