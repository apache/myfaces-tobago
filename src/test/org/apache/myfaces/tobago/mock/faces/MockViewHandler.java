/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 10:37:27.
 * $Id: MockViewHandler.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.FacesException;
import javax.faces.render.RenderKitFactory;
import java.util.Locale;
import java.io.IOException;

public class MockViewHandler extends ViewHandler {

  public Locale calculateLocale(FacesContext facesContext) {
    return null;
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    return RenderKitFactory.HTML_BASIC_RENDER_KIT;
  }

  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    UIViewRoot result = new UIViewRoot();
    result.setViewId(viewId);
          result.setRenderKitId(calculateRenderKitId(facesContext));
    return result;
  }

  public String getActionURL(FacesContext facesContext, String reference) {
    return null;
  }

  public String getResourceURL(FacesContext facesContext, String reference) {
    return null;
  }

  public void renderView(FacesContext facesContext, UIViewRoot uiViewRoot)
      throws IOException, FacesException {
  }

  public UIViewRoot restoreView(FacesContext facesContext, String reference) {
    return null;
  }

  public void writeState(FacesContext facesContext) throws IOException {
  }
}
