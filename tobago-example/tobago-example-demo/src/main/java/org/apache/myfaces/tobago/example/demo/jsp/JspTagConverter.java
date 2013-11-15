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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.util.XmlUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

import java.util.HashMap;
import java.util.Map;

public class JspTagConverter extends AbstractConverter {

  private static final Logger LOG = LoggerFactory.getLogger(JspTagConverter.class);

  private TagConverter tagConverter = new TagConverter();
  private Map<String, String> tags = new HashMap<String, String>();

  public Pattern initPattern() throws MalformedPatternException {
//    return compiler.compile("(?s)<%.*?%>");
//    return compiler.compile("(?s)<%(--)?.*?\\1%>");
    return getCompiler().compile("(?s)(<%--.*?--%>)|(<%.*?%>)");
  }

  public String highlightJavaKeyword(final String java) {
    return getUtil().substitute("s/(\\bassert\\b|break\\b|\\bbyte\\b|\\bboolean\\b"
        + "|\\bcatch\\b|\\bcase\\b|\\bchar\\b|\\bcontinue\\b|\\bdouble\\b"
        + "|\\bdo\\b|\\belse\\b|\\bextends\\b|\\bfalse\\b|\\bfinal\\b"
        + "|\\bfloat\\b|\\bfor\\b|\\bfinally\\b|\\bif\\b|\\bimplements\\b"
        + "|\\bint\\b|\\binterface\\b|\\binstanceof\\b|\\blong\\b|\\blength\\b"
        + "|\\bnew\\b|\\bnull\\b|\\bprivate\\b|\\bprotected\\b|\\bpublic\\b"
        + "|\\breturn\\b|\\bswitch\\b|\\bsynchronized\\b|\\bshort\\b"
        + "|\\bstatic\\b|\\bsuper\\b|\\btry\\b|\\btrue\\b|\\bthis\\b"
        + "|\\bthrow\\b|\\bthrows\\b|\\bvoid\\b|\\bwhile\\b)"
        + "/<span class=\"keyword\">$1<\\/span>/g", java);
  }

  public String convertMatch(final String fragment) {
    final String key = "tag" + tags.size();;
    String tag = XmlUtils.escape(fragment, false);
    if (fragment.startsWith("<%--")) {
      tag = "<span class=\"jsp-comment\">" + tag + "</span>";
    } else if (fragment.startsWith("<%@")) {
      tag = "<span class=\"jsp-directive\">" + tag + "</span>";
    } else if (fragment.startsWith("<%!")) {
      // XXX temporarily hide strings to avoid keyword highlighting in strings
      tag = highlightStrings(tag);
      tag = highlightJavaKeyword(tag);
      tag = "<span class=\"jsp-declaration\">" + tag + "</span>";
    } else if (fragment.startsWith("<%=")) {
      tag = highlightStrings(tag);
      tag = "<span class=\"jsp-scriptlet\">" + tag + "</span>";
    } else if (fragment.startsWith("<%")) {
      // XXX temporarily hide strings to avoid keyword highlighting in strings
      tag = highlightStrings(tag);
      tag = highlightJavaKeyword(tag);
      tag = "<span class=\"jsp-tag\">" + tag + "</span>";
    } else {
      LOG.error("error: " + fragment);
    }
    tags.put(key, tag);
    return "${" + key + "}";
  }

  public String convert(final String input) {
    String result; // = StringUtils.replace(input, "$", "$$");
    result = super.convert(input);
    result = tagConverter.convert(result);
    final StringExpression stringExpression = new StringExpression(result);
    return stringExpression.substitute(tags);
  }

}
