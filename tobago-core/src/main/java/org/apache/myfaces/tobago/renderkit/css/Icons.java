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

package org.apache.myfaces.tobago.renderkit.css;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.regex.Pattern;

/**
 * This is a list of used bootstrap icons in Tobago. Might be extended.
 */
public enum Icons implements CssItem {

  ARROW_DOWN,
  CHEVRON_DOUBLE_LEFT,
  CHEVRON_DOUBLE_RIGHT,
  CHEVRON_LEFT,
  CHEVRON_RIGHT,
  CARET_LEFT,
  CALENDAR3,
  CLOCK,
  THREE_DOTS,
  EXCLAMATION,
  EXCLAMATION_LG,
  FOLDER2_OPEN,
  CARET_RIGHT,
  DASH_SQUARE,
  PLUS_SQUARE,
  QUESTION,
  QUESTION_LG,
  SQUARE,
  SKIP_START,
  SKIP_END;

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Pattern PATTERN = Pattern.compile("^(bi|fa|(fas|far|fal|fad)\\sfa)-[\\w\\d-]+$");
  private static final Pattern PATTERN_BI = Pattern.compile("^bi-[\\w\\d-]+$");
  private static final Pattern PATTERN_FA = Pattern.compile("^fa-[\\w\\d-]+$");
  private static final Pattern PATTERN_FA5 = Pattern.compile("^(fas|far|fal|fad)\\sfa-[\\w\\d-]+$");

  private final String clazz;

  Icons() {
    this.clazz = "bi-" + name().toLowerCase().replaceAll("_", "-");
  }

  @Override
  public String getName() {
    return clazz;
  }

  public static boolean matches(final String value) {
    return value != null && PATTERN.matcher(value).matches();
  }

  public static CssItem custom(final String name) {

    return new CssItem() {

      @Override
      public String getName() {
        if (name == null) {
          return null;
        }
        if (PATTERN_BI.matcher(name).matches()) {
          return name;
        }
        if (PATTERN_FA.matcher(name).matches()) {
          return "fa " + name;
        }
        if (PATTERN_FA5.matcher(name).matches()) {
          return name;
        }
        LOG.warn("Unknown icon format for name: '" + name + "'");
        return null;
      }
    };
  }

}
