/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 10:52:11.
 * $Id: MockRenderKitFactory.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class MockRenderKitFactory extends RenderKitFactory {

  private Map renderKits = new HashMap();

  public void addRenderKit(String renderKitId, RenderKit renderKit) {
      if ((renderKitId == null) || (renderKit == null)) {
          throw new NullPointerException();
      }
      synchronized (renderKits) {
          if (renderKits.containsKey(renderKitId)) {
              throw new IllegalArgumentException(renderKitId);
          }
          renderKits.put(renderKitId, renderKit);
      }
  }


  public RenderKit getRenderKit(FacesContext context, String renderKitId) {
      if (renderKitId == null) {
          throw new NullPointerException();
      }
      synchronized (renderKits) {
          RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
          if (renderKit == null) {
              throw new IllegalArgumentException(renderKitId);
          }
          return (renderKit);
      }
  }


  public Iterator getRenderKitIds() {
      synchronized (renderKits) {
          return (renderKits.keySet().iterator());
      }
  }

}
