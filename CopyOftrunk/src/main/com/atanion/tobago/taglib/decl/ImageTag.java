package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTag;
import com.atanion.util.annotation.UIComponentTagAttribute;



/*
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Aug 6, 2005 3:19:55 PM
 * User: bommel
 * $Id: $
 */
/**
 *  Renders a Image.
 */
@Tag(name="image", bodyContent= BodyContent.EMPTY)
@UIComponentTag(UIComponent="javax.faces.component.UIGraphic")
public interface ImageTag extends TobagoTag, HasIdBindingAndRendered, HasBorder, HasDimension, HasTip {

  /**
   *
   * Absolute url to an image or image name to lookup in tobago resource path
   *
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute()
  void setValue(String value);

  /**
   *
   *  Alternate textual description of the image rendered by this component.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setAlt(String alt);

}
