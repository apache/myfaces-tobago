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

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class AutomaticSeleniumTest extends SeleniumTest {

  private static final String MAVEN_TARGET = "target/tobago-example-test";

  private String url;

  public AutomaticSeleniumTest(String title, String url) {
    this.url = url;
  }

  @Test
  public void testPageConsistency() {
    selenium.open(url);
    checkPage();
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
}
