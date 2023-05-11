package org.apache.myfaces.tobago.internal.renderkit.renderer;

import jakarta.faces.component.behavior.AjaxBehavior;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectOneList;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;

public class SelectOneListRendererUnitTest extends RendererTestBase {

  @Test
  public void ajax() throws IOException {
    final UISelectOneList c = (UISelectOneList) ComponentUtils.createComponent(
        facesContext, Tags.selectOneList.componentType(), RendererTypes.SelectOneList, "id");
    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Stratocaster");
    i1.setItemValue("Stratocaster");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Telecaster");
    i2.setItemValue("Telecaster");
    c.getChildren().add(i2);
    final AjaxBehavior behavior =
        (AjaxBehavior) facesContext.getApplication().createBehavior(AjaxBehavior.BEHAVIOR_ID);
    behavior.setExecute(Arrays.asList("textarea"));
    behavior.setRender(Arrays.asList("panel"));
    c.addClientBehavior("change", behavior);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectOneList/selectOneListAjax.html"), formattedResult());
  }
}
