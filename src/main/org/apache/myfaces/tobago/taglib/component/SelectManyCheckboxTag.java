/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: Sep 9, 2002 4:28:29 PM
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * Render a group of checkboxes.
 */
@Tag(name="selectManyCheckbox")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public class SelectManyCheckboxTag extends SelectManyTag
    implements org.apache.myfaces.tobago.taglib.decl.SelectManyCheckboxTag {
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

