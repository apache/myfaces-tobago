/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.charlotteville.standard.tag;

import com.atanion.tobago.renderkit.GroupBoxRendererBase;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class GroupBoxRenderer extends GroupBoxRendererBase
    implements HeightLayoutRenderer {

// ///////////////////////////////////////////// constant

  private static Log log = LogFactory.getLog(GroupBoxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return 20 + 2; // margin + border
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return 22;  // speyside groupbox header height
  }

// ///////////////////////////////////////////// bean getter + setter

}

