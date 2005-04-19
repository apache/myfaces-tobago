/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasWidth;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * Renders a text editor.
 */
@Tag(name="richTextEditor", bodyContent="JSP=")
public class RichTextEditorTag extends TextInputTag
    implements HasIdBindingAndRendered, HasValue, HasLabelAndAccessKey, HasWidth {

  // ///////////////////////////////////////////// constant

// /////////////////////////////////////////// attributes

  private String statePreview;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setBooleanProperty(component, ATTR_STATE_PREVIEW, statePreview, getIterationHelper());
  }

  public void release() {
    super.release();
    statePreview = null;
  }

// /////////////////////////////////////////// bean getter + setter

  public String getStatePreview() {
    return statePreview;
  }

  public void setStatePreview(String statePreview) {
    this.statePreview = statePreview;
  }
}
