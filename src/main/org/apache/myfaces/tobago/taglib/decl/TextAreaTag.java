package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 5:00:41 PM
 * User: bommel
 * $Id: $
 */

/**
 * Renders a multiline text input control.
 */
@Tag(name="textarea", bodyContent= BodyContent.EMPTY)
public interface TextAreaTag extends TextInputTag, HasIdBindingAndRendered, HasValue, IsReadonly, IsDisabled, HasDimension, HasOnchangeListener, IsFocus, IsRequired, HasLabelAndAccessKey, HasTip {

  /**
   *  The row count for this component.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setRows(String rows);
}
