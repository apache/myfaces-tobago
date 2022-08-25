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

package org.apache.myfaces.tobago.renderkit.css;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TobagoClassUnitTest {

  @Test
  public void testNames() throws NoSuchFieldException {

    final String fieldRegex = "[A-Z][A-Z_0-9]*";
    final String nameRegex = "[a-z][a-zA-Z\\-]*[a-z0-9]";

    for (final TobagoClass value : TobagoClass.values()) {
      final boolean ignoreByTest = TobagoClass.class.getField(value.name()).isAnnotationPresent(Deprecated.class);
      if (!ignoreByTest) {
        final String field = value.toString();
        final String name = value.getName();

        Assertions.assertTrue(field.matches(fieldRegex));
        Assertions.assertTrue(name.matches(nameRegex));

        final StringBuilder calculatedName = new StringBuilder();
        calculatedName.append("tobago-");
        for (int i = 0; i < field.length(); i++) {
          final char c = field.charAt(i);
          if (c == '_') {
            final char nextChar = field.charAt(i + 1);
            if (nextChar == '_') {
              calculatedName.append("-");
            } else {
              calculatedName.append(nextChar);
            }
            i++;
          } else {
            calculatedName.append(Character.toLowerCase(c));
          }
        }

        Assertions.assertEquals(calculatedName.toString(), name, field);
      }
    }
  }

  /**
   * This test checks whether every item of the {@link TobagoClass} occurs in the _tobago.scss.
   */
  @Test
  public void testCompareTobagoCss() throws IOException, NoSuchFieldException {

    final TobagoClass[] allValues = TobagoClass.values();
    final List<TobagoClass> toCheck = new ArrayList<>();
    for (final TobagoClass value : allValues) {
      final boolean ignoreByTest = TobagoClass.class.getField(value.name()).isAnnotationPresent(Deprecated.class);
      if (!ignoreByTest) {
        toCheck.add(value);
      }
    }

    final List<CssItem> missing =
        CssClassUtils.compareCss("../tobago-theme/src/main/scss/_tobago.scss",
            toCheck.toArray(new CssItem[0]));

    Assertions.assertTrue(missing.isEmpty(), "These classes are missing in _tobago.scss: " + missing);
  }

}
