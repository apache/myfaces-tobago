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

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.junit.Assert;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
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
      XPATH_EXPRESSION = XPATH_FACTORY.newXPath().compile("//TABLE/TBODY");
      TR_XPATH = XPATH_FACTORY.newXPath().compile("TR");
      TD_XPATH = XPATH_FACTORY.newXPath().compile("TD");
    } catch (XPathExpressionException e) {
      throw new RuntimeException(e);
    }
  }

  public SeleniumScript(String scriptUrl, String url) throws IOException, SAXException, XPathExpressionException {
    DOMFragmentParser parser = new DOMFragmentParser();
    HTMLDocument document = new HTMLDocumentImpl();
    DocumentFragment fragment = document.createDocumentFragment();
    try {
      parser.parse(scriptUrl, fragment);
      // not nice, it seems that parse also throws a FileNotFoundException sometimes.
      // XXX I don't know why
      if (fragment.getTextContent().contains("The page was not found!")) {
        throw new FileNotFoundException(scriptUrl);
      }
      addSeleniumItems(fragment, url);
    } catch (FileNotFoundException e) {
      // using default
      items.add(new SeleniumScriptItem("open", url, ""));
    }
  }

  private void addSeleniumItems(DocumentFragment fragment, String url) throws XPathExpressionException {

    final Object table = XPATH_EXPRESSION.evaluate(fragment, XPathConstants.NODE);
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
