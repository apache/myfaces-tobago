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

package org.apache.myfaces.tobago.example.demo.formatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.oro.text.perl.Perl5Util;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

public abstract class AbstractConverter implements Converter {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractConverter.class);

  private Pattern pattern;

  private Perl5Matcher matcher = new Perl5Matcher();
  private Perl5Compiler compiler = new Perl5Compiler();
  private Perl5Util util = new Perl5Util();

  public abstract Pattern initPattern() throws MalformedPatternException;

  public Perl5Compiler getCompiler() {
    return compiler;
  }

  public Perl5Util getUtil() {
    return util;
  }

  public Pattern getPattern() {
    if (pattern == null) {
      try {
        pattern = initPattern();
      } catch (final MalformedPatternException e) {
        LOG.error("", e);
      }
    }
    return pattern;
  }

  private String getFragment(final String data, final int start, final int end) {
    return data.substring(start, end);
  }

  protected String convertMisc(final String data, final int start, final int end) {
    return convertMisc(getFragment(data, start, end));
  }

  protected String convertMatch(final String data, final int start, final int end) {
    return convertMatch(getFragment(data, start, end));
  }

  public String convertMisc(final String fragment) {
    return fragment;
  }

  public String convert(final String input) {
    final StringBuilder buffer = new StringBuilder();
    int lastStart = 0;
    final PatternMatcherInput patternMatcherInput = new PatternMatcherInput(input);
    final Pattern pattern = getPattern();
    if (matcher.contains(patternMatcherInput, pattern)) {
      do {
        final MatchResult result = matcher.getMatch();
        final int start = result.beginOffset(0);
        final int end = result.endOffset(0);
        buffer.append(convertMisc(input, lastStart, start));
        buffer.append(convertMatch(input, start, end));
        lastStart = end;
      } while (matcher.contains(patternMatcherInput, pattern));
    }
    buffer.append(convertMisc(input, lastStart, input.length()));
    return buffer.toString();
  }

  public String highlightStrings(final String input) {
    return util.substitute("s/(\".*?\")/<span class=\"string\">$1<\\/span>/g", input);
  }

}
