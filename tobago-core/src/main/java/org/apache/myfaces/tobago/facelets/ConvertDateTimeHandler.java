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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.convert.DateTimeConverter;

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.ConverterConfig;
import javax.faces.view.facelets.ConverterHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;
import java.util.Locale;
import java.util.TimeZone;

import static org.apache.myfaces.tobago.internal.util.Deprecation.LOG;

public class ConvertDateTimeHandler extends ConverterHandler {

  private final TagAttribute binding;
  private final TagAttribute locale;
  private final TagAttribute pattern;
  private final TagAttribute timeZone;
  private final TagAttribute type;

  public ConvertDateTimeHandler(final ConverterConfig config) {
    super(config);
    binding = getAttribute("binding");
    locale = getAttribute("locale");
    pattern = getAttribute("pattern");
    timeZone = getAttribute("timeZone");
    type = getAttribute("type");
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws ELException {
    if (parent instanceof ValueHolder) {
      if (ComponentHandler.isNew(parent)) {
        final ValueHolder valueHolder = (ValueHolder) parent;
        Converter converter = null;
        ValueExpression valueExpression = null;
        if (binding != null) {
          valueExpression = binding.getValueExpression(faceletContext, Converter.class);
          converter = (Converter) valueExpression.getValue(faceletContext);
        }
        if (converter == null) {
          converter = FacesContext.getCurrentInstance().getApplication().createConverter(DateTimeConverter.CONVERTER_ID);
          DateTimeConverter dtConverter = (DateTimeConverter) converter;

          if (locale != null) {
            final Object localeObject = locale.getObject(faceletContext);
            if (localeObject instanceof Locale) {
              dtConverter.setLocale((Locale) localeObject);
            } else {
              LOG.warn("Could not set local.");
            }
          }
          if (pattern != null) {
            final Object patternObject = pattern.getObject(faceletContext);
            if (patternObject instanceof String) {
              dtConverter.setPattern((String) patternObject);
            } else {
              LOG.warn("Could not set pattern.");
            }
          }
          if (timeZone != null) {
            final Object timeZoneObject = timeZone.getObject(faceletContext);
            if (timeZoneObject instanceof TimeZone) {
              dtConverter.setTimeZone((TimeZone) timeZoneObject);
            } else {
              LOG.warn("Could not set time zone.");
            }
          }
          if (type != null) {
            final Object typeObject = type.getObject(faceletContext);
            if (typeObject instanceof String) {
              dtConverter.setType((String) typeObject);
            } else {
              LOG.warn("Could not set type.");
            }
          }

          if (valueExpression != null) {
            valueExpression.setValue(faceletContext, converter);
          }
        }
        if (converter != null) {
          valueHolder.setConverter(converter);
        }
        // TODO else LOG.warn?
      }
    } else {
      throw new TagException(tag, "Parent is not of type ValueHolder, type is: " + parent);
    }
  }
}
