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

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.example.data.CategoryTree;
import org.apache.myfaces.tobago.example.data.MixedCommandTree;
import org.apache.myfaces.tobago.example.data.NamedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SessionScoped
@Named
public class TreeController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TreeController.class);

  private static final String STRONG = "strong";

  private DefaultMutableTreeNode tree;

  private NamedNode mixed;

  public TreeController() {
    // tree
    tree = CategoryTree.createSample2();
    mixed = MixedCommandTree.createSample();
  }

  public String action1() {
    LOG.info("action 1");
    return null;
  }

  public String action2() {
    LOG.info("action 2");
    return null;
  }

  public void actionListener(final ActionEvent event) {
    LOG.info("actionListener");
  }

  public String action3() {
    LOG.info("action 3");
    return null;
  }

  public String createNode() {
/*
    DefaultMutableTreeNode marker = state.getMarker();
    if (marker != null) {
      marker.insert(new DefaultMutableTreeNode(new Node("New Node")), 0);
    } else {
      // todo: print a warning or use root?
    }
*/
    return null;
  }

  public String deleteNode() {
/*
    DefaultMutableTreeNode marker = state.getMarker();
    if (marker != null) {
      marker.removeFromParent();
    } else {
      // todo: print a warning or use root?
    }
*/
    return null;
  }

  public String getCurrentTime() {
    return new SimpleDateFormat("hh:MM:ss").format(new Date());
  }

  public DefaultMutableTreeNode getTree() {
    return tree;
  }

  public void setTree(final DefaultMutableTreeNode tree) {
    this.tree = tree;
  }

  public NamedNode getMixed() {
    return mixed;
  }
}
