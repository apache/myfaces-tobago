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

import org.apache.myfaces.tobago.component.UITreeData;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;

public class MixedTreeModelUnitTest {

  @Test
  public void testLifecycleSmall() {

    MixedTreeModel model = new MixedTreeModel();

    UITreeNode n1 = new UITreeNode();

    model.beginBuildNode(n1);
    model.endBuildNode(n1);

    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeEnd();
  }

  @Test
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

  @Test
  public void testLifecycleFromModel() {

    MixedTreeModel model = new MixedTreeModel();
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode("D_0");
    tree.add(new DefaultMutableTreeNode("D_0_0"));
    tree.add(new DefaultMutableTreeNode("D_0_1"));

    UITreeData data = new UITreeData();
    data.setValue(tree);
    UITreeNode node = new UITreeNode();

//    model.beginBuildNodeData(data);
    model.beginBuildNode(node);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.endBuildNode(node);
//    model.endBuildNodeData(data);

    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0, 0), model.getPath());
    model.onEncodeEnd();
    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
    model.onEncodeEnd();
    model.onEncodeEnd();
  }

  /**
   * * --o Root (0)
   * *   |
   * *   +--o Individual Node (0,0)
   * *   |
   * *   +--o Data Root Node (0,1)
   * *      |
   * *      +--o Data Sub Node 1 (0,1,0)
   * *      |
   * *      +--o Data Sub Node 2 (0,1,1)
   * *         |
   * *         +--o Data Sub Sub Node 3 (0,1,1,0)
   */
  @Test
  public void testLifecycleMixed() {

    MixedTreeModel model = new MixedTreeModel();
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode("root");
    tree.add(new DefaultMutableTreeNode("node1"));
    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("node2");
    node2.add(new DefaultMutableTreeNode("node3"));
    tree.add(node2);

    UITreeData data = new UITreeData();
    data.setValue(tree);
    UITreeNode root = new UITreeNode();
    UITreeNode individual = new UITreeNode();
    UITreeNode node = new UITreeNode();

    model.beginBuildNode(root);
    model.beginBuildNode(individual);
    model.endBuildNode(individual);
//    model.beginBuildNodeData(data);
    model.beginBuildNode(node);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.beginBuildNode(node);
    model.beginBuildNode(node);
    model.endBuildNode(node);
    model.endBuildNode(node);
    model.endBuildNode(node);
//    model.endBuildNodeData(data);
    model.endBuildNode(root);

    model.onEncodeBegin(); // root
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeBegin(); // individual
    Assert.assertEquals(new TreePath(0, 0), model.getPath());
    model.onEncodeEnd(); // individual
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeBegin(); // data root node
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
    model.onEncodeBegin(); // data sub node 1
    Assert.assertEquals(new TreePath(0, 1, 0), model.getPath());
    model.onEncodeEnd(); // data sub node 1
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
    model.onEncodeBegin(); // data sub node 2
    Assert.assertEquals(new TreePath(0, 1, 1), model.getPath());
    model.onEncodeBegin(); // data sub node 3
    Assert.assertEquals(new TreePath(0, 1, 1, 0), model.getPath());
    model.onEncodeEnd(); // data sub node 3
    Assert.assertEquals(new TreePath(0, 1, 1), model.getPath());
    model.onEncodeEnd(); // data sub node 2
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
    model.onEncodeEnd();  // data root node
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeEnd(); // root
  }

}
