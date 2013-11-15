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

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

public class TagConverter extends AbstractConverter {

  public Pattern initPattern() throws MalformedPatternException {
    return getCompiler().compile("(?s)<.*?>");
  }

  public String convertMatch(final String fragment) {
    // String escaped = XmlUtils.escape(fragment);

    final String withLinks = getUtil().substitute("s/^<jsp:include page=\\\"([\\-\\.\\/\\w]+)"
            + "/<jsp:include page=\\\"<a href='viewSource.jsp?jsp=$1'>$1<\\/a>/", fragment);

    String escaped = getUtil().substitute("s/^(<\\/?)(\\w+):/$1<b>$2<\\/b>:/", withLinks);
    escaped = highlightStrings(escaped);
    return "<span class=\"html-tag\">&lt;" + escaped.substring(1, escaped.length()-1) + "></span>";
  }

}
