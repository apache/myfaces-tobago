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

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class TobagoClassUnitTest {

  @Test
  public void testNames() throws NoSuchFieldException {

    final String fieldRegex = "[A-Z_]*[A-Z]";
    final String nameRegex = "[a-z][a-zA-Z\\-]*[a-z]";

    for (TobagoClass value : TobagoClass.values()) {
      boolean ignoreByTest = TobagoClass.class.getField(value.name()).isAnnotationPresent(Deprecated.class);
      if (!ignoreByTest) {
        final String field = value.toString();
        final String name = value.getName();

        Assert.assertTrue(field.matches(fieldRegex));
        Assert.assertTrue(name.matches(nameRegex));

        StringBuilder calculatedName = new StringBuilder();
        calculatedName.append("tobago-");
        for (int i = 0; i < field.length(); i++) {
          char c = field.charAt(i);
          if (c == '_') {
            char nextChar = field.charAt(i + 1);
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

        Assert.assertEquals(field, calculatedName.toString(), name);
      }
    }
  }

  /**
   * This test checks, if every item of the {@link TobagoClass} occurs in the _tobago.scss.
   */
  @Test
  public void testCompareTobagoCss() throws FileNotFoundException {

    CssClassUtils.compareCss("src/main/resources/scss/_tobago.scss", TobagoClass.values());
  }

}
