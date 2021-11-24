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
  public void iconBi() {
    Assertions.assertTrue(Icons.matches("bi-code"));
    Assertions.assertEquals("bi-code", Icons.custom("bi-code").getName());
  }

  @Test
  public void iconBiJpg() {
    Assertions.assertFalse(Icons.matches("bi-code.jpg"));
    Assertions.assertNull(Icons.custom("bi-code.jpg").getName());
  }

  @Test
  public void iconFa() {
    Assertions.assertTrue(Icons.matches("fa-code"));
    Assertions.assertEquals("fa fa-code", Icons.custom("fa-code").getName());
  }

  @Test
  public void iconFx() {
    Assertions.assertFalse(Icons.matches("fx-code"));
    Assertions.assertNull(Icons.custom("fx-code").getName());
  }

  @Test
  public void iconFas() {
    Assertions.assertTrue(Icons.matches("fas fa-code"));
    Assertions.assertEquals("fas fa-code", Icons.custom("fas fa-code").getName());
  }

  @Test
  public void iconFar() {
    Assertions.assertTrue(Icons.matches("far fa-code"));
    Assertions.assertEquals("far fa-code", Icons.custom("far fa-code").getName());
  }

  @Test
  public void iconFarFaAngleLeft() {
    Assertions.assertTrue(Icons.matches("far fa-angle-left"));
    Assertions.assertEquals("far fa-angle-left", Icons.custom("far fa-angle-left").getName());
  }

  @Test
  public void iconFal() {
    Assertions.assertTrue(Icons.matches("fal fa-code"));
    Assertions.assertEquals("fal fa-code", Icons.custom("fal fa-code").getName());
  }

  @Test
  public void iconFad() {
    Assertions.assertTrue(Icons.matches("fad fa-code"));
    Assertions.assertEquals("fad fa-code", Icons.custom("fad fa-code").getName());
  }

  @Test
  public void iconFax() {
    Assertions.assertFalse(Icons.matches("fax fa-code"));
    Assertions.assertNull(Icons.custom("fax fa-code").getName());
  }
}
