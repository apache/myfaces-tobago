/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class TabRenderer extends RendererBase implements HeightLayoutRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TabRenderer.class);
// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

// ///////////////////////////////////////////// bean getter + setter

}

