package org.apache.myfaces.tobago.clientConfig;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jan 26, 2005
 * Time: 11:28:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClientConfigPhaseListener implements PhaseListener {

  public static String[] BEAN_NAMES
      = {"clientConfigController", "clientConfigController2"};

  public void afterPhase(PhaseEvent event) {
  }

  public void beforePhase(PhaseEvent event) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    for (int i = 0; i < BEAN_NAMES.length; i++) {
      String beanName = BEAN_NAMES[i];
      ClientConfigController controller = ClientConfigController
          .getCurrentInstance(facesContext, beanName);

      if (controller != null) {
        controller.loadFromClientProperties();
      }
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }
}
