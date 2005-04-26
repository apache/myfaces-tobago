/*
  * Copyright (c) 2004 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIPanel;

/**
 * 
 *       Renders a menu bar.<br>
 *       Add menu bar as facet name="menuBar" to page tag or use it anywhere
 *       on page.<br>
 *
 */
@Tag(name="menuBar")
@BodyContentDescription(anyClassOf={MenuTag.class, MenuCommandTag.class,
    MenuSelectBooleanTag.class, MenuSelectOneTag.class,
    MenuSeparatorTag.class })
public class MenuBarTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasWidth {

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

}
