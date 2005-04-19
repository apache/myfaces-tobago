/*
  * Copyright (c) 2004 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.util.annotation.Tag;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;

import javax.faces.component.UIPanel;

/**
 * <![CDATA[
 *       Renders a menu bar.<br>
 *       Add menu bar as facet name="menuBar" to page tag or use it anywhere
 *       on page.<br>
 *  ]]>
 */
@Tag(name="menuBar", bodyContent="JSP=(t:menu|t:menuitem|t:menucheck|t:menuradio|t:menuseparator)*")
public class MenuBarTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasWidth {

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

}
