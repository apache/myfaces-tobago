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
import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
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
import java.lang.invoke.MethodHandles;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Stack;

public class TobagoConfigParser extends TobagoConfigEntityResolver {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final int TOBAGO_CONFIG = -1498874611;
  private static final int NAME = 3373707;
  private static final int ORDERING = 1234314708;
  private static final int BEFORE = -1392885889;
  private static final int AFTER = 92734940;
  private static final int THEME_CONFIG = 1930630086;
  private static final int DEFAULT_THEME = -114431171;
  private static final int SUPPORTED_THEME = -822303766;
  private static final int THEME_COOKIE = 1930664680;
  private static final int THEME_SESSION = 753861266;
  private static final int CREATE_SESSION_SECRET = 413906616;
  private static final int CHECK_SESSION_SECRET = 275994924;
  private static final int PREVENT_FRAME_ATTACKS = 270456726;
  private static final int SET_NOSNIFF_HEADER = -1238451304;
  private static final int CONTENT_SECURITY_POLICY = 1207440139;
  private static final int SECURITY_ANNOTATION = 1744426972;
  private static final int DIRECTIVE = -962590641;
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  private static final int RENDERERS = 1839650832;
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  private static final int RENDERER = -494845757;
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  private static final int SUPPORTED_MARKUP = 71904295;
  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  private static final int MARKUP = -1081305560;
  private static final int THEME_DEFINITIONS = -255617156;
  private static final int THEME_DEFINITION = 1515774935;
  private static final int DISPLAY_NAME = 1568910518;
  private static final int FALLBACK = 761243362;
  /**
   * @deprecated since 5.0.0
   */
  @Deprecated
  private static final int VERSIONED = -1407102089;
  private static final int VERSION = 351608024;
  private static final int RESOURCES = -1983070683;
  private static final int INCLUDES = 90259659;
  private static final int EXCLUDES = 1994055129;
  private static final int SANITIZER = 1807639849;
  private static final int SANITIZER_CLASS = -974266412;
  private static final int DECODE_LINE_FEED = -1764519240;
  private static final int ENABLE_TOBAGO_EXCEPTION_HANDLER = 1967055403;
  private static final int SCRIPT = -907685685;
  private static final int STYLE = 109780401;
  private static final int PROPERTIES = -926053069;
  private static final int ENTRY = 96667762;
  private static final int MIME_TYPES = 1081186720;
  private static final int MIME_TYPE = -242217677;
  private static final int EXTENSION = -612557761;
  private static final int TYPE = 3575610;

  private static final int TAGS = 3552281;
  private static final int TAG = 114586;
  private static final int ATTRIBUTE = 13085340;

  private static final String ATTR_MODE = "mode";
  private static final String ATTR_PRODUCTION = "production";
  private static final String ATTR_NAME = "name";
  private static final String ATTR_KEY = "key";
  private static final String ATTR_PRIORITY = "priority";
  private static final String ATTR_TYPE = "type";
  private static final String ATTR_DEFAULT = "default";

  private static final int MAX_PRIORITY = 65536;

  private TobagoConfigFragment tobagoConfig;
  private ThemeImpl currentTheme;
  private Boolean production;
  private boolean exclude;
  private StringBuilder buffer;
  private Properties properties;
  private String entryKey;
  private String directiveName;
  private String extension;
  private String type;
  private String tagName;

  private Stack<String> stack;

  public TobagoConfigParser() {
  }

  public TobagoConfigFragment parse(final URL url)
      throws IOException, SAXException, ParserConfigurationException, URISyntaxException {

    if (LOG.isInfoEnabled()) {
      LOG.info("Parsing configuration file: '{}'", url);
    }

    final TobagoConfigVersion version = new TobagoConfigVersion(url);

    // todo: Is there a solution that validate with both, DTD and XSD?

    if (version.isSchema()) {
      validate(url, version);
    }

    try (InputStream inputStream = url.openStream()) {
      final SAXParserFactory factory = SAXParserFactory.newInstance();
      if (!version.isSchema()) {
        factory.setValidating(true);
      }
      final SAXParser saxParser = factory.newSAXParser();
      saxParser.parse(inputStream, this);
    }
    return tobagoConfig;
  }

