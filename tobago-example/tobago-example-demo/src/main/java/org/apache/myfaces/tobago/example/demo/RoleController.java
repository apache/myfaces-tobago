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

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.time.LocalTime;

@SessionScoped
@Named
public class RoleController implements Serializable {

  private String text;
  private static final String OUTCOME_ADMIN = "admin";
  private String time;

  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  @RolesAllowed({"demo-admin", "demo-guest"})
  public boolean guestBox() {
    return true;
  }

  @RolesAllowed({"demo-admin"})
  public boolean adminBox() {
    return true;
  }

  @RolesAllowed({"demo-admin"})
  public String admin() {
    return OUTCOME_ADMIN;
  }

  public String getTime() {
    return time;
  }

  @RolesAllowed({"demo-admin", "demo-guest"})
  public String refreshTime() {
    time = LocalTime.now().toString();
    return null;
  }
}
