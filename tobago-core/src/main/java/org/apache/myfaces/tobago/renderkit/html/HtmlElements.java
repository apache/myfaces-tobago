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
import java.util.HashSet;
import java.util.Set;

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
  CODE("code", Qualifier.INLINE),
  COL("col", Qualifier.VOID),
  COLGROUP("colgroup"),
  @Deprecated(since = "5.0.0", forRemoval = true)
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
  PRE("pre", Qualifier.INLINE),
  PROGRESS("progress"),
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
  WBR("wbr", Qualifier.VOID),

  TOBAGO_BADGE("tobago-badge"),
  TOBAGO_BAR("tobago-bar"),
  TOBAGO_BEHAVIOR("tobago-behavior", Qualifier.INLINE),
  TOBAGO_BUTTONS("tobago-buttons"),
  TOBAGO_BOX("tobago-box"),
  TOBAGO_CONFIG("tobago-config"),
  TOBAGO_COLUMN_SELECTOR("tobago-column-selector"),
  TOBAGO_DATE("tobago-date"),
  TOBAGO_DROPDOWN("tobago-dropdown"),
  TOBAGO_FILE("tobago-file"),
  TOBAGO_FLEX_LAYOUT("tobago-flex-layout"),
  TOBAGO_FLOW_LAYOUT("tobago-flow-layout"),
  TOBAGO_FOCUS("tobago-focus"),
  TOBAGO_FOOTER("tobago-footer"),
  TOBAGO_FORM("tobago-form"),
  TOBAGO_GRID_LAYOUT("tobago-grid-layout"),
  //  TOBAGO_LABEL("tobago-label"),
  TOBAGO_HEADER("tobago-header"),
  TOBAGO_IMAGE("tobago-image"),
  TOBAGO_IN("tobago-in"),
  TOBAGO_LINKS("tobago-links"),
  TOBAGO_MESSAGES("tobago-messages"),
  TOBAGO_OFFCANVAS("tobago-offcanvas"),
  TOBAGO_OUT("tobago-out", Qualifier.INLINE),
  TOBAGO_PAGE("tobago-page"),
  TOBAGO_PAGINATOR_LIST("tobago-paginator-list"),
  TOBAGO_PAGINATOR_PAGE("tobago-paginator-page"),
  TOBAGO_PAGINATOR_PANEL("tobago-paginator-panel"),
  TOBAGO_PAGINATOR_ROW("tobago-paginator-row"),
  TOBAGO_PANEL("tobago-panel"),
  TOBAGO_POPOVER("tobago-popover"),
  TOBAGO_POPUP("tobago-popup"),
  TOBAGO_PROGRESS("tobago-progress"),
  TOBAGO_RANGE("tobago-range"),
  TOBAGO_RELOAD("tobago-reload"),
  TOBAGO_SCROLL("tobago-scroll"),
  TOBAGO_SECTION("tobago-section"),
  TOBAGO_SEGMENT_LAYOUT("tobago-segment-layout"),
  TOBAGO_SELECT_BOOLEAN_CHECKBOX("tobago-select-boolean-checkbox"),
  TOBAGO_SELECT_BOOLEAN_TOGGLE("tobago-select-boolean-toggle"),
  TOBAGO_SELECT_MANY_CHECKBOX("tobago-select-many-checkbox"),
  TOBAGO_SELECT_MANY_LIST("tobago-select-many-list"),
  TOBAGO_SELECT_MANY_LISTBOX("tobago-select-many-listbox"),
  TOBAGO_SELECT_MANY_SHUTTLE("tobago-select-many-shuttle"),
  TOBAGO_SELECT_ONE_CHOICE("tobago-select-one-choice"),
  TOBAGO_SELECT_ONE_LIST("tobago-select-one-list"),
  TOBAGO_SELECT_ONE_LISTBOX("tobago-select-one-listbox"),
  TOBAGO_SELECT_ONE_RADIO("tobago-select-one-radio"),
  TOBAGO_SEPARATOR("tobago-separator"),
  TOBAGO_SHEET("tobago-sheet"),
  TOBAGO_SPLIT_LAYOUT("tobago-split-layout"),
  TOBAGO_STARS("tobago-stars"),
  TOBAGO_SUGGEST("tobago-suggest"),
  TOBAGO_TAB("tobago-tab"),
  TOBAGO_TAB_GROUP("tobago-tab-group"),
  TOBAGO_TEXTAREA("tobago-textarea"),
  TOBAGO_TOASTS("tobago-toasts"),
  TOBAGO_TREE("tobago-tree"),
  TOBAGO_TREE_LISTBOX("tobago-tree-listbox"),
  TOBAGO_TREE_NODE("tobago-tree-node"),
  TOBAGO_TREE_SELECT("tobago-tree-select");

  private final String value;
  private final boolean voidElement;
  private final boolean inlineElement;

  private static final Set<String> VOIDS = new HashSet<>();
  private static final Set<String> INLINES = new HashSet<>();

  HtmlElements(final String value, final Qualifier... qualifiers) {
    this.value = value;
    this.voidElement = Arrays.asList(qualifiers).contains(Qualifier.VOID);
    this.inlineElement = Arrays.asList(qualifiers).contains(Qualifier.INLINE);
  }

  static {
    for (final HtmlElements htmlElement : values()) {
      if (htmlElement.isVoid()) {
        VOIDS.add(htmlElement.getValue());
      }
      if (htmlElement.isInline()) {
        INLINES.add(htmlElement.getValue());
      }
    }
  }

  public String getValue() {
    return value;
  }

  /**
   * A void HTML elements is an element whose content model never allows it to have contents under any circumstances.
   * See <a href="http://www.w3.org/TR/html-markup/syntax.html#void-element">
   * http://www.w3.org/TR/html-markup/syntax.html#void-element</a>
   */
  public boolean isVoid() {
    return voidElement;
  }

  public boolean isInline() {
    return inlineElement;
  }

  public static boolean isVoid(final String name) {
    return VOIDS.contains(name);
  }

  public static boolean isInline(final String name) {
    return INLINES.contains(name);
  }

  private enum Qualifier {VOID, INLINE}
}
