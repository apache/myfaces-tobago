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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.component.ClientBehaviors;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.renderkit.Collapse;
import org.apache.myfaces.tobago.internal.renderkit.Command;
import org.apache.myfaces.tobago.internal.renderkit.CommandMap;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.MeasureList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class JsonUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private JsonUtils() {
  }

  private static void encode(final StringBuilder builder, final String name, final String[] value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    encode(builder, value);
    builder.append(",");
  }

  private static void encode(final StringBuilder builder, final String[] value) {
    builder.append("[");
    boolean colon = false;
    for (final String item : value) {
      if (colon) {
        builder.append(",");
      }
      builder.append("\"");
      for (int i = 0; i < item.length(); i++) {
        final char c = item.charAt(i);
        if (c == '\"') {
          builder.append("\\\"");
        } else {
          builder.append(c);
        }
      }
      builder.append("\"");
      colon = true;
    }
    builder.append("]");
  }

  private static void encode(final StringBuilder builder, final Boolean[] value) {
    builder.append("[");
    boolean colon = false;
    for (final boolean item : value) {
      if (colon) {
        builder.append(",");
      }
      builder.append(item);
      colon = true;
    }
    builder.append("]");
  }

  private static void encode(final StringBuilder builder, final List<Integer> value) {
    builder.append("[");
    boolean colon = false;
    for (final Integer item : value) {
      if (colon) {
        builder.append(",");
      }
      builder.append(item);
      colon = true;
    }
    builder.append("]");
  }

  private static void encode(final StringBuilder builder, final String name, final Boolean value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(value);
    builder.append(",");
  }

  private static void encode(final StringBuilder builder, final String name, final Integer value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":");
    builder.append(value);
    builder.append(",");
  }

  private static void encode(final StringBuilder builder, final String name, final String value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":\"");
    int length = value.length();
    for (int i = 0; i < length; i++) {
      final char c = value.charAt(i);
      if (c == '\"') {
        builder.append("\\\"");
      } else {
        builder.append(c);
      }
    }
    builder.append("\",");
  }

  public static String encode(final CommandMap commandMap) {

    if (commandMap == null) {
      return null;
    }

    final StringBuilder builder = new StringBuilder();
    builder.append("{");
    final int initialLength = builder.length();

    final Command click = commandMap.getClick();
    if (click != null) {
      encode(builder, ClientBehaviors.click, click);
    }

    final Map<ClientBehaviors, Command> other = commandMap.getOther();
    if (other != null) {
      for (final Map.Entry<ClientBehaviors, Command> entry : other.entrySet()) {
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

  private static void encode(final StringBuilder builder, final ClientBehaviors name, final Command command) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    final int initialLength = builder.length();

    final String clientId = command.getClientId();
    if (clientId != null) {
      encode(builder, "clientId", clientId);
    }
    final String fieldId = command.getFieldId();
    if (fieldId != null) {
      encode(builder, "fieldId", fieldId);
    }
    final Boolean transition = command.getTransition();
    if (transition != null && !transition) { // true is the default, so encoding is needed.
      encode(builder, "transition", transition);
    }
    final String target = command.getTarget();
    if (target != null) {
      encode(builder, "target", target);
    }
    final String execute = command.getExecute();
    if (execute != null) {
      encode(builder, "execute", execute);
    }
    final String render = command.getRender();
    if (render != null) {
      encode(builder, "render", render);
    }
    final Collapse collapse = command.getCollapse();
    if (collapse != null) {
      encode(builder, "collapse", collapse);
    }
    final String confirmation = command.getConfirmation();
    if (confirmation != null) {
      encode(builder, "confirmation", confirmation);
    }
    final Integer delay = command.getDelay();
    if (delay != null) {
      encode(builder, "delay", delay);
    }
    final Boolean omit = command.getOmit();
    if (omit != null && omit) { // false is the default, so encoding is needed.
      encode(builder, "omit", true);
    }

    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

  private static void encode(final StringBuilder builder, final String name, final Collapse collapse) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":{");
    final int initialLength = builder.length();

    final Collapse.Operation action = collapse.getOperation();
    if (action != null) {
      encode(builder, "transition", action.name());
    }
    final String forId = collapse.getFor();
    if (forId != null) {
      encode(builder, "forId", forId);
    }
    if (builder.length() - initialLength > 0) {
      assert builder.charAt(builder.length() - 1) == ',';
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("},");
  }

  public static String encode(final String[] strings) {
    if (strings == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    encode(builder, strings);
    return builder.toString();
  }

  public static String encode(final Integer[] integers) {
    if (integers == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    encode(builder, Arrays.asList(integers));
    return builder.toString();
  }

  public static String encode(final Boolean[] booleans) {
    if (booleans == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    encode(builder, booleans);
    return builder.toString();
  }

  public static String encode(final List<Integer> integers) {
    if (integers == null) {
      return null;
    }
    final StringBuilder builder = new StringBuilder();
    encode(builder, integers);
    return builder.toString();
  }

  private static void encode(final MeasureList layout, final StringBuilder builder) {
    builder.append("[");
    for (final Measure measure : layout) {
      final Measure.Unit unit = measure.getUnit();
      if (unit == Measure.Unit.FR) {
        final float factor = measure.getValue(); // todo: might be better with "fr" suffix, but needs a JS change
        builder.append(factor);
      } else if (unit == Measure.Unit.AUTO) {
        builder.append("\"auto\"");
      } else {
        builder.append("{\"measure\":\"");
        builder.append(measure.serialize());
        builder.append("\"}");
      }
      builder.append(',');
    }
    if (builder.charAt(builder.length() - 1) == ',') {
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append("]");
  }

  public static List<Integer> decodeIntegerArray(final String json) {
    if (json == null) {
      return null;
    }
    String string = json.trim();
    final List<Integer> result = new ArrayList<>();
    if (string.length() < 2 || string.charAt(0) != '[' || string.charAt(string.length() - 1) != ']') {
      LOG.warn("Can't parse JSON array: no surrounding square brackets []: '{}'", string);
    } else {
      string = string.substring(1, string.length() - 1);
      final StringTokenizer tokenizer = new StringTokenizer(string, ",");
      while (tokenizer.hasMoreTokens()) {
        final String token = tokenizer.nextToken().trim();
        try {
          result.add(Integer.parseInt(token));
        } catch (final NumberFormatException e) {
          LOG.warn("Can't parse JSON array: not an integer token: '{}'", token);
          // ignoring so far
        }
      }
    }
    return result;
  }

  public static String encode(final Markup markups) {
    if (markups == null || markups.isEmpty()) {
      return null;
    }
    final StringBuilder builder = new StringBuilder(20);
    builder.append('[');
    for (final String markup : markups) {
      builder.append('"');
      builder.append(markup);
      builder.append('"');
      builder.append(',');
    }
    if (builder.length() > 1) {
      builder.deleteCharAt(builder.length() - 1);
    }
    builder.append(']');
    return builder.toString();
  }

  public static String encode(final MeasureList measures, final String name) {
    final StringBuilder builder = new StringBuilder();
    builder.append("{\"");
    builder.append(name);
    builder.append("\":");
    JsonUtils.encode(measures, builder);
    builder.append("}");
    return builder.toString();
  }

  public static String encodeEmptyArray() {
    return "[]";
  }
}
