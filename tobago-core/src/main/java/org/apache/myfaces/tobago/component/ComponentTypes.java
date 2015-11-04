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

package org.apache.myfaces.tobago.component;

/**
 * Constants for the renderer type
 */
public final class ComponentTypes {

  // TODO: make a complete list.

  public static final String BOX = "org.apache.myfaces.tobago.Box";
  public static final String BUTTON = "org.apache.myfaces.tobago.Button";
  @Deprecated
  public static final String CELL = "org.apache.myfaces.tobago.Cell";
  public static final String COLUMN = "org.apache.myfaces.tobago.Column";
  public static final String COLUMN_EVENT = "org.apache.myfaces.tobago.ColumnEvent";
  public static final String COLUMN_SELECTOR = "org.apache.myfaces.tobago.ColumnSelector";
  public static final String COMMAND = "org.apache.myfaces.tobago.Command";
  public static final String DATE_PICKER = "org.apache.myfaces.tobago.DatePicker";
  public static final String EXTENSION_PANEL = "org.apache.myfaces.tobago.ExtensionPanel";
  public static final String GRID_LAYOUT = "org.apache.myfaces.tobago.GridLayout";
  public static final String IN = "org.apache.myfaces.tobago.In";
  public static final String LINK = "org.apache.myfaces.tobago.Link";
  public static final String OUT = "org.apache.myfaces.tobago.Out";
  public static final String PANEL = "org.apache.myfaces.tobago.Panel";
  public static final String POPUP = "org.apache.myfaces.tobago.Popup";
  public static final String RELOAD = "org.apache.myfaces.tobago.Reload";
  public static final String SCRIPT = "org.apache.myfaces.tobago.Script";
  public static final String SEPARATOR = "org.apache.myfaces.tobago.Separator";
  public static final String SELECT_BOOLEAN_CHECKBOX = "org.apache.myfaces.tobago.SelectBooleanCheckbox";
  public static final String STYLE = "org.apache.myfaces.tobago.Style";

  private ComponentTypes() {
    // to prevent instantiation
  }
}
