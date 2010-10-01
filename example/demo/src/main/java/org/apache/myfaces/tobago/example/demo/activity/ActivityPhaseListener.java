package org.apache.myfaces.tobago.example.demo.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

public class ActivityPhaseListener implements PhaseListener {

  private static final Log LOG = LogFactory.getLog(ActivityPhaseListener.class);

  public void afterPhase(PhaseEvent event) {
  }

  public void beforePhase(PhaseEvent event) {
    final FacesContext facesContext = event.getFacesContext();
    final ActivityList activityList
        = (ActivityList) VariableResolverUtil.resolveVariable(facesContext, ActivityList.NAME);
    String sessionId = ((HttpSession) facesContext.getExternalContext().getSession(true)).getId();

    if (AjaxUtils.isAjaxRequest(facesContext)) {
      activityList.ajaxRequest(sessionId);
    } else {
      activityList.jsfRequest(sessionId);
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }
}
