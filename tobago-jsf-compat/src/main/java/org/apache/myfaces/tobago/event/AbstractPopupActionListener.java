/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Dec 4, 2009
 * Time: 5:10:57 PM
 */
package org.apache.myfaces.tobago.event;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

public abstract class AbstractPopupActionListener implements ActionListener {
  private static final Log LOG = LogFactory.getLog(AbstractPopupActionListener.class);

  public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
    UIComponent popup = getPopup(actionEvent);
    if (popup != null) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("activated "
            + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
      }
      try {
        BeanUtils.setProperty(popup, "activated", true);
      } catch (IllegalAccessException e) {
        LOG.error("", e);
      } catch (InvocationTargetException e) {
        LOG.error("", e);
      }
    }
  }

  protected abstract UIComponent getPopup(ActionEvent actionEvent);
}