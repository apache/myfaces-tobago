package org.apache.myfaces.tobago.example.demo;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
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

@RunWith(Arquillian.class)
@RunAsClient
public class ArquillianTest {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextPath;

    @FindBy(id = "page:i8::field")
    private WebElement inputField;

    @FindBy(id = "page:outputfield")
    private WebElement outputField;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive webArchive = ShrinkWrap.create(MavenImporter.class).loadPomFromFile("pom.xml", "jsf-provided", "!myfaces-2.0").importBuildOutput()
                .as(WebArchive.class);
        webArchive.addClass(ArquillianTest.class);
        // XXX there should be a proper profile in POM for that
        webArchive.delete("/WEB-INF/lib/hibernate-validator-4.3.2.Final.jar");
        return webArchive;
    }

    @Test
    public void shouldCreate() {
        browser.get(contextPath + "/faces/content/20-component/010-input/10-in/in.xhtml");

        Assert.assertEquals("On Server", outputField.getText());
        // TODO results in "Refused to evaluate a string as JavaScript because 'unsafe-eval' is not an allowed source of script in the following Content Security Policy directive"
//        Graphene.guardAjax(inputField).sendKeys("Testtext");

    }
}
