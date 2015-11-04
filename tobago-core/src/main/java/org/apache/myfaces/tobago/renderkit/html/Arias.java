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

public enum Arias implements MarkupLanguageAttributes {

  ACTIVEDESCENDANT("aria-activedescendant"),
  ATOMIC("aria-atomic"),
  BUSY("aria-busy"),
  CONTROLS("aria-controls"),
  DESCRIBEDBY("aria-describedby"),
  DROPEFFECT("aria-dropeffect"),
  EXPANDED("aria-expanded"),
  FLOWTO("aria-flowto"),
  GRABBED("aria-grabbed"),
  HASPOPUP("aria-haspopup"),
  HIDDEN("aria-hidden"),
  LABEL("aria-label"),
  LABELLEDBY("aria-labelledby"),
  LEVEL("aria-level"),
  LIVE("aria-live"),
  ORIENTATION("aria-orientation"),
  OWNS("aria-owns"),
  POSINSET("aria-posinset"),
  PRESSED("aria-pressed"),
  RELEVANT("aria-relevant"),
  SETSIZE("aria-setsize"),
  SORT("aria-sort");

  private String value;

  Arias(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
