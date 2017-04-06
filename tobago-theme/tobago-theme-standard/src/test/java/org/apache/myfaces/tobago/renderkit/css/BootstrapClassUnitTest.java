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

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test if every in Java declared CSS class has really an entry in a CSS file.
 */
public class BootstrapClassUnitTest {

  private List<BootstrapClass> not = Arrays.asList(
      BootstrapClass.COL_XS_1,
      BootstrapClass.COL_XS_2,
      BootstrapClass.COL_XS_3,
      BootstrapClass.COL_XS_4,
      BootstrapClass.COL_XS_5,
      BootstrapClass.COL_XS_6,
      BootstrapClass.COL_XS_7,
      BootstrapClass.COL_XS_8,
      BootstrapClass.COL_XS_9,
      BootstrapClass.COL_XS_10,
      BootstrapClass.COL_XS_11,
      BootstrapClass.COL_XS_12,
      BootstrapClass.NAVBAR_DARK,
      BootstrapClass.NAVBAR_TOGGLEABLE_XS
  );

  /**
   * This test checks, if every item of the {@link BootstrapClass} occurs in the bootstrap.css.
   */
  @Test
  public void testCompareBootstrapCss() throws FileNotFoundException {

    final BootstrapClass[] allValues = BootstrapClass.values();
    final List<BootstrapClass> toCheck = new ArrayList<BootstrapClass>();
    for (BootstrapClass value : allValues) {
      if (!not.contains(value)) {
        toCheck.add(value);
      }
    }

    CssClassUtils.compareCss(
        "src/main/resources/META-INF/resources/tobago/standard/tobago-bootstrap/_version/css/bootstrap.css",
        toCheck.toArray(new BootstrapClass[toCheck.size()]));
  }

}
