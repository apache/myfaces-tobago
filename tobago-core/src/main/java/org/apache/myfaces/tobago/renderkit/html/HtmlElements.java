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

public enum HtmlElements {

  A("a"),
  ABBR("abbr"),
  ADDRESS("address"),
  AREA("area", true),
  ARTICLE("article"),
  ASIDE("aside"),
  AUDIO("audio"),
  B("b"),
  BASE("base", true),
  BDI("bdi"),
  BDO("bdo"),
  BLOCKQUOTE("blockquote"),
  BODY("body"),
  BR("br", true),
  BUTTON("button"),
  CANVAS("canvas"),
  CAPTION("caption"),
  CITE("cite"),
  CODE("code"),
  COL("col", true),
  COLGROUP("colgroup"),
  COMMAND("command", true),
  DATALIST("datalist"),
  DD("dd"),
  DEL("del"),
  DETAILS("details"),
  DFN("dfn"),
  DIV("div"),
  DL("dl"),
  DT("dt"),
  EM("em"),
  EMBED("embed", true),
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
  HR("hr", true),
  HTML("html"),
  I("i"),
  IFRAME("iframe"),
  IMG("img", true),
  INPUT("input", true),
  INS("ins"),
  KBD("kbd"),
  KEYGEN("keygen", true),
  LABEL("label"),
  LEGEND("legend"),
  LI("li"),
  LINK("link", true),
  MAP("map"),
  MARK("mark"),
  MENU("menu"),
  META("meta", true),
  METER("meter"),
  NAV("nav"),
  NOSCRIPT("noscript"),
  OBJECT("object"),
  OL("ol"),
  OPTGROUP("optgroup"),
  OPTION("option"),
  P("p"),
  PARAM("param", true),
  PRE("pre"),
  PRODRESS("prodress"),
  Q("q"),
  RP("rp"),
  RT("rt"),
  RUBY("ruby"),
  S("s"),
  SAMP("samp"),
  SCRIPT("script"),
  SECTION("section"),
  SELECT("select"),
  SMALL("small"),
  SOURCE("source", true),
  SPAN("span"),
  STRONG("strong"),
  STYLE("style"),
  SUB("sub"),
  SUMMARY("summary"),
  SUP("sup"),
  TABLE("table"),
  TBODY("tbody"),
  TD("td"),
  TEXTAREA("textarea"),
  TFOOT("tfoot"),
  TH("th"),
  THEAD("thead"),
  TIME("time"),
  TITLE("title"),
  TR("tr"),
  TRACK("track", true),
  U("u"),
  UL("ul"),
  VAR("var"),
  VIDEO("video"),
  WBR("wbr", true);

  private final String value;
  private final boolean voidElement;

  HtmlElements(String value) {
    this(value, false);
  }

  HtmlElements(String value, boolean voidElement) {
    this.value = value;
    this.voidElement = voidElement;
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
}
