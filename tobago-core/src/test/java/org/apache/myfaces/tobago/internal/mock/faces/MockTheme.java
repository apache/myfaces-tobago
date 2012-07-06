package org.apache.myfaces.tobago.internal.mock.faces;

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

import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.internal.config.RenderersConfig;

import java.util.List;

public class MockTheme implements Theme {

  private String name;

  private String displayName;

  private List<Theme> fallbackThemeList;

  private RenderersConfig config = new MockRenderersConfig();

  public MockTheme(String name, String displayName, List<Theme> fallbackThemeList) {
    this.name = name;
    this.displayName = displayName;
    this.fallbackThemeList = fallbackThemeList;
  }

  public String getName() {
    return name;
  }

  public List<Theme> getFallbackList() {
    return fallbackThemeList;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getResourcePath() {
    return null;
  }

  public RenderersConfig getRenderersConfig() {
    return config;
  }

  public String[] getScriptResources(boolean production) {
    return new String[0];
  }

  public String[] getStyleResources(boolean production) {
    return new String[0];
  }
}
