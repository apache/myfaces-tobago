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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Date: 23.04.2007 16:10:22
 */
public class MixedTreeModel {

  private static final Logger LOG = LoggerFactory.getLogger(MixedTreeModel.class);

  private Node root;
  private Node current;
  private Integer nextChildIndex;
  private Stack<Boolean> junctions = new Stack<Boolean>();

  public void beginBuildNode() {
    Node newNode = new Node();
    if (root == null) {
      root = newNode;
      current = root;
    } else {
      current.add(newNode);
      current = newNode;
    }
  }

  public void endBuildNode() {
    current = current.getParent();
  }

  public void onEncodeBegin() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("current=" + current);
    }
    if (current == null) {
      current = root;
    } else {
      current = current.getChildAt(nextChildIndex);
    }
    nextChildIndex = 0;

    junctions.push(hasCurrentNodeNextSibling());
  }

  public void onEncodeEnd() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("current=" + current);
    }
    Node parent = current.getParent();
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
    return current.hasNextSibling();
  }

  public List<Boolean> getJunctions() {
    Boolean top = junctions.pop();
    List<Boolean> result = new ArrayList<Boolean>(junctions);
    junctions.push(top);
    return result;
  }

  public TreePath getPath() {
    return current.getPath();
  }
}
