package org.apache.myfaces.tobago.example.demo.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class ActivitySessionListener implements HttpSessionListener {

  private static final Log LOG = LogFactory.getLog(ActivitySessionListener.class);

  private static final String SESSION_MAP = ActivitySessionListener.class.getName() + ".SESSION_MAP";

  public void sessionCreated(HttpSessionEvent event) {
    final HttpSession session = event.getSession();
    final ServletContext application = session.getServletContext();
    final ActivityList activityList = (ActivityList) application.getAttribute(ActivityList.NAME);
    activityList.add(new Activity(session));
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    final HttpSession session = event.getSession();
    final ServletContext application = session.getServletContext();
    final ActivityList activityList = (ActivityList) application.getAttribute(ActivityList.NAME);
    activityList.remove(session.getId());
  }
}
