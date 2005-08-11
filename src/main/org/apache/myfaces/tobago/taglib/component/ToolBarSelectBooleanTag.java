package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasAction;
import org.apache.myfaces.tobago.taglib.decl.HasBooleanValue;
import org.apache.myfaces.tobago.taglib.decl.HasCommandType;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.taglib.decl.IsImmediateCommand;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;


/**
 * Renders a selectable command button within a toolbar.
 */
@Tag(name="toolBarCheck")
public class ToolBarSelectBooleanTag extends SelectBooleanCommandTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, HasCommandType,
               HasImage, IsDisabled, HasAction, HasBooleanValue, IsImmediateCommand {

  private String image;


  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
  }

  public void release() {
    super.release();
    image = null;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
