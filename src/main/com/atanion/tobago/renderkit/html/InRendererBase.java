/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package com.atanion.tobago.renderkit.html;

import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class InRendererBase extends InputRendererBase {
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(InRendererBase.class);

// ----------------------------------------------------------- business methods


  public boolean getRendersChildren() {
    return true;
  }

  protected abstract void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException;

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    super.encodeEndTobago(facesContext, component);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();


    renderMain(facesContext, (UIInput) component, writer);
  }

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;
//    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
//      int labelWidht = LayoutUtil.getLabelWidth(component);
//      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
//      space += getConfiguredValue(facesContext, component, "labelSpace");
//    }
//    if (component.getFacet("picker") != null) {
//      int pickerWidth = getConfiguredValue(facesContext, component, "pickerWidth");
//      space += pickerWidth;
//    }
    return space;
  }
}

