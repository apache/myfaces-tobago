/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.01.2004 09:36:50.
 * $Id$
 */
package com.atanion.tobago.el;

import com.atanion.tobago.taglib.component.TobagoTag;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.taglibs.standard.tag.el.core.ExpressionUtil;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class ElUtil {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ElUtil.class);

// ///////////////////////////////////////////// code

  public static String clipReferenceBackets(String reference) {
    if (reference.startsWith("#{") && reference.endsWith("}")) {
      return reference.substring(2, reference.length() - 1);
    } else {
      return reference;
    }
  }

  public static Object evaluateExpression(
      TobagoTag tag, String attribute, String expression, Class expectedType,
      PageContext pageContext) throws JspException {

    try {
      return ExpressionUtil.evalNotNull(tag.getClass().getName(),
          attribute, expression, expectedType, tag, pageContext);
    } catch (NullAttributeException e) {
      LOG.warn("Evaluating: '" + expression + "'", e); // todo: rethink this
      // explicitly allow 'null'
      return null;
    }
  }

  public static Object evaluateExpression(
      Tag tag, String attribute, String expression, Class expectedType,
      PageContext pageContext) throws JspException {

    try {
      return ExpressionUtil.evalNotNull("dummy",
          attribute, expression, expectedType, tag, pageContext);
    } catch (NullAttributeException e) {
      LOG.warn("Evaluating: '" + expression + "'", e); // todo: rethink this
      // explicitly allow 'null'
      return null;
    }
  }

}
