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

import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.Assert;
import org.junit.Test;

import javax.faces.component.UIComponent;

public class ComponentUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testSplitList() {
    Assert.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab cd"));
    Assert.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab  cd"));
    Assert.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab,  cd"));
    Assert.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab , cd"));
    Assert.assertArrayEquals(new String[]{"ab", "cd"}, ComponentUtils.splitList("ab,,cd"));
  }

  @Test
  public void testFindDescendant() {
    final UIComponent p = CreateComponentUtils.createComponent(ComponentTypes.PANEL, RendererTypes.PANEL, "p");
    final UIComponent i = CreateComponentUtils.createComponent(ComponentTypes.IN, RendererTypes.IN, "i");
    p.getChildren().add(i);

    final UIIn in = ComponentUtils.findDescendant(p, UIIn.class);
    Assert.assertEquals(i, in);
  }
}
