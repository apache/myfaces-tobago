package com.atanion.tobago.component;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 4, 2005
 * Time: 5:02:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class UICommand extends javax.faces.component.UICommand {

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Command";

  private boolean active;


  public void processDecodes(FacesContext context) {

      if (context == null) {
          throw new NullPointerException();
      }

      // Skip processing if our rendered flag is false
      if (!isRendered()) {
          return;
      }


      // Process this component itself
      try {
          decode(context);
      } catch (RuntimeException e) {
          context.renderResponse();
          throw e;
      }

    if (active) {
      // Process all facets and children of this component
      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
          UIComponent kid = (UIComponent) kids.next();
          kid.processDecodes(context);
      }
    }

  }

  public void processValidators(FacesContext facesContext) {
    if (active) {
      super.processValidators(facesContext);
    }
  }

  public void processUpdates(FacesContext facesContext) {
    if (active) {
      super.processUpdates(facesContext);
    }
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
