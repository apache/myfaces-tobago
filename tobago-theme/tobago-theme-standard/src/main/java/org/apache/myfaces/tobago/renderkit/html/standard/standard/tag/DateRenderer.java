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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;

public class DateRenderer extends InRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(DateRenderer.class);

  @Override
  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIInput input)
      throws IOException {
    super.writeAdditionalAttributes(facesContext, writer, input);

    String pattern = null;
    final Converter help = getConverter(facesContext, input);
    if (help instanceof DateTimeConverter) {
      final DateTimeConverter converter = (DateTimeConverter) help;
      pattern = DateFormatUtils.findPattern(converter);
    }
    if (pattern == null) {
      pattern = "yyyy-MM-dd";
      LOG.warn("Can't find the pattern for the converter! DatePicker may not work correctly. "
          + "Trying to use: '" + pattern + "'");
    }

    writer.writeAttribute(DataAttributes.PATTERN, pattern, true);

    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(facesContext.getViewRoot().getLocale());
    writer.writeAttribute(DataAttributes.DATE_TIME_I18N, JsonUtils.encode(dateTimeI18n), true);

    final String imageName;
    if (pattern.contains("m")) { // simple guessing
      if (pattern.contains("d")) {
        imageName = "image/date-time.png";
      } else {
        imageName = "image/time.png";
      }
    } else {
      imageName = "image/date.gif";
    }
    final String icon = ResourceManagerUtils.getImageWithPath(facesContext, imageName);
    if (icon != null) {
      writer.writeAttribute(DataAttributes.DATE_TIME_ICON, icon, true);
    }
  }
}
