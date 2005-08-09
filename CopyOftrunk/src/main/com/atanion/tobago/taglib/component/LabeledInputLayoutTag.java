package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UILabeledInputLayout;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 20, 2005
 * Time: 6:44:48 PM
 * To change this template use File | Settings | File Templates.
 */
@Tag(name="labeledInputLayout", bodyContent=BodyContent.EMPTY)
public class LabeledInputLayoutTag extends TobagoTag {

  private String layout;

  private String layoutOrder;




  public String getComponentType() {
    return UILabeledInputLayout.COMPONENT_TYPE;
  }

  public String getLayout() {
    return layout;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(
        component, ATTR_COLUMNS, layout, getIterationHelper());
  }

  /**
   * LayoutConstraints for width layout.
   * Semicolon separated list of tree layout tokens ('*', '&lt;x>*', '&lt;x>px' or '&lt;x>%').
   * Where '*' is equvalent to '1*'.
   */
  @TagAttribute()
  @UIComponentTagAttribute()
  public void setLayout(String layout) {
    this.layout = layout;
  }

  public String getLayoutOrder() {
    return layoutOrder;
  }

  /**
   * layout order description.
   * default in standard theme is "LCP" -> label left, component middle and picker at right
   * anny combinations of this three letters are valid.
   */
//  @TagAttribute()
//  @UIComponentTagAttribute()
  public void setLayoutOrder(String layoutOrder) {
    this.layoutOrder = layoutOrder;
  }
}
