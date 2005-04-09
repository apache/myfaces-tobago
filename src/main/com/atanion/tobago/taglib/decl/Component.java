package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;

import javax.servlet.jsp.JspException;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:36:06 PM
 * User: bommel
 * $Id$
 */
public interface Component {

  @TagAttribute void setId(String s);
  /**
     * Flag indicating whether or not this component should be rendered
     * (during Render Response Phase), or processed on any subsequent form submit.
     *
     *  @param s
     */

  @TagAttribute void setRendered(String s);
  /**
     * The value binding expression linking this component to a property
     * in a backing bean
     *
     * @param binding
     * @throws JspException
     */
     
  @TagAttribute void setBinding(String binding) throws JspException;
}
