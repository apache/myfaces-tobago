/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 17.04.2003 11:28:43.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITree;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.ActionSource;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionListener;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class ActionListenerTag extends TagSupport {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ActionListenerTag.class);

// ///////////////////////////////////////////// attribute

  private String type;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void setType(String type) {
    this.type = type;
  }

  public int doStartTag()
      throws JspException {
    UIComponentTag tag = UIComponentTag.getParentUIComponentTag(pageContext);

    if (tag == null) {
      throw new JspException("Not nested in a UIComponentTag");
    }

    if (!tag.getCreated()) {
      return SKIP_BODY;
    }

    UIComponent component = tag.getComponentInstance();

    if (component == null) {
      throw new JspException("No component found.");
    }

    if (component instanceof ActionSource) {
      ActionListener handler = ComponentUtil.createActionListener(type);
      ((ActionSource) component).addActionListener(handler);
    } else if (component instanceof UITree) {
      component.getAttributes().put(TobagoConstants.ATTR_ACTION_LISTENER, type);
    }

    return SKIP_BODY;
  }

  public void release() {
    type = null;
  }

// ///////////////////////////////////////////// bean getter + setter

}
