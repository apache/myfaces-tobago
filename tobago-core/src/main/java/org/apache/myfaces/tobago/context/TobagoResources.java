package org.apache.myfaces.tobago.context;

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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @since 1.5.0
 */
public final class TobagoResources {

  private boolean production;
  private List<TobagoScript> scriptList = new ArrayList<TobagoScript>();
  private List<TobagoStyle> styleList = new ArrayList<TobagoStyle>();

  public TobagoResources copy() {
    TobagoResources resources = new TobagoResources();
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

  public boolean addScript(TobagoScript script) {
    return scriptList.add(script);
  }

  public boolean addStyle(TobagoStyle style) {
    return styleList.add(style);
  }

  public List<TobagoScript> getScriptList() {
    return scriptList;
  }

  public List<TobagoStyle> getStyleList() {
    return styleList;
  }
}
