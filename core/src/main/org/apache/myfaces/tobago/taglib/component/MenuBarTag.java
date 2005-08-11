/*
  * Copyright (c) 2004 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasWidth;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIPanel;

/**
 * 
 *       Renders a menu bar.<br>
 *       Add menu bar as facet name="menuBar" to page tag or use it anywhere
 *       on page.<br>
 *
 */
@Tag(name="menuBar")
@BodyContentDescription(
    anyClassOf={"org.apache.myfaces.tobago.taglib.component.MenuTag",
    "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })
public class MenuBarTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasWidth {

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

}
