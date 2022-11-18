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

import org.apache.myfaces.tobago.renderkit.css.FileTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlElementsUnitTest {

  @Test
  public void testNames() throws IllegalAccessException {
    for (final Field field : HtmlElements.class.getFields()) {

      final HtmlElements element = (HtmlElements) field.get(null);
      final String value = element.getValue();
      Assertions.assertEquals(
          value,
          element.name().toLowerCase().replaceAll("_", "-"),
          "Check to lower: '" + element + "'");
      Assertions.assertEquals(
          value.toUpperCase().replaceAll("-", "_"),
          element.name(),
          "Check to upper: '" + element + "'");
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

      Assertions.assertEquals(voids.contains(element.getValue()), element.isVoid(), "Check void: '" + element + "'");
    }

  }

  /**
   * This test checks whether every tobago custom element {@link HtmlElements} occurs in the _tobago.scss.
   */
  @Test
  public void testCompareTobagoCustomElement() throws IOException, IllegalAccessException {

    final String fileContent = FileTestUtils.fileToString("../tobago-theme/src/main/scss/_tobago.scss");

    final List<HtmlElements> missing = new ArrayList<>();
    for (final Field field : HtmlElements.class.getFields()) {
      final HtmlElements element = (HtmlElements) field.get(null);
      final String tagName = element.getValue();

      if (tagName.startsWith("tobago-") && !containsTagName(fileContent.toString(), tagName)) {
        missing.add(element);
      }
    }

    Assertions.assertTrue(missing.isEmpty(), "These custom elements are missing in _tobago.scss: " + missing);
  }

  private boolean containsTagName(final String content, final String tagName) {
    return content.contains(tagName + " ")
        || content.contains(tagName + "{")
        || content.contains(tagName + ",")
        || content.contains(tagName + ":")
        || content.contains(tagName + ".")
        || content.contains(tagName + ">");
  }
}
