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

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Test if every in Java declared CSS class has really an entry in a CSS file.
 */
public class CssClassCompareUnitTest extends AbstractTobagoTestBase {

  private static final Logger LOG = LoggerFactory.getLogger(CssClassCompareUnitTest.class);

  /**
   * This test checks, if every item of the {@link BootstrapClass} occurs in the bootstrap.css.
   */
  @Test
  public void testCompareBootstrapCss() throws FileNotFoundException {
    File bootstrapCss = new File(
        "src/main/resources/META-INF/resources/org/apache/myfaces/"
            + "tobago/renderkit/html/standard/standard/bootstrap/4.0.0-alpha.5/css/bootstrap.css");
    compareCss(bootstrapCss, BootstrapClass.values());
  }

  /**
   * This test checks, if every item of the {@link TobagoClass} occurs in the _tobago.scss.
   */
  @Test
  public void testCompareTobagoCss() throws FileNotFoundException {
    File tobagoCss = new File(
        "src/main/scss/_tobago.scss");

    List<CssItem> classNames = new ArrayList<CssItem>();
    for (TobagoClass value : TobagoClass.values()) {
      if (!value.getName().startsWith("tobago-")) {
        classNames.add(value);
      }
    }

    compareCss(tobagoCss, classNames.toArray(new CssItem[classNames.size()]));
  }

  private void compareCss(File cssFile, CssItem[] cssItems) throws FileNotFoundException {
    Assert.assertTrue(cssFile.exists());

    String fileContent = new Scanner(cssFile).useDelimiter("\\Z").next();

    for (CssItem cssItem : cssItems) {
      final String className = cssItem.getName();

      Assert.assertTrue("'" + className + "' exist in " + cssItem.getClass().getName() + " but not in "
              + cssFile.getName(),
          containsClassName(fileContent, className));
    }
  }

  private boolean containsClassName(String content, String className) {
    return content.contains("." + className + " ")
        || content.contains("." + className + "{")
        || content.contains("." + className + ",")
        || content.contains("." + className + ":")
        || content.contains("." + className + ".")
        || content.contains("." + className + ">");
  }
}
