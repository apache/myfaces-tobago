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

package org.apache.myfaces.tobago.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated since 4.4.0
 */
@Deprecated
public class BeanComparatorUnitTest {

  @Test
  public void testComparingInstancesOfDifferentClasses() {
    final List<Fruit> original = new ArrayList<>();
    original.add(new Apple("Golden Delicious"));
    original.add(new Apple("Schöner aus Boskoop"));
    original.add(new Pear("Williams Christ"));
    original.add(new Pear("Köstliche aus Charneux"));

    final BeanComparator ascendingComparator = new BeanComparator("name", null, false);
    final List<Fruit> ascending = new ArrayList<>(original);
    ascending.sort(ascendingComparator);

    Assert.assertEquals("#0", original.get(0), ascending.get(0));
    Assert.assertEquals("#1", original.get(3), ascending.get(1));
    Assert.assertEquals("#2", original.get(1), ascending.get(2));
    Assert.assertEquals("#3", original.get(2), ascending.get(3));

    final BeanComparator descendingComparator = new BeanComparator("name", null, true);
    final List<Fruit> descending = new ArrayList<>(original);
    descending.sort(descendingComparator);

    Assert.assertEquals("#0", original.get(2), descending.get(0));
    Assert.assertEquals("#1", original.get(1), descending.get(1));
    Assert.assertEquals("#2", original.get(3), descending.get(2));
    Assert.assertEquals("#3", original.get(0), descending.get(3));
  }

  public interface Fruit {

    String getName();

    void setName(String name);
  }

  public static class Apple implements Fruit {

    private String name;

    public Apple(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return getName();
    }
  }

  public static class Pear implements Fruit {

    private String name;

    public Pear(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return getName();
    }
  }
}
