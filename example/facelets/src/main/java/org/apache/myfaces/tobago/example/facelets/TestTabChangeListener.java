package org.apache.myfaces.tobago.example.facelets;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.event.TabChangeEvent;
import org.apache.myfaces.tobago.event.TabChangeListener;

public class TestTabChangeListener implements TabChangeListener {

  private static final Log LOG = LogFactory.getLog(TestTabChangeListener.class);

  public TestTabChangeListener() {
    LOG.info("listener: constructor");
  }

  public void processTabChange(TabChangeEvent event) {
    LOG.info("listener: Change index from " + event.getOldTabIndex() + " to " + event.getNewTabIndex());
  }
}
