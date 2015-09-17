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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * XXX preliminary
 */
public class CustomClass implements CssItem {

  private static final Logger LOG = LoggerFactory.getLogger(CustomClass.class);

  private static final Pattern CSS_CLASS_PATTERN = Pattern.compile("[\\w-]+");

  private final String[] classes;

  private CustomClass(List<String> list) {
    classes = list.toArray(new String[list.size()]);
  }

  public static CustomClass valueOf(String text) {

    final StringTokenizer tokenizer = new StringTokenizer(text, " ");
    final List<String> result = new ArrayList<String>(tokenizer.countTokens());
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      final Matcher matcher = CSS_CLASS_PATTERN.matcher(token);
      if (matcher.matches()) {
        if (!result.contains(token)) {
          result.add(token);
        } else {
          LOG.warn("Duplicate CSS class name: '{}' which is part of '{}'", token, text);
        }
      } else {
        LOG.error("Invalid CSS class name: '{}' which is part of '{}'", token, text);
      }
    }
    if (result.size() > 0) {
      return new CustomClass(result);
    } else {
      return null;
    }

  }

  @Override
  public String getName() {
    switch (classes.length) {
      case 0:
        return "";
      case 1:
        return classes[0];
      default:
        final StringBuilder builder = new StringBuilder(classes[0]);
        for (int i = 1; i < classes.length; i++) {
          builder.append(' ');
          builder.append(classes[i]);
        }
        return builder.toString();
    }
  }
}
