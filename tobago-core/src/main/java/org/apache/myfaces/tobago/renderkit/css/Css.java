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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @since 3.0.0
 */
public class Css {

  private static final Logger LOG = LoggerFactory.getLogger(Css.class);

  private final Set<String> list = new HashSet<String>();

  public Css() {
  }

  public void add(String... cssList) {
    for (String css : cssList) {
      // todo: check for forbidden letters: regexp
      if (css.contains(" ")) {
        LOG.warn("Not a valid css class name: " + css);
      }
      list.add(css);
    }
  }

/* todo
  public void add(BootstrapClass... cssList) {
    for (String css : cssList) {
      // todo: check for forbidden letters: regexp
      if (css.contains(" ")) {
        LOG.warn("Not a valid css class name: " + css);
      }
      list.add(css);
    }
  }
*/

  public static Css valueOf(final String string) {

    final StringTokenizer tokenizer = new StringTokenizer(string, " ");
    final Css css = new Css();
    while (tokenizer.hasMoreTokens()) {
      css.add(tokenizer.nextToken());
    }
    return css;
  }

  public static Css valueOf(final Object object) {

    if (object == null) {
      return new Css();
    } else if (object instanceof String) {
      return valueOf((String) object);
    } else {
      return valueOf(object.toString());
    }
  }

  public String encode() {
    return StringUtils.join(new ArrayList<String>(list), ' ');
  }

  public String toString() {
    return encode();
  }

}
