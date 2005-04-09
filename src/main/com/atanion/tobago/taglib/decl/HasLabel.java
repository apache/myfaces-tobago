package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:33:53 PM
 * User: bommel
 * $Id$
 */
public interface HasLabel {

  @TagAttribute void setLabel(String label);
}
