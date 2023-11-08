package org.apache.myfaces.tobago.internal.util;

import jakarta.faces.component.UISelectItemGroups;
import jakarta.faces.model.SelectItem;
import jakarta.faces.model.SelectItemGroup;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectOneList;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class SelectItemUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void simple() {
    UISelectOneList oneList = new UISelectOneList();
    Iterable<SelectItem> itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Assertions.assertFalse(itemIterator.iterator().hasNext());

    UISelectItem item = new UISelectItem();
    oneList.getChildren().add(item);
    itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Iterator<SelectItem> iterator = itemIterator.iterator();
    Assertions.assertTrue(iterator.hasNext());
    Assertions.assertNotNull(iterator.next());
    Assertions.assertFalse(iterator.hasNext());
  }

  @Test
  void groups() {
    UISelectOneList oneList = new UISelectOneList();
    UISelectItemGroups groups = new UISelectItemGroups();
    UISelectItem item = new UISelectItem();
    groups.getChildren().add(item);
    oneList.getChildren().add(groups);
    Iterable<SelectItem> itemIterator = SelectItemUtils.getItemIterator(facesContext, oneList);
    Iterator<SelectItem> iterator = itemIterator.iterator();
    Assertions.assertTrue(iterator.hasNext());
    Object object = iterator.next();
    Assertions.assertNotNull(object);
    Assertions.assertInstanceOf(SelectItemGroup.class, object);
    SelectItemGroup selectItemGroup = (SelectItemGroup) object;
    SelectItem[] items = selectItemGroup.getSelectItems();
    Assertions.assertNotNull(items);
    Assertions.assertEquals(1, items.length);
    Assertions.assertFalse(iterator.hasNext());
  }
}
