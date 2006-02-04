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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_RENDER_RANGE;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a set of option related to and same type as the <strong>for</strong>
 * component.
 */
@Tag(name = "selectReference", bodyContent = BodyContent.EMPTY)
public class SelectReferenceTag extends TobagoTag
    implements HasIdBindingAndRendered {

  private String forComponent;
  private String renderRange;

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public String getFor() {
    return forComponent;
  }

  public void release() {
    super.release();
    forComponent = null;
    renderRange = null;
  }

  /**
   * Id of the component, this is related to.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, forComponent);
    ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange);
  }

  public String getRenderRange() {
    return renderRange;
  }

  /**
   * Range of items to render.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}
