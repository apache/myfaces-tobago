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

package org.apache.myfaces.tobago.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class TestController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

  public boolean hasTest() {
    final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    final String testJsUrl = "/" + getBase() + ".test.js";
    try {
      return externalContext.getResource(testJsUrl) != null;
    } catch (MalformedURLException e) {
      LOG.error("URL was: " + testJsUrl, e);
    }
    return false;
  }

  public String getBase() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final String viewId = facesContext.getViewRoot().getViewId();
    return viewId.substring(1, viewId.length() - 6); //remove leading '/' and trailing '.xhtml'
  }

  public List<String> getAllPages() {
    List<String> pages = new ArrayList<String>();

    final File rootDir = new File("src/main/webapp/content");
    if (rootDir.exists()) {
      for (String page : getXHTMLs(rootDir)) {
        pages.add(page.substring(16));
      }
    }
    return pages;
  }

  private List<String> getXHTMLs(File dir) {
    List<String> xhtmls = new ArrayList<String>();
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        xhtmls.addAll(getXHTMLs(file));
      } else if (!file.getName().startsWith("x-") && file.getName().endsWith(".xhtml")) {
        xhtmls.add(file.getPath());
      }
    }
    return xhtmls;
  }

  public List<TestPage> getTestPages() {
    List<TestPage> testPages = new ArrayList<TestPage>();

    int idCount = 1;
    final File rootDir = new File("src/main/webapp/content");
    if (rootDir.exists()) {
      for (String testJs : getTestJs(rootDir)) {
        final String base = testJs.substring(16, testJs.length() - 8);
        testPages.add(new TestPage("tp" + idCount++, base));
      }
    }
    return testPages;
  }

  private List<String> getTestJs(File dir) {
    List<String> testJsFiles = new ArrayList<String>();
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        testJsFiles.addAll(getTestJs(file));
      } else if (!file.getName().startsWith("x-") && file.getName().endsWith(".test.js")) {
        testJsFiles.add(file.getPath());
      }
    }
    return testJsFiles;
  }

  public class TestPage {
    private final String id;
    private final String base;

    TestPage(String id, String base) {
      this.id = id;
      this.base = base;
    }

    public String getId() {
      return id;
    }

    public String getBase() {
      return base;
    }
  }
}
