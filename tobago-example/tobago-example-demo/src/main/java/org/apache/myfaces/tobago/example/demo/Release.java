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

public enum Release {

  v2_4_4("12349632", true),
  v2_4_5("12349661", false, true),
  v2_5_0("12345962", false, true),

  v4_5_3("12349662", true),
  v4_5_4("12350057", false, true),

  v5_0_0("12338729", false, true),
  v5_0_1("12344151", false, true),
  v5_1_0("12344152", false, true);

  private final String jira;
  private final String version;
  private final boolean current;
  private final boolean unreleased;

  Release(final String jira) {
    this(jira, false, false);
  }

  Release(final String jira, final boolean current) {
    this(jira, current, false);
  }

  Release(final String jira, final boolean current, final boolean unreleased) {
    this.current = current;
    this.jira = jira;
    this.unreleased = unreleased;
    version = name()
        .substring(1)
        .replaceAll("_alpha_", "-alpha-")
        .replaceAll("_beta_", "-beta-")
        .replace('_', '.');
  }

  public String getVersion() {
    return version;
  }

  public boolean isCurrent() {
    return current;
  }

  public boolean isUnreleased() {
    return unreleased;
  }

  public String getJira() {
    return jira;
  }
}
