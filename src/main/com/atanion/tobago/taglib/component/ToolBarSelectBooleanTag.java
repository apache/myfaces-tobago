package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasImage;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasBooleanValue;
import com.atanion.util.annotation.Tag;

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
