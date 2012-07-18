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

import org.apache.myfaces.tobago.util.Parameterized;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class AutomaticSeleniumTest extends SeleniumTest {

  private static final Logger LOG = LoggerFactory.getLogger(AutomaticSeleniumTest.class);

  private static final String MAVEN_TARGET = "target/tobago-example-test";

  private String url;

  public AutomaticSeleniumTest(String title, String url) {
    this.url = url;
  }

  @Test
  public void testPageConsistency() throws Exception {
    SeleniumScript script = getSeleniumScript(url);

    getCommandProcessor().doCommand("open", new String[] {"http://localhost:8080/" + "KillSession", ""});

    for (SeleniumScriptItem item : script.getItems()) {
      LOG.info("Calling: " + item);
      getCommandProcessor().doCommand(item.getCommand(), item.getParameters());
      checkPage();
    }
  }

  private SeleniumScript getSeleniumScript(String url) throws XPathExpressionException, SAXException, IOException {
    String seleniumUrl = url.substring("/faces/".length());
    seleniumUrl = seleniumUrl.substring(0, seleniumUrl.lastIndexOf("."));
    seleniumUrl = "http://localhost:8080/" + seleniumUrl + ".selenium.html";
    return new SeleniumScript(seleniumUrl, url);
  }

  @Parameterized.Parameters
  public static Collection<Object[]> findPages() {
    List<String> paths = new ArrayList<String>();

    String base = MAVEN_TARGET + '/';

    // e.g. in the IDE normally the base dir is the tobago project root
    if (!new File(base).exists()) {
      base = "tobago-example/tobago-example-test/" + base;
    }
    
    collect(paths, base, "");

    Collections.sort(paths);

    List<Object[]> result = new ArrayList<Object[]>();

    for (String path : paths) {
      Object[] objects = {
          path.replace('.', '_'), // because dots will be displayed strange in the IDE
          createUrl(path)
      };
      result.add(objects);
    }

    return result;
  }

  private static void collect(List<String> result, String base, String directory) {

    final File file = new File(base + directory);

    if (!file.exists()) {
      throw new RuntimeException("Input directory doesn't exists: '" + file.getAbsolutePath() + "'");
    }

    if (!file.isDirectory()) {
      throw new RuntimeException("Input is not a directory: '" + file.getAbsolutePath() + "'");
    }

    String[] filenames = file.list();

    for (String filename : filenames) {

      String path = directory + '/' + filename;

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
