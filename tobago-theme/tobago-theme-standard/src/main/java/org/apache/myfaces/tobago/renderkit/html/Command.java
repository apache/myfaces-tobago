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
 * @since 1.6.0
 */
// XXX work in progress
public class Command {

  private Boolean transition;
  private String target;
  private String url;
  private String[] partially;
  private String focus;
  private String confirmation;
  private Integer delay;
  /**
   * @deprecated
   */
  @Deprecated
  private String script;

  public Command(
      Boolean transition, String target, String url, String[] partially, String focus, String confirmation,
      Integer delay) {
    this.transition = transition;
    this.target = target;
    this.url = url;
    this.partially = partially;
    this.focus = focus;
    this.confirmation = confirmation;
    this.delay = delay;
  }

  /**
   * @deprecated
   */
  @Deprecated
  public void setScript(String script) {
    this.script = script;
  }

  public void encodeJson(StringBuilder builder) {
    builder.append("{");
    int initialLength = builder.length();
    if (transition != null && !transition) { // true is the default, so encoding is needed.
      encodeJsonAttribute(builder, "transition", transition);
    }
    if (target != null) {
      encodeJsonAttribute(builder, "target", target);
    }
    if (url != null) {
      encodeJsonAttribute(builder, "url", url);
    }
    if (partially != null && partially.length > 0) {
      encodeJsonAttribute(builder, "partially", partially);
    }
    if (focus != null) {
      encodeJsonAttribute(builder, "focus", focus);
    }
    if (confirmation != null) {
      encodeJsonAttribute(builder, "confirmation", confirmation);
    }
    if (delay != null) {
      encodeJsonAttribute(builder, "delay", delay);
    }
    if (script != null) {
      encodeJsonAttribute(builder, "script", script);
    }

    if (builder.length() - initialLength > 0) {
      builder.deleteCharAt(builder.length() - 1);
    }

    builder.append("}");
  }

  private void encodeJsonAttribute(StringBuilder builder, String name, String[] value) {
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

  private void encodeJsonAttribute(StringBuilder builder, String name, Boolean value) {
    encodeJsonAttributeIntern(builder, name, Boolean.toString(value));
  }

  private void encodeJsonAttribute(StringBuilder builder, String name, Integer value) {
    encodeJsonAttributeIntern(builder,  name, Integer.toString(value));
  }

  private void encodeJsonAttribute(StringBuilder builder, String name, String value) {
    value = value.replaceAll("\\\"", "\\\\\\\"");
    encodeJsonAttributeIntern(builder, name, value);
  }

  private void encodeJsonAttributeIntern(StringBuilder builder, String name, String value) {
    builder.append("\"");
    builder.append(name);
    builder.append("\":\"");
    builder.append(value);
    builder.append("\",");
  }
}
