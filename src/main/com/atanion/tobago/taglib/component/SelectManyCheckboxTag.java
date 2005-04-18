/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Sep 9, 2002 4:28:29 PM
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.HasRenderRange;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

@Tag(name="selectManyCheckbox")
public class SelectManyCheckboxTag extends SelectManyTag
    implements HasValue, IsDisabled, HasId, IsInline, HasRenderRange,
               IsRendered,  HasBinding {
// ----------------------------------------------------------------- attributes

  private String renderRange;

// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());
  }

  public void release() {
    super.release();
    renderRange = null;
  }
// ------------------------------------------------------------ getter + setter

  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}

