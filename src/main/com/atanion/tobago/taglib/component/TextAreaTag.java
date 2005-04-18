/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasHeight;
import com.atanion.tobago.taglib.decl.HasRows;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsReadOnly;
import com.atanion.tobago.taglib.decl.IsFocus;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.IsRequired;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

@Tag(name="textarea", bodyContent="empty")
public class TextAreaTag extends TextInputTag
    implements HasId, HasValue,  IsReadOnly, IsDisabled, HasWidth, HasHeight,
               HasRows, HasOnchangeListener, IsFocus, IsRequired,
               HasLabelAndAccessKey, IsRendered, HasBinding, HasTip
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

  public void setRows(String rows) {
    this.rows = rows;
  }
}

