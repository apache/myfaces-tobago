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

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequestScoped
@Named
public class ApiController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

  public String getApiBase() {
    return "https://javadoc.io/doc/org.apache.myfaces.tobago/tobago-core";
  }

  public String getTldBase() {
    return "https://myfaces.apache.org/tobago/doc";
  }

  public String getTobagoTld() {
    return getTldBase() + "/" + getCurrentRelease() + "/tld";
  }

  public String getFacesTld() {
    return "https://jakarta.ee/specifications/faces/3.0/vdldoc";
  }

  public boolean isOnlyCurrent() {
    return onlyCurrent;
  }

  public void setOnlyCurrent(boolean onlyCurrent) {
    this.onlyCurrent = onlyCurrent;
  }

  public String getVersion500() {
    return Release.v5_0_0.getVersion();
  }

  public String getCurrentRelease() {
    for (Release release : releases) {
      if (release.isCurrent()) {
        return release.getVersion();
      }
    }
    LOG.error("No current release found!");
    return Release.v6_7_2.getVersion(); // should not happen
  }

  public String getJiraUrl(final String version) {
    final Release release = Release.valueOf("v" + version.replaceAll("\\.", "_"));
    return "https://issues.apache.org/jira/secure/ReleaseNote.jspa?projectId=12310273&version=" + release.getJira();
  }
}
