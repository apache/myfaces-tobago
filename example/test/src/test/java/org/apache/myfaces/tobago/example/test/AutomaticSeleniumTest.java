package org.apache.myfaces.tobago.example.test;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.thoughtworks.selenium.SeleniumException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class AutomaticSeleniumTest extends SeleniumTest {

  private static final String MAVEN_TARGET = "target/tobago-example-test";
  private static final String CONTEXT_PATH = "tobago-example-test";
  private static final String SERVLET_MAPPING = "faces";

  private String url;

  public AutomaticSeleniumTest(String title, String url) {
    this.url = url;
  }

  @Test
  public void testPageConsistency() {
    selenium.open("/tobago-example-test/org/apache/myfaces/tobago/renderkit/html/standard/standard/script/logging.js");
    selenium.open(url);
    Assert.assertFalse("Page '" + url + "' contains a 404. Source=" + selenium.getHtmlSource(), pageNotFound());
    try {
      Assert.assertFalse("Page '" + url + "' has error severity. Source=" + selenium.getHtmlSource(), isErrorOnPage());
    } catch (SeleniumException e) {
      Assert.fail("Page '" + url + "' is broken. Not a Tobago page? Source=" + selenium.getHtmlSource() + " exception=" + e);
    }
  }

  /**
   * Was the page not found?
   *
   * @return True if the page not found.
   */
  // XXX might be improved, I didn't find a way to read the HTTP status code
  protected boolean pageNotFound() {
    return selenium.getHtmlSource().contains("404");
  }

  /**
   * Checks the page for the Tobago JavaScript Logging Framework and tests its severity.
   *
   * @return True if the severity level of the page is error
   * @throws SeleniumException If the page is not a Tobago page, or any other problem with JavaScrpt or the page.
   */
  protected boolean isErrorOnPage() throws SeleniumException {
    String errorSeverity = selenium.getEval("window.LOG.getMaximumSeverity() >= window.LOG.ERROR");
    return Boolean.parseBoolean(errorSeverity);
  }

  @Parameterized.Parameters
  public static Collection<Object[]> findPages() {
    List<Object[]> result = new ArrayList<Object[]>();

    collect(result, MAVEN_TARGET + '/', "");

    return result;
  }

  private static void collect(List<Object[]> result, String base, String directory) {
    String[] filenames = new File(base + directory).list();

    for (String filename : filenames) {

      String path = directory + '/' + filename;

      if (new File(base + path).isDirectory()) {
        collect(result, base, path);
        continue;
      }

      if (Filter.isValid(path)) {
        Object[] objects = {
            path,
            createUrl(path)
        };
        result.add(objects);
      }
    }
  }

  protected static String createUrl(String page) {
    return '/' + CONTEXT_PATH + '/' + SERVLET_MAPPING + '/' + page;
  }

}
