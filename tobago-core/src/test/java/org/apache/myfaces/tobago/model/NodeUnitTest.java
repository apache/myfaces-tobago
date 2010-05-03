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

import junit.framework.TestCase;

public class NodeUnitTest extends TestCase {

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

  public void testIsRoot() {
    assertTrue(r.isRoot());
    assertFalse(a.isRoot());
    assertFalse(b.isRoot());
    assertFalse(c.isRoot());
    assertFalse(x.isRoot());
    assertFalse(y.isRoot());
    assertFalse(pi.isRoot());
  }

  public void testGetChildCount() {
    assertEquals(3, r.getChildCount());
    assertEquals(0, a.getChildCount());
    assertEquals(2, b.getChildCount());
    assertEquals(0, c.getChildCount());
    assertEquals(0, x.getChildCount());
    assertEquals(1, y.getChildCount());
    assertEquals(0, pi.getChildCount());
  }

  public void testHasNextSibling() {
    assertFalse(r.hasNextSibling());
    assertTrue(a.hasNextSibling());
    assertTrue(b.hasNextSibling());
    assertFalse(c.hasNextSibling());
    assertTrue(x.hasNextSibling());
    assertFalse(y.hasNextSibling());
    assertFalse(pi.hasNextSibling());
  }

}
