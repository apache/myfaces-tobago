/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Dec 18, 2002 2:02:20 PM
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.event.StateChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UITabGroup extends UIPanel implements StateHolder{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.TabGroup";

// ///////////////////////////////////////////// attribute

  private int activeIndex;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildren(FacesContext context)
      throws IOException {
    // childeren are rendered by encodeEnd'jsp
  }

  public UIPanel[] getTabs() {
    List tabs = new ArrayList();
    for (Iterator kids = getChildren().iterator(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      if (kid instanceof UIPanel) {
        if (kid.isRendered()) {
          tabs.add(kid);
        }
      } else {
        LOG.error("Invalid component in UITabGroup: " + kid);
      }
    }
    return (UIPanel[]) tabs.toArray(new UIPanel[tabs.size()]);
  }

  public UIPanel getActiveTab() {
    return getTabs()[activeIndex];
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updateState(context);
  }

  public void updateState(FacesContext facesContext) {
    ValueBinding stateBinding
        = getValueBinding(TobagoConstants.ATTR_STATE_BINDING);
    if (stateBinding != null) {
      stateBinding.setValue(facesContext, new Integer(activeIndex));
    }
  }

  public void addStateChangeListener(StateChangeListener listener) {
    if (LOG.isWarnEnabled() && ! ComponentUtil.getBooleanAttribute(
        this, TobagoConstants.ATTR_SERVER_SIDE_TABS)) {
      LOG.warn("Adding StateChangeListener to Client side Tabgroup!");
    }
    addFacesListener(listener);
  }
  public void removeStateChangeListener(StateChangeListener listener) {
    removeFacesListener(listener);
  }
  public StateChangeListener[] getStateChangeListener(){
    StateChangeListener listener[] =
        (StateChangeListener[]) getFacesListeners(StateChangeListener.class);
    return listener;
  }

// ///////////////////////////////////////////// bean getter + setter

  public int getActiveIndex() {
    return activeIndex;
  }

  public void setActiveIndex(int activeIndex) {
    this.activeIndex = activeIndex;
  }
}
