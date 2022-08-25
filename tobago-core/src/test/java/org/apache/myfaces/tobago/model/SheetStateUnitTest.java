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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SheetStateUnitTest {

  @Test
  public void testState() {
    final SheetState state = new SheetState(3);
    final SortedColumnList list = state.getSortedColumnList();

    Assertions.assertNull(list.getFirst());
    Assertions.assertEquals(0, list.size());
    Assertions.assertEquals(0, list.getToBeSortedLevel());

    state.updateSortState("a");

    Assertions.assertEquals("a", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(1, list.getToBeSortedLevel());

    state.updateSortState("a");

    Assertions.assertEquals("a", list.getFirst().getId());
    Assertions.assertFalse(list.getFirst().isAscending());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(1, list.getToBeSortedLevel());

    state.updateSortState("a");

    Assertions.assertEquals("a", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(1, list.size());
    Assertions.assertEquals(1, list.getToBeSortedLevel());

    state.updateSortState("b");

    Assertions.assertEquals("b", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(2, list.getToBeSortedLevel());

    state.sorted();

    Assertions.assertEquals("b", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(0, list.getToBeSortedLevel());

    state.updateSortState("b");

    Assertions.assertEquals("b", list.getFirst().getId());
    Assertions.assertFalse(list.getFirst().isAscending());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, list.getToBeSortedLevel());

    state.updateSortState("b");

    Assertions.assertEquals("b", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(2, list.size());
    Assertions.assertEquals(1, list.getToBeSortedLevel());

    state.updateSortState("c");

    Assertions.assertEquals("c", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(2, list.getToBeSortedLevel());

    state.updateSortState("d");

    Assertions.assertEquals("d", list.getFirst().getId());
    Assertions.assertTrue(list.getFirst().isAscending());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(3, list.getToBeSortedLevel());

    state.updateSortState("d");

    Assertions.assertEquals("d", list.getFirst().getId());
    Assertions.assertFalse(list.getFirst().isAscending());
    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(3, list.getToBeSortedLevel());

    Assertions.assertEquals(-1, list.indexOf("a"));
    Assertions.assertEquals(2, list.indexOf("b"));
    Assertions.assertEquals(1, list.indexOf("c"));
    Assertions.assertEquals(0, list.indexOf("d"));

  }

  @Test
  public void testBug() {
    final SheetState state = new SheetState(3);
    final SortedColumnList list = state.getSortedColumnList();

    state.updateSortState("a");

    state.updateSortState("b");

    state.updateSortState("c");

    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(3, list.getToBeSortedLevel());

    state.updateSortState("b");

    Assertions.assertEquals(3, list.size());
    Assertions.assertEquals(3, list.getToBeSortedLevel());
  }
}
