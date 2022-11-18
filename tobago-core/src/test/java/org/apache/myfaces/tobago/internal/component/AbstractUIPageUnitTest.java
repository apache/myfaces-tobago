package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbstractUIPageUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testCutIteratorFromId() {

    final AbstractUIPage page = (AbstractUIPage) ComponentUtils.createComponent(
        facesContext, Tags.page.componentType(), RendererTypes.Page, null);

    Assertions.assertEquals("abc", page.cutIteratorFromId("abc"));

    Assertions.assertEquals("a:b:c", page.cutIteratorFromId("a:b:c"));

    Assertions.assertEquals("a:c", page.cutIteratorFromId("a:5:c"));

    Assertions.assertEquals("a:c", page.cutIteratorFromId("a:55555555555555:c"));

    Assertions.assertEquals("a:c", page.cutIteratorFromId("a:555:555:555:55555:c"));

    Assertions.assertEquals("", page.cutIteratorFromId(""));

    Assertions.assertEquals("", page.cutIteratorFromId("5"));

    Assertions.assertEquals("sheet", page.cutIteratorFromId("sheet:5"));
  }
}
