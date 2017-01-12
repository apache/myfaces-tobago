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

package org.apache.myfaces.tobago.internal.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentSecurityPolicy {

  private Mode mode;
  private List<String> directiveList;

  private boolean unmodifiable = false;

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new RuntimeException("The configuration must not be changed after initialization!");
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    unmodifiable = true;
    directiveList = Collections.unmodifiableList(directiveList);
  }

  public ContentSecurityPolicy(final String mode) {
    this.mode = Mode.parse(mode);
    this.directiveList = new ArrayList<String>();
  }

  public void merge(final ContentSecurityPolicy other) {
    checkLocked();
    directiveList.addAll(other.directiveList);
    mode = other.mode;
  }

  public List<String> getDirectiveList() {
    return directiveList;
  }

  public Mode getMode() {
    return mode;
  }

  @Override
  public String toString() {
    return "ContentSecurityPolicy{"
        + "mode=" + mode
        + ", directiveList=" + directiveList
        + '}';
  }

  public static enum Mode {
    ON("on"),
    OFF("off"),
    REPORT_ONLY("report-only");

    private final String value;

    private Mode(final String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public static Mode parse(final String string) {
      if (ON.value.equals(string)) {
        return ON;
      } else if (OFF.value.equals(string)) {
        return OFF;
      } else if (REPORT_ONLY.value.equals(string)) {
        return REPORT_ONLY;
      } else {
        throw new IllegalArgumentException("Found: " + string);
      }
    }
  }

}
