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
