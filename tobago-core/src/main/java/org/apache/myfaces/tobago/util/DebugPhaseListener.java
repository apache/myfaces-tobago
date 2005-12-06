package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.event.PhaseListener;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Dec 6, 2005
 * Time: 7:50:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugPhaseListener implements PhaseListener {
  private static final Log LOG = LogFactory.getLog(DebugPhaseListener.class);
  public void afterPhase(PhaseEvent phaseEvent) {
    LOG.debug("After Phase :" + phaseEvent.getPhaseId());
  }

  public void beforePhase(PhaseEvent phaseEvent) {
    LOG.debug("Before Phase :" + phaseEvent.getPhaseId());
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
