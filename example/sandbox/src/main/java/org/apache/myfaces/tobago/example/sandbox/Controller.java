package org.apache.myfaces.tobago.example.sandbox;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.TreeState;

import javax.swing.tree.DefaultMutableTreeNode;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private static final String STRONG = "strong";

  private DefaultMutableTreeNode tree;

  private TreeState state;

  private int sliderValue;

  public Controller() {
    // tree
    tree = new DefaultMutableTreeNode(new Node("Category"));
    tree.insert(new DefaultMutableTreeNode(new Node("Sports")), 0);
    tree.insert(new DefaultMutableTreeNode(new Node("Movies")), 0);
    DefaultMutableTreeNode music = new DefaultMutableTreeNode(new Node("Music"));
    tree.insert(music, 0);
    tree.insert(new DefaultMutableTreeNode(new Node("Games")), 0);
    DefaultMutableTreeNode temp = new DefaultMutableTreeNode(new Node("Science"));
    temp.insert(new DefaultMutableTreeNode(new Node("Geography", STRONG)), 0);
    temp.insert(new DefaultMutableTreeNode(new Node("Mathematics", STRONG)), 0);
    DefaultMutableTreeNode temp2 = new DefaultMutableTreeNode(new Node("Astronomy"));
    temp2.insert(new DefaultMutableTreeNode(new Node("Education")), 0);
    temp2.insert(new DefaultMutableTreeNode(new Node("Pictures")), 0);
    temp.insert(temp2, 2);
    tree.insert(temp, 2);

    // state

    state = new TreeState();
    state.addExpandState(tree);
    state.addExpandState(temp);
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


  public int getSliderValue() {
    return sliderValue;
  }

  public void setSliderValue(int sliderValue) {
    this.sliderValue = sliderValue;
  }

  public String sliderSubmit() {
    LOG.info("Slider: "+sliderValue);
    return null;
  }
}
