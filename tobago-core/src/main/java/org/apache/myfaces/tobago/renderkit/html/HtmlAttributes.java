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
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONBLUR("onblur"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONCHANGE("onchange"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONCLICK("onclick"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONDBLCLICK("ondblclick"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONFOCUS("onfocus"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONFOCUSIN("onfocusin"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONKEYDOWN("onkeydown"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONKEYPRESS("onkeypress"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONKEYUP("onkeyup"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONLOAD("onload"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
  ONMOUSEOVER("onmouseover"),
  /**
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
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
   * @deprecated Since 2.0.0. This attribute work not with SCP
   */
  @Deprecated
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

