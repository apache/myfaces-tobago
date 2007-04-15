package org.apache.myfaces.tobago.example.addressbook.web;


import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.io.IOException;

public class StartupPhaseListener implements PhaseListener {

  private static final Log LOG = LogFactory.getLog(StartupPhaseListener.class);
  public static final String LOGGED_IN
      = StartupPhaseListener.class.getName() + ".LOGGED_IN";
  public static final String PRINCIPAL
      = StartupPhaseListener.class.getName() + ".PRINCIPAL";

  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }

  public void beforePhase(PhaseEvent event) {

    FacesContext facesContext = event.getFacesContext();
    ExternalContext externalContext = facesContext.getExternalContext();
    String pathInfo = externalContext.getRequestPathInfo();
    if (LOG.isDebugEnabled()) {
      LOG.debug("externalContext.getRequestPathInfo() = '" + pathInfo + "'");
    }

    if (pathInfo.equals("/error.jsp") || // todo: not nice, find a declarative way.
        pathInfo.startsWith("/auth/")) {
      Object session = externalContext.getSession(false);
      if (session != null) {
        externalContext.getSessionMap().put(LOGGED_IN, Boolean.FALSE);
      }
      return; // nothing to do.
    }

    Boolean loggedIn = (Boolean) // todo: not nice to get this object directly from the session
        externalContext.getSessionMap().get(LOGGED_IN);

    if (! BooleanUtils.toBoolean(loggedIn)) {
      try {
        externalContext.getSessionMap().put(LOGGED_IN, Boolean.TRUE);
        String forward = externalContext.getRequestContextPath()
            + "/faces/application/start.jsp";
        externalContext.redirect(externalContext.encodeResourceURL(forward));
      } catch (Exception e) {
        LOG.error("", e);
        String forward = externalContext.getRequestContextPath()
            + "/error.jsp";
        try {
          externalContext.redirect(externalContext.encodeResourceURL(forward));
        } catch (IOException e2) {
          LOG.error("", e2);
          throw new FacesException("Can't redirect to errorpage '"
              + forward + "'");
        }
      }
    }
  }

  public void afterPhase(PhaseEvent event) {
  }

}
