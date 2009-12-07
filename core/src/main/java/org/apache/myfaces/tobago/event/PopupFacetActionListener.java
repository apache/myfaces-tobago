/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Dec 4, 2009
 * Time: 5:16:38 PM
 */
package org.apache.myfaces.tobago.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.AbstractUIPopup;
import org.apache.myfaces.tobago.component.Facets;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class PopupFacetActionListener extends AbstractPopupActionListener {

  private static final Log LOG = LogFactory.getLog(PopupActionListener.class);

  @Override
  protected UIComponent getPopup(ActionEvent actionEvent) {
    UIComponent component = actionEvent.getComponent().getFacet(Facets.POPUP);
    if (component instanceof AbstractUIPopup) {
      return component;
    } else {
      LOG.error("Found no popup facet in component "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
    }
    return null;
  }
}