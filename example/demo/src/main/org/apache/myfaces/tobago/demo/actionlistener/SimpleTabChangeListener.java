package org.apache.myfaces.tobago.demo.actionlistener;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.event.StateChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: 13.12.2004
 * Time: 16:55:44
 * To change this template use File | Settings | File Templates.
 */
public class SimpleTabChangeListener implements TabChangeListener {

  private static final Log LOG = LogFactory.getLog(SimpleTabChangeListener.class);

  public void processStateChange(StateChangeEvent stateChangeEvent) {
    LOG.info("TabState has Changed: from " + stateChangeEvent.getOldState()
        + " to " + stateChangeEvent.getNewState());

  }
}
