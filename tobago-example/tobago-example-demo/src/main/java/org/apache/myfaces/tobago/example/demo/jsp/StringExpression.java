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

package org.apache.myfaces.tobago.example.demo.jsp;

/*
 * Created on: Apr 10, 2002, 11:04:12 PM
 * $Id$
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;


public class StringExpression implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(StringExpression.class);

  private static final long serialVersionUID = -152894508593519474L;

  private String string;

  public StringExpression(String string) {
    this.string = string;
  }

  public String toString() {
    return string;
  }

  public String substitute(Map<String, String> variables) {
    return replaceVariables(string, variables);
  }

  // implementation copied from ant.ProjectHelper
  private static String replaceVariables(
      String stringExpression, Map<String, String> variables) {
    if (stringExpression == null) {
      return null;
    }

    Vector<String> fragments = new Vector<String>();
    Vector<String> propertyRefs = new Vector<String>();
    parsePropertyString(stringExpression, fragments, propertyRefs);

    StringBuilder sb = new StringBuilder();
    Enumeration<String> i = fragments.elements();
    Enumeration<String> j = propertyRefs.elements();
    while (i.hasMoreElements()) {
      String fragment = i.nextElement();
      if (fragment == null) {
        String propertyName = j.nextElement();
        if (!variables.containsKey(propertyName)) {
          // throw exception ?
          LOG.error("Property ${" + propertyName + "} has not been set");
        }
        fragment = (variables.containsKey(propertyName))
            ? variables.get(propertyName)
            : "${" + propertyName + "}";
      }
      sb.append(fragment);
    }
    return sb.toString();
  }

  private static void parsePropertyString(
      String value, Vector<String> fragments, Vector<String> propertyRefs) {
    int prev = 0;
    int pos;
    while ((pos = value.indexOf("$", prev)) >= 0) {
      if (pos > 0) {
        fragments.addElement(value.substring(prev, pos));
      }
      if (pos == (value.length() - 1)) {
        fragments.addElement("$");
        prev = pos + 1;
      } else if (value.charAt(pos + 1) != '{') {
        fragments.addElement(value.substring(pos + 1, pos + 2));
        prev = pos + 2;
      } else {
        int endName = value.indexOf('}', pos);
        if (endName < 0) {
          throw new IllegalArgumentException("Syntax error in property: " + value);
        }
        String propertyName = value.substring(pos + 2, endName);
        fragments.addElement(null);
        propertyRefs.addElement(propertyName);
        prev = endName + 1;
      }
    }
    if (prev < value.length()) {
      fragments.addElement(value.substring(prev));
    }
  }

}
