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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.model.TreePath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NavigationNode extends DefaultMutableTreeNode implements Comparable<NavigationNode> {
  private static final Logger LOG = LoggerFactory.getLogger(NavigationNode.class);

  private final String name;
  private final String label;
  private String labelPath;
  private final String branch;
  private final String outcome;

  private final NavigationTree tree;

  /**
   * Cache the TreePath for optimization.
   */
  private TreePath treePath;

  public NavigationNode(final String path, final NavigationTree tree) {

    this.tree = tree;
    outcome = path;
    final Pattern pattern = Pattern.compile("(.*)/([^/]*)\\.(xhtml)");
//      final Pattern pattern = Pattern.compile("([\\d\\d/]*\\d\\d[^/]*)/([^/]*)\\.(xhtml)");
    final Matcher matcher = pattern.matcher(path);
    matcher.find();
    branch = matcher.group(1);
    name = matcher.group(2);
//    final String extension = matcher.group(3);
    label = StringUtils.firstToUpperCase(name.replaceAll("[_]", " "));
  }

  @Override
  public int compareTo(final NavigationNode other) {
    return branch.compareTo(other.getBranch());
  }

  public String action() {
    tree.gotoNode(this);
    return outcome;
  }

  public void evaluateTreePath() {
    treePath = new TreePath(this);

    final StringBuilder builder = new StringBuilder(this.getLabel());
    NavigationNode parent = (NavigationNode) this.getParent();
    while (parent != null && parent != tree.getTree()) {
      builder.insert(0, parent.getLabel() + " → ");
      parent = (NavigationNode) parent.getParent();
    }
    labelPath = builder.toString();
  }

  @Override
  public NavigationNode getNextNode() {
    return (NavigationNode) super.getNextNode();
  }

  @Override
  public NavigationNode getPreviousNode() {
    return (NavigationNode) super.getPreviousNode();
  }

  public String getName() {
    return name;
  }

  public String getBranch() {
    return branch;
  }

  public String getLabel() {
    return label;
  }

  public String getLabelPath() {
    return labelPath;
  }

  public String getOutcome() {
    return outcome;
  }

  public TreePath getTreePath() {
    return treePath;
  }

  @Override
  public String toString() {
    return outcome;
  }

}
