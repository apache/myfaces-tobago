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

public class ExpandedStateUnitTest {

  private static final DefaultMutableTreeNode ROOT = new DefaultMutableTreeNode("root");
  private static final DefaultMutableTreeNode A = new DefaultMutableTreeNode("a");
  private static final DefaultMutableTreeNode B = new DefaultMutableTreeNode("b");
  private static final DefaultMutableTreeNode C = new DefaultMutableTreeNode("c");
  private static final DefaultMutableTreeNode A1 = new DefaultMutableTreeNode("a1");
  private static final DefaultMutableTreeNode A2 = new DefaultMutableTreeNode("a2");
  private static final DefaultMutableTreeNode A3 = new DefaultMutableTreeNode("a3");
  private static final DefaultMutableTreeNode B1 = new DefaultMutableTreeNode("b1");
  private static final DefaultMutableTreeNode B2 = new DefaultMutableTreeNode("b2");
  private static final DefaultMutableTreeNode B3 = new DefaultMutableTreeNode("b3");
  private static final DefaultMutableTreeNode C1 = new DefaultMutableTreeNode("c1");
  private static final DefaultMutableTreeNode C2 = new DefaultMutableTreeNode("c2");
  private static final DefaultMutableTreeNode C3 = new DefaultMutableTreeNode("c3");

  @BeforeClass
  public static void setup() {
    ROOT.add(A);
    ROOT.add(B);
    ROOT.add(C);
    A.add(A1);
    A.add(A2);
    A.add(A3);
    B.add(B1);
    B.add(B2);
    B.add(B3);
    C.add(C1);
    C.add(C2);
    C.add(C3);
  }

  /**
   * <pre>
   *  + root
   *  |
   *  +-+ a
   *  | |
   *  | +-+ a1
   *  | |
   *  | +-+ a2
   *  | |
   *  | +-+ a3
   *  |
   *  +-+ b
   *  | |
   *  | +-+ b1
   *  | |
   *  | +-+ b2
   *  | |
   *  | +-+ b3
   *  |
   *  +-+ c
   *    |
   *    +-+ c1
   *    |
   *    +-+ c2
   *    |
   *    +-+ c3
   *
   * </pre>
   */
  @Test
  public void test() {

    final ExpandedState state = new ExpandedState(2);

    final TreePath root = new TreePath(ROOT);
    final TreePath a = new TreePath(A);
    final TreePath b = new TreePath(B);
    final TreePath c = new TreePath(C);
    final TreePath a1 = new TreePath(A1);
    final TreePath a2 = new TreePath(A2);
    final TreePath a3 = new TreePath(A3);
    final TreePath b1 = new TreePath(B1);
    final TreePath b2 = new TreePath(B2);
    final TreePath b3 = new TreePath(B3);
    final TreePath c1 = new TreePath(C1);
    final TreePath c2 = new TreePath(C2);
    final TreePath c3 = new TreePath(C3);

    Assert.assertTrue(state.isExpanded(ROOT));

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertTrue(state.isExpanded(b));
    Assert.assertTrue(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));

    state.expand(a);
    state.expand(a1);
    state.expand(a2);
    state.expand(c);

    state.collapse(root);
    state.collapse(b);
    state.collapse(b3);
    state.collapse(a2);
    state.collapse(c);

    Assert.assertFalse(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertTrue(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(1, state.getExpandedSet().size());
    Assert.assertEquals(3, state.getCollapsedSet().size());

    // expand(1) should only expand the root node
    state.expand(1);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertTrue(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(1, state.getExpandedSet().size());
    Assert.assertEquals(2, state.getCollapsedSet().size());

    // collapse(3) should collapse a1 only
    state.collapse(3);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(2, state.getCollapsedSet().size());

    // expand(2) should expand b and c
    state.expand(2);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertTrue(state.isExpanded(b));
    Assert.assertTrue(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    // collapse(2) should expand b and c
    state.collapse(2);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertFalse(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    // expand(2) should expand b and c
    state.expand(a);
    state.expand(b);
    state.expand(2);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertTrue(state.isExpanded(b));
    Assert.assertTrue(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    // collapse(2) should expand b and c
    state.collapse(a);
    state.collapse(b);
    state.collapse(2);

    Assert.assertTrue(state.isExpanded(root));
    Assert.assertFalse(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    state.expand(2);
    state.expand(b); // do something noise
    state.expand(c1); // do something noise
    state.collapseAll();
    Assert.assertFalse(state.isExpanded(root));
    Assert.assertFalse(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    state.expand(2);
    state.expand(b); // do something noise
    state.expand(c1); // do something noise
    state.expandAll();
    Assert.assertTrue(state.isExpanded(root));
    Assert.assertTrue(state.isExpanded(a));
    Assert.assertTrue(state.isExpanded(b));
    Assert.assertTrue(state.isExpanded(c));
    Assert.assertTrue(state.isExpanded(a1));
    Assert.assertTrue(state.isExpanded(a2));
    Assert.assertTrue(state.isExpanded(a3));
    Assert.assertTrue(state.isExpanded(b1));
    Assert.assertTrue(state.isExpanded(b2));
    Assert.assertTrue(state.isExpanded(b3));
    Assert.assertTrue(state.isExpanded(c1));
    Assert.assertTrue(state.isExpanded(c2));
    Assert.assertTrue(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    state.expand(2);
    state.expand(b); // do something noise
    state.expand(c1); // do something noise
    state.collapseAllButRoot();
    Assert.assertTrue(state.isExpanded(root));
    Assert.assertFalse(state.isExpanded(a));
    Assert.assertFalse(state.isExpanded(b));
    Assert.assertFalse(state.isExpanded(c));
    Assert.assertFalse(state.isExpanded(a1));
    Assert.assertFalse(state.isExpanded(a2));
    Assert.assertFalse(state.isExpanded(a3));
    Assert.assertFalse(state.isExpanded(b1));
    Assert.assertFalse(state.isExpanded(b2));
    Assert.assertFalse(state.isExpanded(b3));
    Assert.assertFalse(state.isExpanded(c1));
    Assert.assertFalse(state.isExpanded(c2));
    Assert.assertFalse(state.isExpanded(c3));
    Assert.assertEquals(0, state.getExpandedSet().size());
    Assert.assertEquals(0, state.getCollapsedSet().size());

    // text TreeNode

    state.expand(A);
    Assert.assertTrue(state.isExpanded(A));
    state.collapse(A);
    Assert.assertFalse(state.isExpanded(A));


  }
}
