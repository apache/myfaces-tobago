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

  private boolean production;
  private List<ThemeScript> scriptList = new ArrayList<ThemeScript>();
  private List<ThemeStyle> styleList = new ArrayList<ThemeStyle>();

  public ThemeResources copy() {
    ThemeResources resources = new ThemeResources();
    resources.setProduction(isProduction());
    resources.scriptList.addAll(scriptList);
    resources.styleList.addAll(styleList);
    return resources;
  }

  public boolean isProduction() {
    return production;
  }

  public void setProduction(boolean production) {
    this.production = production;
  }

  public boolean addScript(ThemeScript script) {
    return scriptList.add(script);
  }

  public boolean addStyle(ThemeStyle style) {
    return styleList.add(style);
  }

  public List<ThemeScript> getScriptList() {
    return scriptList;
  }

  public List<ThemeStyle> getStyleList() {
    return styleList;
  }
}
