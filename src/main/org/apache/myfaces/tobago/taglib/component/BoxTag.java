/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasDimension;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.util.annotation.Tag;

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

