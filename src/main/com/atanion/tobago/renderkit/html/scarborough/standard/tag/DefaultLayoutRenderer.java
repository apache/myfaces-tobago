package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.renderkit.LayoutRenderer;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultLayoutRenderer extends LayoutRenderer {

  private static final Log LOG = LogFactory.getLog(DefaultLayoutRenderer.class);

  public void layoutWidth(FacesContext facesContext, UIComponent component) {
    HtmlRendererUtil.layoutWidth(facesContext, component);
  }

  public void layoutHeight(FacesContext facesContext, UIComponent component) {
    HtmlRendererUtil.layoutHeight(facesContext, component);
  }


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
      for (Iterator i = component.getChildren().iterator(); i.hasNext();) {
        UIComponent child = (UIComponent) i.next();
        HtmlRendererUtil.encodeHtml(facesContext, child);
      }
  }
}
