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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Named
@ApplicationScoped
public class ActivityList implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  // XXX using the session id as key is not good for applications with login, because the container should change
  // XXX the session id while the login process.
  private Map<String, Activity> data = new ConcurrentHashMap<>();

  public void add(final Activity activity) {
    LOG.info("Adding session id: '{}'", activity.getSessionId());
    data.put(activity.getSessionId(), activity);
  }

  public void remove(final String sessionId) {
    LOG.info("Removing session id: '{}'", sessionId);
    data.remove(sessionId);
  }

  public List<Activity> getValues() {
    return new ArrayList<>(data.values());
  }

  public void executeJsfRequest(final String sessionId) {
    final Activity activity = data.get(sessionId);
    if (activity != null) {
      activity.executeJsfRequest();
    } else {
      LOG.error("Ignoring sessionId='{}'", sessionId);
    }
  }

  public void executeAjaxRequest(final String sessionId) {
    final Activity activity = data.get(sessionId);
    if (activity != null) {
      activity.executeAjaxRequest();
    } else {
      LOG.error("Ignoring sessionId='{}'", sessionId);
    }
  }
}
