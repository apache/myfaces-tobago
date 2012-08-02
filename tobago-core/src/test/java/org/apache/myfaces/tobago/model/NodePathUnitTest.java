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

import org.junit.Assert;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;

public class NodePathUnitTest {

  @Test
  public void testGetPath() {
    TreePath nodePath = new TreePath(0, 1, 2);
    Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2}, nodePath.getPath()));
    TreePath nodePath2 = new TreePath(nodePath, 3);
    Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2, 3}, nodePath2.getPath()));
  }

  @Test
  public void testGetPathString() {
    TreePath nodePath = new TreePath(0, 1, 2);
    Assert.assertEquals("_0_1_2", nodePath.getPathString());
    TreePath nodePath2 = new TreePath(nodePath, 3);
    Assert.assertEquals("_0_1_2_3", nodePath2.getPathString());
  }

  @Test
  public void testGetNode() {

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
    DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("node1");
    root.add(node1);
    DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("node2");
    DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("node3");
    node2.add(node3);
    root.add(node2);

    Assert.assertEquals(root, new TreePath(0).getNode(root));
    Assert.assertEquals(node1, new TreePath(0, 0).getNode(root));
    Assert.assertEquals(node2, new TreePath(0, 1).getNode(root));
    Assert.assertEquals(node3, new TreePath(0, 1, 0).getNode(root));
  }

  @Test
  public void testGetNode2() {

    DefaultMutableTreeNode tree = new DefaultMutableTreeNode("Category");
    tree.add(new DefaultMutableTreeNode("Sports"));
    tree.add(new DefaultMutableTreeNode("Movies"));
    DefaultMutableTreeNode sience = new DefaultMutableTreeNode("Science");
    tree.add(sience);
    sience.add(new DefaultMutableTreeNode("Geography"));
    sience.add(new DefaultMutableTreeNode("Mathematics"));
    DefaultMutableTreeNode astronomy = new DefaultMutableTreeNode("Astronomy");
    astronomy.add(new DefaultMutableTreeNode("Education"));
    astronomy.add(new DefaultMutableTreeNode("Pictures"));
    sience.add(astronomy);
    tree.add(new DefaultMutableTreeNode("Music"));
    tree.add(new DefaultMutableTreeNode("Games"));

    Assert.assertEquals("Category", new TreePath(0).getNode(tree).getUserObject());
    Assert.assertEquals("Sports", new TreePath(0, 0).getNode(tree).getUserObject());
    Assert.assertEquals("Astronomy", new TreePath(0, 2, 2).getNode(tree).getUserObject());
    Assert.assertEquals("Games", new TreePath(0, 4).getNode(tree).getUserObject());
  }
}
