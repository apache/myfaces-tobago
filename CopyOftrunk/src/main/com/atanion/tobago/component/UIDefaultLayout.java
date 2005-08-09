/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 06.12.2004 20:49:49.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class UIDefaultLayout extends UILayout
    implements TobagoConstants {
  private static final Log LOG = LogFactory.getLog(UIDefaultLayout.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.DefaultLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.Layout";


  private static UIDefaultLayout instance;

  public static synchronized UIDefaultLayout getInstance() {
    if (instance == null) {
      instance = (UIDefaultLayout)
          ComponentUtil.createComponent(COMPONENT_TYPE, RENDERER_TYPE_DEFAULT_LAYOUT);
      instance.setId("UIDefaultLayout");
    }
    return instance;
  }

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    super.layoutBegin(facesContext, component);
    for (Object child : component.getChildren()) {
      ((UIComponent)child).getAttributes().remove(ATTR_INNER_WIDTH);
      ((UIComponent)child).getAttributes().remove(ATTR_INNER_HEIGHT);
      ((UIComponent)child).getAttributes().remove(ATTR_LAYOUT_WIDTH);
      ((UIComponent)child).getAttributes().remove(ATTR_LAYOUT_HEIGHT);
    }
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }

}
