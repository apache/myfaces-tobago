package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.LayoutManager;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 3, 2004 12:07:38 AM
 * User: bommel
 * $Id$
 */
public class FoRendererBase extends RendererBase {

  private static final Log LOG = LogFactory.getLog(FoRendererBase.class);

  private LayoutManager getLayoutManager(
      FacesContext facesContext,
      UIComponent component) {
    LayoutManager layoutManager = null;

    Renderer renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer instanceof LayoutManager) {
      return  (LayoutManager) renderer;
    }
    return layoutManager;
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** begin    " + component);
    }
    try {

//      createClassAttribute(component);

      encodeBeginTobago(facesContext, component);

    } catch (IOException e) {
      throw e;
    } catch (RuntimeException e) {
      LOG.error("catched RuntimeException :", e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("*   begin    " + component);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {

      encodeEndTobago(facesContext, component);

    } catch (IOException e) {
      throw e;
    } catch (RuntimeException e) {
      LOG.error("catched " + e + " :" + e.getMessage(), e);
      throw e;
    } catch (Throwable e) {
      LOG.error("catched Throwable :", e);
      throw new RuntimeException(e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*   end      " + component);
    }
  }
}
