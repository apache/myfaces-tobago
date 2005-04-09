package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:25:12 PM
 * User: bommel
 * $Id$
 */
public interface HasDoctype {
  /**
   * values for doctype :
        'strict'   : HTML 4.01 Strict DTD
        'loose'    : HTML 4.01 Transitional DTD
        'frameset' : HTML 4.01 Frameset DTD
        all other values are ignored and no DOCTYPE is set.
        default value is 'loose'
   *
   * @param doctype
   */
  @TagAttribute void setDoctype(String doctype);
}
