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

package org.apache.myfaces.tobago.example.security;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;


@PermitAll()
public class Controller {

  public String logout() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    if (session != null) {
      session.invalidate();
    }
    return "logout";
  }

  @RolesAllowed({ "tobago-example-security" })
  public String doSomething() {
    return null;
  }

  public String doSomethingElse() {
    return null;
  }

  @RolesAllowed({ "tobago-example-security" })  
  public String admin() {
    return null;
  }

}
