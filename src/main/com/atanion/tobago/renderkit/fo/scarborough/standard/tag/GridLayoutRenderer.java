package com.atanion.tobago.renderkit.fo.scarborough.standard.tag;

import com.atanion.tobago.renderkit.LayoutManager;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Dec 1, 2004 7:25:02 PM
 * User: bommel
 * $Id$
 */
public class GridLayoutRenderer extends RendererBase
    implements LayoutManager {
  private static int suppe = 0;
  private static final Log LOG = LogFactory.getLog(GridLayoutRenderer.class);
  public boolean getRendersChildren() {
    return false;
  }
  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    Layout layout = Layout.getLayout(component.getParent());
    layout.setOrientation(Layout.TOP_ORIENTATION);
    if (component.getAttributes().get("columns")!=null) {
      layout.setOrientation(Layout.LEFT_ORIENTATION);
    }
    Layout.putLayout(component, layout);

  }



  public void layoutEnd(FacesContext facesContext, UIComponent component) {
    new Exception().printStackTrace();
    suppe++;
    if (suppe==4) {
      throw new IllegalStateException("");
    }
    List children = component.getParent().getChildren();
    ResponseWriter writer = facesContext.getResponseWriter();
    Layout layout = Layout.getLayout(component.getParent());

    System.err.println("parent ist " + component.getParent() +"\nlayout ist " + component);
    for (int i = 0; i<children.size();i++) {
      System.err.println("i = " + i + " size = " + children.size());
      int incrementX = layout.getWidth()/children.size();
      UIComponent cell = (UIComponent) children.get(i);
      System.err.println(cell);
      if (! (cell instanceof UIMessages)) {
      try {
        FoUtils.startBlockContainer(writer, component);
        if (layout.getOrientation()==Layout.TOP_ORIENTATION) {

          FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,
              layout.getWidth(), layout.getX(), layout.getY()+FoUtils.DEFAULT_HEIGHT*i);
        } else {

          FoUtils.layoutBlockContainer(writer, FoUtils.DEFAULT_HEIGHT,
              layout.getWidth()/children.size(), layout.getX()+incrementX*i, layout.getY());

        }
        System.err.println("+++++++++++++++++++++++++++++++++++++++++++vor encode");
        RenderUtil.encode(facesContext, cell);
        System.err.println("+++++++++++++++++++++++++++++++++++++++++++nach encode");

        FoUtils.endBlockContainer(writer);
      } catch (IOException e) {
        LOG.error("", e);
      }
      }
    }

  }

}
