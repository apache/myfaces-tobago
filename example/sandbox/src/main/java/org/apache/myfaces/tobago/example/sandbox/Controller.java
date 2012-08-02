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

package org.apache.myfaces.tobago.example.sandbox;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.model.TreeState;
import org.apache.myfaces.tobago.model.Wizard;

import javax.swing.tree.DefaultMutableTreeNode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private static final String STRONG = "strong";

  private DefaultMutableTreeNode tree;

  private TreeState state;

  private int sliderValue;

  private Wizard wizard;

  private String filterType;
  private String filterValue;

  public Controller() {
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

    // state

    state = new TreeState();
    state.addExpandState(tree);
    state.addExpandState(temp);
    state.addSelection(temp2);
    state.setMarker(music);

    // wizard

    wizard = new Wizard();
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

  public String createFilter() {
    LOG.info("Filter type: '" + filterType + "'");
    if ("fileInto".equals(filterType)) {
      return "fileIntoCondition";
    } else if ("forward".equals(filterType)) {
      return "forwardCondition";
    }
    throw new RuntimeException("No filter type set.");
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


  public int getSliderValue() {
    return sliderValue;
  }

  public void setSliderValue(int sliderValue) {
    this.sliderValue = sliderValue;
  }

  public String sliderSubmit() {
    LOG.info("Slider: " + sliderValue);
    return null;
  }

  public Wizard getWizard() {
    return wizard;
  }

  public String getFilterType() {
    return filterType;
  }

  public void setFilterType(String filterType) {
    this.filterType = filterType;
  }

  public String getFilterValue() {
    return filterValue;
  }

  public void setFilterValue(String filterValue) {
    this.filterValue = filterValue;
  }
}
