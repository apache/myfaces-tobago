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

package org.apache.myfaces.tobago.context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ThemeResourcesUnitTest {

  @Test
  public void basic() {
    final ThemeResources resources = new ThemeResources(false);
    // empty
    // empty
    Assertions.assertEquals(0, resources.getScriptList().size());
    Assertions.assertEquals(0, resources.getStyleList().size());

    final ThemeScript a = new ThemeScript();
    a.setName("a");
    // a
    // empty
    resources.addIncludeScript(a);
    Assertions.assertEquals(1, resources.getScriptList().size());
    Assertions.assertEquals(0, resources.getStyleList().size());
    Assertions.assertEquals("a", resources.getScriptList().get(0).getName());

    final ThemeScript b = new ThemeScript();
    b.setName("b");
    resources.addIncludeScript(b);
    // a b
    // empty
    Assertions.assertEquals(2, resources.getScriptList().size());
    Assertions.assertEquals(0, resources.getStyleList().size());
    Assertions.assertEquals("a", resources.getScriptList().get(0).getName());
    Assertions.assertEquals("b", resources.getScriptList().get(1).getName());

    final ThemeStyle c = new ThemeStyle();
    c.setName("c");
    resources.addIncludeStyle(c);
    // a b
    // c
    Assertions.assertEquals(2, resources.getScriptList().size());
    Assertions.assertEquals(1, resources.getStyleList().size());
    Assertions.assertEquals("a", resources.getScriptList().get(0).getName());
    Assertions.assertEquals("b", resources.getScriptList().get(1).getName());
    Assertions.assertEquals("c", resources.getStyleList().get(0).getName());

    // merging exclude
    final ThemeResources resources2 = new ThemeResources(false);
    final ThemeScript aEx = new ThemeScript();
    aEx.setName("a");
    resources2.addExcludeScript(aEx);
    final ThemeStyle d = new ThemeStyle();
    d.setName("d");
    resources2.addIncludeStyle(d);

    final ThemeResources merge = ThemeResources.merge(resources, resources2);
    // a b  merge with -a       ->   b
    // c    merge with d        ->   c d
    Assertions.assertEquals(1, merge.getScriptList().size());
    Assertions.assertEquals(2, merge.getStyleList().size());
    Assertions.assertEquals("b", merge.getScriptList().get(0).getName());
    Assertions.assertEquals("c", merge.getStyleList().get(0).getName());
    Assertions.assertEquals("d", merge.getStyleList().get(1).getName());
  }

  @Test
  public void prodVsDev() {
    final ThemeResources dev = new ThemeResources(false);
    Assertions.assertFalse(dev.isProduction());

    final ThemeResources prod = new ThemeResources(true);
    Assertions.assertTrue(prod.isProduction());
  }
}
