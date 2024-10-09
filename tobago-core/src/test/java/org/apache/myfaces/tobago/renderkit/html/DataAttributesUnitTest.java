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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class DataAttributesUnitTest {

  @Test
  public void testAttributeNames() throws IllegalAccessException {
    for (final DataAttributes d : DataAttributes.values()) {

// todo      if (d.getAnnotation(Deprecated.class) != null) {
//        // ignore the check for deprecated fields
//        continue;
//      }
      final String value = d.getValue();
      Assertions.assertTrue(value.matches("data(-tobago)?(-[a-z0-9]+)*-[a-z0-9]+"),
          "Regexp check: value='" + value + "'");

      final String extension
          = value.startsWith("data-tobago-")
          ? value.substring("data-tobago-".length())
          : value.substring("data-".length());
      final String name = d.name();
      Assertions.assertEquals(name, extension.toUpperCase(Locale.ROOT).replaceAll("-", "_"));
    }
  }
}
