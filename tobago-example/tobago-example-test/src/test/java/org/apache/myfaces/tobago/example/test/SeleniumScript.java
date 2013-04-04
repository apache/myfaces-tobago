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

import org.junit.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SeleniumScript {

  private static final XPathFactory XPATH_FACTORY;
  private static final XPathExpression XPATH_EXPRESSION;
  private static final XPathExpression TR_XPATH;
  private static final XPathExpression TD_XPATH;

  private List<SeleniumScriptItem> items = new ArrayList<SeleniumScriptItem>();

  static {
    try {
      XPATH_FACTORY = XPathFactory.newInstance();
      XPATH_EXPRESSION = XPATH_FACTORY.newXPath().compile("//table/tbody");
      TR_XPATH = XPATH_FACTORY.newXPath().compile("tr");
      TD_XPATH = XPATH_FACTORY.newXPath().compile("td");
    } catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  public SeleniumScript(URL scriptUrl, String url)
      throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder parser = factory.newDocumentBuilder();
      parser.setEntityResolver(new EntityResolver() {
        public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException, IOException {
          // do not any resource resolving
          return new InputSource(new StringReader(""));
        }
      });

      Document document = parser.parse(scriptUrl.openStream());
      addSeleniumItems(document, url);
    } catch (FileNotFoundException e) {
      // using default
      items.add(new SeleniumScriptItem("open", url, ""));
    }
  }

  private void addSeleniumItems(Document document, String url) throws XPathExpressionException {

    final Object table = XPATH_EXPRESSION.evaluate(document, XPathConstants.NODE);
    final NodeList trList = (NodeList) TR_XPATH.evaluate(table, XPathConstants.NODESET);

    for (int i = 0; i < trList.getLength(); i++) {
      final Node tr = trList.item(i);
      final NodeList tdList = (NodeList) TD_XPATH.evaluate(tr, XPathConstants.NODESET);

      Assert.assertEquals(3, tdList.getLength());

      String command = tdList.item(0).getTextContent();
      String parameter1 = tdList.item(1).getTextContent();
      String parameter2 = tdList.item(2).getTextContent();
      if (command.equals("open")) {
        // for open commands, use the filename, not the name in the script,
        // because we need the script for *.jspx and *.xhtml
        final String realPrefix = url.substring(0, url.lastIndexOf("."));
        final String scriptPrefix = parameter1.substring(0, parameter1.lastIndexOf("."));
        Assert.assertEquals("Is the url in the script correct?", scriptPrefix, realPrefix);
        parameter1 = url;
      }

      items.add(new SeleniumScriptItem(command, parameter1, parameter2));
    }
  }

  public List<SeleniumScriptItem> getItems() {
    return items;
  }
}
