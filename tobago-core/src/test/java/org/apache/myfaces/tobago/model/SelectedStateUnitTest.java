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
import org.junit.Test;

public class SelectedStateUnitTest {

  @Test
  public void testAncestorOfSelected() {
    SelectedState state = new SelectedState();
    state.select(new TreePath(0, 0));
    state.select(new TreePath(1, 1, 1));

    Assert.assertTrue(state.isAncestorOfSelected(new TreePath()));
    Assert.assertTrue(state.isAncestorOfSelected(new TreePath(0)));
    Assert.assertTrue(state.isAncestorOfSelected(new TreePath(0, 0)));
    Assert.assertTrue(state.isAncestorOfSelected(new TreePath(1)));
    Assert.assertTrue(state.isAncestorOfSelected(new TreePath(1, 1)));
    Assert.assertTrue(state.isAncestorOfSelected(new TreePath(1, 1, 1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(2)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(0, 1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(1, 0)));
  }

  @Test
  public void testAncestorOfSelectedEmpty() {
    SelectedState state = new SelectedState();

    Assert.assertFalse(state.isAncestorOfSelected(new TreePath()));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(0)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(0, 0)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(1, 1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(1, 1, 1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(2)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(0, 1)));
    Assert.assertFalse(state.isAncestorOfSelected(new TreePath(1, 0)));
  }

  @Test
  public void testSelected() {
    SelectedState state = new SelectedState();
    state.select(new TreePath(0, 0));
    state.select(new TreePath(1, 1, 1));

    Assert.assertFalse(state.isSelected(new TreePath()));
    Assert.assertFalse(state.isSelected(new TreePath(0)));
    Assert.assertTrue(state.isSelected(new TreePath(0, 0)));
    Assert.assertFalse(state.isSelected(new TreePath(1)));
    Assert.assertFalse(state.isSelected(new TreePath(1, 1)));
    Assert.assertTrue(state.isSelected(new TreePath(1, 1, 1)));
    Assert.assertFalse(state.isSelected(new TreePath(2)));
    Assert.assertFalse(state.isSelected(new TreePath(0, 1)));
    Assert.assertFalse(state.isSelected(new TreePath(1, 0)));
  }

}
