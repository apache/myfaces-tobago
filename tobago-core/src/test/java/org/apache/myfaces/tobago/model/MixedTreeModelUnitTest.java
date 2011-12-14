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
import org.junit.Assert;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;

public class MixedTreeModelUnitTest {

  @Test
  public void testLifecycleSmall() {

    MixedTreeModel model = new MixedTreeModel();

    model.beginBuildNode();
    model.endBuildNode();

    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0), model.getPath());
  }

  @Test
  public void testLifecycleStatic() {

    MixedTreeModel model = new MixedTreeModel();

    model.beginBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.endBuildNode();

    model.onEncodeBegin();
    model.onEncodeBegin();
    model.onEncodeBegin();
  }

  @Test
  public void testLifecycleFromModel() {

    MixedTreeModel model = new MixedTreeModel();
    DefaultMutableTreeNode tree = new DefaultMutableTreeNode("D_0");
    tree.add(new DefaultMutableTreeNode("D_0_0"));
    tree.add(new DefaultMutableTreeNode("D_0_1"));

    UITreeData data = new UITreeData();
    data.setValue(tree);

    model.beginBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.endBuildNode();

    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0, 0), model.getPath());
    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
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

    model.beginBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.beginBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.beginBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.endBuildNode();
    model.endBuildNode();
    model.beginBuildNode();
    model.endBuildNode();
    model.endBuildNode();

    model.onEncodeBegin(); // root
    Assert.assertEquals(new TreePath(0), model.getPath());
    model.onEncodeBegin(); // individual
    Assert.assertEquals(new TreePath(0, 0), model.getPath());
    model.onEncodeBegin(); // data root node
    Assert.assertEquals(new TreePath(0, 1), model.getPath());
    model.onEncodeBegin(); // data sub node 1
    Assert.assertEquals(new TreePath(0, 1, 0), model.getPath());
    model.onEncodeBegin(); // data sub node 2
    Assert.assertEquals(new TreePath(0, 1, 1), model.getPath());
    model.onEncodeBegin(); // data sub node 3
    Assert.assertEquals(new TreePath(0, 1, 1, 0), model.getPath());
    model.onEncodeBegin();
    Assert.assertEquals(new TreePath(0, 2), model.getPath());
  }

}
