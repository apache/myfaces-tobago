package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import java.io.IOException;
import java.util.Iterator;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 30, 2004 6:04:53 PM
 * User: bommel
 * $Id$
 */
public class PanelRenderer extends RendererBase {

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildrenTobago(FacesContext facesContext,
       UIComponent uiComponent) throws IOException {
    Layout.putLayout(uiComponent, Layout.getLayout(uiComponent.getParent()));
    System.err.println(Layout.getLayout(uiComponent));
    UIPanel component = (UIPanel) uiComponent ;
    for (Iterator i = component.getChildren().iterator(); i.hasNext(); ) {
       UIComponent child = (UIComponent) i.next();
       RenderUtil.encode(facesContext, child);
     }
   }

   public void encodeEndTobago(FacesContext facesContext,
       UIComponent uiComponent) throws IOException {

     UIPanel component = (UIPanel) uiComponent ;
     BodyContentHandler bodyContentHandler = (BodyContentHandler)
         component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);

     if (bodyContentHandler != null) {
       ResponseWriter writer = facesContext.getResponseWriter();
       writer.write(bodyContentHandler.getBodyContent());
     }
   }


}
