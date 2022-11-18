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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
@Named
public class TestController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public boolean hasTest() {
    final ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    final String testJsUrl = "/" + getBase() + ".test.js";
    try {
      return externalContext.getResource(testJsUrl) != null;
    } catch (final MalformedURLException e) {
      LOG.error("URL was: " + testJsUrl, e);
    }
    return false;
  }

  public String getBase() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final String viewId = facesContext.getViewRoot().getViewId();
    return viewId.substring(1, viewId.length() - 6); //remove leading '/' and trailing '.xhtml'
  }

  public List<TestPage> getAllTestPages() throws UnsupportedEncodingException {
    final List<TestPage> pages = new ArrayList<>();

    String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    realPath = realPath.endsWith("/") ? realPath : realPath.concat("/");

    final File rootDir = new File(realPath + "content");
    if (rootDir.exists()) {
      int idCount = 1;
      for (final String page : getXHTMLs(rootDir)) {
        final String base = page.substring(realPath.length(), page.length() - ".xhtml".length());
        pages.add(new TestPage(
            "tp" + idCount++,
            URLEncoder.encode(base, StandardCharsets.UTF_8.name()),
            base));
        // todo: StandardCharsets.UTF_8.name() can be simplified with Java 10
      }
    }
    return pages;
  }

  private List<String> getXHTMLs(final File dir) {
    final List<String> xhtmls = new ArrayList<>();
    for (final File file : dir.listFiles()) {
      if (file.isDirectory()) {
        xhtmls.addAll(getXHTMLs(file));
      } else if (!file.getName().startsWith("x-") && file.getName().endsWith(".xhtml")) {
        xhtmls.add(file.getPath());
      }
    }
    return xhtmls;
  }

  public List<TestPage> getTestPages() throws UnsupportedEncodingException {
    final List<TestPage> testPages = new ArrayList<>();

    String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
    realPath = realPath.endsWith("/") ? realPath : realPath.concat("/");

    final File rootDir = new File(realPath + "content");
    if (rootDir.exists()) {
      int idCount = 1;
      for (final String testJs : getTestJs(rootDir)) {
        final String base = testJs.substring(realPath.length(), testJs.length() - ".test.js".length());
        testPages.add(new TestPage(
            "tp" + idCount++,
            URLEncoder.encode(base, StandardCharsets.UTF_8.name()),
            base));
        // todo: StandardCharsets.UTF_8.name() can be simplified with Java 10
      }
    }
    return testPages;
  }

  private List<String> getTestJs(final File dir) {
    final List<String> testJsFiles = new ArrayList<>();
    for (final File file : dir.listFiles()) {
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
    private final String label;
    private final String shortLabel;

    TestPage(final String id, final String base, final String label) {
      this.id = id;
      this.base = base;
      String back = label.startsWith("content/") ? label.substring("content/".length()) : label;
      this.label = StringUtils.abbreviateMiddle(back, "...", 50);
      this.shortLabel = StringUtils.abbreviateMiddle(back, "...", 30);
    }

    public String getId() {
      return id;
    }

    public String getBase() {
      return base;
    }

    public String getLabel() {
      return label;
    }

    public String getShortLabel() {
      return shortLabel;
    }
  }
}
