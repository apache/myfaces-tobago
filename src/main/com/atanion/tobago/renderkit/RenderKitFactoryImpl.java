/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 08.04.2003 10:32:46.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RenderKitFactoryImpl extends RenderKitFactory {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private Map renderKits = new HashMap();

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void addRenderKit(String renderKitId, RenderKit renderKit) {
    synchronized (renderKits) {
      renderKits.put(renderKitId, renderKit);
    }
  }

  public RenderKit getRenderKit(FacesContext facesContext, String renderKitId) {
    synchronized (renderKits) {
      if (!renderKits.containsKey(renderKitId)) {
        renderKits.put(renderKitId, new TobagoRenderKit(renderKitId));
      }
    }
    return (RenderKit) renderKits.get(renderKitId);
  }

  public Iterator getRenderKitIds() {
    return renderKits.keySet().iterator();
  }

// ///////////////////////////////////////////// bean getter + setter

}
