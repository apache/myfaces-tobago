/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Dec 18, 2002 2:02:20 PM
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.NamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.atanion.tobago.TobagoConstants;

public class UITabGroup extends UIPanel {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.TabGroup";

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
        tabs.add(kid);
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

// ///////////////////////////////////////////// bean getter + setter

  public int getActiveIndex() {
    return activeIndex;
  }

  public void setActiveIndex(int activeIndex) {
    this.activeIndex = activeIndex;
  }
}
