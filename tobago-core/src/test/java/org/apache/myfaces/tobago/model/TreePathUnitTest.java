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
import org.junit.BeforeClass;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Arrays;

/*
 * <pre>
 *  + Root               Path: []
 *  |
 *  +-+ Node             Path: [0]
 *  | |
 *  | +-+ Sub-Node       Path: [0, 0]
 *  | |
 *  | +-+ Sub-Node       Path: [0, 1]
 *  |
 *  +-+ Node             Path: [1]
 *    |
 *    +-+ Sub-Node       Path: [1, 0]
 *    |
 *    +-+ Sub-Node       Path: [1, 1]
 *    |
 *    +-+ Sub-Node       Path: [1, 2]
 * </pre>
 */
public class TreePathUnitTest {

  private static final DefaultMutableTreeNode ROOT = new DefaultMutableTreeNode("root");
  private static final DefaultMutableTreeNode A = new DefaultMutableTreeNode("a");
  private static final DefaultMutableTreeNode B = new DefaultMutableTreeNode("b");
  private static final DefaultMutableTreeNode A1 = new DefaultMutableTreeNode("a1");
  private static final DefaultMutableTreeNode A2 = new DefaultMutableTreeNode("a2");
  private static final DefaultMutableTreeNode B1 = new DefaultMutableTreeNode("b1");
  private static final DefaultMutableTreeNode B2 = new DefaultMutableTreeNode("b2");
  private static final DefaultMutableTreeNode B3 = new DefaultMutableTreeNode("b3");

  @BeforeClass
  public static void setup() {
    ROOT.add(A);
    ROOT.add(B);
    A.add(A1);
    A.add(A2);
    B.add(B1);
    B.add(B2);
    B.add(B3);
  }

  @Test
  public void test() {

    final TreePath root = new TreePath(ROOT);
    final TreePath a = new TreePath(A);
    final TreePath b = new TreePath(B);
    final TreePath a1 = new TreePath(A1);
    final TreePath a2 = new TreePath(A2);
    final TreePath b1 = new TreePath(B1);
    final TreePath b2 = new TreePath(B2);
    final TreePath b3 = new TreePath(B3);

    Assert.assertEquals(0, root.getPath().length);
    Assert.assertEquals(1, a.getPath().length);
    Assert.assertEquals(0, a.getPath()[0]);
    Assert.assertEquals(1, b.getPath().length);
    Assert.assertEquals(1, b.getPath()[0]);
    Assert.assertEquals(2, a1.getPath().length);
    Assert.assertEquals(0, a1.getPath()[0]);
    Assert.assertEquals(0, a1.getPath()[1]);
    Assert.assertEquals(2, a2.getPath().length);
    Assert.assertEquals(0, a2.getPath()[0]);
    Assert.assertEquals(1, a2.getPath()[1]);
    Assert.assertEquals(2, b1.getPath().length);
    Assert.assertEquals(1, b1.getPath()[0]);
    Assert.assertEquals(0, b1.getPath()[1]);
    Assert.assertEquals(2, b2.getPath().length);
    Assert.assertEquals(1, b2.getPath()[0]);
    Assert.assertEquals(1, b2.getPath()[1]);
    Assert.assertEquals(2, b3.getPath().length);
    Assert.assertEquals(1, b3.getPath()[0]);
    Assert.assertEquals(2, b3.getPath()[1]);
  }

  @Test
  public void testGetPath() {
    TreePath treePath = new TreePath(0, 1, 2);
    Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2}, treePath.getPath()));
    TreePath treePath2 = new TreePath(treePath, 3);
    Assert.assertTrue(Arrays.equals(new int[]{0, 1, 2, 3}, treePath2.getPath()));
  }

  @Test
  public void testGetPathString() {
    TreePath treePath = new TreePath(0, 1, 2);
    Assert.assertEquals("_0_1_2", treePath.getPathString());
    TreePath treePath2 = new TreePath(treePath, 3);
    Assert.assertEquals("_0_1_2_3", treePath2.getPathString());
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
    DefaultMutableTreeNode science = new DefaultMutableTreeNode("Science");
    tree.add(science);
    science.add(new DefaultMutableTreeNode("Geography"));
    science.add(new DefaultMutableTreeNode("Mathematics"));
    DefaultMutableTreeNode astronomy = new DefaultMutableTreeNode("Astronomy");
    astronomy.add(new DefaultMutableTreeNode("Education"));
    astronomy.add(new DefaultMutableTreeNode("Pictures"));
    science.add(astronomy);
    tree.add(new DefaultMutableTreeNode("Music"));
    tree.add(new DefaultMutableTreeNode("Games"));

    Assert.assertEquals("Category", new TreePath(0).getNode(tree).getUserObject());
    Assert.assertEquals("Sports", new TreePath(0, 0).getNode(tree).getUserObject());
    Assert.assertEquals("Astronomy", new TreePath(0, 2, 2).getNode(tree).getUserObject());
    Assert.assertEquals("Games", new TreePath(0, 4).getNode(tree).getUserObject());
  }
}
