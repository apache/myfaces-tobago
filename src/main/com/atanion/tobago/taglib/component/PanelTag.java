/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 14:50:02.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasDimension;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

/**
 * Intended for use in situations when only one UIComponent child can be
 * nested, such as in the case of facets.
 */
@Tag(name="panel", bodyContent="JSP")
public class PanelTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasDimension {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

// ///////////////////////////////////////////// bean getter + setter

}

