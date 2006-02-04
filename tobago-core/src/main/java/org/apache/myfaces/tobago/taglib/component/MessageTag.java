package org.apache.myfaces.tobago.taglib.component;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOR;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;

/**
 * Renders error/validation message.
 */
@Tag(name = "message", bodyContent = BodyContent.EMPTY)
public class MessageTag extends TobagoTag
    implements HasIdBindingAndRendered, HasFor {

  private String forComponent;

  public String getComponentType() {
    return UIMessage.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
  }

  public void release() {
    super.release();
    forComponent = null;
  }

  public String getFor() {
    return forComponent;
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }
}
