/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.taglib.decl.HasDimension;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasLabel;
import org.apache.myfaces.tobago.apt.annotation.Tag;

/**
 * Renders a panel with border and title.
 */
@Tag(name="box")
public class BoxTag extends TobagoBodyTag
    implements HasIdBindingAndRendered, HasDimension, HasLabel {

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }
}

