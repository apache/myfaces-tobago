package com.atanion.tobago.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UILayout;
import com.atanion.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
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
    UILayout layout = (UILayout) component;
    int height = 0;

    UIComponent container = layout.getParent();
    if (LOG.isInfoEnabled() &&  container.getChildCount() > 1) {
      LOG.info("Can't calculate fixedHeight! "
                 + "using estimation by contained components. ");
    }
    height += LayoutUtil.calculateFixedHeightForChildren(facesContext, container);

    RendererBase containerRenderer =
        ComponentUtil.getRenderer(facesContext, layout.getParent());
    height += containerRenderer.getHeaderHeight(facesContext, layout.getParent());
    height += containerRenderer.getPaddingHeight(facesContext, layout.getParent());

    return height;
  }

}
