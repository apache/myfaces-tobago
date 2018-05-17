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

package org.apache.myfaces.tobago.example.test;

import java.io.Serializable;

public abstract class AbstractDynamicPanel implements Serializable {

  private static final String[] PANEL_FILES = new String[]{
      "x-panel-1.xhtml",
      "x-panel-2.xhtml",
      "x-panel-3.xhtml"};

  private final String name;

  public AbstractDynamicPanel() {
    final String simpleName = this.getClass().getSimpleName();
    final int pos = simpleName.length() - 1 - "Controller".length();
    final int number = Integer.parseInt(simpleName.substring(pos, pos + 1)) - 1;
    this.name = PANEL_FILES[number];
  }

  public String getName() {
    return name;
  }
}
