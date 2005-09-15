/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved.
 * Created: Dec 18, 2002 2:02:20 PM
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.event.TabChangeListener;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UITabGroup extends UIPanel {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UITabGroup.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.TabGroup";

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

  public void encodeEnd(FacesContext facesContext) throws IOException {
    resetTabLayout();
    super.encodeEnd(facesContext);
  }



  private void resetTabLayout() {
    for (UIPanel tab : getTabs()) {
        tab.getAttributes().remove(ATTR_LAYOUT_WIDTH);
//        tab.getAttributes().remove(TobagoConstants.ATTR_INNER_WIDTH);
        tab.getAttributes().remove(ATTR_LAYOUT_HEIGHT);
//        tab.getAttributes().remove(TobagoConstants.ATTR_INNER_HEIGHT);
    }
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
    ValueBinding stateBinding = getValueBinding(ATTR_STATE);
    if (stateBinding != null) {
      stateBinding.setValue(facesContext, new Integer(activeIndex));
    }
  }

  public void addTabChangeListener(TabChangeListener listener) {
    if (LOG.isWarnEnabled() && ! ComponentUtil.getBooleanAttribute(
        this, ATTR_SERVER_SIDE_TABS)) {
      LOG.warn("Adding TabChangeListener to Client side Tabgroup!");
    }
    addFacesListener(listener);
  }

  public void removeTabChangeListener(TabChangeListener listener) {
    removeFacesListener(listener);
  }

// ///////////////////////////////////////////// bean getter + setter

  public int getActiveIndex() {
    return activeIndex;
  }

  public void setActiveIndex(int activeIndex) {
    this.activeIndex = activeIndex;
  }
}
