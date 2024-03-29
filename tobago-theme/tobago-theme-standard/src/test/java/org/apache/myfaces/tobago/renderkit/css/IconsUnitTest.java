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

public class IconsUnitTest {

  @Test
  public void bootstrapIcons() {
    Assertions.assertTrue(Icons.matches("bi-code"));
    Assertions.assertEquals("bi-code", Icons.custom("bi-code").getName());
    Assertions.assertFalse(Icons.matches("bi-code.jpg"));
    Assertions.assertNull(Icons.custom("bi-code.jpg").getName());
  }

  @Test
  public void iconFontawesome4() {
    Assertions.assertTrue(Icons.matches("fa-code"));
    Assertions.assertEquals("fa fa-code", Icons.custom("fa-code").getName());
    Assertions.assertFalse(Icons.matches("fx-code"));
    Assertions.assertNull(Icons.custom("fx-code").getName());
  }

  @Test
  public void iconFontawesome5() {
    Assertions.assertTrue(Icons.matches("fas fa-code"));
    Assertions.assertEquals("fas fa-code", Icons.custom("fas fa-code").getName());
    Assertions.assertTrue(Icons.matches("far fa-code"));
    Assertions.assertEquals("far fa-code", Icons.custom("far fa-code").getName());
    Assertions.assertTrue(Icons.matches("far fa-angle-left"));
    Assertions.assertEquals("far fa-angle-left", Icons.custom("far fa-angle-left").getName());
    Assertions.assertTrue(Icons.matches("fal fa-code"));
    Assertions.assertEquals("fal fa-code", Icons.custom("fal fa-code").getName());
    Assertions.assertTrue(Icons.matches("fad fa-code"));
    Assertions.assertEquals("fad fa-code", Icons.custom("fad fa-code").getName());
    Assertions.assertFalse(Icons.matches("fax fa-code"));
    Assertions.assertNull(Icons.custom("fax fa-code").getName());
    Assertions.assertTrue(Icons.matches("fab fa-elementor"));
    Assertions.assertEquals("fab fa-elementor", Icons.custom("fab fa-elementor").getName());
  }

  @Test
  public void iconFontawesome6() {
    Assertions.assertTrue(Icons.matches("fa-solid fa-code"));
    Assertions.assertTrue(Icons.matches("fa-regular fa-code"));
    Assertions.assertTrue(Icons.matches("fa-light fa-code"));
    Assertions.assertTrue(Icons.matches("fa-duotone fa-code"));
    Assertions.assertTrue(Icons.matches("fa-thin fa-code"));
    Assertions.assertTrue(Icons.matches("fa-sharp fa-solid fa-code"));
    Assertions.assertTrue(Icons.matches("fa-sharp fa-regular fa-code"));
    Assertions.assertTrue(Icons.matches("fa-sharp fa-light fa-code"));
    Assertions.assertTrue(Icons.matches("fa-sharp fa-thin fa-code"));
    Assertions.assertTrue(Icons.matches("fa-solid fa-magnifying-glass"));
    Assertions.assertFalse(Icons.matches("fa-asdf fa-magnifying-glass"));
    Assertions.assertFalse(Icons.matches("fa-sharp fa-asdf fa-magnifying-glass"));
    Assertions.assertFalse(Icons.matches("fa-shark fa-regular fa-magnifying-glass"));
  }
}
