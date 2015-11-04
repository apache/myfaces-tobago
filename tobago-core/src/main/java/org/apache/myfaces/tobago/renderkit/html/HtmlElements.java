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

package org.apache.myfaces.tobago.renderkit.html;

import java.util.Arrays;

public enum HtmlElements {

  A("a", Qualifier.INLINE),
  ABBR("abbr", Qualifier.INLINE),
  ADDRESS("address"),
  AREA("area", Qualifier.VOID),
  ARTICLE("article"),
  ASIDE("aside"),
  AUDIO("audio"),
  B("b", Qualifier.INLINE),
  BASE("base", Qualifier.VOID),
  BDI("bdi"),
  BDO("bdo"),
  BLOCKQUOTE("blockquote"),
  BODY("body"),
  BR("br", Qualifier.VOID),
  BUTTON("button", Qualifier.INLINE),
  CANVAS("canvas"),
  CAPTION("caption"),
  CITE("cite", Qualifier.INLINE),
  CODE("code"),
  COL("col", Qualifier.VOID),
  COLGROUP("colgroup"),
  COMMAND("command", Qualifier.VOID),
  DATALIST("datalist"),
  DD("dd"),
  DEL("del"),
  DETAILS("details"),
  DFN("dfn"),
  DIV("div"),
  DL("dl"),
  DT("dt"),
  EM("em", Qualifier.INLINE),
  EMBED("embed", Qualifier.VOID),
  FIELDSET("fieldset"),
  FIGCAPTION("figcaption"),
  FIGURE("figure"),
  FOOTER("footer"),
  FORM("form"),
  H1("h1"),
  H2("h2"),
  H3("h3"),
  H4("h4"),
  H5("h5"),
  H6("h6"),
  HEAD("head"),
  HEADER("header"),
  HGROUP("hgroup"),
  HR("hr", Qualifier.VOID),
  HTML("html"),
  I("i", Qualifier.INLINE),
  IFRAME("iframe"),
  IMG("img", Qualifier.VOID, Qualifier.INLINE),
  INPUT("input", Qualifier.VOID, Qualifier.INLINE),
  INS("ins"),
  KBD("kbd"),
  KEYGEN("keygen", Qualifier.VOID),
  LABEL("label", Qualifier.INLINE),
  LEGEND("legend"),
  LI("li"),
  LINK("link", Qualifier.VOID),
  MAP("map"),
  MARK("mark"),
  MENU("menu"),
  META("meta", Qualifier.VOID),
  METER("meter"),
  NAV("nav"),
  NOSCRIPT("noscript"),
  OBJECT("object"),
  OL("ol"),
  OPTGROUP("optgroup"),
  OPTION("option"),
  P("p"),
  PARAM("param", Qualifier.VOID),
  PRE("pre"),
  PRODRESS("prodress"),
  Q("q"),
  RP("rp"),
  RT("rt"),
  RUBY("ruby"),
  S("s"),
  SAMP("samp"),
  SCRIPT("script", Qualifier.INLINE),
  SECTION("section"),
  SELECT("select", Qualifier.INLINE),
  SMALL("small"),
  SOURCE("source", Qualifier.VOID),
  SPAN("span", Qualifier.INLINE),
  STRONG("strong"),
  STYLE("style"),
  SUB("sub", Qualifier.INLINE),
  SUMMARY("summary"),
  SUP("sup", Qualifier.INLINE),
  TABLE("table"),
  TBODY("tbody"),
  TD("td"),
  TEXTAREA("textarea", Qualifier.INLINE),
  TFOOT("tfoot"),
  TH("th"),
  THEAD("thead"),
  TIME("time"),
  TITLE("title"),
  TR("tr"),
  TRACK("track", Qualifier.VOID),
  U("u", Qualifier.INLINE),
  UL("ul"),
  VAR("var"),
  VIDEO("video"),
  WBR("wbr", Qualifier.VOID);

  private final String value;
  private final boolean voidElement;
  private final boolean inlineElement;

  HtmlElements(String value, Qualifier... qualifiers) {
    this.value = value;
    this.voidElement = Arrays.asList(qualifiers).contains(Qualifier.VOID);
    this.inlineElement = Arrays.asList(qualifiers).contains(Qualifier.INLINE);
  }

  public String getValue() {
    return value;
  }

  /**
   * A void HTML elements is an element whose content model never allows it to have contents under any circumstances.
   * See <a href="http://www.w3.org/TR/html-markup/syntax.html#void-element">
   *   http://www.w3.org/TR/html-markup/syntax.html#void-element</a>
   */
  public boolean isVoid() {
    return voidElement;
  }

  public boolean isInline() {
    return inlineElement;
  }

  private enum Qualifier {VOID, INLINE}
}
