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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(Arquillian.class)
@RunAsClient
public class QUnitTests {

  private static final Logger LOG = LoggerFactory.getLogger(QUnitTests.class);
  private static List<String> testedPages = new LinkedList<>();

  @Drone
  private WebDriver browser;
  @ArquillianResource
  private URL contextPath;
  @FindByJQuery("#qunit-testresult")
  private WebElement qunitTestresult;
  @FindByJQuery("#qunit-tests")
  private WebElement qunitTests;

  @Deployment
  public static WebArchive createDeployment() {
    WebArchive webArchive;
    try {
      webArchive = createWebArchive(new File("tobago-example/tobago-example-demo/pom.xml"));
    } catch (final Exception e) {
      webArchive = createWebArchive(new File("pom.xml")); // Jenkins
    }
    return disableCSP(webArchive);
  }

  private static WebArchive createWebArchive(final File pom) {
    final WebArchive webArchive = ShrinkWrap.create(MavenImporter.class).
        loadPomFromFile(pom, "jsf-provided", "!myfaces-2.0").importBuildOutput()
        .as(WebArchive.class);
    // XXX there should be a proper profile in POM for that
    webArchive.delete("/WEB-INF/lib/hibernate-validator-4.3.2.Final.jar");
    return webArchive;
  }

  private static WebArchive disableCSP(final WebArchive webArchive) {
    try {
      final InputStream inputStream = webArchive.get("WEB-INF/tobago-config.xml").getAsset().openStream();

      final StringWriter stringWriter = new StringWriter();
      IOUtils.copy(inputStream, stringWriter, Charset.defaultCharset());
      final String modifiedTobagoConfig = stringWriter.toString()
          .replace("<content-security-policy mode=\"on\">", "<content-security-policy mode=\"off\">");

      final File tobagoConfigXml = new File("target/tobago-config.xml");
      FileUtils.writeStringToFile(tobagoConfigXml, modifiedTobagoConfig, Charset.defaultCharset());
      webArchive.addAsWebInfResource(tobagoConfigXml);
    } catch (final IOException e) {
      LOG.error("could not modify tobago-config.xml", e);
    }
    return webArchive;
  }

  private void setupBrowser(final String base, final boolean accessTest) throws UnsupportedEncodingException {
    LOG.info("setup browser for: " + base + ".xhtml | accessTest=" + accessTest);
    browser.get(contextPath + "test.xhtml?base=" + URLEncoder.encode(base, "UTF-8")
        + (accessTest ? "&accessTest=true" : ""));
  }

  private void runStandardTest(final String page) throws UnsupportedEncodingException, InterruptedException {
    testedPages.add(page);

    if (!ignorePages().contains(page)) {
      final String base = page.substring(0, page.length() - 6);
      setupBrowser(base, false);

      checkQUnitResults(page);
    }
  }

