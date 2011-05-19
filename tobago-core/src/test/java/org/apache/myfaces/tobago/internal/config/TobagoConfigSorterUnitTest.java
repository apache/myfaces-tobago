package org.apache.myfaces.tobago.internal.config;

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

import java.util.ArrayList;
import java.util.List;

public class TobagoConfigSorterUnitTest {

  @Test
  public void testCompare() {

    // config + names

    TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    TobagoConfigFragment c = new TobagoConfigFragment();
    c.setName("c");

    TobagoConfigFragment d = new TobagoConfigFragment();
    d.setName("d");

    TobagoConfigFragment e = new TobagoConfigFragment();
    e.setName("e");

    TobagoConfigFragment f = new TobagoConfigFragment();
    f.setName("f");

    TobagoConfigFragment m = new TobagoConfigFragment();
    m.setName("m");

    TobagoConfigFragment n = new TobagoConfigFragment();
    n.setName("n");

    // unnamed
    TobagoConfigFragment u1 = new TobagoConfigFragment();
    TobagoConfigFragment u2 = new TobagoConfigFragment();
    TobagoConfigFragment u3 = new TobagoConfigFragment();

    // before
    a.getBefore().add("b");
    b.getBefore().add("c");

    u1.getBefore().add("d");
    u2.getBefore().add("d");

    u2.getBefore().add("y"); // not relevant

    // after
    e.getAfter().add("d");
    f.getAfter().add("e");

    u1.getAfter().add("c");
    u2.getAfter().add("c");

    u2.getAfter().add("z"); // not relevant

    n.getAfter().add("m");

    List<TobagoConfigFragment> list = new ArrayList<TobagoConfigFragment>();
    list.add(a);
    list.add(b);
    list.add(c);
    list.add(d);
    list.add(e);
    list.add(f);
    list.add(u1);
    list.add(u2);
    list.add(u3);
    list.add(m);
    list.add(n);

    TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assert.assertEquals(9, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    Assert.assertEquals(28, sorter.getPairs().size());

    sorter.ensureIrreflexive();

    sorter.ensureAntiSymmetric();

    sorter.sort0();

    Assert.assertEquals(a, list.get(0));
    Assert.assertEquals(b, list.get(1));
    Assert.assertEquals(c, list.get(2));
    Assert.assertEquals(u1, list.get(3));
    Assert.assertEquals(u2, list.get(4));
    Assert.assertEquals(d, list.get(5));
    Assert.assertEquals(e, list.get(6));
    Assert.assertEquals(f, list.get(7));
    Assert.assertEquals(u3, list.get(8));
    Assert.assertEquals(m, list.get(9));
    Assert.assertEquals(n, list.get(10));
  }

  @Test
  public void testCycle() {

    // config + names

    TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    b.getBefore().add("a");

    List<TobagoConfigFragment> list = new ArrayList<TobagoConfigFragment>();
    list.add(a);
    list.add(b);

    TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assert.assertEquals(2, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    try {
      sorter.ensureIrreflexive();
      sorter.ensureAntiSymmetric();

      Assert.fail("Cycle was not found");
    } catch (RuntimeException e) {
      // must find the cycle
    }
  }

  @Test
  public void testCycle2() {

    // config + names

    TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    // after
    a.getAfter().add("b");

    List<TobagoConfigFragment> list = new ArrayList<TobagoConfigFragment>();
    list.add(a);
    list.add(b);

    TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    sorter.createRelevantPairs();

    Assert.assertEquals(2, sorter.getPairs().size()); // all but these with "z" and "y"

    sorter.makeTransitive();

    try {
      sorter.ensureIrreflexive();
      sorter.ensureAntiSymmetric();

      Assert.fail("Cycle was not found");
    } catch (RuntimeException e) {
      // must find the cycle
    }
  }
}
