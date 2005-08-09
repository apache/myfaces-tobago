/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created on: 02.09.2002, 23:33:19
 * $Id: AbstractConverter.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */
package com.atanion.tobago.demo.jsp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public abstract class AbstractConverter implements Converter {

  private static final Log log = LogFactory.getLog(AbstractConverter.class);

  private Pattern pattern;

  protected Perl5Matcher matcher = new Perl5Matcher();
  protected Perl5Compiler compiler = new Perl5Compiler();
  protected Perl5Util util = new Perl5Util();

  public abstract Pattern initPattern() throws MalformedPatternException;

  public Pattern getPattern() {
    if (pattern == null) {
      try {
        pattern = initPattern();
      } catch (MalformedPatternException e) {
        log.error("", e);
      }
    }
    return pattern;
  }

  private String getFragment(String data, int start, int end) {
    return data.substring(start, end);
  }

  protected String convertMisc(String data, int start, int end) {
    return convertMisc(getFragment(data, start, end));
  }

  protected String convertMatch(String data, int start, int end) {
    return convertMatch(getFragment(data, start, end));
  }

  public String convertMisc(String fragment) {
    return fragment;
  }

  public String convert(String input) {
    StringBuffer buffer = new StringBuffer();
    int lastStart = 0;
    PatternMatcherInput patternMatcherInput = new PatternMatcherInput(input);
    Pattern pattern = getPattern();
    if (matcher.contains(patternMatcherInput, pattern)) {
      do {
        MatchResult result = matcher.getMatch();
        int start = result.beginOffset(0);
        int end = result.endOffset(0);
        buffer.append(convertMisc(input, lastStart, start));
        buffer.append(convertMatch(input, start, end));
        lastStart = end;
      } while (matcher.contains(patternMatcherInput, pattern));
    }
    buffer.append(convertMisc(input, lastStart, input.length()));
    return buffer.toString();
  }

  public String highlightStrings(String input) {
    return util.substitute("s/(\".*?\")/<span class=\"string\">$1<\\/span>/g", input);
  }

}
