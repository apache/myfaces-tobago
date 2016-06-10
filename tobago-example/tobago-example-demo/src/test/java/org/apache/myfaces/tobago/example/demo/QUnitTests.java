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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

@RunWith(Arquillian.class)
@RunAsClient
public class QUnitTests {

  private static final Logger LOG = LoggerFactory.getLogger(QUnitTests.class);

  @Drone
  private WebDriver browser;
  @ArquillianResource
  private URL contextPath;

  @FindByJQuery("#page\\:navbtns\\:toggletest")
  private WebElement toggletest;
  @FindByJQuery("#qunit")
  private WebElement qunit;

  @Deployment
  public static WebArchive createDeployment() {
    File pom = new File("tobago-example/tobago-example-demo/pom.xml");
    WebArchive webArchive = ShrinkWrap.create(MavenImporter.class).
            loadPomFromFile(pom, "jsf-provided", "!myfaces-2.0").importBuildOutput()
            .as(WebArchive.class);
    // XXX there should be a proper profile in POM for that
    webArchive.delete("/WEB-INF/lib/hibernate-validator-4.3.2.Final.jar");
    return webArchive;
  }

  private void checkResults(String page) {
    String testJs = page.substring(0, page.length() - 6) + ".test.js";
    browser.get(contextPath + "/faces/test.xhtml?page=" + page + "&testjs=" + testJs);

    WebElement qunitTests = qunit.findElement(By.id("qunit-tests"));
    List<WebElement> testCases = qunitTests.findElements(By.xpath("li"));
    Assert.assertTrue("There must be at least one test case.", testCases.size() > 0);

    for (WebElement testCase : testCases) {
      String testName = testCase.findElement(By.className("test-name")).getText();
      String runtime = testCase.findElement(By.className("runtime")).getText();

      if ("pass".equals(testCase.getAttribute("class"))) {
        LOG.info("test '" + testName + "' for " + page + " passed in " + runtime);
        Assert.assertTrue(true);
      } else if ("fail".equals(testCase.getAttribute("class"))) {
        WebElement assertList = testCase.findElement(By.className("qunit-assert-list"));
        List<WebElement> asserts = assertList.findElements(By.tagName("li"));

        int assertionCount = 1;
        for (WebElement assertion : asserts) {
          if ("pass".equals(assertion.getAttribute("class"))) {
            Assert.assertTrue(true);
          } else if ("fail".equals(assertion.getAttribute("class"))) {
            LOG.warn("test '" + testName + "' for " + page + " failed on assertion " + assertionCount);
            String expected = assertion.findElement(By.className("test-expected")).getText();
            expected = expected.substring(12, expected.length() - 1);
            String actual = assertion.findElement(By.className("test-actual")).getText();
            actual = actual.substring(10, actual.length() - 1);
            Assert.assertEquals(expected, actual);
          }
        }
      }
    }
  }

  @Test
  public void allPages() {

  }

  @Test
  public void in() {
    String page = "content/20-component/010-input/10-in/in.xhtml";
    checkResults(page);
  }

  @Test
  public void suggest() {
    String page = "content/20-component/010-input/20-suggest/suggest.xhtml";
    checkResults(page);
  }

  @Test
  public void date() {
    String page = "content/40-test/1040-date/date.xhtml";
    checkResults(page);
  }
}
