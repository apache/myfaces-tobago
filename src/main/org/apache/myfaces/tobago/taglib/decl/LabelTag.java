package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 3:48:09 PM
 * User: bommel
 * $Id: $
 */

/**
 * Renders a label component.
 */
@Tag(name="label", bodyContent= BodyContent.EMPTY)
@UIComponentTag(UIComponent="javax.faces.component.UIOutput")
public interface LabelTag extends BeanTag, HasIdBindingAndRendered, HasLabelWithAccessKey, HasFor, IsInline, HasWidth, HasTip, HasValue {

  /**
   *   Text value to display as label. Overwritten by 'labelWithAccessKey'
   */
  @TagAttribute @UIComponentTagAttribute()
  void setValue(String value);

}
