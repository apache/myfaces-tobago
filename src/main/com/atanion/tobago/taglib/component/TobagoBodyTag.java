/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 16:19:49
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;

public abstract class TobagoBodyTag extends TobagoTag implements BodyTag {

// /////////////////////////////////////////// constants

  private static final Log LOG = LogFactory.getLog(TobagoBodyTag.class);

// /////////////////////////////////////////// attributes

  protected BodyContent bodyContent;

// /////////////////////////////////////////// constructors

  public TobagoBodyTag() {
  }

// /////////////////////////////////////////// code

  public void doInitBody() throws JspException {
  }

  public void release() {
    bodyContent = null;
    super.release();
  }

  public void setBodyContent(BodyContent bodyContent) {
    this.bodyContent = bodyContent;
  }

  protected int getDoStartValue() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  protected void setProperties(UIComponent component) {
//    clearBodyContentHandler(component);
    super.setProperties(component);
  }

  public int doStartTag() throws JspException {
    int result = super.doStartTag();
    clearBodyContentHandler(getComponentInstance());
    return result;
  }

  public int doEndTag() throws JspException {
//    Log.debug("doEndTag()   " + getRendererType());
    handleBodyContent();
    return super.doEndTag();
  }

  public int doAfterBody() throws JspException {
    return SKIP_BODY;
  }

  public void handleBodyContent() {
    UIComponent component = getComponentInstance();

    String rawContent = extractBodyContent();

    if (rawContent != null) {
//      LOG.debug("add new raw content = '" + rawContent + "'");
      BodyContentHandler bodyContentHandler = (BodyContentHandler)
          component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
      if (bodyContentHandler == null) {
        bodyContentHandler = new BodyContentHandler();
        component.getAttributes().put(
            TobagoConstants.ATTR_BODY_CONTENT, bodyContentHandler);
      }
      if (isSuppressed() || getComponentInstance().getRendersChildren()) {
//        LOG.debug("addRawContent  Component: " + this);
        bodyContentHandler.addRawContent(rawContent, component, pageContext);
      } else {
//        LOG.debug("addBodyContent  Component: " + this);
        bodyContentHandler.addBodyContent(rawContent);
      }
    }
  }

  protected String extractBodyContent() {
    if (bodyContent != null) {
      String content = bodyContent.getString();
      bodyContent.clearBody();
//      LOG.debug("extraxt bodyContent :\"" + content + "\"");
      String tmp = new String(content); // fixme: is this not a bit slow? It is needed?
      tmp.replace('\n', ' ');
      tmp.replace('\n', ' ');
      if (tmp.trim().length() > 0) { // if there are only whitespaces: drop bodyContent
        return content;
      }
    }
    return null;
  }

  protected void clearBodyContentHandler(UIComponent component) {
    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);
    if (bodyContentHandler != null) {
      bodyContentHandler.clearBodyContent();
    } else {
      LOG.debug("bodyContentHandler == null!!! component=" + component);
    }
  }

}
