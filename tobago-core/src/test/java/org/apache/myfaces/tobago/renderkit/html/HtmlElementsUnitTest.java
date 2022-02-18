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

package org.apache.myfaces.tobago.renderkit.html;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class HtmlElementsUnitTest {

  @Test
  public void testNames() throws IllegalAccessException {
    for (final Field field : HtmlElements.class.getFields()) {

      final HtmlElements element = (HtmlElements) field.get(null);
      final String value = element.getValue();
      Assert.assertEquals("Check to lower: '" + element + "'", value, element.name().toLowerCase());
      Assert.assertEquals("Check to upper: '" + element + "'", value.toUpperCase(), element.name());
    }
  }

  @Test
  public void testVoid() throws IllegalAccessException {

    // list from spec.
    final List<String> voids = Arrays.asList(
      "area", "base", "br", "col", "command", "embed",
      "hr", "img", "input", "keygen", "link", "meta",
      "param", "source", "track", "wbr");

    for (final Field field : HtmlElements.class.getFields()) {
      final HtmlElements element = (HtmlElements) field.get(null);

      Assert.assertEquals("Check void: '" + element + "'", voids.contains(element.getValue()), element.isVoid());
    }

  }
}
