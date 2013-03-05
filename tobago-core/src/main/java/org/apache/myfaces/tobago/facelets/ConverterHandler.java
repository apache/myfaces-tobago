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

import javax.el.ELException;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributeException;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagException;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

public class ConverterHandler extends TagHandler {

  private final TagAttribute converterId;

  private final TagAttribute binding;

  public ConverterHandler(TagConfig config) {
    super(config);
    binding = getAttribute("binding");
    converterId = getAttribute("type");
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws IOException, FacesException, ELException {
    if (parent instanceof ValueHolder) {
      if (ComponentHandler.isNew(parent)) {
        ValueHolder valueHolder = (ValueHolder) parent;
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
          } catch (Exception e) {
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
