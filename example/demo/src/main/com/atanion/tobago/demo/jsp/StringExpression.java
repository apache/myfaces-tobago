/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: Apr 10, 2002, 11:04:12 PM
 * $Id: StringExpression.java,v 1.1.1.1 2004/04/15 18:40:59 idus Exp $
 */
package com.atanion.tobago.demo.jsp;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class StringExpression implements Serializable {

  private static final Log LOG = LogFactory.getLog(StringExpression.class);

  private static final long serialVersionUID = -152894508593519474L;

  private String string;

  public StringExpression(String string) {
    this.string = string;
  }

  public String toString() {
    return string;
  }

  public String substitute(Map<String,String> variables) {
    return replaceVariables(string, variables);
  }

  // implementation copied from ant.ProjectHelper
  private static String replaceVariables(
      String stringExpression, Map<String,String> variables) {
    if (stringExpression == null) {
      return null;
    }

    Vector<String> fragments = new Vector<String>();
    Vector<String> propertyRefs = new Vector<String>();
    parsePropertyString(stringExpression, fragments, propertyRefs);

    StringBuffer sb = new StringBuffer();
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

  public static void main(String[] args) {
    Map<String,String> env = new HashMap<String, String>();
    env.put("v1", "var1");
    env.put("v2", "var2");
    String expression = "bla${v1}blup${v2}";
    System.out.println(expression + "->" + replaceVariables(expression, env));
    System.out.println(replaceVariables("bla$${v1}blup${v2}", env));
    System.out.println(replaceVariables("bla$$", env));
    System.out.println(replaceVariables("bla$$$", env));
    System.out.println(replaceVariables("bla$$$$", env));
    System.out.println(replaceVariables("bla${v1}blup${v3}", env));
    System.out.println(replaceVariables("bla${v1}blup${v3", env));
  }
}
