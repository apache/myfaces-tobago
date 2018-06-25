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

import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
@Named
public class ApiController implements Serializable {

  private List<Release> releases;
  private boolean onlyCurrent = true;

  public ApiController() {
    init();
  }

  public void init(final AjaxBehaviorEvent event) {
    init();
  }

  private void init() {
    releases = new ArrayList<>();
    for (Release release : Release.values()) {
      if (!release.isUnreleased() && (!onlyCurrent || release.isCurrent())) {
        releases.add(release);
      }
    }
    Collections.reverse(releases);
  }

  public List<Release> getReleases() {
    return releases;
  }

  public String getBase() {
    return "http://myfaces.apache.org/tobago";
  }

  public boolean isOnlyCurrent() {
    return onlyCurrent;
  }

  public void setOnlyCurrent(boolean onlyCurrent) {
    this.onlyCurrent = onlyCurrent;
  }

  public String getVersion430() {
    return Release.v4_3_0.getVersion();
  }
}
