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

import java.util.regex.Pattern;

/**
 * This is a list of used font-awesome icons in Tobago. Feel free to extend.
 */
public enum Icons implements CssItem {

  ANGLE_DOUBLE_LEFT,
  ANGLE_DOUBLE_RIGHT,
  ANGLE_DOWN,
  ANGLE_LEFT,
  ANGLE_RIGHT,
  ANGLE_UP,
  BACKWARD,
  BARS,
  CALENDAR,
  CLOCK_O,
  ELLIPSIS_H,
  EXCLAMATION,
  FOLDER_OPEN,
  FORWARD,
  MINUS_SQUARE_O,
  PLUS_SQUARE_O,
  QUESTION,
  SQUARE_O,
  STEP_BACKWARD,
  STEP_FORWARD;

  private static final Logger LOG = LoggerFactory.getLogger(Icons.class);

  public static final CssItem FA = new CssItem() {
    @Override
    public String getName() {
      return "fa";
    }
  };

  private static final Pattern PATTERN = Pattern.compile("^(fa(-[a-z]+)+)$");

  private String fa;

  Icons() {
    this.fa = "fa-" + name().toLowerCase().replaceAll("_", "-");
  }

  @Override
  public String getName() {
    return fa;
  }

  public static CssItem custom(final String name) {

    return new CssItem() {

      @Override
      public String getName() {
        if (PATTERN.matcher(name).matches()) {
          return name;
        } else {
          LOG.warn("Unknown Icon: '" + name + "'");
          return null;
        }
      }
    };
  }

}
