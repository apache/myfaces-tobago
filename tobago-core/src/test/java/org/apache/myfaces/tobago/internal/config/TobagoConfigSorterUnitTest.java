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

package org.apache.myfaces.tobago.internal.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class TobagoConfigSorterUnitTest {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Test
  public void testCompare() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    final TobagoConfigFragment c = new TobagoConfigFragment();
    c.setName("c");

    final TobagoConfigFragment d = new TobagoConfigFragment();
    d.setName("d");

    final TobagoConfigFragment e = new TobagoConfigFragment();
    e.setName("e");

    final TobagoConfigFragment f = new TobagoConfigFragment();
    f.setName("f");

    final TobagoConfigFragment m = new TobagoConfigFragment();
    m.setName("m");

    final TobagoConfigFragment n = new TobagoConfigFragment();
    n.setName("n");

    // unnamed
    final TobagoConfigFragment u1 = new TobagoConfigFragment();
    final TobagoConfigFragment u2 = new TobagoConfigFragment();
    final TobagoConfigFragment u3 = new TobagoConfigFragment();

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

    final List<TobagoConfigFragment> list = new ArrayList<>();
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

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    final List<TobagoConfigFragment> result = sorter.topologicalSort();

    Assertions.assertEquals(a, result.get(0));
    Assertions.assertEquals(b, result.get(1));
    Assertions.assertEquals(c, result.get(2));
    Assertions.assertEquals(u1, result.get(3));
    Assertions.assertEquals(u2, result.get(4));
    Assertions.assertEquals(d, result.get(5));
    Assertions.assertEquals(e, result.get(6));
    Assertions.assertEquals(f, result.get(7));
    Assertions.assertEquals(u3, result.get(8));
    Assertions.assertEquals(m, result.get(9));
    Assertions.assertEquals(n, result.get(10));
  }

  @Test
  public void test0() {

    // config + names

    final List<TobagoConfigFragment> list = new ArrayList<>();

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    final List<TobagoConfigFragment> result = sorter.topologicalSort();

    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  public void testCycle1Before() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    a.getBefore().add("a");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    try {
      sorter.topologicalSort();

      Assertions.fail("Cycle was not detected!");
    } catch (final RuntimeException e) {
      LOG.info("Success: Cycle found: {}", e.getMessage());
    }
  }

  @Test
  public void testCycle1After() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    a.getAfter().add("a");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    try {
      sorter.topologicalSort();

      Assertions.fail("Cycle was not detected!");
    } catch (final RuntimeException e) {
      LOG.info("Success: Cycle found: {}", e.getMessage());
    }
  }

  @Test
  public void testCycle2() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    b.getBefore().add("a");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    try {
      sorter.topologicalSort();

      Assertions.fail("Cycle was not detected!");
    } catch (final RuntimeException e) {
      LOG.info("Success: Cycle found: {}", e.getMessage());
    }
  }

  @Test
  public void testCycle2BeforeAfter() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    // before
    a.getBefore().add("b");
    // after
    a.getAfter().add("b");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);
    try {
      sorter.topologicalSort();

      Assertions.fail("Cycle was not detected!");
    } catch (final RuntimeException e) {
      LOG.info("Success: Cycle found: {}", e.getMessage());
    }
  }

  @Test
  public void testCycle3() {

    // config + names

    final TobagoConfigFragment a = new TobagoConfigFragment();
    a.setName("a");

    final TobagoConfigFragment b = new TobagoConfigFragment();
    b.setName("b");

    final TobagoConfigFragment c = new TobagoConfigFragment();
    c.setName("c");

    // before
    a.getBefore().add("b");
    b.getBefore().add("c");
    a.getAfter().add("c");

    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(a);
    list.add(b);
    list.add(c);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    try {
      sorter.topologicalSort();

      Assertions.fail("Cycle was not detected!");
    } catch (final RuntimeException e) {
      LOG.info("Success: Cycle found: {}", e.getMessage());
    }
  }

  @Test
  public void testReal() {

    // config + names

    final TobagoConfigFragment blank = new TobagoConfigFragment();
    blank.setName("tobago-example-blank");

    final TobagoConfigFragment charlotteville = new TobagoConfigFragment();
    charlotteville.setName("tobago-theme-charlotteville");

    final TobagoConfigFragment roxborough = new TobagoConfigFragment();
    roxborough.setName("tobago-theme-roxborough");

    final TobagoConfigFragment scarborough = new TobagoConfigFragment();
    scarborough.setName("tobago-theme-scarborough");

    final TobagoConfigFragment speyside = new TobagoConfigFragment();
    speyside.setName("tobago-theme-speyside");

    final TobagoConfigFragment standard = new TobagoConfigFragment();
    standard.setName("tobago-theme-standard");

    final TobagoConfigFragment core = new TobagoConfigFragment();
    core.setName("tobago-core");

    // after
    blank.getAfter().add(speyside.getName());
    blank.getAfter().add(scarborough.getName());
    blank.getAfter().add(roxborough.getName());
    blank.getAfter().add(standard.getName());
    blank.getAfter().add(charlotteville.getName());
    charlotteville.getAfter().add(standard.getName());
    roxborough.getAfter().add(standard.getName());
    scarborough.getAfter().add(standard.getName());
    speyside.getAfter().add(standard.getName());
    standard.getAfter().add(core.getName());


    final List<TobagoConfigFragment> list = new ArrayList<>();
    list.add(blank);
    list.add(charlotteville);
    list.add(roxborough);
    list.add(scarborough);
    list.add(speyside);
    list.add(standard);
    list.add(core);

    final TobagoConfigSorter sorter = new TobagoConfigSorter(list);

    final List<TobagoConfigFragment> result = sorter.topologicalSort();

    Assertions.assertEquals(core, result.get(0));
    Assertions.assertEquals(standard, result.get(1));
    Assertions.assertEquals(blank, result.get(6));
    final int blankPos = result.indexOf(blank);
    final int speysidePos = result.indexOf(speyside);
    Assertions.assertTrue(blankPos > speysidePos);
  }
}
