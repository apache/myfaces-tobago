package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:51:04 PM
 * User: bommel
 * $Id$
 */
public interface HasConverter {

  @TagAttribute void setConverter(String converter);
}
