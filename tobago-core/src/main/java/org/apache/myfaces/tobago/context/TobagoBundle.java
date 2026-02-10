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

package org.apache.myfaces.tobago.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * This class works like the Java resource bundle mechanism for a named resource bundle
 * and adds the functionality of the tobago themes and also supports XML properties files.
 * This class should be extended and used in the faces-config.xml.
 *
 * Warning: The locale selection doesn't work correctly.
 *
 * @since 1.5.0
 * @deprecated since 4.3.0, use standard ResourceBundles (without XML) support please.
 */
@Deprecated
public class TobagoBundle extends ResourceBundle {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoBundle.class);

  private String bundleName;
  private ResourceBundle bundle;

  public TobagoBundle(final String bundleName) {
    this.bundleName = bundleName;
    this.bundle = ResourceBundle.getBundle(bundleName, new XmlTobagoBundle.XMLResourceBundleControl());
  }

  @Override
  protected Object handleGetObject(final String key) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Searching for '{}' in bundle '{}'", key, bundleName);
    }
    return bundle.getObject(key);
  }

  @Override
  public Enumeration<String> getKeys() {
    return Collections.enumeration(Collections.emptyList());
  }

  public String getBundleName() {
    return bundleName;
  }

  public static class XmlTobagoBundle extends ResourceBundle {

    private static final String XML = "xml";

    private Properties props;

    private XmlTobagoBundle(final InputStream stream) throws IOException {
      props = new Properties();
      props.loadFromXML(stream);
    }

    @Override
    protected Object handleGetObject(final String key) {
      return props.getProperty(key);
    }

    @Override
    public Enumeration<String> getKeys() {
      final Set<String> handleKeys = props.stringPropertyNames();
      return Collections.enumeration(handleKeys);
    }

    public static class XMLResourceBundleControl extends Control {

      @Override
      public List<String> getFormats(final String baseName) {
        return Collections.singletonList(XML);
      }

      @Override
      public ResourceBundle newBundle(
          final String baseName, final Locale locale, final String format, final ClassLoader loader,
          final boolean reload)
          throws IllegalAccessException, InstantiationException,
          IOException {

        if (baseName == null || locale == null || format == null || loader == null) {
          throw new NullPointerException();
        }
        ResourceBundle bundle = null;
        if (!format.equals(XML)) {
          return null;
        }

        final String bundleName = toBundleName(baseName, locale);
        final String resourceName = toResourceName(bundleName, format);
        final URL url = loader.getResource(resourceName);
        if (url == null) {
          return null;
        }
        final URLConnection connection = url.openConnection();
        if (connection == null) {
          return null;
        }
        if (reload) {
          connection.setUseCaches(false);
        }
        try (BufferedInputStream bis = new BufferedInputStream(connection.getInputStream())) {
          bundle = new XmlTobagoBundle(bis);
        }

        return bundle;
      }
    }
  }
}
