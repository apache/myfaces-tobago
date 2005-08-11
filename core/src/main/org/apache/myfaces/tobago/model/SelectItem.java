package org.apache.myfaces.tobago.model;

import org.apache.myfaces.tobago.component.UISelectItem;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 5, 2005
 * Time: 6:11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectItem extends javax.faces.model.SelectItem {

  private String image;

  public SelectItem() {
    super();
  }

  public SelectItem(UISelectItem component) {
    super(component.getItemValue() == null ? "" : component.getItemValue(),
        component.getItemLabel(), component.getItemDescription(),
        component.isItemDisabled());
    image = component.getItemImage();
  }

  public SelectItem(Object value) {
    super(value);
  }

  public SelectItem(Object value, String label) {
    super(value, label);
  }

  public SelectItem(Object value, String label, String description) {
    super(value, label, description);
  }

  public SelectItem(Object value, String label, String description, String image) {
    super(value, label, description);
    this.image = image;
  }

  public SelectItem(Object value, String label, String description,
                    boolean disabled, String image) {
    super(value, label, description, disabled);
    this.image = image;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
