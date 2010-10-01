package org.apache.myfaces.tobago.example.demo.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ActivityServletContextListener implements ServletContextListener {

  private static final Log LOG = LogFactory.getLog(ActivityServletContextListener.class);

  public void contextInitialized(ServletContextEvent event) {
    final ServletContext application = event.getServletContext();
    application.setAttribute(ActivityList.NAME, new ActivityList());
  }

  public void contextDestroyed(ServletContextEvent event) {
    final ServletContext application = event.getServletContext();
    application.removeAttribute(ActivityList.NAME);
  }
}
