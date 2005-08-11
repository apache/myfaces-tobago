package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 3:55:04 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a progressbar.
 */
@Tag(name="progress", bodyContent= BodyContent.EMPTY)
@UIComponentTag(UIComponent="javax.faces.component.UIOutput")
public interface ProgressTag extends BeanTag, HasIdBindingAndRendered, HasTip {

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"javax.swing.BoundedRangeModel"})
  void setValue(String value);
}
