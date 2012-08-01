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
import org.junit.Before;
import org.junit.Test;

public class NodeUnitTest {

  private Node r;
  private Node a;
  private Node b;
  private Node c;
  private Node x;
  private Node y;
  private Node pi;

  /**
   * * --o r (0)
   * *   |
   * *   +--o a (0,0)
   * *   |
   * *   +--o b (0,1)
   * *   |   |
   * *   |   +--o x (0,1,0)
   * *   |   |
   * *   |   +--o y (0,1,1)
   * *   |      |
   * *   |      +--o π (0,1,1,0)
   * *   |
   * *   +--o c (0,1,1,0)
   */
  @Before
  public void setUp() {

    r = new Node();
    a = new Node();
    b = new Node();
    c = new Node();
    x = new Node();
    y = new Node();
    pi = new Node();

    r.add(a);
    r.add(b);
    r.add(c);

    b.add(x);
    b.add(y);

    y.add(pi);

  }

  @Test
  public void testIsRoot() {
    Assert.assertTrue(r.isRoot());
    Assert.assertFalse(a.isRoot());
    Assert.assertFalse(b.isRoot());
    Assert.assertFalse(c.isRoot());
    Assert.assertFalse(x.isRoot());
    Assert.assertFalse(y.isRoot());
    Assert.assertFalse(pi.isRoot());
  }

  @Test
  public void testGetChildCount() {
    Assert.assertEquals(3, r.getChildCount());
    Assert.assertEquals(0, a.getChildCount());
    Assert.assertEquals(2, b.getChildCount());
    Assert.assertEquals(0, c.getChildCount());
    Assert.assertEquals(0, x.getChildCount());
    Assert.assertEquals(1, y.getChildCount());
    Assert.assertEquals(0, pi.getChildCount());
  }

  @Test
  public void testHasNextSibling() {
    Assert.assertFalse(r.hasNextSibling());
    Assert.assertTrue(a.hasNextSibling());
    Assert.assertTrue(b.hasNextSibling());
    Assert.assertFalse(c.hasNextSibling());
    Assert.assertTrue(x.hasNextSibling());
    Assert.assertFalse(y.hasNextSibling());
    Assert.assertFalse(pi.hasNextSibling());
  }

}
