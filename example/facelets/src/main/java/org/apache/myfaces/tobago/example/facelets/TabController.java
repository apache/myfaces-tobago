package org.apache.myfaces.tobago.example.facelets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;

public class TabController {

  private static final Log LOG = LogFactory.getLog(TabController.class);

  private TabChangeListener listener;

  public void processTabChange(TabChangeEvent event) {
    LOG.info("Controller: Change index from " + event.getOldTabIndex() + " to " + event.getNewTabIndex());
  }

  public TabChangeListener getListener() {
    LOG.info("get listener");
    return listener;
  }

  public void setListener(TabChangeListener listener) {
    LOG.info("set listener");
    this.listener = listener;
  }
}
