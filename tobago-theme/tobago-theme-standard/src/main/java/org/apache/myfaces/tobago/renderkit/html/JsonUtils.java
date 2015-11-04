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

import org.apache.myfaces.tobago.internal.context.DateTimeI18n;

import java.util.Map;

public class JsonUtils {

  private JsonUtils() {
  }

  private static void encode(final StringBuilder builder, final String name, final String[] value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    encode(builder, value);
    builder.append(",");
  }

  public static void encode(StringBuilder builder, String[] value) {
    builder.append("[");
    boolean colon = false;
    for (final String item : value) {
      if (colon) {
        builder.append(",");
      }
      builder.append("\"");
      builder.append(item);
      builder.append("\"");
      colon = true;
    }
    builder.append("]");
  }

  static void encode(final StringBuilder builder, final String name, final Boolean value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(Boolean.toString(value));
    builder.append(",");
  }

  static void encode(final StringBuilder builder, final String name, final Integer value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(Integer.toString(value));
    builder.append(",");
  }

  static void encode(final StringBuilder builder, final String name, String value) {
    value = value.replaceAll("\\\"", "\\\\\\\""); // todo: optimize
    builder.append("\"");
    builder.append(name);
    builder.append("\":\"");
    builder.append(value);
    builder.append("\",");
  }

  public static String encode(final CommandMap commandMap) {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    final int initialLength = builder.length();

    final Command click = commandMap.getClick();
    if (click != null) {
      encode(builder, "click", click);
    }

    final Map<String, Command> other = commandMap.getOther();
    if (other != null) {
      for(final Map.Entry<String, Command> entry : other.entrySet()) {
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

  static void encode(final StringBuilder builder, final String name, final Command command) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    final int initialLength = builder.length();

    final String action = command.getAction();
    if (action != null) {
      encode(builder, "action", action);
    }
    final Boolean transition = command.getTransition();
    if (transition != null && !transition) { // true is the default, so encoding is needed.
      encode(builder, "transition", transition);
    }
    final String target = command.getTarget();
    if (target != null) {
      encode(builder, "target", target);
    }
    final String url = command.getUrl();
    if (url != null) {
      encode(builder, "url", url);
    }
    final String[] partially = command.getPartially();
    if (partially != null && partially.length > 0) {
      if (partially.length == 1) {
        encode(builder, "partially", partially[0]);
      } else {
        encode(builder, "partially", partially);
      }
    }
    final String focus = command.getFocus();
    if (focus != null) {
      encode(builder, "focus", focus);
    }
    final String confirmation = command.getConfirmation();
    if (confirmation != null) {
      encode(builder, "confirmation", confirmation);
    }
    final Integer delay  = command.getDelay();
    if (delay != null) {
      encode(builder, "delay", delay);
    }
    final Popup popup = command.getPopup();
    if (popup != null) {
      encode(builder, "popup", popup);
    }
    final Boolean omit = command.getOmit();
    if (omit != null && omit) { // false is the default, so encoding is needed.
      encode(builder, "omit", omit);
    }

    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

  static void encode(final StringBuilder builder, final String name, final Popup popup) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    final int initialLength = builder.length();

    final String command = popup.getCommand();
    if (command != null) {
      encode(builder, "command", command);
    }
    final Boolean immediate = popup.isImmediate();
    if (immediate != null) {
      encode(builder, "immediate", immediate);
    }
    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

  public static String encode(final DateTimeI18n dateTimeI18n) {
    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    final int initialLength = builder.length();

    encode(builder, "monthNames", dateTimeI18n.getMonthNames());
    encode(builder, "monthNamesShort", dateTimeI18n.getMonthNamesShort());
    encode(builder, "dayNames", dateTimeI18n.getDayNames());
    encode(builder, "dayNamesShort", dateTimeI18n.getDayNamesShort());
    encode(builder, "dayNamesMin", dateTimeI18n.getDayNamesMin());
    encode(builder, "firstDay", dateTimeI18n.getFirstDay());

    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append("}");
    return builder.toString();
  }

  public static String encode(final String[] strings) {
    if (strings == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    encode(builder, strings);
    return builder.toString();
  }
}
