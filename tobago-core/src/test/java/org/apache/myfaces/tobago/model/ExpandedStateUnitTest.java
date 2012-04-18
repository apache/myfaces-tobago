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

import org.junit.Assert;
import org.junit.Test;

import javax.swing.tree.DefaultMutableTreeNode;

public class ExpandedStateUnitTest {

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

    DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

    DefaultMutableTreeNode a = new DefaultMutableTreeNode("a");
    DefaultMutableTreeNode b = new DefaultMutableTreeNode("b");
    DefaultMutableTreeNode c = new DefaultMutableTreeNode("c");
    root.add(a);
    root.add(b);
    root.add(c);

    DefaultMutableTreeNode a1 = new DefaultMutableTreeNode("a1");
    DefaultMutableTreeNode a2 = new DefaultMutableTreeNode("a2");
    DefaultMutableTreeNode a3 = new DefaultMutableTreeNode("a3");
    DefaultMutableTreeNode b1 = new DefaultMutableTreeNode("b1");
    DefaultMutableTreeNode b2 = new DefaultMutableTreeNode("b2");
    DefaultMutableTreeNode b3 = new DefaultMutableTreeNode("b3");
    DefaultMutableTreeNode c1 = new DefaultMutableTreeNode("c1");
    DefaultMutableTreeNode c2 = new DefaultMutableTreeNode("c2");
    DefaultMutableTreeNode c3 = new DefaultMutableTreeNode("c3");

    a.add(a1);
    a.add(a2);
    a.add(a3);
    b.add(b1);
    b.add(b2);
    b.add(b3);
    c.add(c1);
    c.add(c2);
    c.add(c3);

    ExpandedState state = new ExpandedState(2);

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

  }
}
