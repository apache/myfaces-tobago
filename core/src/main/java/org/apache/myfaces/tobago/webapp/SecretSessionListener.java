package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SecretSessionListener implements HttpSessionListener {

  public void sessionCreated(HttpSessionEvent sessionEvent) {
    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isCheckSessionSecret()) {
      Secret.create(sessionEvent.getSession());
    }
  }

  public void sessionDestroyed(HttpSessionEvent se) {
  }
}
