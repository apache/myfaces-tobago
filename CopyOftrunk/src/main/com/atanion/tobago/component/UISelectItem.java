package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;

import javax.faces.el.ValueBinding;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 11, 2005
 * Time: 11:15:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class UISelectItem extends javax.faces.component.UISelectItem {

  public static final String COMPONENT_TYPE = "com.atanion.tobago.SelectItem";


  private String itemImage;

  public void setImage(String image) {
    setItemImage(image);
  }

  public String getItemImage() {
    if (itemImage != null) {
      return itemImage;
    }

    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_IMAGE);
    if (vb != null) {
      return ((String) vb.getValue(getFacesContext()));
    } else {
      return null;
    }
  }

  public void setItemImage(String itemImage) {
    this.itemImage = itemImage;
  }

}
