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

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

// todo: make more general (e.g. support other trees)

// todo: Should be the model for the whole tree, (also for tc:treeNode on the JSP)
public class TreeModel {

  private static final Log LOG = LogFactory.getLog(TreeModel.class);

  private Map<String, DefaultMutableTreeNode> nodes = new HashMap<String, DefaultMutableTreeNode>();
  private List<String> keys = new ArrayList<String>();

  // XXX not nice
  private List<Tag> doubleKeys = new ArrayList<Tag>(); // with "begin tags" and "end tags"

  public TreeModel(DefaultMutableTreeNode node) {
    putNodes(node, "", 0);
  }

  private void putNodes(
      DefaultMutableTreeNode node, String position, int index) {

    if (node == null) { // XXX hotfix
      LOG.warn("node is null");
      return;
    }

    position += "_" + index;

    keys.add(position);
    doubleKeys.add(new Tag(position, true));
    nodes.put(position, node);

    index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) e.nextElement();
      putNodes(subNode, position, index);
      index++;
    }

    doubleKeys.add(new Tag(position, false));
  }

  public DefaultMutableTreeNode getNode(String pathIndex) {
    return nodes.get(pathIndex);
  }

  public List<String> getPathIndexList() {
    return Collections.unmodifiableList(keys);
  }

  public List<Tag> getDoublePathIndexList() {
    return Collections.unmodifiableList(doubleKeys);
  }

  public String getParentPathIndex(String pathIndex) {
    int lastUnderscore = pathIndex.lastIndexOf('_');
    switch (lastUnderscore) {
      case -1:
        throw new IllegalArgumentException();
      case 0:
        return null;
      default:
        return pathIndex.substring(0, lastUnderscore);
    }
  }

  public static class Tag {

    private String name;

    private boolean start;

    public Tag(String name, boolean start) {
      this.name = name;
      this.start = start;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public boolean isStart() {
      return start;
    }

    public void setStart(boolean start) {
      this.start = start;
    }
  }
}
