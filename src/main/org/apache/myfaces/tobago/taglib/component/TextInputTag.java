package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIInput;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 18, 2005
 * Time: 5:04:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextInputTag extends InputTag implements com.atanion.tobago.taglib.decl.TextInputTag {
  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }

  public int doEndTag() throws JspException {
    UIComponent component = getComponentInstance();
    if (component.getFacet(FACET_LAYOUT) == null) {
      UIComponent layout = ComponentUtil.createLabeledInputLayoutComponent();
      component.getFacets().put(FACET_LAYOUT, layout);
    }
    return super.doEndTag();
  }
}
