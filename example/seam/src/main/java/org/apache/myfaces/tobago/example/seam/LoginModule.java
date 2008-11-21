package org.apache.myfaces.tobago.example.seam;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.bpm.Actor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Name("login")
public class LoginModule {

  private static final Log LOG = LogFactory.getLog(LoginModule.class);

  @In
  private Actor actor;

  private String user = "fixme";

  public LoginModule() {
    LOG.info("LOGIN MODULE: " + this);
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String login() {
    actor.setId(user);
    return "/todo.jsp";
  }
}
