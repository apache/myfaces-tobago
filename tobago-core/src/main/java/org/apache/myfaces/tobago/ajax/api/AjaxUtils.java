package org.apache.myfaces.tobago.ajax.api;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: 12.10.2005
 * Time: 13:11:05
 * To change this template use File | Settings | File Templates.
 */
public class AjaxUtils {



  public static void processAjax(FacesContext facesContext, UIComponent component) throws IOException {

    component.processValidators(facesContext);

    if (! facesContext.getRenderResponse()) {
      component.processUpdates(facesContext);
    }

    // invokeApplication here ??

    renderAjax(facesContext, component);
  }

  private static void renderAjax(FacesContext facesContext, UIComponent component) throws IOException {
    final Iterator facetsAndChildren = component.getFacetsAndChildren();
    while (facetsAndChildren.hasNext()) {
      UIComponent child = (UIComponent) facetsAndChildren.next();
      if (child instanceof AjaxComponent) {
        ((AjaxComponent)child).processAjax(facesContext);
      }
      else {
        renderAjax(facesContext, child);
      }
      if (facesContext.getResponseComplete()) {
        return;
      }
    }
  }

  public static void checkParamValidity(FacesContext facesContext, UIComponent uiComponent, Class compClass)
  {
    if(facesContext == null)
      throw new NullPointerException("facesContext may not be null");
    if(uiComponent == null)
      throw new NullPointerException("uiComponent may not be null");

    //if (compClass != null && !(compClass.isAssignableFrom(uiComponent.getClass())))
    // why isAssignableFrom with additional getClass method call if isInstance does the same?
    if (compClass != null && !(compClass.isInstance(uiComponent)))
    {
      throw new IllegalArgumentException("uiComponent : "
          + uiComponent.getClass().getName() + " is not instance of "
          + compClass.getName() + " as it should be");
    }
  }




  public static void encodeAjaxComponent(FacesContext facesContext, UIComponent component) throws IOException {
    if (facesContext == null) throw new NullPointerException("facesContext");
    if (!component.isRendered()) return;
    Renderer renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null && renderer instanceof AjaxRenderer)
    {
      ((AjaxRenderer) renderer).encodeAjax(facesContext, component);
    }
  }


  public static String createUrl(final FacesContext facesContext, final String clientId) {
    final String viewId = facesContext.getViewRoot().getViewId();
    final ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    final String actionURL = viewHandler.getActionURL(facesContext, viewId);
    return facesContext.getExternalContext().encodeActionURL(
        actionURL+"?affectedAjaxComponent=" + clientId);
  }
}
