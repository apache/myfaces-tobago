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

  public String getTestBase() {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final ExternalContext externalContext = facesContext.getExternalContext();
    final String viewId = facesContext.getViewRoot().getViewId();
    final String base = viewId.substring(0, viewId.length() - 6);
    try {
      if (externalContext.getResource(base + ".test.js") != null) {
        return base;
      }
    } catch (MalformedURLException e) {
      LOG.error("viewId='" + viewId + "'", e);
    }

    return null;
  }

  public List<TestPage> getTestPages() {
    List<TestPage> testPages = new ArrayList<TestPage>();

    int idCount = 1;
    final File rootDir = new File("src/main/webapp/content");
    for (String testJs : getTestJs(rootDir)) {
      final String xhtml = "/faces/" + testJs.substring(16, testJs.length() - 8) + ".xhtml";
      final String adjustedTestJs = "/" + testJs.substring(16);
      testPages.add(new TestPage("tp" + idCount++, xhtml, adjustedTestJs));
    }
    return testPages;
  }

  private List<String> getTestJs(File dir) {
    List<String> xhtmls = new ArrayList<String>();
    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        xhtmls.addAll(getTestJs(file));
      } else if (!file.getName().startsWith("x-") && file.getName().endsWith(".test.js")) {
        xhtmls.add(file.getPath());
      }
    }
    return xhtmls;
  }

  public class TestPage {
    private final String id;
    private final String xhtml;
    private final String js;

    TestPage(String id, String xhtml, String js) {
      this.id = id;
      this.xhtml = xhtml;
      this.js = js;
    }

    public String getId() {
      return id;
    }

    public String getXhtml() {
      return xhtml;
    }

    public String getJs() {
      return js;
    }
  }
}
