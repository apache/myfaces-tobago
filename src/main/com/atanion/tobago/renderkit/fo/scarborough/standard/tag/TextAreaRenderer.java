package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:43:42 PM
 * User: bommel
 * $Id$
 */
public class TextAreaRenderer extends InputRendererBase {
  private static final Log LOG = LogFactory.getLog(TextAreaRenderer.class);

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {
      component.getAttributes().put(
          ATTR_ENCODING_ACTIVE,
          Boolean.TRUE);


      encodeEndTobago(facesContext, component);

      component.getAttributes().put(
          ATTR_ENCODING_ACTIVE,
          Boolean.FALSE);
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

  public boolean getRendersChildren() {
    return false;
  }
  public void encodeEndTobago(FacesContext facesContext,
        UIComponent component) throws IOException {
      String text = ComponentUtil.currentValue(component);
      if (text == null) {
        text = "";
      }
      Layout layout = Layout.getLayout(component.getParent());
      //layout.addMargin(200, 0, 0, 0);

      ResponseWriter writer = facesContext.getResponseWriter();
     if (!Layout.isInLayout(component)) {
        FoUtils.startBlockContainer(writer, component);
        FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,layout.getWidth(), layout.getX(), layout.getY() );
      }
      FoUtils.writeTextBlockAlignLeft(writer, component, "TextArea");
      if (!Layout.isInLayout(component)) {
        FoUtils.endBlockContainer(writer);
      }
      if (!Layout.isInLayout(component)) {
        layout.addMargin(200, 0, 0, 0);
      }


    }


}
