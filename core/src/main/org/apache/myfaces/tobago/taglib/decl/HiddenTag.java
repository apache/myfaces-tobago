package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
@UIComponentTag(UIComponent="org.apache.myfaces.tobago.component.UIInput")
public interface HiddenTag extends BeanTag, HasId, HasBinding, HasValue {

  @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="true")
  public void setInline(String inline);

}
