/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.RangeParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectMany;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectReferenceRenderer extends RendererBase{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectReferenceRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    String referenceId = (String)
        component.getAttributes().get(TobagoConstants.ATTR_FOR);
    UIComponent reference = component.findComponent(referenceId);

    reference.getAttributes().put(TobagoConstants.ATTR_RENDER_RANGE_EXTERN,
        component.getAttributes().get(TobagoConstants.ATTR_RENDER_RANGE));

    RenderUtil.encode(facesContext, reference);

    reference.getAttributes().remove(TobagoConstants.ATTR_RENDER_RANGE_EXTERN);
  }

// ///////////////////////////////////////////// bean getter + setter

}

