/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 14:50:02.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.Tag;

/**
 * Intended for use in situations when only one UIComponent child can be
 * nested, such as in the case of facets.
 */
@Tag(name="panel")
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

