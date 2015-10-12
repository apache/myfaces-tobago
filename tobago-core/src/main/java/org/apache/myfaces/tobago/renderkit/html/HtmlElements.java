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
  AREA("area"),
  B("b"),
  BASE("base"),
  BODY("body"),
  BR("br"),
  BUTTON("button"),
  COL("col"),
  COLGROUP("colgroup"),
  DIV("div"),
  FIELDSET("fieldset"),
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
  HR("hr"),
  HTML("html"),
  IFRAME("iframe"),
  IMG("img"),
  INPUT("input"),
  LABEL("label"),
  LEGEND("legend"),
  LI("li"),
  LINK("link"),
  META("meta"),
  NAV("nav"),
  NOSCRIPT("noscript"),
  OL("ol"),
  OPTGROUP("optgroup"),
  OPTION("option"),
  P("p"),
  PARAM("param"),
  SCRIPT("script"),
  SELECT("select"),
  SPAN("span"),
  STYLE("style"),
  TABLE("table"),
  TBODY("tbody"),
  TD("td"),
  TEXTAREA("textarea"),
  TH("th"),
  TITLE("title"),
  TR("tr"),

  @Deprecated
  U("u"),
  UL("ul");

  private final String value;

  HtmlElements(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
