package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UISelectItem;
import org.apache.myfaces.tobago.component.UISelectManyShuttle;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.faces.application.FacesMessage;
import java.io.IOException;

public class SelectManyShuttleRendererUnitTest extends RendererTestBase {

  @Test
  public void simple() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/simple.html"), formattedResult());
  }

  @Test
  public void label() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setLabel("label");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/label.html"), formattedResult());
  }
  //help


  @Test
  public void errorMessage() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setValid(false);
    facesContext.addMessage("id",
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/error-message.html"), formattedResult());
  }

  @Test
  public void help() throws IOException {
    final UISelectManyShuttle c = (UISelectManyShuttle) ComponentUtils.createComponent(
        facesContext, Tags.selectManyShuttle.componentType(), RendererTypes.SelectManyShuttle, "id");
    c.setHelp("Help!");

    final UISelectItem i1 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i1");
    i1.setItemLabel("Value 1");
    c.getChildren().add(i1);
    final UISelectItem i2 = (UISelectItem) ComponentUtils.createComponent(
        facesContext, Tags.selectItem.componentType(), null, "i2");
    i2.setItemLabel("Value 2");
    c.getChildren().add(i2);

    c.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/selectManyShuttle/help.html"), formattedResult());
  }
}
