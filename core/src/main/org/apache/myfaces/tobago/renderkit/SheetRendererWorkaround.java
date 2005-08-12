/*
 * Copyright (c) 2005 Atanion GmbH, Germany
 * All rights reserved. Created 12.08.2005 09:44:12.
 * $Id: $
 */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.UIData;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

// fixme: this is only a workaround to separate the theme from the core
/** @deprecated */
public interface SheetRendererWorkaround {

  boolean needVerticalScrollbar(FacesContext facesContext, UIData data);

  int getScrollbarWidth(FacesContext facesContext, UIComponent component);

  int getContentBorder(FacesContext facesContext, UIData data);
}
