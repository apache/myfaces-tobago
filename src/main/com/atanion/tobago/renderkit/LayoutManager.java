package com.atanion.tobago.renderkit;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.09.2003 at 12:45:50.
  * $Id$
  */
public interface LayoutManager {

  public void layoutBegin(FacesContext facesContext, UIComponent component);

}
