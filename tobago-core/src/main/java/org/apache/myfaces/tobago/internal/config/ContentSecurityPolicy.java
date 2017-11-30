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

import org.apache.myfaces.tobago.exception.TobagoConfigurationException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ContentSecurityPolicy {

  private Mode mode;
  private Map<String, String> directiveMap;

  private boolean unmodifiable = false;

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new TobagoConfigurationException("The configuration must not be changed after initialization!");
    }
  }

  /**
   * Lock the configuration, so it cannot be modified any more.
   */
  public void lock() {
    unmodifiable = true;
    directiveMap = Collections.unmodifiableMap(directiveMap);
  }

  public ContentSecurityPolicy(final String mode) {
    this.mode = Mode.parse(mode);
    this.directiveMap = new HashMap<>();
  }

  public void merge(final ContentSecurityPolicy other) {
    checkLocked();
    for (final Map.Entry<String, String> entry : other.directiveMap.entrySet()) {
      addDirective(entry.getKey(), entry.getValue());
    }
    mode = other.mode;
  }

  public void addDirective(final String name, final String text) {
    final String old = directiveMap.get(name);
    if (old != null) {
      directiveMap.put(name, old + ' ' + text);
    } else {
      directiveMap.put(name, text);
    }
  }

  public Map<String, String> getDirectiveMap() {
    return directiveMap;
  }

  public Mode getMode() {
    return mode;
  }

  @Override
  public String toString() {
    return "ContentSecurityPolicy{"
        + "mode=" + mode
        + ", directiveMap=" + directiveMap
        + '}';
  }

  public enum Mode {
    ON("on"),
    OFF("off"),
    REPORT_ONLY("report-only");

    private final String value;

    Mode(final String value) {
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
