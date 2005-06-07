package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.component.ComponentUtil;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 18, 2005
 * Time: 5:04:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextInputTag extends InputTag {
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
