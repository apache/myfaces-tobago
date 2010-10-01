package org.apache.myfaces.tobago.example.demo.activity;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ActivityList {

  private static final Log LOG = LogFactory.getLog(ActivityList.class);

  public static final String NAME = "activities";

  private Map<String, Activity> data = new ConcurrentHashMap<String, Activity>();
  
  public void add(Activity activity) {
    LOG.info("Adding session id: " + activity.getSessionId());
    data.put(activity.getSessionId(),activity);
  }

  public void remove(String sessionId) {
    LOG.info("Removing session id: " + sessionId);
    final Activity activity = data.remove(sessionId);
  }

  public List<Activity> getValues() {
    final Collection<Activity> values = data.values();
    ArrayList<Activity> result = new ArrayList<Activity>();
    result.addAll(values);
    return result;
  }

  public void jsfRequest(String sessionId) {
    data.get(sessionId).jsfRequest();
  }

  public void ajaxRequest(String sessionId) {
    data.get(sessionId).ajaxRequest();
  }
}
