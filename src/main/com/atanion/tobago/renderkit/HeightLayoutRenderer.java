package com.atanion.tobago.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.09.2003 at 14:22:00.
  * $Id$
  */
public interface HeightLayoutRenderer {

  public int getHeaderHeight(FacesContext facesContext, UIComponent component);

}
