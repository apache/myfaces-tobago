/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created on: 02.09.2002, 23:40:26
 * $Id: TagConverter.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */
package com.atanion.tobago.demo.jsp;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;

public class TagConverter extends AbstractConverter {

  public Pattern initPattern() throws MalformedPatternException {
    return compiler.compile("(?s)<.*?>");
  }

  public String convertMatch(String fragment) {
    // String escaped = XmlUtils.escape(fragment);

    String withLinks = util.substitute(
        "s/^<jsp:include page=\\\"([\\-\\.\\/\\w]+)" +
        "/<jsp:include page=\\\"<a href='viewSource.jsp?jsp=$1'>$1<\\/a>/", fragment);

    String escaped = util.substitute("s/^(<\\/?)(\\w+):/$1<b>$2<\\/b>:/", withLinks);
    escaped = highlightStrings(escaped);
    return "<span class=\"html-tag\">&lt;" + escaped.substring(1, escaped.length()-1) + "></span>";
  }

}
