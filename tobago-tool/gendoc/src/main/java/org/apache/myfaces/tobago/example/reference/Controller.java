package org.apache.myfaces.tobago.example.reference;

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

import org.apache.myfaces.tobago.taglib.component.ButtonTag;
import org.apache.myfaces.tobago.taglib.component.LinkTag;
import org.apache.myfaces.tobago.taglib.extension.InExtensionTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.tagext.TagSupport;
import java.util.List;
import java.util.ArrayList;

public class Controller {

  private static final Log LOG = LogFactory.getLog(Controller.class);

  private List<TagData> tags;

  private List<AttributeData> attributes;

  public Controller() {
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
      tag.setValue("Hallo Tester");
      tag.setLabel("Label");
      return tag;
    } catch (Exception e) {
      LOG.error("", e); // fixme
      throw new RuntimeException(e);
    }
  }

  public List<TagData> getTags() {
    return tags;
  }

  public List<AttributeData> getAttributes() {
    return attributes;
  }
}

