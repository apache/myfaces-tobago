/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

public class RichTextEditorTag extends TextBoxTag{

// ///////////////////////////////////////////// constant

  private static final Log log = LogFactory.getLog(RichTextEditorTag.class);

// /////////////////////////////////////////// attributes

  boolean statePreview;

// /////////////////////////////////////////// constructors

  public RichTextEditorTag() {
    super();
    statePreview = false;
  }

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_STATE_PREVIEW, new Boolean(statePreview));
  }

  public void release() {
    super.release();
    statePreview = false;
  }

// /////////////////////////////////////////// bean getter + setter


}
