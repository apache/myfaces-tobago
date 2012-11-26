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

package org.apache.myfaces.tobago.example.reference;

import org.apache.myfaces.tobago.internal.taglib.ButtonTag;
import org.apache.myfaces.tobago.internal.taglib.LinkTag;
import org.apache.myfaces.tobago.internal.taglib.extension.InExtensionTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.ArrayList;
import java.util.List;

public class DynamicController {
  
  private static final Logger LOG = LoggerFactory.getLogger(DynamicController.class);

  private List<TagData> tags;

  private List<AttributeData> attributes;

  public DynamicController() {
    tags = new ArrayList<TagData>();
    TagData in = new TagData(InExtensionTag.class);
    in.setName("In");
    in.setTip("Ein In");
    tags.add(in);
    TagData button = new TagData(ButtonTag.class);
    button.setName("Button");
    button.setTip("Ein Knopf");
    tags.add(button);
    TagData link = new TagData(LinkTag.class);
    link.setName("Link");
    link.setTip("Ein Link");
    tags.add(link);
    attributes = new ArrayList<AttributeData>();
  }

   public TagSupport createTag() {
    try {
      Class clazz = tags.get(0).getTagClass();
      InExtensionTag tag = (InExtensionTag) clazz.newInstance();
      tag.setValue(createStringValueExpression("Hallo Tester"));
      tag.setLabel(createStringValueExpression("Label"));
      return tag;
    } catch (Exception e) {
      LOG.error("", e); // fixme
      throw new RuntimeException(e);
    }
  }

  protected ValueExpression createStringValueExpression(String expression) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ValueExpression value = facesContext.getApplication().getExpressionFactory().
        createValueExpression(facesContext.getELContext(), expression, String.class);
    return value;
  }
  
  public List<TagData> getTags() {
    return tags;
  }

  public List<AttributeData> getAttributes() {
    return attributes;
  }
}
