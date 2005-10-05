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
 * Created: 23.07.2002 19:33:37
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILabel;

import javax.faces.component.UIComponent;

public class LabelTag extends BeanTag implements
    org.apache.myfaces.tobago.taglib.decl.LabelTag {

  // TODO ?? _for ?
  private String _for;
  private String labelWithAccessKey;
  private String accessKey;
  private String tip;

  @Override
  public String getComponentType() {
    return UILabel.COMPONENT_TYPE;
  }

  public String getFor() {
    return _for;
  }

  @Override
  public void release() {
    super.release();
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
    _for = null;
  }

  public void setFor(String _for) {
    this._for = _for;
  }

  @Override
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_FOR, _for);
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey);
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip);
  }

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}

