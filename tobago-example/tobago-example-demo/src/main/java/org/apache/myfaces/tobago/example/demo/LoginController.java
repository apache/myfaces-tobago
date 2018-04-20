/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Named
@RequestScoped
public class LoginController {

  private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

  private String username;
  private String password;

  public String login() throws ServletException, IOException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
    final HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

    LOG.info("Try to login user: '{}'", username);
    request.login(username, password);
    LOG.info("Successful login user: '{}'", username);

    return "/content/30-concept/80-security/20-roles/x-login.xhtml?faces-redirect=true";
  }

  public String logout() throws ServletException, IOException {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

    request.logout();

    return facesContext.getViewRoot().getViewId();
  }

  public String resetSession() throws IOException {
    LOG.info("Resetting the session.");
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    final ExternalContext externalContext = facesContext.getExternalContext();
/* XXX reset theme doesn't work
    CookieUtils.removeThemeNameCookie(
        (HttpServletRequest)externalContext.getRequest(),
        (HttpServletResponse) externalContext.getResponse());
*/
    externalContext.redirect(externalContext.getRequestContextPath() + "/");
    facesContext.responseComplete();
    return null;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }
}
