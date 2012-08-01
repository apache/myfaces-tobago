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

package org.apache.myfaces.tobago.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @deprecated since 1.6.0
 */
@Deprecated
public class Node {

  private List<Node> children;

  private Node parent;

  public void add(Node node) {
    initChildren();
    children.add(node);
    node.setParent(this);
  }

  public List<Node> getChildren() {
    initChildren();
    return children;
  }

  private void initChildren() {
    if (children == null) {
      children = new ArrayList<Node>();
    }
  }

  public Node getChildAt(int index) {
    initChildren();
    return children.get(index);
  }

  public boolean isRoot() {
    return parent == null;
  }

  public int getIndex(Node node) {
    for (int i = 0; i < children.size(); i++) {
      Node child = children.get(i);
      if (child.equals(node)) {
        return i;
      }
    }
    return -1;
  }

  public int getChildCount() {
    return children == null ? 0 : children.size();
  }

  public boolean hasNextSibling() {
    return parent != null && parent.getIndex(this) + 1 < parent.getChildCount();
  }

  public Node nextSibling() {
    if (parent == null) {
      return null;
    }
    final int nextIndex = parent.getIndex(this) + 1;
    return nextIndex < parent.getChildCount() ? parent.getChildAt(nextIndex) : null;
  }

  public TreePath getPath() {
    List<Integer> result = new ArrayList<Integer>();
    Node node = this;
    Node parent = this.parent;
    while (parent != null) {
      int index = parent.getIndex(node);
      result.add(index);
      node = parent;
      parent = node.getParent();
    }
    result.add(0);
    Collections.reverse(result);
    return new TreePath(result);
  }

  public Node getParent() {
    return parent;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }
}
