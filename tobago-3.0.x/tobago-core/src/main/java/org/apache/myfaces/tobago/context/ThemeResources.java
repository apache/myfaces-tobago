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

package org.apache.myfaces.tobago.context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the script and style files for production and development stage.
 *
 * @since 1.5.0
 */
public final class ThemeResources implements Serializable {

  private final boolean production;
  private final List<ThemeScript> scriptList = new ArrayList<ThemeScript>();
  private final List<ThemeScript> scriptExcludes = new ArrayList<ThemeScript>();
  private final List<ThemeStyle> styleList = new ArrayList<ThemeStyle>();
  private final List<ThemeStyle> styleExcludes = new ArrayList<ThemeStyle>();

  public ThemeResources(boolean production) {
    this.production = production;
  }

  public void merge(final ThemeResources toAddResources) {
    if (this == toAddResources) {
      return;
    }
    for (int i = toAddResources.scriptList.size() - 1; i >= 0; i--) {
      final ThemeScript script = toAddResources.scriptList.get(i);
      scriptList.remove(script);
      if (!scriptExcludes.contains(script)) {
        scriptList.add(0, script);
      }
    }
    for (int i = toAddResources.styleList.size() - 1; i >= 0; i--) {
      final ThemeStyle style = toAddResources.styleList.get(i);
      styleList.remove(style);
      if (!styleExcludes.contains(style)) {
        styleList.add(0, style);
      }
    }
  }

  public boolean isProduction() {
    return production;
  }

  public boolean addScript(final ThemeScript script, boolean exclude) {
    return exclude ? scriptExcludes.add(script) : scriptList.add(script);
  }

  public boolean addStyle(final ThemeStyle style, boolean exclude) {
    return exclude ? styleExcludes.add(style) : styleList.add(style);
  }

  public List<ThemeScript> getScriptList() {
    return scriptList;
  }

  public List<ThemeStyle> getStyleList() {
    return styleList;
  }
}
