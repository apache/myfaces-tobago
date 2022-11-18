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

import jakarta.el.ELException;
import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.ValueHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagAttributeException;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagException;
import jakarta.faces.view.facelets.TagHandler;

import java.io.IOException;

public class ConverterHandler extends TagHandler {

  private final TagAttribute converterId;

  private final TagAttribute binding;

  public ConverterHandler(final TagConfig config) {
    super(config);
    binding = getAttribute("binding");
    converterId = getAttribute("type");
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent)
      throws IOException, ELException {
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
          try {
            converter = FacesContext.getCurrentInstance().getApplication().createConverter(
                (String) converterId.getValueExpression(faceletContext, String.class).getValue(faceletContext));
          } catch (final Exception e) {
            throw new TagAttributeException(tag, converterId, e.getCause());
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