  @Override
  public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
    super.ignorableWhitespace(ch, start, length);
  }

  @Override
  public void startDocument() throws SAXException {

    buffer = new StringBuilder();
    stack = new Stack<>();
  }

  @Override
  public void endDocument() throws SAXException {
    assert stack.empty();
    stack = null;
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
      throws SAXException {

    // No unused content should be collected, specially text mixed with tags.
    assert buffer.toString().trim().length() == 0;

    buffer.setLength(0);
    stack.add(qName);

    switch (qName.hashCode()) {

      case TOBAGO_CONFIG:
        tobagoConfig = new TobagoConfigFragment();
        break;

      case CONTENT_SECURITY_POLICY:
        final String mode = attributes.getValue(ATTR_MODE);
        tobagoConfig.setContentSecurityPolicy(new ContentSecurityPolicy(mode));
        break;

      case THEME_DEFINITION:
        currentTheme = new ThemeImpl();
        tobagoConfig.addThemeDefinition(currentTheme);
        break;

      case RESOURCES:
        production = Boolean.parseBoolean(attributes.getValue(ATTR_PRODUCTION));
        break;

      case EXCLUDES:
        exclude = true;
        break;

      case SCRIPT:
        final ThemeScript script = new ThemeScript();
        script.setName(attributes.getValue(ATTR_NAME));
        script.setType(attributes.getValue(ATTR_TYPE));
        final String scriptPriority = attributes.getValue(ATTR_PRIORITY);
        script.setPriority(scriptPriority != null ? Integer.parseUnsignedInt(scriptPriority) : MAX_PRIORITY);
        if (production) {
          if (exclude) {
            currentTheme.getProductionResources().addExcludeScript(script);
          } else {
            currentTheme.getProductionResources().addIncludeScript(script);
          }
        } else {
          if (exclude) {
            currentTheme.getDevelopmentResources().addExcludeScript(script);
          } else {
            currentTheme.getDevelopmentResources().addIncludeScript(script);
          }
        }
        break;

      case STYLE:
        final ThemeStyle style = new ThemeStyle();
        style.setName(attributes.getValue(ATTR_NAME));
        final String stylePriority = attributes.getValue(ATTR_PRIORITY);
        style.setPriority(stylePriority != null ? Integer.parseUnsignedInt(stylePriority) : MAX_PRIORITY);
        if (production) {
          if (exclude) {
            currentTheme.getProductionResources().addExcludeStyle(style);
          } else {
            currentTheme.getProductionResources().addIncludeStyle(style);
          }
        } else {
          if (exclude) {
            currentTheme.getDevelopmentResources().addExcludeStyle(style);
          } else {
            currentTheme.getDevelopmentResources().addIncludeStyle(style);
          }
        }
        break;

      case PROPERTIES:
        properties = new Properties();
        break;

      case ENTRY:
        entryKey = attributes.getValue(ATTR_KEY);
        break;

      case DIRECTIVE:
        directiveName = attributes.getValue(ATTR_NAME);
        break;

      case TAG:
        tagName = attributes.getValue(ATTR_NAME);
        break;

      case ATTRIBUTE:
        currentTheme.addTagDefault(tagName, attributes.getValue(ATTR_NAME), attributes.getValue(ATTR_DEFAULT));
        break;

      case NAME:
      case ORDERING:
      case BEFORE:
      case AFTER:
      case THEME_CONFIG:
      case DEFAULT_THEME:
      case SUPPORTED_THEME:
      case THEME_COOKIE:
      case THEME_SESSION:
      case SUPPORTED_MARKUP:
      case MARKUP:
      case CREATE_SESSION_SECRET:
      case CHECK_SESSION_SECRET:
      case SECURITY_ANNOTATION:
      case PREVENT_FRAME_ATTACKS:
      case SET_NOSNIFF_HEADER:
      case THEME_DEFINITIONS:
      case DISPLAY_NAME:
      case VERSION:
      case VERSIONED:
      case FALLBACK:
      case SANITIZER:
      case SANITIZER_CLASS:
      case MIME_TYPES:
      case MIME_TYPE:
      case EXTENSION:
      case TYPE:
      case RENDERERS:
      case RENDERER:
      case INCLUDES:
      case TAGS:
        // nothing to do
        break;

      default:
        LOG.warn("Ignoring unknown start tag <" + qName + "> with hashCode=" + qName.hashCode());
    }
  }

  @Override
  public void characters(final char[] ch, final int start, final int length) throws SAXException {
    buffer.append(ch, start, length);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName) throws SAXException {
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

          case THEME_DEFINITION:
            currentTheme.setName(text);
            break;

          case RENDERER:
            // nothing to do
            break;

          default:
            LOG.warn("Ignoring unknown parent <" + parent + "> of tag <name>");
        }
        break;

      case DEFAULT_THEME:
        tobagoConfig.setDefaultThemeName(text);
        break;

      case SUPPORTED_THEME:
        tobagoConfig.addSupportedThemeName(text);
        break;

      case THEME_COOKIE:
        tobagoConfig.setThemeCookie(text);
        break;

      case THEME_SESSION:
        tobagoConfig.setThemeSession(text);
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

      case SET_NOSNIFF_HEADER:
        tobagoConfig.setSetNosniffHeader(Boolean.parseBoolean(text));
        break;

      case SECURITY_ANNOTATION:
        tobagoConfig.setSecurityAnnotation(SecurityAnnotation.valueOf(text));
        break;

      case DIRECTIVE:
        if (directiveName == null) { // before Tobago 4.0
          final int i = text.indexOf(' ');
          if (i < 1) {
            throw new TobagoConfigurationException("CSP directive can't be parsed!");
          }
          tobagoConfig.getContentSecurityPolicy().addDirective(text.substring(0, i), text.substring(i + 1));
        } else {
          tobagoConfig.getContentSecurityPolicy().addDirective(directiveName, text);
        }
        directiveName = null;
        break;

      case DISPLAY_NAME:
        currentTheme.setDisplayName(text);
        break;

      case FALLBACK:
        currentTheme.setFallbackName(text);
        break;

      case THEME_DEFINITION:
        currentTheme = null;
        break;

      case VERSION:
        currentTheme.setVersion(text);
        break;

      case RESOURCES:
        production = null;
        break;

      case EXCLUDES:
        exclude = false;
        break;

      case SANITIZER_CLASS:
        tobagoConfig.setSanitizerClass(text);
        break;

      case SANITIZER:
        if (properties != null) {
          tobagoConfig.setSanitizerProperties(properties);
          if (properties.get("whitelist") != null) {
            LOG.warn("<sanitizer><properties><entry key=\"whitelist\"> is deprecated:"
                + " use <sanitizer><properties><entry key=\"safelist\"> instead.");
          }
        }
        properties = null;
        break;

      case DECODE_LINE_FEED:
        tobagoConfig.setDecodeLineFeed(Boolean.parseBoolean(text));
        break;

      case ENABLE_TOBAGO_EXCEPTION_HANDLER:
        tobagoConfig.setEnableTobagoExceptionHandler(Boolean.parseBoolean(text));
        break;

      case ENTRY:
        properties.setProperty(entryKey, text);
        entryKey = null;
        break;

      case EXTENSION:
        extension = text;
        break;

      case TYPE:
        type = text;
        break;

      case MIME_TYPE:
        tobagoConfig.addMimeType(extension, type);
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
      case PROPERTIES:
      case MIME_TYPES:
      case MARKUP:
      case INCLUDES:
      case VERSIONED:
      case ATTRIBUTE:
      case TAG:
      case TAGS:
        // nothing to do
        break;

      default:
        LOG.warn("Ignoring unknown end tag <" + qName + ">");
    }

    stack.pop();
  }

  @Override
  public void warning(final SAXParseException e) throws SAXException {
    throw e;
  }

  @Override
  public void error(final SAXParseException e) throws SAXException {
    throw e;
  }

  @Override
  public void fatalError(final SAXParseException e) throws SAXException {
    throw e;
  }

  private void validate(final URL url, final TobagoConfigVersion version)
      throws URISyntaxException, SAXException, IOException {

    final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    final Schema schema;
    if ("5.1".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_5_1));
    } else if ("5.0".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_5_0));
    } else if ("4.0".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_4_0));
    } else if ("3.0".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_3_0));
    } else if ("2.0.6".equals(version.getVersion())) {
      schema = schemaFactory.newSchema(getClass().getResource(TOBAGO_CONFIG_XSD_2_0_6));
    } else if ("2.0".equals(version.getVersion())) {
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
