package org.apache.myfaces.tobago.facelets;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagAttributeException;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.FaceletContext;

import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.el.ELException;
import javax.el.ValueExpression;
import java.io.IOException;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Oct 13, 2006
 * Time: 6:17:49 PM
 */
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
      // only process if parent was just created
      if (parent.getParent() == null) {
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
        valueHolder.setConverter(converter);
      }
    } else {
      throw new TagException(tag, "Parent is not of type ValueHolder, type is: " + parent);
    }
  }
}
