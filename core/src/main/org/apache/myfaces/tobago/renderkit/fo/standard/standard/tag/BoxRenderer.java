package org.apache.myfaces.tobago.renderkit.fo.scarborough.standard.tag;

import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import java.io.IOException;
import java.util.Iterator;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Nov 30, 2004 6:20:54 PM
 * User: bommel
 * $Id$
 */
public class BoxRenderer extends FoRendererBase {

  public boolean getRendersChildren() {
    return true;
  }


  public void encodeBegin(FacesContext facesContext,
       UIComponent uiComponent) throws IOException {
    Layout.putLayout(uiComponent, Layout.getLayout(uiComponent.getParent()));
    super.encodeBegin(facesContext, uiComponent);
  }

  public void encodeBeginTobago(FacesContext facesContext, UIComponent component) throws IOException {
    Layout layout = Layout.getLayout(component.getParent());
    int borderWidth = 5;
    int height = 50;
    int padding = 0;
    int width = layout.getWidth();
    int x = 0;
    int y = 0;
    int boxHeight = height+padding*2+borderWidth;
    int height2 = layout.getHeight()-boxHeight;
    int width2 =  layout.getWidth();
    int x2= 0;
    int y2 = boxHeight;


    ResponseWriter writer = facesContext.getResponseWriter();

    //Layout box = new Layout(layout.getWidth(), height);
    FoUtils.startBlockContainer(writer, component);
    FoUtils.writeBorder(writer, borderWidth);
    FoUtils.layoutBlockContainer(writer, height, width, x, y);
    //writer.writeAttribute("padding", Layout.getMM(padding), null);
    String labelString = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);
    FoUtils.writeTextBlockAlignStart(writer, component, labelString);
    // TODO UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    FoUtils.endBlockContainer(writer);


    FoUtils.startBlockContainer(writer, component);
    FoUtils.writeBorder(writer, borderWidth);
    FoUtils.layoutBlockContainer(writer, height2, width2, x2, y2);


    Layout.putLayout(component, layout);



  }




  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    FoUtils.endBlockContainer(writer);
  }
}
