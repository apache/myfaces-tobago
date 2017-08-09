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

import java.io.File;
import java.net.URL;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * if PhantomJS is configured, ensure that content-security-policy mode="off" is in tobago-config.xml otherwise
 * it will result in "Refused to evaluate a string as JavaScript because 'unsafe-eval' is not an allowed
 * source of script in the following Content Security Policy directive"
 * see: https://github.com/ariya/phantomjs/issues/13114
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ArquillianTest {

  @Drone
  private WebDriver browser;

  @ArquillianResource
  private URL contextPath;

  @FindBy(id = "page:iajax::field")
  private WebElement input;

  @FindBy(id = "page:oajax")
  private WebElement output;

  @FindByJQuery("#page\\:oajax > label")
  private WebElement outputLabel;

  @FindByJQuery("#page\\:oajax > span")
  private WebElement outputField;

  @Deployment(testable = false)
  public static WebArchive createDeployment() {
    File pom = new File("tobago-example/tobago-example-demo/pom.xml");
    WebArchive webArchive = ShrinkWrap.create(MavenImporter.class).
            loadPomFromFile(pom, "jsf-provided", "!myfaces-2.0").importBuildOutput()
            .as(WebArchive.class);
    // XXX there should be a proper profile in POM for that
    webArchive.delete("/WEB-INF/lib/hibernate-validator-4.3.2.Final.jar");
    return webArchive;
  }

  @Test
  public void ajaxOnChange() {
    browser.get(contextPath + "/faces/content/20-component/010-input/10-in/in.xhtml");

    Assert.assertEquals("On Server", outputLabel.getText());
    Assert.assertEquals("", outputField.getText());

    final String text = "Testtext";
    input.sendKeys(text);
    output.click();
//    Graphene.waitAjax();
/*
    try {
      Thread.sleep(30000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
*/

    Assert.assertEquals(text, outputField.getText());


//    JavascriptExecutor js = (JavascriptExecutor) browser;
//    js.executeScript("$(Tobago.Utils.escapeClientId('{}')).blur();", output.getAttribute("id"));

  }
}
