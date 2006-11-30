package org.apache.myfaces.tobago.component;

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

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

// todo: make more general (e.g. support other trees)
public class TreeModel {

  private static final Log LOG = LogFactory.getLog(TreeModel.class);

  Map<String, DefaultMutableTreeNode> nodes = new HashMap<String, DefaultMutableTreeNode>();
  List<String> keys = new ArrayList<String>();

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
    nodes.put(position, node);

    index = 0;
    for (Enumeration e = node.children(); e.hasMoreElements();) {
      DefaultMutableTreeNode subNode = (DefaultMutableTreeNode) e.nextElement();
      putNodes(subNode, position, index);
      index++;
    }
  }

  public DefaultMutableTreeNode getNode(String pathIndex) {
    return nodes.get(pathIndex);
  }

  public List<String> getPathIndexList() {
    return Collections.unmodifiableList(keys);
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
}
