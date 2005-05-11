/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasDimension;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsFocus;
import com.atanion.tobago.taglib.decl.IsReadonly;
import com.atanion.tobago.taglib.decl.IsRequired;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;

/**
 * Renders a multiline text input control.
 */
@Tag(name="textarea", bodyContent=BodyContent.EMPTY)
public class TextAreaTag extends TextInputTag
    implements HasIdBindingAndRendered, HasValue,  IsReadonly, IsDisabled,
               HasDimension, HasOnchangeListener, IsFocus, IsRequired,
               HasLabelAndAccessKey, HasTip
     {
// ----------------------------------------------------------------- attributes

  private String rows;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    rows = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_ROWS, rows, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter
  public String getRows() {
    return rows;
  }


  /**
   *  The row count for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setRows(String rows) {
    this.rows = rows;
  }
}
