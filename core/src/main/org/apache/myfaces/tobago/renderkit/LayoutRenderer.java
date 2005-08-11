package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.util.LayoutUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * User: weber
 * Date: Feb 22, 2005
 * Time: 3:05:58 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class LayoutRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(LayoutRenderer.class);

  public abstract void layoutWidth(FacesContext facesContext, UIComponent component) ;
  public abstract void layoutHeight(FacesContext facesContext, UIComponent component);
  public abstract void prepareRender(FacesContext facesContext, UIComponent component);


  public void encodeChildrenOfComponent(FacesContext facesContext, UIComponent component)
      throws IOException {
    // use renderer of component
    RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
    renderer.encodeChildren(facesContext, component);
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    int height = 0;

    if (LOG.isInfoEnabled() &&  component.getChildCount() > 1) {
      LOG.info("Can't calculate fixedHeight! "
                 + "using estimation by contained components. ");
    }
    height += LayoutUtil.calculateFixedHeightForChildren(facesContext, component);

    RendererBase containerRenderer =
        ComponentUtil.getRenderer(facesContext, component);
    height += containerRenderer.getHeaderHeight(facesContext, component);
    height += containerRenderer.getPaddingHeight(facesContext, component);

    return height;
  }

}
