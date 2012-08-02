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
