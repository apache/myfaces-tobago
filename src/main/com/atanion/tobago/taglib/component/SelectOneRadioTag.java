/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 13, 2002 3:04:03 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasRenderRange;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 *  Render a set of radiobuttons.
 */
@Tag(name="selectOneRadio")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public class SelectOneRadioTag extends SelectOneTag
    implements HasValue, IsDisabled, HasId, HasOnchangeListener, IsInline,
               HasRenderRange, IsRendered, HasBinding
    {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String renderRange;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());

  }

  public void release() {
    super.release();
    renderRange = null;
  }

// /////////////////////////////////////////// bean getter + setter
  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}

