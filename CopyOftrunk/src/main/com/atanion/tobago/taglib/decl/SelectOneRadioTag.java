package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 6:11:03 PM
 * User: bommel
 * $Id: $
 */
/**
 *  Render a set of radiobuttons.
 */
@Tag(name="selectOneRadio")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public interface SelectOneRadioTag extends SelectOneTag, HasValue, IsDisabled, HasId, HasOnchangeListener, IsInline, HasRenderRange, IsRendered, HasBinding {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type="java.lang.Boolean")
  void setRequired(String required);
}
