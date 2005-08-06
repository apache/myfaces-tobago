package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.UIComponentTag;
import com.atanion.util.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 4:08:32 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a 'hidden' input element.
 */
@Tag(name="hidden")
@UIComponentTag(UIComponent="com.atanion.tobago.component.UIInput")
public interface HiddenTag extends BeanTag, HasId, HasBinding, HasValue {

  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="true")
  public void setInline(String inline);

}
