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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.layout.Measure;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Test if every in Java declared CSS class has really an entry in a CSS file.
 */
public class BootstrapClassUnitTest {

  @Test
  public void testNames() throws NoSuchFieldException {

    final String fieldRegex = "[A-Z][A-Z0-9_]*[A-Z0-9]";
    final String nameRegex = "[a-z][a-z0-9\\-]*[a-z0-9]";

    for (final BootstrapClass value : BootstrapClass.values()) {
      final boolean ignoreByTest = BootstrapClass.class.getField(value.name()).isAnnotationPresent(Deprecated.class);
      if (!ignoreByTest) {
        final String field = value.toString();
        final String name = value.getName();

        Assertions.assertTrue(field.matches(fieldRegex),
            "testing: '" + field + "' matches regexp for consts like FOO_BAR");
        Assertions.assertTrue(name.matches(nameRegex), "testing: '" + name + "' matches regexp for CSS like foo-bar");

        final StringBuilder calculatedName = new StringBuilder();
        for (int i = 0; i < field.length(); i++) {
          final char c = field.charAt(i);
          if (c == '_') {
            calculatedName.append("-");
          } else {
            calculatedName.append(Character.toLowerCase(c));
          }
        }

        Assertions.assertEquals(calculatedName.toString(), name, field);
      }
    }
  }

  /**
   * This test checks, if every item of the {@link BootstrapClass} occurs in the tobago.css.
   */
  @Test
  public void testCompareBootstrapCss() throws IOException, NoSuchFieldException {

    final BootstrapClass[] allValues = BootstrapClass.values();
    final List<BootstrapClass> toCheck = new ArrayList<>();
    for (final BootstrapClass value : allValues) {
      final boolean ignoreByTest = BootstrapClass.class.getField(value.name()).isAnnotationPresent(Deprecated.class);
      if (!ignoreByTest) {
        toCheck.add(value);
      }
    }

    final List<CssItem> missing = CssClassUtils.compareCss(
        "src/main/css/tobago.css",
        toCheck.toArray(new BootstrapClass[0]));

    Assertions.assertTrue(missing.isEmpty(), "These classes are missing in tobago.css: " + missing);
  }

  @Test
  public void testValueOfMeasureAttributes() {
    Assertions.assertEquals(BootstrapClass.COL_1,
        BootstrapClass.valueOf(new Measure("1", Measure.Unit.SEG), Attributes.extraSmall));
    Assertions.assertEquals(BootstrapClass.COL_12,
        BootstrapClass.valueOf(new Measure("12", Measure.Unit.SEG), Attributes.extraSmall));
    Assertions.assertEquals(null,
        BootstrapClass.valueOf((Measure) null, Attributes.extraSmall));
    Assertions.assertEquals(BootstrapClass.COL_MD_5,
        BootstrapClass.valueOf(new Measure("5", Measure.Unit.SEG), Attributes.medium));
    Assertions.assertEquals(BootstrapClass.COL_LG,
        BootstrapClass.valueOf(Measure.valueOf("*"), Attributes.large));
    Assertions.assertEquals(BootstrapClass.COL_XL_AUTO,
        BootstrapClass.valueOf(Measure.valueOf("auto"), Attributes.extraLarge));
    Assertions.assertEquals(BootstrapClass.COL_XXL_5,
        BootstrapClass.valueOf(new Measure("5", Measure.Unit.SEG), Attributes.extra2Large));
    Assertions.assertEquals(BootstrapClass.COL_XXL_AUTO,
        BootstrapClass.valueOf(Measure.valueOf("auto"), Attributes.extra2Large));
  }
}
