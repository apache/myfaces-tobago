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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.util.Parameterized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AutomaticSeleniumTest extends SeleniumTest {

  private static final Logger LOG = LoggerFactory.getLogger(AutomaticSeleniumTest.class);

  private static final String MAVEN_TARGET = "target/tobago-example-test";

  private String url;

  public AutomaticSeleniumTest(final String title, final String url) {
    this.url = url;
  }

  @Test
  public void testPageConsistency() throws Exception {
    final SeleniumScript script = getSeleniumScript(url);

    getSelenium().killSession();

    for (final SeleniumScriptItem item : script.getItems()) {
      LOG.info("Calling: " + item);
      getSelenium().command(item.getCommand(), item.getParameters()[0], item.getParameters()[1]);
      getSelenium().checkPage();
    }
  }

  private SeleniumScript getSeleniumScript(final String url)
      throws XPathExpressionException, SAXException, IOException, ParserConfigurationException {
    String seleniumUrl = url.substring("/faces/".length());
    seleniumUrl = seleniumUrl.substring(0, seleniumUrl.lastIndexOf("."));
    seleniumUrl = "http://localhost:8080/" + seleniumUrl + ".selenium.html";
    return new SeleniumScript(new URL(seleniumUrl), url);
  }

  @Parameterized.Parameters
  public static Collection<Object[]> findPages() {
    final List<String> paths = new ArrayList<String>();

    String base = MAVEN_TARGET + '/';

    // e.g. in the IDE normally the base dir is the tobago project root
    if (!new File(base).exists()) {
      base = "tobago-example/tobago-example-test/" + base;
    }

    collect(paths, base, "");

    Collections.sort(paths);

    final List<Object[]> result = new ArrayList<Object[]>();

    for (final String path : paths) {
      final Object[] objects = {
          path.replace('.', '_'), // because dots will be displayed strange in the IDE
          createUrl(path)
      };
      result.add(objects);
    }

    return result;
  }

  private static void collect(final List<String> result, final String base, final String directory) {

    final File file = new File(base + directory);

    if (!file.exists()) {
      throw new RuntimeException("Input directory doesn't exists: '" + file.getAbsolutePath() + "'");
    }

    if (!file.isDirectory()) {
      throw new RuntimeException("Input is not a directory: '" + file.getAbsolutePath() + "'");
    }

    final String[] filenames = file.list();

    for (final String filename : filenames) {

      final String path = directory + '/' + filename;

      if (new File(base + path).isDirectory()) {
        collect(result, base, path);
        continue;
      }

      if (TestPageFilter.isValid(path) && !TestPageFilter.isDisabled(path) && !TestPageFilter.isTodo(path)) {
        result.add(path);
      }
    }
  }
}
