package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTag;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 5, 2005 3:32:40 PM
 * User: bommel
 * $Id: $
 */

/**
 * Renders a text
 */
@Tag(name="out")
@BodyContentDescription(anyTagOf="f:converter|f:convertNumber|f:convertDateTime|...")
@UIComponentTag(UIComponent="javax.faces.component.UIOutput")

public interface OutTag extends BeanTag, HasIdBindingAndRendered, HasConverter, IsInline, HasTip, HasValue {

  /**
   *  The current value of this component.
   */
  @TagAttribute @UIComponentTagAttribute()
  void setValue(String value);

  /**
   * Flag indicating that characters that are
   * sensitive in HTML and XML markup must be escaped.
   * This flag is set to "true" by default.
   */
  @TagAttribute
  @UIComponentTagAttribute(type={"java.lang.Boolean"}, defaultValue="true")
  void setEscape(String escape);

  /**
   * Indicate markup of this component.
   * Possible values are 'none', 'strong' and 'deleted'
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="none")
  void setMarkup(String markup);
}
