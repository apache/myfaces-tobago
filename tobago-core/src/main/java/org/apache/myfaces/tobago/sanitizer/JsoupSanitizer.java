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

import org.apache.myfaces.tobago.exception.TobagoConfigurationException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

/**
 * The JsoupSanitizer uses the jsoup library http://jsoup.org/ to check against malicious code.
 */
public class JsoupSanitizer implements Sanitizer {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private Safelist safelist;
  private String safelistName;

  private boolean unmodifiable = false;

  @Override
  public String sanitize(final String html) {

    final String safe = Jsoup.clean(html, safelist);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Sanitized: " + safe);
    }
    return safe;
  }

  @Override
  public void setProperties(final Properties configuration) {
    checkLocked();

    unmodifiable = true;

    for (String key : configuration.stringPropertyNames()) {
      if ("whitelist".equals(key)) {
        key = "safelist";
      }
      if ("safelist".equals(key)) {
        safelistName = configuration.getProperty(key);
        if ("basic".equals(safelistName)) {
          safelist = Safelist.basic();
        } else if ("basicWithImages".equals(safelistName)) {
          safelist = Safelist.basicWithImages();
        } else if ("none".equals(safelistName)) {
          safelist = Safelist.none();
        } else if ("relaxed".equals(safelistName)) {
          safelist = Safelist.relaxed();
        } else if ("simpleText".equals(safelistName)) {
          safelist = Safelist.simpleText();
        } else {
          throw new TobagoConfigurationException(
              "Unknown configuration value for 'safelist' in tobago-config.xml found! value='" + safelistName + "'");
        }
      } else {
        throw new TobagoConfigurationException(
            "Unknown configuration key in tobago-config.xml found! key='" + key + "'");
      }
    }

    if (LOG.isInfoEnabled()) {
      LOG.warn("Using safelist '" + safelistName + "' for sanitizing!");
    }
  }

  private void checkLocked() throws IllegalStateException {
    if (unmodifiable) {
      throw new TobagoConfigurationException("The configuration must not be changed after initialization!");
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + " safelist='" + safelistName + "'";
  }

}
