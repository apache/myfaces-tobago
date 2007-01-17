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
import org.apache.myfaces.tobago.context.ResourceManagerUtil;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.faces.context.FacesContext;

/**
 * User: lofwyr
 * Date: 09.01.2007
 * Time: 10:41:49
 */
public class Navigation {

  private DefaultMutableTreeNode tree;

  private TreeState state;

  public Navigation() {

    tree = new DefaultMutableTreeNode(new Node("Root", null));

    DefaultMutableTreeNode overview = new DefaultMutableTreeNode(new Node("overview", "overview/intro"));
//    overview.add(new DefaultMutableTreeNode(new Node("intro", "overview/intro")));
    overview.add(new DefaultMutableTreeNode(new Node("basicControls", "overview/basicControls")));
    overview.add(new DefaultMutableTreeNode(new Node("sheetControl", "overview/sheetControl")));
    overview.add(new DefaultMutableTreeNode(new Node("treeControl", "overview/treeControl")));
    overview.add(new DefaultMutableTreeNode(new Node("tabControl", "overview/tabControl")));
    overview.add(new DefaultMutableTreeNode(new Node("toolbar", "overview/toolbar")));
    overview.add(new DefaultMutableTreeNode(new Node("validation", "overview/validation")));
    overview.add(new DefaultMutableTreeNode(new Node("form", "overview/form")));
    overview.add(new DefaultMutableTreeNode(new Node("themes", "overview/themes")));
    overview.add(new DefaultMutableTreeNode(new Node("browser", "overview/browser")));
    overview.add(new DefaultMutableTreeNode(new Node("locale", "overview/locale")));
    overview.add(new DefaultMutableTreeNode(new Node("layout", "overview/layout")));
    tree.add(overview);

    DefaultMutableTreeNode bestPractice = new DefaultMutableTreeNode(new Node("bestPractice", "best-practice/intro"));
    bestPractice.add(new DefaultMutableTreeNode(new Node("error", "best-practice/error")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("theme", "best-practice/theme")));
    bestPractice.add(new DefaultMutableTreeNode(new Node("transition", "best-practice/transition")));
    tree.add(bestPractice);

    state = new TreeState();
    state.expand(tree, 2);
  }

  public String navigate() {
    Node selected = (Node) state.getMarker().getUserObject();
    return selected.getOutcome();
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

  public static class Node {

    private String title;
    private String id;
    private String outcome;


    public Node(String key, String outcome) {
      this.title = ResourceManagerUtil.getProperty(
          FacesContext.getCurrentInstance(), "overview", key);
      this.id = key;
      this.outcome = outcome;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public String getOutcome() {
      return outcome;
    }

    public void setOutcome(String outcome) {
      this.outcome = outcome;
    }
  }
}
