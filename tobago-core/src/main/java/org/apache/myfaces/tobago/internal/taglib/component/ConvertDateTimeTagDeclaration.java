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

package org.apache.myfaces.tobago.internal.taglib.component;


import org.apache.myfaces.tobago.apt.annotation.ConverterTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.convert.DateTimeConverter;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasFor;

import jakarta.el.ValueExpression;

/**
 * Register a DateTimeConverter instance on the UIComponent associated with the closest parent UIComponent custom
 * action.
 *
 * @deprecated Since 5.0.0. Should work with &lt;f:convertDateTime%gt; since JSF 2.3.
 */
@Deprecated
@Tag(name = "convertDateTime")
@ConverterTag(
    converterId = DateTimeConverter.CONVERTER_ID,
    faceletHandler = "org.apache.myfaces.tobago.facelets.ConvertDateTimeHandler")
public interface ConvertDateTimeTagDeclaration extends HasFor {

  /**
   * A ValueExpression that evaluates to an instance of
   * {@link org.apache.myfaces.tobago.facelets.ConvertDateTimeHandler}.
   */
  @TagAttribute(name = "binding", type = "org.apache.myfaces.tobago.convert.DateTimeConverter")
  void setBinding(ValueExpression binding);

  /**
   * Locale whose predefined styles for dates and times are used during formatting or parsing. If not specified, the
   * Locale returned by FacesContext.getViewRoot().getLocale() will be used. Value must be either a VB expression that
   * evaluates to a java.util.Locale instance, or a String that is valid to pass as the first argument to the
   * constructor java.util.Locale(String language, String country). The empty string is passed as the second argument.
   */
  @TagAttribute(type = "java.lang.Object")
  void setLocale(ValueExpression locale);

  /**
   * Custom formatting pattern which determines how the date/time string should be formatted and parsed.
   * Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported.
   */
  @TagAttribute()
  void setPattern(String pattern);

  /**
   * Time zone in which to interpret any time information in the date String. Value must be either a VB expression that
   * evaluates to a java.util.TimeZone instance, or a String that is a timezone ID as described in the javadocs for
   * java.util.TimeZone.getTimeZone().
   */
  @TagAttribute(type = "java.lang.Object")
  void setTimeZone(ValueExpression timeZone);


  /**
   * Specifies what contents the string value will be formatted to include, or parsed expecting. Valid values are
   * "date", "time", "both", "calendar", "localDate", "localDateTime", "localTime", "offsetTime", "offsetDateTime", and
   * "zonedDateTime". The values starting with "local", "offset" and "zoned" correspond to Java SE 8 Date Time API
   * classes in package java.time with the name derived by upper casing the first letter. For example,
   * java.time.LocalDate for the value "localDate". Default value is "date".
   */
  @TagAttribute()
  void setType(String type);
}
