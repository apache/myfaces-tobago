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

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Since Tobago 3.0.0
 */
public class CustomClass implements CssItem {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Pattern CSS_CLASS_PATTERN = Pattern.compile("[\\w-]+");

  private String name;
  private ValueExpression valueExpression;

  public CustomClass(final String name) {
    this.name = name;
  }

  public CustomClass(final ValueExpression valueExpression) {
    this.valueExpression = valueExpression;
  }

  @Override
  public String getName() {
    final String string;
    if (name != null) {
      string = name;
    } else if (valueExpression != null) {
      final FacesContext facesContext = FacesContext.getCurrentInstance();
      final ELContext elContext = facesContext.getELContext();
      final Object valueExpressionValue = valueExpression.getValue(elContext);
      if (valueExpressionValue != null) {
        string = valueExpressionValue.toString();
      } else {
        return "";
      }
    } else {
      return "";
    }

    final StringTokenizer tokenizer = new StringTokenizer(string, " ");
    final StringBuilder result = new StringBuilder();
    boolean first = true;
    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken();
      final Matcher matcher = CSS_CLASS_PATTERN.matcher(token);
      if (matcher.matches()) {
        if (!first) {
          result.append(' ');
        }
        result.append(token);
        first = false;
      } else {
        LOG.error("Invalid CSS class name: '{}' which is part of '{}'", token, string);
      }
    }
    return result.toString();
  }

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  public static CustomClass valueOf(final String text) {
    return new CustomClass(text);
  }
}
