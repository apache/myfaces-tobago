package org.apache.myfaces.tobago.example.demo.activity;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: lofwyr
 * Date: 30.09.2010
 * Time: 16:47:47
 * To change this template use File | Settings | File Templates.
 */
public class Activity {

  private String sessionId;

  private Date creationDate;

  private int jsfRequest;

  private int ajaxRequest;

  public Activity(HttpSession session) {
    this.sessionId = session.getId();
    this.creationDate = new Date(session.getCreationTime());
  }

  public void jsfRequest() {
    jsfRequest++;
  }

  public void ajaxRequest() {
      ajaxRequest++;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public int getJsfRequest() {
    return jsfRequest;
  }

  public void setJsfRequest(int jsfRequest) {
    this.jsfRequest = jsfRequest;
  }

  public int getAjaxRequest() {
    return ajaxRequest;
  }

  public void setAjaxRequest(int ajaxRequest) {
    this.ajaxRequest = ajaxRequest;
  }
}
