/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.scarborough.mozilla.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class BoxRenderer extends
    com.atanion.tobago.renderkit.html.scarborough.standard.tag.BoxRenderer {

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {
    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);
    super.encodeBeginTobago(facesContext, component);
  }

  protected String getAttrStyleKey() {
    return TobagoConstants.ATTR_STYLE_BODY;
  }

// ///////////////////////////////////////////// bean getter + setter

}

