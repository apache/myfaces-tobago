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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.Attributes;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

/**
 *  @deprecated since 2.0.8
 */
public final class HtmlUtils {

  public static final String LAYOUT_ATTRIBUTE_PREFIX = "layout.";
  /**
   *  @deprecated since 2.0.8, use HtmlRendererUtils
   */
  public static final String CHAR_NON_BEAKING_SPACE = "\u00a0";

  private HtmlUtils() {
  }

  public static String generateAttribute(final String name, final Object value) {
    final String stringValue;
    if (value == null) {
      stringValue = null;
    } else if (value instanceof String) {
      stringValue = (String) value;
    } else {
      stringValue = value.toString();
    }
    return (stringValue != null && stringValue.length() > 0)
        ? name + "=\"" + value + "\""
        : "";
  }

  public static String appendAttribute(final UIComponent component, final String name,
      final String appendValue) {
    final Object attribute = component.getAttributes().get(name);
    return attribute != null
        ? attribute.toString() + " " + appendValue : appendValue;
  }

  /** @deprecated Since 2.0.8. Not allowed with CSP */
  public static String generateOnchange(final UIInput component,
      final FacesContext facesContext) {

    /*Validator[] validators = component.getValidators();
    for (int i = 0; i < validators.length; i++) {
      if (validators[i] instanceof LongRangeValidator) {
        String functionCall = "validateLongRange('"
            + component.getClientId(facesContext) + "')";
        if (LOG.isDebugEnabled()) {
          LOG.debug("validator functionCall: " + functionCall);
        }
        buffer.append(functionCall);
      } else {
        buffer.append("true");
      }
      if (i + 1 < validators.length) { // is not last
        buffer.append(" && ");
      }
    } */

    final Object onchange = component.getAttributes().get(Attributes.ONCHANGE);
    if (onchange != null) {
      return onchange.toString();
    }
    return null;
  }

}
