package org.apache.myfaces.tobago.model;

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

import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.component.UITreeNodeData;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Stack;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

/**
 * User: lofwyr
 * Date: 23.04.2007 16:10:22
 */
public class MixedTreeModel {

  private DefaultMutableTreeNode root;
  private DefaultMutableTreeNode current;
  private Integer nextChildIndex;
  private DefaultMutableTreeNode dataRoot;
  private boolean isInData;
  private Stack<Boolean> junctions = new Stack<Boolean>();

  public void beginBuildNode(UITreeNode node) {
    if (!isInData) {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(node.getAttributes().get(ATTR_LABEL));
      if (root == null) {
        root = newNode;
      } else {
        current.add(newNode);
        current = newNode;
      }
    }
  }

  public void endBuildNode(UITreeNode node) {
    if (!isInData) {
      current = (DefaultMutableTreeNode) current.getParent();
    }
  }

  public void beginBuildNodeData(UITreeNodeData data) {
    current = new DefaultMutableTreeNode(data.getValue());
    if (root == null) {
      root = current;
    } else {
      root.add(current);
    }
    isInData = true;
  }

  public void endBuildNodeData(UITreeNodeData data) {
    current = (DefaultMutableTreeNode) current.getParent();
    isInData = false;
  }

  public void onEncodeBegin() {
    if (current == null) {
      current = root;
    } else {
      current = (DefaultMutableTreeNode) current.getChildAt(nextChildIndex);
      if (!isInData && current.getUserObject() instanceof DefaultMutableTreeNode) {
        isInData = true;
        dataRoot = current;
        current = (DefaultMutableTreeNode) current.getUserObject();
      }
    }
    nextChildIndex = 0;

    junctions.push(hasCurrentNodeNextSibling());
  }

  public void onEncodeEnd() {
    if (isInData && current.isRoot()) {
      current = dataRoot;
      isInData = false;
    }
    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) current.getParent();
    if (parent != null) {
      nextChildIndex = parent.getIndex(current) + 1;
      current = parent;
    } else {
      nextChildIndex = null;
      current = null;
    }

    junctions.pop();
  }

  public boolean hasCurrentNodeNextSibling() {
    if (isInData && current.isRoot()) {
      return dataRoot.getNextSibling() != null;
    } else {
      return current.getNextSibling() != null;
    }
  }

  public boolean isFolder() {
    return current.getChildCount() > 0;
  }

  public int getDepth() {
    return junctions.size();
  }

  public List<Boolean> getJunctions() {
    Boolean top = junctions.pop();
    List<Boolean> result = Collections.unmodifiableList(new ArrayList<Boolean>(junctions));
    junctions.push(top);
    return result;
  }

}
