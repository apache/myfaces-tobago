package org.apache.myfaces.tobago.context;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.digester.Digester;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import javax.faces.FacesException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * User: lofwyr
 * Date: 14.02.2006 20:34:41
 *
 * @since 1.0.7
 */
class ThemeParser {

  private static final Log LOG = LogFactory.getLog(ThemeParser.class);

  private Digester digester;

  ThemeParser() {
    digester = new Digester();
    configure();
  }

  private Digester configure() {

// todo   digester.setValidating(true);
    digester.setValidating(false);

    digester.addCallMethod("tobago-theme/name", "setName", 0);
    digester.addCallMethod("tobago-theme/deprecated-name", "setDeprecatedName", 0);
    digester.addCallMethod("tobago-theme/resource-path", "setResourcePath", 0);
    digester.addCallMethod("tobago-theme/fallback", "setFallbackName", 0);
    digester.addObjectCreate("tobago-theme/supported-markup", MarkupConfigImpl.class);
    digester.addSetNext("tobago-theme/supported-markup", "setMarkupConfig");
    digester.addObjectCreate("tobago-theme/supported-markup/renderer",  RendererMarkup.class);
    digester.addSetNext("tobago-theme/supported-markup/renderer", "addRenderer");
    digester.addCallMethod("tobago-theme/supported-markup/renderer/name", "setName", 0);
    digester.addCallMethod("tobago-theme/supported-markup/renderer/markup", "addMarkup" , 0);

    return digester;
  }

  public ThemeImpl parse(URL url)
      throws IOException, SAXException, FacesException {

    InputStream inputStream = null;
    try {
      inputStream = url.openStream();
      ThemeImpl theme = new ThemeImpl();
      digester.push(theme);
      digester.parse(inputStream);
      if (LOG.isInfoEnabled()) {
        LOG.info("Found theme: '" + theme.getName() + "'");
      }
      return theme;
    } finally {
      IOUtils.closeQuietly(inputStream);
    }
  }

}
