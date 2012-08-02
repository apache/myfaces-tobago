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

import junit.framework.TestCase;
import org.apache.myfaces.tobago.component.UITreeData;
import org.apache.myfaces.tobago.component.UITreeNode;

import javax.swing.tree.DefaultMutableTreeNode;

public class MixedTreeModelUnitTest extends TestCase {

  public void testLifecycleSmall() {

    MixedTreeModel model = new MixedTreeModel();

    UITreeNode n1 = new UITreeNode();

    model.beginBuildNode(n1);
    model.endBuildNode(n1);

    model.onEncodeBegin();
    model.onEncodeEnd();
  }

  public void testLifecycleStatic() {

    MixedTreeModel model = new MixedTreeModel();

    UITreeNode n1 = new UITreeNode();
    UITreeNode n2 = new UITreeNode();
    UITreeNode n3 = new UITreeNode();

    model.beginBuildNode(n1);
    model.beginBuildNode(n2);
    model.endBuildNode(n2);
    model.beginBuildNode(n3);
    model.endBuildNode(n3);
    model.endBuildNode(n1);

    model.onEncodeBegin();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeEnd();
  }

  public void testLifecycleFromModel() {

    MixedTreeModel model = new MixedTreeModel();
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode();
    tree.add(new DefaultMutableTreeNode());
    tree.add(new DefaultMutableTreeNode());

    UITreeData data = new UITreeData();
    data.setValue(tree);
    UITreeNode node = new UITreeNode();

    model.beginBuildNodeData(data);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.endBuildNodeData(data);

    model.onEncodeBegin();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeEnd();
  }

  public void testLifecycleMixed() {

    MixedTreeModel model = new MixedTreeModel();
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode();
    tree.add(new DefaultMutableTreeNode());
    tree.add(new DefaultMutableTreeNode());

    UITreeData data = new UITreeData();
    data.setValue(tree);
    UITreeNode node = new UITreeNode();

    model.beginBuildNode(node);
    model.beginBuildNodeData(data);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.endBuildNodeData(data);
    model.endBuildNode(node);

    model.onEncodeBegin();
    model.onEncodeBegin();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeBegin();
    model.onEncodeEnd();
    model.onEncodeEnd();
    model.onEncodeEnd();
  }

}
