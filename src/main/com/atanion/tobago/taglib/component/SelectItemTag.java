package com.atanion.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.component.UISelectItem;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 11, 2005
 * Time: 10:48:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class SelectItemTag extends TobagoTag {

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
