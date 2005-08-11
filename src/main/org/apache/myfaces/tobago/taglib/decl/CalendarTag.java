package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 6, 2005 3:24:33 PM
 * User: bommel
 * $Id: $
 */
/**
 * Renders a calendar.
 */
@Tag(name="calendar", bodyContent= BodyContent.EMPTY)
@UIComponentTag(UIComponent="javax.faces.component.UIOutput")
public interface CalendarTag extends TobagoTag, HasIdBindingAndRendered, HasValue {

  /**
   *  The current value of this component.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.util.Calendar", "java.util.Date"})
  void setValue(String value);
}
