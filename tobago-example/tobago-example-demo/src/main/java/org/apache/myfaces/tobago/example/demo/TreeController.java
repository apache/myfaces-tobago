package org.apache.myfaces.tobago.example.demo;

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

import org.apache.myfaces.tobago.model.TreeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.event.ActionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TreeController {

  private static final Logger LOG = LoggerFactory.getLogger(TreeController.class);

  private static final String STRONG = "strong";

  private DefaultMutableTreeNode tree;

  private TreeState state;

  public TreeController() {
    // tree
    tree = new DefaultMutableTreeNode(new Node("1 Category"));
    tree.add(new DefaultMutableTreeNode(new Node("1.1 Sports")));
    tree.add(new DefaultMutableTreeNode(new Node("1.2 Movies")));
    DefaultMutableTreeNode temp = new DefaultMutableTreeNode(new Node("1.3 Science"));
    tree.add(temp);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(new Node("1.4 Music"));
    tree.add(music);
    tree.add(new DefaultMutableTreeNode(new Node("1.5 Games")));
    temp.add(new DefaultMutableTreeNode(new Node("1.3.1 Geography (strong markup)", STRONG)));
    temp.add(new DefaultMutableTreeNode(new Node("1.3.2 Mathematics (strong markup)", STRONG)));
    DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode(new Node("1.3.3 Pictures"));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.1 Education")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.2 Family")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.3 Comercial")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.4 Summer (disabled)", true)));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.5 Winter (disabled)", true)));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.6 Red")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.7 Black")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.8 White")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.9 Good")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.10 Evil")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.11 Flower")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.12 Animal")));
    temp2.add(new DefaultMutableTreeNode(new Node("1.3.3.13 Personal")));
    temp.add(temp2);
    DefaultMutableTreeNode bulk = new DefaultMutableTreeNode(new Node("1.6 Bulk"));
    for (int i = 0; i < 5; i++) {
      bulk.add(new DefaultMutableTreeNode(new Node("1.6." + (i + 1) + " Some Node")));
    }
    tree.add(bulk);
    ((Node) tree.getUserObject()).setExpanded(true);
    ((Node) temp.getUserObject()).setExpanded(true);
    ((Node) tree.getUserObject()).setSelected(true);
    ((Node) temp.getUserObject()).setSelected(true);

    // state
    state = new TreeState();
    state.addSelection(temp2);
    state.setMarker(music);
  }

  public String action1() {
    LOG.info("action 1");
    return null;
  }

  public String action2() {
    LOG.info("action 2");
    return null;
  }

  public void actionListener(ActionEvent event) {
    LOG.info("actionListener");
  }

  public String action3() {
    LOG.info("action 3");
    return null;
  }

  public String createNode() {
    DefaultMutableTreeNode marker = state.getMarker();
    if (marker != null) {
      marker.insert(new DefaultMutableTreeNode(new Node("New Node")), 0);
    } else {
      // todo: print a warning or use root?
    }
    return null;
  }

  public String deleteNode() {
    DefaultMutableTreeNode marker = state.getMarker();
    if (marker != null) {
      marker.removeFromParent();
    } else {
      // todo: print a warning or use root?
    }
    return null;
  }

  public String getCurrentTime() {
    return new SimpleDateFormat("hh:MM:ss").format(new Date());
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
