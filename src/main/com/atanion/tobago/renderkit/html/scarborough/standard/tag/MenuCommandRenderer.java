/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 21.09.2004 at 17:26:57.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.renderkit.CommandRendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MenuCommandRenderer extends CommandRendererBase {

  // rendering is done by MenuBarRenderer,
  // but we need decoding in CommandRendererBase

  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

}