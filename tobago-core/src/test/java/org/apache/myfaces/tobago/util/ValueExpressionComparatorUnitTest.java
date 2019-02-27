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

import org.apache.myfaces.test.el.MockValueExpression;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.internal.util.Fruit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ValueExpressionComparatorUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testComparingInstancesOfDifferentClasses() {

    final List<Fruit> original = Fruit.getFreshFruits();

    final ValueExpressionComparator ascendingComparator = new ValueExpressionComparator(
        facesContext, "var", new MockValueExpression("#{var.name}", String.class), false, null);
    final List<Fruit> ascending = new ArrayList<>(original);
    ascending.sort(ascendingComparator);

    Assertions.assertEquals(original.get(0), ascending.get(0), "#0");
    Assertions.assertEquals(original.get(3), ascending.get(1), "#1");
    Assertions.assertEquals(original.get(1), ascending.get(2), "#2");
    Assertions.assertEquals(original.get(2), ascending.get(3), "#3");

    final ValueExpressionComparator descendingComparator = new ValueExpressionComparator(
        facesContext, "var", new MockValueExpression("#{var.name}", String.class), true, null);

    final List<Fruit> descending = new ArrayList<>(original);
    descending.sort(descendingComparator);

    Assertions.assertEquals(original.get(2), descending.get(0), "#0");
    Assertions.assertEquals(original.get(1), descending.get(1), "#1");
    Assertions.assertEquals(original.get(3), descending.get(2), "#2");
    Assertions.assertEquals(original.get(0), descending.get(3), "#3");
  }
}
