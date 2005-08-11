package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 4:53:25 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a text input field.
 */
@Tag(name="in")
@BodyContentDescription(anyTagOf="facestag")
public interface InTag extends TextInputTag, HasValue, HasIdBindingAndRendered, HasConverter, IsReadonly, IsDisabled, HasWidth, HasOnchangeListener, IsInline, IsFocus, IsRequired, HasTip, HasLabelAndAccessKey {

  /**
   * Flag indicating whether or not this component should be rendered as
   * password field , so you will not see the typed charakters.
   */
  @TagAttribute
  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue = "false")
  void setPassword(String password);
}
