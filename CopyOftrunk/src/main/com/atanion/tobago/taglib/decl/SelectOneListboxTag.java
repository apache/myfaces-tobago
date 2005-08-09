package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 6:08:24 PM
 * User: bommel
 * $Id: $
 */
/**
 * Render a single selection option listbox.
 */
@Tag(name="selectOneListbox")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public interface SelectOneListboxTag extends SelectOneTag, HasId, HasValue, IsDisabled, IsReadonly, HasOnchangeListener, HasLabelAndAccessKey, IsRendered, HasBinding, HasHeight, HasTip {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute(type=String.class)
  @UIComponentTagAttribute(type="java.lang.Boolean")
  void setRequired(String required);
}
