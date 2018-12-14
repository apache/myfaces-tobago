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

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Locale;

public class ResourcesUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testDefault() {
    Assertions.assertEquals("First Page", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testGermany() {
    facesContext.getViewRoot().setLocale(Locale.GERMANY);
    Assertions.assertEquals("erste Seite", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testSpanish() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));
    Assertions.assertEquals("Primera Página", ResourceUtils.getString("sheet.first"));
  }

  @Test
  public void testFallback() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("ja"));
    Assertions.assertEquals("First Page", ResourceUtils.getString("sheet.first"));
  }
}
