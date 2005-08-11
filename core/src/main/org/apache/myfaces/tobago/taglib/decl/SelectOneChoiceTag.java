package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 6:05:28 PM
 * User: bommel
 * $Id: $
 */

/**
 * Render a single selection dropdown list.
 */
@Tag(name="selectOneChoice")
@BodyContentDescription(anyTagOf="(<f:selectItems>|<f:selectItem>|<t:selectItem>)+ <f:facet>* " )
public interface SelectOneChoiceTag extends SelectOneTag, HasId, HasValue, IsDisabled, IsReadonly, HasOnchangeListener, IsInline, HasLabelAndAccessKey, IsRendered, HasBinding, HasTip {

  /**
   * Flag indicating that selecting an Item representing a Value is Required.
   * If an SelectItem was choosen which underling value is an empty string an
   * ValidationError occurs and a Error Message is rendered.
   */
  @TagAttribute(type=String.class)
  @UIComponentTagAttribute(type="java.lang.Boolean")
  void setRequired(String required);
}
