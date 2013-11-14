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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.context.ThemeScript;
import org.apache.myfaces.tobago.context.ThemeStyle;
import org.apache.myfaces.tobago.internal.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Stack;

public class TobagoConfigParser extends TobagoConfigEntityResolver {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigParser.class);

  private static final int TOBAGO_CONFIG = -1498874611;
  private static final int NAME = 3373707;
  private static final int ORDERING = 1234314708;
  private static final int BEFORE = -1392885889;
  private static final int AFTER = 92734940;
  private static final int THEME_CONFIG = 1930630086;
  private static final int DEFAULT_THEME = -114431171;
  private static final int SUPPORTED_THEME = -822303766;
  private static final int RESOURCE_DIR = -385546674;
  private static final int CREATE_SESSION_SECRET = 413906616;
  private static final int CHECK_SESSION_SECRET = 275994924;
  private static final int PREVENT_FRAME_ATTACKS = 270456726;
  private static final int CONTENT_SECURITY_POLICY = 1207440139;
  private static final int DIRECTIVE = -962590641;
  private static final int RENDERERS = 1839650832;
  private static final int RENDERER = -494845757;
  private static final int SUPPORTED_MARKUP = 71904295;
  private static final int MARKUP = -1081305560;
  private static final int THEME_DEFINITIONS = -255617156;
  private static final int THEME_DEFINITION = 1515774935;
  private static final int DISPLAY_NAME = 1568910518;
  private static final int RESOURCE_PATH = 933304964;
  private static final int FALLBACK = 761243362;
  private static final int VERSIONED = -1407102089;
  private static final int RESOURCES = -1983070683;
  private static final int SCRIPT = -907685685;
  private static final int STYLE = 109780401;

  private TobagoConfigFragment tobagoConfig;
  private RendererConfig currentRenderer;
  private ThemeImpl currentTheme;
  private Boolean production;
  private StringBuilder buffer;

  private Stack<String> stack;

  public TobagoConfigParser() {
  }

  public TobagoConfigFragment parse(URL url)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    if (LOG.isInfoEnabled()) {
      LOG.info("Parsing configuration file: '{}'", url);
    }

    final TobagoConfigVersion version = new TobagoConfigVersion(url);

    // todo: Is there a solution that validate with both, DTD and XSD?

    if (version.isSchema()) {
      validate(url, version);
    }

    InputStream inputStream = null;
    try {
      inputStream = url.openStream();
      final SAXParserFactory factory = SAXParserFactory.newInstance();
      if (!version.isSchema()) {
        factory.setValidating(true);
      }
      final SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputStream, this);
    } finally {
      IoUtils.closeQuietly(inputStream);
    }
    return tobagoConfig;
  }

  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    super.ignorableWhitespace(ch, start, length);
  }

  @Override
  public void startDocument() throws SAXException {

    buffer = new StringBuilder();
    stack = new Stack<String>();
  }

  @Override
  public void endDocument() throws SAXException {
    assert stack.empty();
    stack = null;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

    // No unused content should be collected, specially text mixed with tags.
    assert buffer.toString().trim().length() == 0;

    buffer.setLength(0);
    stack.add(qName);

    switch (qName.hashCode()) {

      case TOBAGO_CONFIG:
        tobagoConfig = new TobagoConfigFragment();
        break;

      case CONTENT_SECURITY_POLICY:
        final String mode = attributes.getValue("mode");
        tobagoConfig.setContentSecurityPolicy(new ContentSecurityPolicy(mode));
        break;

      case RENDERERS:
        if (currentTheme != null) {
          currentTheme.setRenderersConfig(new RenderersConfigImpl());
        } else {
          tobagoConfig.setRenderersConfig(new RenderersConfigImpl());
        }
        break;

      case RENDERER:
        currentRenderer = new RendererConfig();
        break;

      case THEME_DEFINITION:
        currentTheme = new ThemeImpl();
        tobagoConfig.addThemeDefinition(currentTheme);
        break;

      case RESOURCES:
        production = Boolean.parseBoolean(attributes.getValue("production"));
        break;

      case SCRIPT:
        final ThemeScript script = new ThemeScript();
        script.setName(attributes.getValue("name"));
        if (production) {
          currentTheme.getProductionResources().addScript(script);
        } else {
          currentTheme.getResources().addScript(script);
        }
        break;

      case STYLE:
        final ThemeStyle style = new ThemeStyle();
        style.setName(attributes.getValue("name"));
        if (production) {
          currentTheme.getProductionResources().addStyle(style);
        } else {
          currentTheme.getResources().addStyle(style);
        }
        break;

      case NAME:
      case ORDERING:
      case BEFORE:
      case AFTER:
      case RESOURCE_DIR:
      case THEME_CONFIG:
      case DEFAULT_THEME:
      case SUPPORTED_THEME:
      case SUPPORTED_MARKUP:
      case MARKUP:
      case CREATE_SESSION_SECRET:
      case CHECK_SESSION_SECRET:
      case PREVENT_FRAME_ATTACKS:
      case DIRECTIVE:
      case THEME_DEFINITIONS:
      case DISPLAY_NAME:
      case RESOURCE_PATH:
      case VERSIONED:
      case FALLBACK:
        break;

      default:
        LOG.warn("Ignoring unknown start tag <" + qName + ">");
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    buffer.append(ch, start, length);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    assert qName.equals(stack.peek());

    final String text = buffer.toString().trim();
    buffer.setLength(0);

    switch (qName.hashCode()) {

      case NAME:
        final String parent = stack.get(stack.size() - 2);
        switch (parent.hashCode()) {

          case TOBAGO_CONFIG:
            tobagoConfig.setName(text);
            break;

          case BEFORE:
            tobagoConfig.addBefore(text);
            break;

          case AFTER:
            tobagoConfig.addAfter(text);
            break;

          case RENDERER:
            currentRenderer.setName(text);
            if (currentTheme != null) {
              ((RenderersConfigImpl) currentTheme.getRenderersConfig()).addRenderer(currentRenderer);
            } else {
              ((RenderersConfigImpl) tobagoConfig.getRenderersConfig()).addRenderer(currentRenderer);
            }
            break;

          case THEME_DEFINITION:
            currentTheme.setName(text);
            break;

          default:
            LOG.warn("Ignoring unknown parent <" + qName + "> of tag <name>");
        }
        break;

      case DEFAULT_THEME:
        tobagoConfig.setDefaultThemeName(text);
        break;

      case SUPPORTED_THEME:
        tobagoConfig.addSupportedThemeName(text);
        break;

      case RESOURCE_DIR:
        tobagoConfig.addResourceDir(text);
        break;

      case CREATE_SESSION_SECRET:
        tobagoConfig.setCreateSessionSecret(text);
        break;

      case CHECK_SESSION_SECRET:
        tobagoConfig.setCheckSessionSecret(text);
        break;

      case PREVENT_FRAME_ATTACKS:
        tobagoConfig.setPreventFrameAttacks(Boolean.parseBoolean(text));
        break;

      case DIRECTIVE:
        tobagoConfig.getContentSecurityPolicy().getDirectiveList().add(text);
        break;

      case MARKUP:
        currentRenderer.addSupportedMarkup(text);
        break;

      case DISPLAY_NAME:
        currentTheme.setDisplayName(text);
        break;

      case RESOURCE_PATH:
        currentTheme.setResourcePath(text);
        break;

      case FALLBACK:
        currentTheme.setFallbackName(text);
        break;

      case THEME_DEFINITION:
        currentTheme = null;
        break;

      case VERSIONED:
        currentTheme.setVersioned(Boolean.parseBoolean(text));
        break;

      case RESOURCES:
        production = null;
        break;

      case TOBAGO_CONFIG:
      case THEME_CONFIG:
      case ORDERING:
      case BEFORE:
      case AFTER:
      case SUPPORTED_MARKUP:
      case CONTENT_SECURITY_POLICY:
      case THEME_DEFINITIONS:
      case RENDERERS:
      case RENDERER:
      case SCRIPT:
      case STYLE:
        break;

      default:
        LOG.warn("Ignoring unknown end tag <" + qName + ">");
    }

    stack.pop();
  }

  @Override
  public void warning(SAXParseException e) throws SAXException {
    throw e;
  }

  @Override
  public void error(SAXParseException e) throws SAXException {
    throw e;
  }

  @Override
  public void fatalError(SAXParseException e) throws SAXException {
    throw e;
  }

  private void validate(final URL url, final TobagoConfigVersion version)
      throws URISyntaxException, SAXException, IOException {

    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    final Schema schema;
    if ("2.0".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_2_0));
    } else if ("1.6".equals(version.getVersion())) {
      LOG.warn("Using deprecated schema with version attribute 1.6 in file: '" + url + "'");
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_1_6));
    } else if ("1.5".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_1_5));
    } else {
      throw new SAXException("Using unknown version attribute '" + version.getVersion() + "' in file: '" + url + "'");
    }
    final Validator validator = schema.newValidator();
    final Source source = new StreamSource(url.openStream());

    validator.validate(source);
  }

}
