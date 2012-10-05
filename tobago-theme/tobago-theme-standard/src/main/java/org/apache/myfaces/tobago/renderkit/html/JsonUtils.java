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

import java.util.Map;

public class JsonUtils {

  private JsonUtils() {
  }

  private static void encode(StringBuilder builder, String name, String[] value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":\"");
    boolean colon = false;
    for (String item : value) {
      if (colon) {
        builder.append(",");
      }
      builder.append(item);
      colon = true;
    }
    builder.append("\",");
  }

  static void encode(StringBuilder builder, String name, Boolean value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(Boolean.toString(value));
    builder.append(",");
  }

  static void encode(StringBuilder builder, String name, Integer value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(Integer.toString(value));
    builder.append(",");
  }

  static void encode(StringBuilder builder, String name, String value) {
    value = value.replaceAll("\\\"", "\\\\\\\"");
    builder.append("\"");
    builder.append(name);
    builder.append("\":\"");
    builder.append(value);
    builder.append("\",");
  }

  public static String encode(CommandMap commandMap) {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    int initialLength = builder.length();

    Command click = commandMap.getClick();
    if (click != null) {
      encode(builder, "click", click);
    }

    final Map<String,Command> other = commandMap.getOther();
    if (other != null) {
      for(Map.Entry<String, Command> entry : other.entrySet()) {
        encode(builder, entry.getKey(), entry.getValue());
      }
    }

    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("}");
    return builder.toString();
  }

  static void encode(StringBuilder builder, String name, Command command) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    int initialLength = builder.length();

    String action = command.getAction();
    if (action != null) {
      encode(builder, "action", action);
    }
    Boolean transition = command.getTransition();
    if (transition != null && !transition) { // true is the default, so encoding is needed.
      encode(builder, "transition", transition);
    }
    String target = command.getTarget();
    if (target != null) {
      encode(builder, "target", target);
    }
    String url = command.getUrl();
    if (url != null) {
      encode(builder, "url", url);
    }
    String[] partially = command.getPartially();
    if (partially != null && partially.length > 0) {
      encode(builder, "partially", partially);
    }
    String focus = command.getFocus();
    if (focus != null) {
      encode(builder, "focus", focus);
    }
    String confirmation = command.getConfirmation();
    if (confirmation != null) {
      encode(builder, "confirmation", confirmation);
    }
    Integer delay  = command.getDelay();
    if (delay != null) {
      encode(builder, "delay", delay);
    }
    Popup popup = command.getPopup();
    if (popup != null) {
      encode(builder, "popup", popup);
    }
    String script = command.getScript();
    if (script != null) {
      encode(builder, "script", script);
    }

    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

  static void encode(StringBuilder builder, String name, Popup popup) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    int initialLength = builder.length();

    String command = popup.getCommand();
    if (command != null) {
      encode(builder, "command", command);
    }
    Boolean immediate = popup.isImmediate();
    if (immediate != null) {
      encode(builder, "immediate", immediate);
    }
    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

}
