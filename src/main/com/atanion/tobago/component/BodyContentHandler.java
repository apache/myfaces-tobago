/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 12.01.2004 10:56:46.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.taglib.component.TextTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Iterator;

public class BodyContentHandler {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(BodyContentHandler.class);

// ///////////////////////////////////////////// attribute

  private String bodyContent = "";
//  private List volatileList;
//  private int nextVolatileIndex;


  private TextTag verbatimTag;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  /**
   * @deprecated only for testing
   */
  private void debugging(UIComponent component) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    Iterator j = component.getChildren().iterator();
    for (; j.hasNext();) {
      UIComponent child = (UIComponent) j.next();
      LOG.debug("*** next");
      LOG.debug(" " + child.getClass().getName());
      LOG.debug(" " + child.getId());
      LOG.debug(" " + child.getClientId(facesContext));
      if (child.getRendererType().equals("Verbatim")) {
        LOG.debug(" " + ComponentUtil.currentValue(component));
      }
      LOG.debug("-------------------------------------------");
    }
  }


  public void addRawContent(String rawContent, UIComponent bodyComponent,
      PageContext pageContext) {

    javax.faces.component.UIOutput verbatim = null;
    prepareVerbatimTag();
    verbatimTag.setPageContext(pageContext);
    try {
      verbatimTag.doStartTag();
      verbatim =
          (javax.faces.component.UIOutput) verbatimTag.getComponentInstance();
      verbatim.setValue(rawContent);
      verbatim.setRendererType(verbatimTag.getRendererType());
      verbatimTag.doEndTag();
    } catch (JspException e) {
      LOG.debug("", e);
    }
  }

  private void prepareVerbatimTag() {
    if (verbatimTag == null) {
      verbatimTag = new TextTag();
    } else {
      verbatimTag.release();
    }
  }

  public void setBodyContent(String bodyContent) {
    this.bodyContent = bodyContent;
  }

  public String getBodyContent() {
    return bodyContent;
  }

  public void addBodyContent(String bodyContent) {
    this.bodyContent += bodyContent;
  }

  public void clearBodyContent() {
    bodyContent = "";
  }

// ///////////////////////////////////////////// bean getter + setter

}
