package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTag;
import com.atanion.util.annotation.UIComponentTagAttribute;

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
