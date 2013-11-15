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

package org.apache.myfaces.tobago.example.demo.info;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

public class Activity implements Serializable {

  private String sessionId;

  private Date creationDate;

  private int jsfRequest;

  private int ajaxRequest;

  public Activity(final HttpSession session) {
    this.sessionId = session.getId();
    this.creationDate = new Date(session.getCreationTime());
  }

  public void executeJsfRequest() {
    jsfRequest++;
  }

  public void executeAjaxRequest() {
      ajaxRequest++;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(final String sessionId) {
    this.sessionId = sessionId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(final Date creationDate) {
    this.creationDate = creationDate;
  }

  public int getJsfRequest() {
    return jsfRequest;
  }

  public void setJsfRequest(final int jsfRequest) {
    this.jsfRequest = jsfRequest;
  }

  public int getAjaxRequest() {
    return ajaxRequest;
  }

  public void setAjaxRequest(final int ajaxRequest) {
    this.ajaxRequest = ajaxRequest;
  }
}
