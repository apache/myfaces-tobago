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

package org.apache.myfaces.tobago.facelets;

public class TobagoTagLibrary extends AbstractTobagoTagLibrary {

  public static final String NAMESPACE = "http://myfaces.apache.org/tobago/component";

  public static final TobagoTagLibrary INSTANCE = new TobagoTagLibrary();

  public TobagoTagLibrary() {

    super(NAMESPACE);

    // todo: write a unit test to check against the TagDeclaration classes.

    addTobagoComponent("box");
    addTobagoComponent("button");
    addTobagoComponent("calendar");
    addTobagoComponent("cell");
    addTobagoComponent("columnEvent", null);
    addTobagoComponent("columnNode", "TreeNode");
    addTobagoComponent("columnSelector");
    addTobagoComponent("column");
    addTobagoComponent("command");
    addTobagoComponent("datePicker");
    addTobagoComponent("date");
    addTobagoComponent("file");
    addTobagoComponent("flowLayout");
    addTobagoComponent("form");
    addTobagoComponent("gridLayout");
    addTobagoComponent("hidden");
    addTobagoComponent("image");
    addTobagoComponent("in");
    addTobagoComponent("label");
    addTobagoComponent("link");
    addTobagoComponent("mediator");
    addTobagoComponent("menuBar");
    addTobagoComponent("menuCommand");
    addTobagoComponent("menuItem", "MenuCommand");
    addTobagoComponent("menuSeparator");
    addTobagoComponent("menu");
    addTobagoComponent("messages");
    addTobagoComponent("object");
    addTobagoComponent("out");
    addTobagoComponent("page");
    addTobagoComponent("panel");
    addTobagoComponent("popup");
    addTobagoComponent("progress");
    addTobagoComponent("reload", null);
    addTobagoComponent("script");
    addTobagoComponent("selectBooleanCheckbox");
    addTobagoComponent("selectItem", null);
    addTobagoComponent("selectItems", null);
    addTobagoComponent("selectManyCheckbox");
    addTobagoComponent("selectManyListbox");
    addTobagoComponent("selectManyShuttle");
    addTobagoComponent("selectOneChoice");
    addTobagoComponent("selectOneListbox");
    addTobagoComponent("selectOneRadio");
    addTobagoComponent("selectReference");
    addTobagoComponent("separator");
    addTobagoComponent("sheetLayout");
    addTobagoComponent("sheet");
    addTobagoComponent("style");
    addTobagoComponent("tabGroupLayout");
    addTobagoComponent("tabGroup");
    addTobagoComponent("tab");
    addTobagoComponent("textarea");
    addTobagoComponent("time");
    addTobagoComponent("toolBarCommand", "Button");
    addTobagoComponent("toolBarCheck", "MenuCommand");
    addTobagoComponent("toolBarSelectOne", "MenuCommand");
    addTobagoComponent("toolBarSeparator", null);
    addTobagoComponent("toolBar");
    addTobagoComponent("treeCommand");
    addTobagoComponent("treeData");
    addTobagoComponent("treeIcon");
    addTobagoComponent("treeIndent");
    addTobagoComponent("treeLabel");
    addTobagoComponent("treeListbox");
    addTobagoComponent("treeMenu");
    addTobagoComponent("treeNode");
    addTobagoComponent("treeSelect");
    addTobagoComponent("tree");
    addTobagoComponent("wizard");
  }
}
