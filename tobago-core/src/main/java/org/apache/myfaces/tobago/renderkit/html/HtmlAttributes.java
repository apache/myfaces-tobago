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

/**
 * HTML standard attributes. For non-standard attributes {@link CustomAttributes}
 */
public enum HtmlAttributes implements MarkupLanguageAttributes {

  ACCEPT_CHARSET("accept-charset"),
  ACCEPT("accept"),
  ACCESSKEY("accesskey"),
  ACTION("action"),
  ALIGN("align"),
  ALT("alt"),
  AUTOCOMPLETE("autocomplete"),
  AUTOFOCUS("autofocus"),
  BORDER("border"),
  CHARSET("charset"),
  CELLPADDING("cellpadding"),
  CELLSPACING("cellspacing"),
  CHECKED("checked"),
  CLASS("class"),
  COLSPAN("colspan"),
  CONTENT("content"),
  DEFER("defer"),
  DISABLED("disabled"),
  ENCTYPE("enctype"),
  FOCUS_ON_ERROR("focus-on-error"),
  FOR("for"),
  FRAMEBORDER("frameborder"),
  FREQUENCY("frequency"),
  HEIGHT("height"),
  HREF("href"),
  HREFLANG("hreflang"),
  HTTP_EQUIV("http-equiv"),
  ID("id"),
  LABEL("label"),
  LANG("lang"),
  MAX("max"),
  MAXLENGTH("maxlength"),
  MEDIA("media"),
  METHOD("method"),
  MULTIPLE("multiple"),
  NAME("name"),
  NONCE("nonce"),
  MIN("min"),
  MINLENGTH("minlength"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONBLUR("onblur"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONCHANGE("onchange"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONCLICK("onclick"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONDBLCLICK("ondblclick"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONFOCUS("onfocus"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONFOCUSIN("onfocusin"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONKEYDOWN("onkeydown"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONKEYPRESS("onkeypress"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONKEYUP("onkeyup"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONLOAD("onload"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONMOUSEOVER("onmouseover"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  ONMOUSEOUT("onmouseout"),
  PATTERN("pattern"),
  PLACEHOLDER("placeholder"),
  READONLY("readonly"),
  REL("rel"),
  REV("rev"),
  REQUIRED("required"),
  ROLE("role"),
  ROWS("rows"),
  ROWSPAN("rowspan"),
  SANDBOX("sandbox"),
  SCROLL("scroll"),
  SELECTED("selected"),
  SIZE("size"),
  SRC("src"),
  STEP("step"),
  /**
   * @deprecated This attribute work not with SCP
   */
  @Deprecated(since = "2.0.0", forRemoval = true)
  STYLE("style"),
  SUMMARY("summary"),
  TABINDEX("tabindex"),
  TARGET("target"),
  TITLE("title"),
  TYPE("type"),
  VALIGN("valign"),
  VALUE("value"),
  WAIT_OVERLAY_DELAY_AJAX("wait-overlay-delay-ajax"),
  WAIT_OVERLAY_DELAY_FULL("wait-overlay-delay-full"),
  WIDTH("width"),
  XMLNS("xmlns");

  private final String value;

  HtmlAttributes(final String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

}

