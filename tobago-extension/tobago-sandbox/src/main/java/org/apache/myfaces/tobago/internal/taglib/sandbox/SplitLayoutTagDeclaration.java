package org.apache.myfaces.tobago.internal.taglib.sandbox;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.internal.component.AbstractUIGridLayout;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBinding;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasBorder;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasCurrentMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasId;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargin;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMargins;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasMarkup;
import org.apache.myfaces.tobago.internal.taglib.declaration.HasSpacing;
import org.apache.myfaces.tobago.layout.Orientation;

@Tag(name = "splitLayout", bodyContent = BodyContent.EMPTY)
@UIComponentTag(
    uiComponent = "org.apache.myfaces.tobago.component.UISplitLayout",
    uiComponentBaseClass = "org.apache.myfaces.tobago.internal.component.AbstractUISplitLayout",
    uiComponentFacesClass = "javax.faces.component.UIComponentBase",
    componentFamily = AbstractUIGridLayout.COMPONENT_FAMILY,
    rendererType = "SplitLayout",
    allowedChildComponenents = "NONE", isLayout = true)
public interface SplitLayoutTagDeclaration extends HasId, HasBorder, HasSpacing, HasMargin,
    HasMargins, HasBinding, HasMarkup, HasCurrentMarkup {

  @TagAttribute
  @UIComponentTagAttribute(defaultValue = "1*;1*")
  void setLayout(String rows);

  @TagAttribute
  @UIComponentTagAttribute(allowedValues = {"HORIZONTAL", "VERTICAL"})
  void setOrientation(String orientation);

  /**
   * This attribute advises the layout manager, to not use space that comes from non rendered components.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean")
  void setRigid(String rigid);
}
