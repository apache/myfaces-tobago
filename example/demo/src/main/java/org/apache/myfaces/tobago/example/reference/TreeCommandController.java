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

package org.apache.myfaces.tobago.example.reference;

import org.apache.myfaces.tobago.model.TreeState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeCommandController {

  private static final Log LOG = LogFactory.getLog(TreeCommandController.class);


  private DefaultMutableTreeNode tree;
  private TreeState state;


  public TreeCommandController() {
    tree = new DefaultMutableTreeNode("Category");
    tree.insert(new DefaultMutableTreeNode("Sports"), 0);
    tree.insert(new DefaultMutableTreeNode("Movies"), 0);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode("Music");
    tree.insert(music, 0);
    tree.insert(new DefaultMutableTreeNode("Games"), 0);
    DefaultMutableTreeNode temp = new DefaultMutableTreeNode("Science");
    temp.insert(
        new DefaultMutableTreeNode("Geography"), 0);
    temp.insert(
        new DefaultMutableTreeNode("Mathematics"), 0);
    DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode("Astronomy");
    temp2.insert(new DefaultMutableTreeNode("Education"), 0);
    temp2.insert(new DefaultMutableTreeNode("Pictures"), 0);
    temp.insert(temp2, 2);
    tree.insert(temp, 2);
    state = new TreeState();
    state.addExpandState(tree);
    state.addExpandState(temp);
    state.setMarker(music);
  }

  public String command() {
    LOG.info(state.getMarker());
    return null;
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public void setTree(DefaultMutableTreeNode tree) {
    this.tree = tree;
  }

  public TreeState getState() {
    return state;
  }

  public void setState(TreeState state) {
    this.state = state;
  }
}