  private void checkQUnitResults(final String page) throws InterruptedException {
    final boolean timeout = waitForTest(page);
    final List<WebElement> testCases = qunitTests.findElements(By.xpath("li"));
    Assert.assertTrue("There must be at least one test case.", testCases.size() > 0);

    final String textContent = qunitTests.getAttribute("textContent");

    final boolean testFailed = timeout || (textContent != null
        && textContent.contains(" msfailed@ ") || textContent.contains("Expected:") || textContent.contains("Result:"));

    int testCaseCount = 1;
    final StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(qunitTestresult.getAttribute("textContent"));
    stringBuilder.append("\n");

    if (testFailed) {
      for (final WebElement testCase : testCases) {
        final String testName = getText(testCase, "test-name");
        final String testStatus = testCase.getAttribute("class").toUpperCase();

        stringBuilder.append(testCaseCount++);
        stringBuilder.append(". ");
        stringBuilder.append(testStatus);
        stringBuilder.append(": ");
        stringBuilder.append(testName);
        stringBuilder.append(" (");
        stringBuilder.append(getText(testCase, "runtime"));
        stringBuilder.append(")\n");

        final WebElement assertList = testCase.findElement(By.className("qunit-assert-list"));
        final List<WebElement> asserts = assertList.findElements(By.tagName("li"));
        int assertCount = 1;
        for (final WebElement assertion : asserts) {
          final String assertStatus = assertion.getAttribute("class");

          stringBuilder.append("- ");
          if (assertCount <= 9) {
            stringBuilder.append("0");
          }
          stringBuilder.append(assertCount++);
          stringBuilder.append(". ");
          stringBuilder.append(assertStatus);
          stringBuilder.append(": ");
          stringBuilder.append(getText(assertion, "test-message"));
          stringBuilder.append(getText(assertion, "runtime"));
          stringBuilder.append("\n");

          final String assertExpected = getText(assertion, "test-expected");
          if (!"null".equals(assertExpected)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertExpected);
            stringBuilder.append("\n");
          }
          final String assertResult = getText(assertion, "test-actual");
          if (!"null".equals(assertResult)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertResult);
            stringBuilder.append("\n");
          }
          final String assertSource = getText(assertion, "test-source");
          if (!"null".equals(assertSource)) {
            stringBuilder.append("-- ");
            stringBuilder.append(assertSource);
            stringBuilder.append("\n");
          }
        }

        stringBuilder.append(getText(testCase, "qunit-source"));
        stringBuilder.append("\n\n");
      }
    }

    if (testFailed) {
      final String message = "Tests for " + page + " FAILED:\n" + stringBuilder.toString();
      LOG.warn(message);
      Assert.fail(message);
    } else {
      final String message = "Tests for " + page + " PASSED:\n" + stringBuilder.toString();
      LOG.info(message);
      Assert.assertTrue(message, true);
    }
  }

  private boolean waitForTest(final String page) throws InterruptedException {
    final long endTime = System.currentTimeMillis() + 90000;
    String lastStatus = null;
    while (System.currentTimeMillis() < endTime) {
      try {
        final String status = qunitTestresult.getAttribute("textContent");
        if (status == null) {
          LOG.info(page + " status=null");
        } else if (!status.equals(lastStatus)) {
          lastStatus = status;
          LOG.info(page + " " + status + " (" + (endTime - System.currentTimeMillis()) + "ms left)");
          if (status.startsWith("Tests completed")) {
            return false;
          }
        }
      } catch (final Exception e) {
        Thread.sleep(200);
      }
    }
    LOG.warn(page + " timeout");
    return true;
  }

  private String getText(final WebElement webElement, final String className) {
    final List<WebElement> elements = webElement.findElements(By.className(className));
    if (elements.size() > 0) {
      return elements.get(0).getAttribute("textContent");
    } else {
      return "null";
    }
  }

  @AfterClass
  public static void checkMissingTests() {
    if (testedPages.size() > 1) {
      final List<String> pages = getXHTMLs();
      int testablePagesCount = 0;

      final StringBuilder stringBuilder = new StringBuilder();
      for (final String page : pages) {
        if (new File(page.substring(0, page.length() - 6) + ".test.js").exists()) {
          testablePagesCount++;

          final String pathForBrowser = page.substring(page.indexOf("src/main/webapp/") + "src/main/webapp/".length());
          if (!testedPages.contains(pathForBrowser)) {
            final String errorString = "missing testmethod for " + pathForBrowser;
            LOG.warn(errorString);
            stringBuilder.append("\n");
            stringBuilder.append(errorString);
          }
        }
      }
      Assert.assertEquals(stringBuilder.toString(), testablePagesCount, testedPages.size());
    }
  }

  @Test
  public void testAccessAllPages() throws UnsupportedEncodingException, InterruptedException {
    final List<String> pages = getXHTMLs();
    List<WebElement> results;

    // Test if 'has no exception' test is correct.
    setupBrowser("error/exception", true);
    final boolean timeoutException = waitForTest("error/exception");
    Assert.assertFalse("Could not verify 'has no exception' test: TIMEOUT", timeoutException);
    results = qunitTests.findElements(By.xpath("li"));
    boolean testException = false;
    for (final WebElement result : results) {
      if ("has no exception".equals(result.findElement(By.className("test-name")).getText())) {
        Assert.assertEquals(result.getAttribute("class"), "fail");
        testException = true;
      }
    }
    Assert.assertTrue("Could not verify 'has no exception' test.", testException);

    // Test if 'has no 404' test is correct.
    setupBrowser("error/404", true);
    final boolean timeout404 = waitForTest("error/404");
    Assert.assertFalse("Could not verify 'has no 404' test. TIMEOUT", timeout404);
    boolean test404 = false;
    results = qunitTests.findElements(By.xpath("li"));
    for (final WebElement result : results) {
      if ("has no 404".equals(result.findElement(By.className("test-name")).getText())) {
        Assert.assertEquals(result.getAttribute("class"), "fail");
        test404 = true;
      }
    }
    Assert.assertTrue("Could not verify 'has no 404' test.", test404);

    for (final String page : pages) {
      final String pathForBrowser = page.substring(page.indexOf("src/main/webapp/") + "src/main/webapp/".length(),
          page.length() - 6);

      //TODO: reimplement/remove/cleanup attic tests - currently too much failed
      if (!pathForBrowser.contains("content/40-test/90000-attic/")) {
        setupBrowser(pathForBrowser, true);
        checkQUnitResults(page);
      }
    }
  }

  private static List<String> getXHTMLs() {
    File rootDir = new File("tobago-example/tobago-example-demo/src/main/webapp/content");
    if (!rootDir.exists()) {
      rootDir = new File("src/main/webapp/content"); // Jenkins.
    }
    return getXHTMLs(rootDir);
  }

  private static List<String> getXHTMLs(final File dir) {
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

  private List<String> ignorePages() {
    final List<String> ignore = new ArrayList<>();
    // Miscalculation of width.
    ignore.add("content/20-component/010-input/50-input-group/group.xhtml");
    //PhantomJs miscalculate the height of the dropdown box
    ignore.add("content/40-test/3000-sheet/10-sheet-types/sheet-types.xhtml");
    // Works only for larger browser window
    ignore.add("content/40-test/4810-labelLayoutTop/labelLayoutTop.xhtml");
    //ajaxListener doesn't work for <tc:in> events: focus, blur, click, dblclick
    ignore.add("content/40-test/6000-event/event.xhtml");
    return ignore;
  }

  @Test
  public void in() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/010-input/10-in/in.xhtml";
    runStandardTest(page);
  }

  @Test
  public void suggest() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/010-input/20-suggest/suggest.xhtml";
    runStandardTest(page);
  }

  @Test
  public void date() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/010-input/40-date/date.xhtml";
    runStandardTest(page);
  }

  @Test
  public void group() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/010-input/50-input-group/group.xhtml";
    runStandardTest(page);
  }

  @Test
  public void selectBooleanCheckbox() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/030-select/10-selectBooleanCheckbox/selectBooleanCheckbox.xhtml";
    runStandardTest(page);
  }

  @Test
  public void selectOneChoice() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/030-select/20-selectOneChoice/selectOneChoice.xhtml";
    runStandardTest(page);
  }

  @Test
  public void selectOneRadio() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/030-select/30-selectOneRadio/selectOneRadio.xhtml";
    runStandardTest(page);
  }

  @Test
  public void selectOneListbox() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/030-select/40-selectOneListbox/selectOneListbox.xhtml";
    runStandardTest(page);
  }

  @Test
  public void selectManyCheckbox() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/030-select/50-selectManyCheckbox/selectManyCheckbox.xhtml";
    runStandardTest(page);
  }

  @Test
  public void buttons() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/040-command/20-buttons/buttons.xhtml";
    runStandardTest(page);
  }

  @Test
  public void popup() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/060-popup/popup.xhtml";
    runStandardTest(page);
  }

  @Test
  public void sheetSorting() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/080-sheet/10-sort/sheet-sorting.xhtml";
    runStandardTest(page);
  }

  @Test
  public void sheetEvent() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/080-sheet/30-event/sheet-event.xhtml";
    runStandardTest(page);
  }

  @Test
  public void treeSelect() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/20-component/090-tree/01-select/tree-select.xhtml";
    runStandardTest(page);
  }

  @Test
  public void contentValidation() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/06-validation/00/content-validation.xhtml";
    runStandardTest(page);
  }

  @Test
  public void validationJsr303() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/06-validation/01/validation-jsr303.xhtml";
    runStandardTest(page);
  }

  @Test
  public void form() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/08-form/form.xhtml";
    runStandardTest(page);
  }

  @Test
  public void formRequired() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/08-form/10-required/form-required.xhtml";
    runStandardTest(page);
  }

  @Test
  public void formAjax() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/08-form/20-ajax/form-ajax.xhtml";
    runStandardTest(page);
  }

  @Test
  public void forEach() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/51-for-each/for-each.xhtml";
    runStandardTest(page);
  }

  @Test
  public void collapsibleBox() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml";
    runStandardTest(page);
  }

  @Test
  public void collapsiblePopup() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/53-collapsible/10-collapsible-popup/collapsible-popup.xhtml";
    runStandardTest(page);
  }

  @Test
  public void collapsiblePanel() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/53-collapsible/20-collapsible-panel/collapsible-panel.xhtml";
    runStandardTest(page);
  }

  @Test
  public void collapsibleSection() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/30-concept/53-collapsible/30-collapsible-section/collapsible-section.xhtml";
    runStandardTest(page);
  }

  @Test
  public void suggestMethod() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/35-deprecated/15-suggest-method/suggest-method.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testDate() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/1040-date/date.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testTabgroupStyle() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/2500-tab/tabgroup-style.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testSheetTypes() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/3000-sheet/10-sheet-types/sheet-types.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testButtonLink() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/4000-button+link/button+link.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testAjaxDropdown() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/4000-button+link/4050-ajax-dropdown/ajax-dropdown.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testIdMarkup() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/4800-labelLayout/100-id-markup/id-markup.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testLabelLayoutTop() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/4810-labelLayoutTop/labelLayoutTop.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testEvent() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/6000-event/event.xhtml";
    runStandardTest(page);
  }

  @Test
  public void rendererBaseGetCurrentValue() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/50000-java/10-rendererBase-getCurrentValue/rendererBase-getCurrentValue.xhtml";
    runStandardTest(page);
  }

  @Test
  public void ajaxExecute() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/50000-java/20-ajax-execute/ajax-execute.xhtml";
    runStandardTest(page);
  }

  @Test
  public void ajaxSpecialCharacter() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/50000-java/30-ajax-special-character/ajax-special-character.xhtml";
    runStandardTest(page);
  }

  @Test
  public void testBehavior() throws UnsupportedEncodingException, InterruptedException {
    final String page = "content/40-test/6500-behavior/behavior.xhtml";
    runStandardTest(page);
  }
}
