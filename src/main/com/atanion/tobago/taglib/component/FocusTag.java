/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Oct 28, 2002
 * Time: 1:55:28 PM
 * To change this template use Options | File Templates.
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIPage;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class FocusTag extends TobagoTag {
// ----------------------------------------------------------- class attributes

  private static final Log LOG = LogFactory.getLog(FocusTag.class);

// ----------------------------------------------------------------- attributes

  private String componentReference;

// ----------------------------------------------------------- business methods

  public int doEndTag() throws JspException {
    return EVAL_PAGE;
  }

  public int doStartTag() throws JspException {
    PageTag page = PageTag.findPageTag(pageContext);
    if (page != null) {
      ((UIPage)page.getComponentInstance()).storeFocusId(componentReference);
    } else {
      LOG.error("Can't find page-tag!", new Exception("No Page in Context"));
    }
    return SKIP_BODY;
  }

  public String getComponentType() { // fixme
    LOG.warn("Fixme: what is to do here?");
    return null;
  }

  public void release() {
    super.release();
    componentReference = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getComponentReference() {
    return componentReference;
  }

  public void setComponentReference(String componentReference) {
    this.componentReference = componentReference;
  }
}

