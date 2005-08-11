/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.BodyContentHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

public abstract class TobagoBodyTag extends TobagoTag implements BodyTag {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(TobagoBodyTag.class);

// ----------------------------------------------------------------- attributes

  protected BodyContent bodyContent;

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface BodyTag

  public void doInitBody() throws JspException {
  }

// ---------------------------- interface IterationTag


  public int doAfterBody() throws JspException {
    return SKIP_BODY;
  }

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    handleBodyContent();
    return super.doEndTag();
  }

  public void handleBodyContent() {
    UIComponent component = getComponentInstance();

    String rawContent = extractBodyContent();

    if (rawContent != null) {
      BodyContentHandler bodyContentHandler = (BodyContentHandler)
          component.getAttributes().get(ATTR_BODY_CONTENT);
      if (bodyContentHandler == null) {
        bodyContentHandler = new BodyContentHandler();
        component.getAttributes().put(
            ATTR_BODY_CONTENT, bodyContentHandler);
      }
      if (isSuppressed() || getComponentInstance().getRendersChildren()) {
        bodyContentHandler.addRawContent(rawContent, component, pageContext);
      } else {
        bodyContentHandler.addBodyContent(rawContent);
      }
    }
  }

  protected String extractBodyContent() {
    if (bodyContent != null) {
      String content = bodyContent.getString();
      bodyContent.clearBody();
      String tmp = new String(content); // fixme: is this not a bit slow? It is needed?
      tmp.replace('\n', ' ');
      tmp.replace('\n', ' ');
      if (tmp.trim().length() > 0) { // if there are only whitespaces: drop bodyContent
        return content;
      }
    }
    return null;
  }

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    clearBodyContentHandler(getComponentInstance());
    return result;
  }

  protected void clearBodyContentHandler(UIComponent component) {
    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(ATTR_BODY_CONTENT);
    if (bodyContentHandler != null) {
      bodyContentHandler.clearBodyContent();
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("bodyContentHandler == null!!! component=" + component);
      }
    }
  }

  protected int getDoStartValue() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  public void release() {
    super.release();
    bodyContent = null;
  }

// ------------------------------------------------------------ getter + setter

  public void setBodyContent(BodyContent bodyContent) {
    this.bodyContent = bodyContent;
  }
}

