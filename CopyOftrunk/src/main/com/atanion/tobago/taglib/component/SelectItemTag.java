package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UISelectItem;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

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
