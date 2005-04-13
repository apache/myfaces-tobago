/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasDimension;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.Tag;

@Tag(name="box")
public class BoxTag extends TobagoBodyTag
    implements HasId, HasDimension, HasLabel, IsRendered, HasBinding 
    {

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }
}

