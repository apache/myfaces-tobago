/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created Jan 20, 2003.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasFor;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;

/**
 * Renders error/validation messages.
 */
@Tag(name="messages", bodyContent=BodyContent.EMPTY)
public class MessagesTag extends TobagoTag
    implements HasIdBindingAndRendered, HasFor {


// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String _for;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIMessages.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for, getIterationHelper());
  }

  public void release() {
    super.release();
    _for = null;
  }
  
// ///////////////////////////////////////////// bean getter + setter

  public String getFor() {
    return _for;
  }

  public void setFor(String _for) {
    this._for = _for;
  }
}
