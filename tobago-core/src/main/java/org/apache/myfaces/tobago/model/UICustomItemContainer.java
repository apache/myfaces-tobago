package org.apache.myfaces.tobago.model;

import javax.faces.component.UISelectItems;
import java.util.Set;

public class UICustomItemContainer extends UISelectItems {

  public UICustomItemContainer() {
  }

  public UICustomItemContainer(Set<javax.faces.model.SelectItem> validCustomItems) {
    setValue(validCustomItems);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return true;
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}