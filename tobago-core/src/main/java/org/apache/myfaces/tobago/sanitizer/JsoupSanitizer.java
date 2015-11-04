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

package org.apache.myfaces.tobago.sanitizer;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * The JsoupSanitizer uses the jsoup library http://jsoup.org/ to check against malicious code.
 */
public class JsoupSanitizer implements Sanitizer {

  private static final Logger LOG = LoggerFactory.getLogger(JsoupSanitizer.class);

  private Whitelist whitelist;
  private String whitelistName;

  private boolean unmodifiable = false;

  public String sanitize(final String html) {

    final String safe = Jsoup.clean(html, whitelist);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Sanitized: " + safe);
    }
    return safe;
  }

  public void setProperties(final Properties configuration) {
    checkLocked();

    unmodifiable = true;

    for (final String key : configuration.stringPropertyNames()) {
      if ("whitelist".equals(key)) {
        whitelistName = configuration.getProperty(key);
        if ("basic".equals(whitelistName)) {
          whitelist = Whitelist.basic();
        } else if ("basicWithImages".equals(whitelistName)) {
          whitelist = Whitelist.basicWithImages();
        } else if ("none".equals(whitelistName)) {
          whitelist = Whitelist.none();
        } else if ("relaxed".equals(whitelistName)) {
          whitelist = Whitelist.relaxed();
        } else if ("simpleText".equals(whitelistName)) {
          whitelist = Whitelist.simpleText();
        } else {
          throw new RuntimeException(
              "Unknown configuration value for 'whitelist' in tobago-config.xml found! value='" + whitelistName + "'");
        }
      } else {
        throw new RuntimeException("Unknown configuration key in tobago-config.xml found! key='" + key + "'");
      }
    }

    if (LOG.isInfoEnabled()) {
      LOG.warn("Using whitelist '" + whitelistName + "' for sanitizing!");
    }
  }

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new RuntimeException("The configuration must not be changed after initialization!");
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " whitelist='" + whitelistName + "'";
  }

}
