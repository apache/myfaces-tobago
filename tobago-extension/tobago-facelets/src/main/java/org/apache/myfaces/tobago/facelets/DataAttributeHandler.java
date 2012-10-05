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

import com.sun.facelets.FaceletContext;
import com.sun.facelets.tag.TagAttribute;
import com.sun.facelets.tag.TagConfig;
import com.sun.facelets.tag.TagException;
import com.sun.facelets.tag.TagHandler;
import com.sun.facelets.tag.jsf.ComponentSupport;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.el.ELException;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;

/**
  * PRELIMINARY - SUBJECT TO CHANGE
 */
public final class DataAttributeHandler extends TagHandler {

  private final TagAttribute name;

  private final TagAttribute value;

  public DataAttributeHandler(TagConfig config) {
    super(config);
    this.name = getRequiredAttribute(Attributes.NAME);
    this.value = getRequiredAttribute(Attributes.VALUE);
  }

  public void apply(FaceletContext faceletContext, UIComponent parent)
      throws FacesException, ELException {
    if (parent == null) {
      throw new TagException(tag, "Parent UIComponent was null");
    }

    if (ComponentSupport.isNew(parent)) {

      Object attributeName = name.isLiteral()
          ? (Object) name.getValue(faceletContext)
          : name.getValueExpression(faceletContext, Object.class);
      Object attributeValue = value.isLiteral()
          ? (Object) value.getValue(faceletContext)
          : value.getValueExpression(faceletContext, Object.class);
      ComponentUtils.putDataAttribute(parent, attributeName, attributeValue);
    }
  }
}
