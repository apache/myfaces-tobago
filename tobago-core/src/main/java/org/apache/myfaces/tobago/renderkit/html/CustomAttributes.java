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

package org.apache.myfaces.tobago.renderkit.html;

public enum CustomAttributes implements MarkupLanguageAttributes {

  COLLAPSE_ACTION("collapse-action"),
  COLLAPSE_TARGET("collapse-target"),
  CONFIRMATION("confirmation"),
  DATA("data"),
  DELAY("delay"),
  EVENT("event"),
  /**
   * &lt;f:ajax&gt; attribute
   */
  EXECUTE("execute"),
  FOCUS_ID("focus-id"),
  /**
   * The index of the tab inside the tab group.
   */
  INDEX("index"),
  LOCAL_MENU("local-menu"),
  MAX_ITEMS("max-items"),
  MIN_CHARS("min-chars"),
  OMIT("omit"),
  ORIENTATION("orientation"),
  /**
   * &lt;f:ajax&gt; attribute
   */
  RENDER("render"),
  /**
   * The mode of the tab switch: client, reloadTab, reloadPage.
   */
  SWITCH_TYPE("switch-type"),
  TOTAL_COUNT("total-count"),
  DECOUPLED("decoupled"),
  UPDATE("update");

  private final String value;

  CustomAttributes(final String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

}
