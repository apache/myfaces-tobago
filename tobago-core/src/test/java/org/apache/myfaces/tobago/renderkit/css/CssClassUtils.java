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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class CssClassUtils {

  /**
   * Checks, if CSS class names are defined in the file.
   */
  static void compareCss(final String cssFileName, final CssItem[] cssItems) throws FileNotFoundException {

    File cssFile = new File(cssFileName);
    Assert.assertTrue(cssFile.exists());

    String fileContent = new Scanner(cssFile).useDelimiter("\\Z").next();

    for (CssItem cssItem : cssItems) {
      final String className = cssItem.getName();

      Assert.assertTrue("'" + className + "' exist in " + cssItem.getClass().getName() + " but not in "
              + cssFile.getName(),
          containsClassName(fileContent, className));
    }
  }

  private static boolean containsClassName(final String content, final String className) {
    return content.contains("." + className + " ")
        || content.contains("." + className + "{")
        || content.contains("." + className + ",")
        || content.contains("." + className + ":")
        || content.contains("." + className + ".")
        || content.contains("." + className + ">");
  }
}
