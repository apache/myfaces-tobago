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

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import jakarta.enterprise.inject.spi.CDI;

public class ActivitySessionListener implements HttpSessionListener {

  @Override
  public void sessionCreated(final HttpSessionEvent event) {
    final HttpSession session = event.getSession();
    final ActivityList activityList = CDI.current().select(ActivityList.class).get();

    activityList.add(new Activity(session));
  }

  @Override
  public void sessionDestroyed(final HttpSessionEvent event) {
    final HttpSession session = event.getSession();
    final ActivityList activityList = CDI.current().select(ActivityList.class).get();

    activityList.remove(session.getId());
  }
}
