/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jul 9, 2002
 * Time: 7:12:23 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class SelectItemsTag extends TobagoTag {

  private String value;

  public String getComponentType() {
    return UISelectItems.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    UISelectItems items = (UISelectItems) component;
    if (value != null) {
      if (isValueReference(value)) {
        ValueBinding valueBinding = FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
        items.setValueBinding("value", valueBinding);
      } else {
        items.setValue(value);
      }
    }

  }


  public void setValue(String value) {
    this.value = value;
  }
}
