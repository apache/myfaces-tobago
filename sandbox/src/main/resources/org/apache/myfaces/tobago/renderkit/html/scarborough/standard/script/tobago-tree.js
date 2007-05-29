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

function tobagoTreeNodeToggle(node, treeHiddenId, openFolderIcon, folderIcon, openMenuIcon, closeMenuIcon) {
  LOG.debug("toggle(" + node + ", " + treeHiddenId + ", " + openFolderIcon + ", " + folderIcon + ", "
      + openMenuIcon + ", " + closeMenuIcon + ")");
  var content = document.getElementById(node.id + "-cont");
  if (content) {
    var expandedState = document.getElementById(node.id + '-expanded');
    var icon = document.getElementById(node.id + '-icon');
    var menuIcon = document.getElementById(node.id + '-menuIcon');
    var junction = document.getElementById(node.id + '-junction');
    var hidden = document.getElementById(treeHiddenId);
    if (content.style.display == 'none') {
      content.style.display = 'block';
      if (icon) {
        icon.src = openFolderIcon;
      }
      if (menuIcon) {
        menuIcon.src = openMenuIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/plus\./, "minus.");
      }
      hidden.value = hidden.value + nodeStateId(node) + ";";
      expandedState.value = "true";
    } else {
      content.style.display = 'none';
      if (icon) {
        icon.src = folderIcon;
      }
      if (menuIcon) {
        menuIcon.src = closeMenuIcon;
      }
      if (junction) {
        junction.src = junction.src.replace(/minus\./, "plus.");
      }
      hidden.value = hidden.value.replace(";" + nodeStateId(node) + ";", ";");
      expandedState.value = "false";
    }
  }
}

function nodeStateId(node) {
  // this must do the same as nodeStateId() in TreeRenderer.java
  return node.id.substring(node.id.lastIndexOf(':') + 1);
}
