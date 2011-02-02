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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_NAVIGATION_BAR;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SELECTED_INDEX;
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
import java.util.Collection;

public class UITabGroup extends UIPanelBase implements TabChangeSource, AjaxComponent {

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

  private Integer selectedIndex;
  private int renderedIndex;
  private String switchType;
  private Boolean immediate;
  private Boolean showNavigationBar;
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
    super.encodeBegin(facesContext);
    // encodeBegin() is never called on UITab,
    //  so we need to prepare the layout here
    if (SWITCH_TYPE_CLIENT.equals(switchType)) {
      //noinspection unchecked
      for (UIComponent tab: (List<UIComponent>) getChildren()) {
        if (tab instanceof UITab) {
          UILayout.getLayout(tab).layoutBegin(facesContext, tab);
        }
      }
    } else {
      UIPanelBase tab = getRenderedTab();
      UILayout.getLayout(tab).layoutBegin(facesContext, tab);
    }
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

  public boolean isShowNavigationBar() {
    if (showNavigationBar != null) {
      return showNavigationBar;
    }
    ValueBinding vb = getValueBinding(ATTR_SHOW_NAVIGATION_BAR);
    if (vb != null) {
      return (!Boolean.FALSE.equals(vb.getValue(getFacesContext())));
    } else {
      return true;
    }
  }

  public void setShowNavigationBar(boolean showNavigationBar) {
    this.showNavigationBar = showNavigationBar;
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

  @Override
  public void encodeChildren(FacesContext context)
      throws IOException {
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {
    resetTabLayout();
    super.encodeEnd(facesContext);
    setRenderedIndex(getSelectedIndex());
  }

  private void resetTabLayout() {
    for (UIComponent component : (List<UIComponent>) getChildren()) {
      component.getAttributes().remove(ATTR_LAYOUT_WIDTH);
      component.getAttributes().remove(ATTR_LAYOUT_HEIGHT);
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
      Integer index = ((TabChangeEvent) facesEvent).getNewTabIndex();
      ValueBinding vb = getValueBinding(ATTR_SELECTED_INDEX);
      if (vb != null) {
        vb.setValue(getFacesContext(), index);
      } else {
        setSelectedIndex(index);
      }
      getFacesContext().renderResponse();
    }
  }

  public void setTabChangeListener(MethodBinding tabStateChangeListener) {
    this.tabChangeListener = tabStateChangeListener;
  }

  public MethodBinding getTabChangeListener() {
    return tabChangeListener;
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
    Object[] state = new Object[7];
    state[0] = super.saveState(context);
    state[1] = renderedIndex;
    state[2] = selectedIndex;
    state[3] = saveAttachedState(context, tabChangeListener);
    state[4] = switchType;
    state[5] = immediate;
    state[6] = showNavigationBar;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    renderedIndex = (Integer) values[1];
    selectedIndex = (Integer) values[2];
    tabChangeListener = (MethodBinding) restoreAttachedState(context, values[3]);
    switchType = (String) values[4];
    immediate = (Boolean) values[5];
    showNavigationBar = (Boolean) values[6];
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    setRenderedIndex(getSelectedIndex());
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public int getSelectedIndex() {
    if (selectedIndex != null) {
      return selectedIndex;
    }
    ValueBinding vb = getValueBinding(ATTR_SELECTED_INDEX);
    if (vb != null) {
      Integer value = (Integer) vb.getValue(getFacesContext());
      if (value != null) {
        return value;
      }
    }
    return 0;
  }

  public void setSelectedIndex(int selectedIndex) {
    this.selectedIndex = selectedIndex;
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
    } else if (value == null) {
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
}
