package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;

/*
 * Date: May 2, 2007
 * Time: 1:11:25 PM
 */
public class LayoutTokens {
  private static final Log LOG = LogFactory.getLog(LayoutTokens.class);

  private List<LayoutToken> tokens = new ArrayList<LayoutToken>();

  public int getSize() {
    return tokens.size();
  }

  public void set(int index, LayoutToken token) {
    tokens.set(index, token);
  }

  public boolean isEmpty() {
    return getSize() == 0;
  }

  public LayoutToken get(int index) {
    return tokens.get(index);
  }

  public void shrinkSizeTo(int size) {
    for (int i = getSize()-1; i >= size; i--) {
      tokens.remove(i);
    }
  }

  public void ensureSize(int size, LayoutToken token) {
    for (int index = getSize(); index < size; index++) {
      addToken(token);
    }
  }

  private void addToken(LayoutToken token) {
    tokens.add(token);
  }

  public static LayoutTokens parse(String[]  tokens) {
    LayoutTokens layoutTokens = new LayoutTokens();

    for (String token: tokens) {
      parseToken(token, layoutTokens);
    }
    return layoutTokens;
  }

  public static LayoutTokens parse(String tokens) {
    return parse(tokens, null);
  }



  public static LayoutTokens parse(String tokens, LayoutToken defaultToken) {
    LayoutTokens layoutTokens = new LayoutTokens();
    if (tokens == null) {
      layoutTokens.addToken(defaultToken);
      return layoutTokens;
    }
    StringTokenizer tokenizer = new StringTokenizer(tokens, ";");

    while (tokenizer.hasMoreTokens()) {
      String token = tokenizer.nextToken().trim();
      parseToken(token, layoutTokens);
    }
    return layoutTokens;
  }

  private static void parseToken(String token, LayoutTokens layoutTokens) {
    try {
    // TODO optimize me
      if ("*".equals(token)) {
        layoutTokens.addToken(new RelativeLayoutToken(1));
      } else if (token.equals("fixed")) {
        layoutTokens.addToken(new FixedLayoutToken());
      } else if (token.equals("minimum")) {
        layoutTokens.addToken(new MinimumLayoutToken());
      } else if (token.matches("\\d+px")) {
        layoutTokens.addToken(new PixelLayoutToken(Integer.parseInt(token.replaceAll("\\D", ""))));
      } else if (token.matches("^\\d+\\%")) {
        layoutTokens.addToken(new PercentLayoutToken(Integer.parseInt(token.replaceAll("\\D", ""))));
      } else if (token.matches("^\\d+\\*")) {
        layoutTokens.addToken(new RelativeLayoutToken(Integer.parseInt(token.replaceAll("\\D", ""))));
      } else {
        LOG.error("Unknown layout token " + token + " ignoring");
      }
    } catch (NumberFormatException e) {
      LOG.error("Error parsing layout token " + token, e);
    }
  }

  public String toString() {
    StringBuilder str = new StringBuilder();
    for (LayoutToken token: tokens) {
      str.append(token);
      str.append(";");
    }
    return str.toString();
  }


}

