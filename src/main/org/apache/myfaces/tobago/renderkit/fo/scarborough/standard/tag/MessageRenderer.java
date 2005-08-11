package org.apache.myfaces.tobago.renderkit.fo.scarborough.standard.tag;

import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:31:22 PM
 * User: bommel
 * $Id$
 */
public class MessageRenderer extends MessageRendererBase {
  private static final Log LOG = LogFactory.getLog(MessageRenderer.class);

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
