/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.01.2004 11:15:50.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public interface DirectRenderer {

  public void encodeDirectBegin(FacesContext facesContext,
      UIComponent component)
      throws IOException;

  public void encodeDirectChildren(FacesContext facesContext,
      UIComponent component)
      throws IOException;

  public void encodeDirectEnd(FacesContext facesContext, UIComponent component)
      throws IOException;

}
