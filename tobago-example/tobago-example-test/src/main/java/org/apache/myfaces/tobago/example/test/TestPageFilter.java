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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestPageFilter {

  public static final List<String> ALLOWED = Arrays.asList(
      ".*\\/",
      ".*\\.xhtml",
      ".*\\.jsp",
      ".*\\.jspx",
      ".*\\.html"
  );

  public static final Set<String> HIDDEN = new HashSet<String>(Arrays.asList(
      "/META-INF.*",
      "/WEB-INF.*",
      "/org/.*",
      "/src/.*",
      ".*/\\.svn/.*"
  ));

  /**
   * Internal pages and pages that are impossible to run.
   */
  public static final Set<String> DISABLED = new HashSet<String>(Arrays.asList(
      ".*-fragment\\.xhtml", // intern
      ".*-fragment\\.jspx", // intern

      "/index.html", // intern
      "/navigation.*", // intern

      "/404.*", // meta test

      "/meta-test/meta-1.*", // meta test
      "/meta-test/meta-2.*\\.jspx", // meta test
      "/meta-test/meta-3.*\\.xhtml", // meta test
      "/meta-test/meta-4.*", // meta test
      "/meta-test/meta-5.*", // meta test

      "/partially/encoding.jspx", // not possible in JSP
      "/tc/attribute/mode-valueIfSet.jspx", // set id="${id}" not possible with JSP.
      "/tc/tree/tree-simple-with-data.jspx", // not possible in JSP
      "/tc/button/plain.html", // intern
      "/tc/button/plain_de.html", // intern

      "/tf/.*jspx" // tf: library doen't work with JSP
  ));

  /**
   * Switched off temporary.
   */
  public static final Set<String> TODO = new HashSet<String>(Arrays.asList(
      "/tc/gridLayout/rendered-combinations.jspx", // uses facelets templating
      "/tc/gridLayout/scrolling-2-levels.*", // todo: ?
      "/tc/gridLayout/scrolling-tab.*", // todo: measurement problem with firefox?
      "/tc/gridLayout/tabGroup-.*", // todo: measurement problem with firefox?
      "/tc/gridLayout/transparent-for-layout.jspx", // uses facelets templating
      "/tc/mediator/tomahawk-inputHtml.*", // todo
      "/tc/treeListbox/*", // todo
      "/tx/label/label.xhtml", // todo
      "/type/.*" // todo
  ));

  public static boolean isValid(String name) {

    // 1st all has to start with a '/' slash

    if (!name.startsWith("/")) {
      return false;
    }

    // 2nd the positive check

    boolean matches = false;
    for (String allowed : ALLOWED) {
      if (name.matches(allowed)) {
        matches = true;
      }
    }
    if (!matches) {
      return false;
    }

    // 3rd the negative check

    for (String hidden : HIDDEN) {
      if (name.matches(hidden)) {
        return false;
      }
    }

    return true;
  }

  public static boolean isDisabled(String name) {
    for (String disabled : DISABLED) {
      if (name.matches(disabled)) {
        return true;
      }
    }

    return false;
  }

  public static boolean isTodo(String name) {
    for (String todo : TODO) {
      if (name.matches(todo)) {
        return true;
      }
    }

    return false;
  }

}
