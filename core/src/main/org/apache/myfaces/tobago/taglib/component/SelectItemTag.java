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
package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasId;

import javax.faces.component.UIComponent;
import static org.apache.myfaces.tobago.TobagoConstants.*;

/**
 * Add a child UISelectItem component to the UIComponent
 *  associated with the closed parent UIComponent custom
 *  action.
 */
@Tag(name="selectItem", bodyContent=BodyContent.EMPTY)
public class SelectItemTag extends TobagoTag implements HasBinding, HasId {

  private static final Log LOG = LogFactory.getLog(SelectItemTag.class);

  private String itemDescription;

  private String itemDisabled;

  private String itemLabel;

  private String itemValue;

  private String value;

  private String itemImage;


  public String getComponentType() {
    return UISelectItem.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, "itemDescription", itemDescription, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, "itemDisabled", itemDisabled, getIterationHelper());
    ComponentUtil.setStringProperty(component, "itemLabel", itemLabel, getIterationHelper());
    ComponentUtil.setStringProperty(component, "itemValue", itemValue, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, itemImage, getIterationHelper());
  }

  public void release() {
    super.release();
    itemDescription = null;
    itemDisabled = null;
    itemLabel = null;
    itemValue = null;
    value = null;
    itemImage = null;
  }

  public String getItemDescription() {
    return itemDescription;
  }
  /**
   * Flag indicating whether the option created
   *  by this component is disabled.
   */
  @TagAttribute @UIComponentTagAttribute(type={"java.lang.Boolean"}, defaultValue="false")

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public String getItemDisabled() {
    return itemDisabled;
  }

  /**
   * Flag indicating whether the option created
   *  by this component is disabled.
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.lang.Boolean"}, defaultValue="false")
  public void setItemDisabled(String itemDisabled) {
    this.itemDisabled = itemDisabled;
  }

  public String getItemLabel() {
    return itemLabel;
  }

  /**
   * Label to be displayed to the user for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setItemLabel(String itemLabel) {
    this.itemLabel = itemLabel;
  }

  public String getItemValue() {
    return itemValue;
  }

  /**
   * Value to be returned to the server if this option is selected by the user.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setItemValue(String itemValue) {
    this.itemValue = itemValue;
  }

  public String getValue() {
    return value;
  }

  /**
   * Value binding expression pointing at a SelectItem instance containing
   * the information for this option.
   */
  @TagAttribute @UIComponentTagAttribute(type="javax.faces.model.SelectItem")
  public void setValue(String value) {
    this.value = value;
  }

  public String getItemImage() {
    return itemImage;
  }

  /**
   * Image to be displayed to the user for this option.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setItemImage(String itemImage) {
    this.itemImage = itemImage;
  }
}
