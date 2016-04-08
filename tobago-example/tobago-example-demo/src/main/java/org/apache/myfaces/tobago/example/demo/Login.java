package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Named
@RequestScoped
public class Login {

  private static final Logger LOG = LoggerFactory.getLogger(Login.class);

  private String username;
  private String password;

  public void login() throws ServletException, IOException {
    final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

    LOG.info("Try to login user: '{}'", username);
    request.login(username, password);
    LOG.info("Successful login user: '{}'", username);

    response.sendRedirect(response.encodeRedirectURL("/content/30-concept/80-security/content-security-policy.xhtml"));
  }

  public void logout() throws ServletException, IOException {
    final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

    request.logout();

    response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/"));
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
