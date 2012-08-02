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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import org.apache.myfaces.tobago.component.UITreeData;
import org.apache.myfaces.tobago.component.UITreeNode;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * User: lofwyr
 * Date: 23.04.2007 16:10:22
 */
public class MixedTreeModel {

  private static final Log LOG = LogFactory.getLog(MixedTreeModel.class);

  private DefaultMutableTreeNode root;
  private DefaultMutableTreeNode current;
  private Integer nextChildIndex;
  private DefaultMutableTreeNode dataRoot;
  private boolean isInData;
  private Stack<Boolean> junctions = new Stack<Boolean>();

  public void beginBuildNode(UITreeNode node) {
    if (LOG.isDebugEnabled()) {
      LOG.debug(node.getAttributes().get(ATTR_LABEL));
    }
    if (!isInData) {
      DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(node.getAttributes().get(ATTR_LABEL));
      if (root == null) {
        root = newNode;
        current = root;
      } else {
        current.add(newNode);
        current = newNode;
      }
    }
  }

  public void endBuildNode(UITreeNode node) {
    if (LOG.isDebugEnabled()) {
      LOG.debug(node.getAttributes().get(ATTR_LABEL));
    }
    if (!isInData) {
      current = (DefaultMutableTreeNode) current.getParent();
    }
  }

  public void beginBuildNodeData(UITreeData data) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("var=" + data.getVar());
    }
    current = new DefaultMutableTreeNode(data.getValue());
    if (root == null) {
      root = current;
    } else {
      root.add(current);
    }
    isInData = true;
  }

  public void endBuildNodeData(UITreeData data) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("var=" + data.getVar());
    }
    current = (DefaultMutableTreeNode) current.getParent();
    isInData = false;
  }

  public void onEncodeBegin() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("current=" + current);
    }
    if (current == null) {
      current = root;
    } else {
      current = (DefaultMutableTreeNode) current.getChildAt(nextChildIndex);
    }
    if (!isInData && current.getUserObject() instanceof DefaultMutableTreeNode) {
      isInData = true;
      dataRoot = current;
      current = (DefaultMutableTreeNode) current.getUserObject();
    }
    nextChildIndex = 0;

    junctions.push(hasCurrentNodeNextSibling());
  }

  public void onEncodeEnd() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("current=" + current);
    }
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
    boolean result;
    if (isInData && current.isRoot()) {
      result = dataRoot.getNextSibling() != null;
    } else {
      result = current.getNextSibling() != null;
    }
    return result;
  }

  public boolean isFolder() {
    boolean folder = current.getChildCount() > 0;
    return folder;
  }

  public int getDepth() {
    int depth = junctions.size();
    return depth;
  }

  public boolean isRoot() {
    return junctions.size() < 2;
  }

  public List<Boolean> getJunctions() {
    Boolean top = junctions.pop();
    List<Boolean> result = new ArrayList<Boolean>(junctions);
    junctions.push(top);
    return result;
  }

}
