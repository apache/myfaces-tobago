package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:42:47 PM
 * User: bommel
 * $Id$
 */
public class TextBoxRenderer extends InputRendererBase {

  public void encodeEndTobago(FacesContext facesContext,
        UIComponent component) throws IOException {
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);

    ResponseWriter writer = facesContext.getResponseWriter();
    Layout layout = Layout.getLayout(component.getParent());
    Layout in = layout.createWithMargin(0,0,0,0);
    in.setParent(layout);
    if (label != null) {
      FoUtils.startBlockContainer(writer, component);
      FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,layout.getWidth()/2, layout.getX(), layout.getY() );
      RenderUtil.encode(facesContext, label);
      FoUtils.endBlockContainer(writer);
    }

    //in.addMargin(200, 0, 200, 0);
    String text = ComponentUtil.currentValue(component);
    if (text == null) {
      text = "";
    }
    FoUtils.startBlockContainer(writer, component);
    FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT, layout.getWidth()/2, layout.getX()+layout.getWidth()/2, layout.getY());
    FoUtils.writeTextBlockAlignLeft(writer, component, "TextBox");
    FoUtils.endBlockContainer(writer);
    layout.addMargin(200, 0, 0, 0);


    }


}
