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

  private static Log log = LogFactory.getLog(FocusTag.class);

  private String componentReference;

  public String getComponentType() { // fixme
    log.warn("Fixme: what is to do here?");
    return null;
  }

  public int doStartTag() throws JspException {
    PageTag page = PageTag.findPageTag(pageContext);
    if (page != null) {
      ((UIPage)page.getComponentInstance()).storeFocusId(componentReference);
    } else {
      log.error("Can't find page-tag!", new Exception("No Page in Context"));
    }
    return SKIP_BODY;
  }

  public int doEndTag() throws JspException {
    return EVAL_PAGE;
  }

  public String getComponentReference() {
    return componentReference;
  }

  public void setComponentReference(String componentReference) {
    this.componentReference = componentReference;
  }

}
