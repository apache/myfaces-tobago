/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.01.2004 11:15:50.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public interface TobagoRenderer {

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException;

  public void encodeChildrenTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException;

  public void encodeEndTobago(FacesContext facesContext, UIComponent component)
      throws IOException;

}
