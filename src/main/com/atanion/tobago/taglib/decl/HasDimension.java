package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:32:53 PM
 * User: bommel
 * $Id$
 */
public interface HasDimension {

  @TagAttribute void setHeight(String height);

  @TagAttribute void setWidth(String width);
}
