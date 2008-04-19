package org.apache.myfaces.tobago.ajax.api;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.myfaces.tobago.ajax.api.AjaxUtils.encodeJavascriptString;

import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class AjaxResponse {
  public static final int CODE_SUCCESS = 200;
  public static final int CODE_NOT_MODIFIED = 304;
  public static final int CODE_RELOAD_REQUIRED = 309;
  public static final int CODE_ERROR = 500;


  private static final String JAVASCRIPT_PATTERN = "(<script([^>]*))>(\\s*<!--)?(.*?)(</script>)";
  private static final Pattern JAVASCRIPT_EXTRACTOR
      = Pattern.compile(JAVASCRIPT_PATTERN, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

  private int responseCode;
  private String ajaxId;
  private String html;
  private String javaScript;

  public AjaxResponse(String ajaxId, int responseCode, String html) {
    this.responseCode = responseCode;
    this.ajaxId = ajaxId;
    if (responseCode == CODE_SUCCESS) {
      Matcher matcher = JAVASCRIPT_EXTRACTOR.matcher(html);
      javaScript = "";
      while (matcher.find()) {
        javaScript += matcher.group(4) + "\n\n";
      }
      this.html = matcher.replaceAll("");
    } else {
      this.html = "";
      javaScript = "";
    }
  }

  public AjaxResponse(String ajaxId, int responseCode, String html, String javaScript) {
    this.responseCode = responseCode;
    this.ajaxId = ajaxId;
    if (responseCode == CODE_SUCCESS) {
      this.html = html;
      this.javaScript = javaScript;
    } else {
      this.html = "";
      this.javaScript = "";
    }
  }

  public int getResponseCode() {
    return responseCode;
  }

  public String getAjaxId() {
    return ajaxId;
  }

  public String toJson() {
    StringBuilder json
        = new StringBuilder(html.length() + javaScript.length() + ajaxId.length() + 50);
    json.append("{\n    ajaxId: \"").append(ajaxId).append("\",\n");
    json.append("    responseCode: ").append(responseCode).append(",\n");
    json.append("    html: \"").append(encodeJavascriptString(html)).append("\",\n");
    json.append("    script: function() {\n").append(javaScript).append("\n    }\n  }");
    return json.toString();
  }

}
