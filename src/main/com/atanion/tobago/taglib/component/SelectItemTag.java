package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UISelectItem;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.HasItemDescription;
import com.atanion.tobago.taglib.decl.HasItemLabel;
import com.atanion.tobago.taglib.decl.HasItemValue;
import com.atanion.tobago.taglib.decl.HasItemImage;
import com.atanion.tobago.taglib.decl.IsItemDisabled;
import com.atanion.util.annotation.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

/**
 * Add a child UISelectItem component to the UIComponent
 *  associated with the closed parent UIComponent custom
 *  action.
 */
@Tag(name="selectItem", bodyContent="empty")
public class SelectItemTag extends TobagoTag
    implements HasBinding, HasId, HasValue, HasItemDescription, HasItemLabel,
    HasItemValue, HasItemImage, IsItemDisabled {

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

  public void setItemDescription(String itemDescription) {
    this.itemDescription = itemDescription;
  }

  public String getItemDisabled() {
    return itemDisabled;
  }

  public void setItemDisabled(String itemDisabled) {
    this.itemDisabled = itemDisabled;
  }

  public String getItemLabel() {
    return itemLabel;
  }

  public void setItemLabel(String itemLabel) {
    this.itemLabel = itemLabel;
  }

  public String getItemValue() {
    return itemValue;
  }

  public void setItemValue(String itemValue) {
    this.itemValue = itemValue;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getItemImage() {
    return itemImage;
  }

  public void setItemImage(String itemImage) {
    this.itemImage = itemImage;
  }
}
