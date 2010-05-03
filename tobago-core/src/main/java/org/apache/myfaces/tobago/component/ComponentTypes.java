package org.apache.myfaces.tobago.component;

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

/**
 * Constants for the renderer type
 */
public final class ComponentTypes {

  // TODO: make a complete list.

  public static final String COLUMN = "org.apache.myfaces.tobago.Column";
  public static final String GRID_LAYOUT = "org.apache.myfaces.tobago.GridLayout";
  public static final String OUT = "org.apache.myfaces.tobago.Out";
  public static final String SELECT_BOOLEAN_CHECKBOX = "org.apache.myfaces.tobago.SelectBooleanCheckbox";
  public static final String SHEET_LAYOUT = "org.apache.myfaces.tobago.SheetLayout";
  public static final String TAB_GROUP_LAYOUT = "org.apache.myfaces.tobago.TabGroupLayout";

  private ComponentTypes() {
    // to prevent instantiation
  }
}
