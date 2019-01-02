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

package org.apache.myfaces.tobago.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MarginTokens {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private List<Margin> margins = new ArrayList<>();

  public int getSize() {
    return margins.size();
  }

  public Margin get(final int index) {
    return margins.get(index);
  }

  public void addMargin(final Margin token) {
    margins.add(token);
  }

  public List<Margin> getMargins() {
    return margins;
  }

  public static MarginTokens parse(final String tokens) {
    if (tokens == null) {
      return null;
    }
    final MarginTokens marginTokens = new MarginTokens();
    final StringTokenizer tokenizer = new StringTokenizer(tokens, "; ");

    while (tokenizer.hasMoreTokens()) {
      final String token = tokenizer.nextToken().trim();
      marginTokens.addMargin(parseToken(token));
    }
    return marginTokens;
  }

  public static Margin parseToken(final String token) {
    for (final Margin margin : Margin.values()) {
      if (margin.name().equals(token)) {
        return margin;
      }
    }
    LOG.error("Error parsing layout token '" + token + "'! Using 'none' instead.");
    return Margin.none;
  }
}
