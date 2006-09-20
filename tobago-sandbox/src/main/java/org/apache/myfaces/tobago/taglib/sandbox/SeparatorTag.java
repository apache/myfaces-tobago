package org.apache.myfaces.tobago.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.taglib.component.TobagoTag;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.component.UISeparator;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 18, 2006
 * Time: 8:02:34 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Renders a separator.
 */
@Tag(name = "separator", bodyContent = BodyContent.EMPTY)
@UIComponentTag(rendererType = "Separator",
    uiComponent = "org.apache.myfaces.tobago.component.UISeparator")


public class SeparatorTag  extends TobagoTag implements HasIdBindingAndRendered {

   public String getComponentType() {
    return UISeparator.COMPONENT_TYPE;
  }


}
