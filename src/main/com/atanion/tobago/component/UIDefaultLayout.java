/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 06.12.2004 20:49:49.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class UIDefaultLayout extends UILayout
    implements TobagoConstants {


  public static final String COMPONENT_TYPE = "com.atanion.tobago.DefaultLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.Layout";


  private static UIDefaultLayout instance;

  public static synchronized UIDefaultLayout getInstance() {
    if (instance == null) {
      instance = (UIDefaultLayout)
          ComponentUtil.createComponent(COMPONENT_TYPE, RENDERER_TYPE_DEFAULT_LAYOUT);
    }
    return instance;
  }


  public String getFamily() {
    return COMPONENT_FAMILY;
  }

}
