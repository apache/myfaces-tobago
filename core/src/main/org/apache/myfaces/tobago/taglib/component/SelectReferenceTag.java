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
  * Created 29.03.2004 at 15:41:39.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a set of option related to and same type as the <strong>for</strong>
 * component.
 */
@Tag(name="selectReference", bodyContent=BodyContent.EMPTY)
public class SelectReferenceTag extends TobagoTag
    implements HasIdBindingAndRendered {
// ----------------------------------------------------------------- attributes

  private String _for;

  private String renderRange;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public String getFor() {
    return _for;
  }

  public void release() {
    super.release();
    _for = null;
    renderRange = null;
  }


  /**
   *  Id of the component, this is related to.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute()
  public void setFor(String _for) {
    this._for = _for;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_FOR, _for, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getRenderRange() {
    return renderRange;
  }


  /**
   *  Range of items to render.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute()
  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}
