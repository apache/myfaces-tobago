/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

public class TabRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return 8;
  }

  public int getPaddingHeight(FacesContext facesContext, UIComponent component) {
    return 11;
  }

// ///////////////////////////////////////////// bean getter + setter

}

