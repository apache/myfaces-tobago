package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasImage;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Mar 29, 2005
 * Time: 3:52:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Tag(name="toolBarCheck")
public class ToolBarSelectBooleanTag extends MenuSelectBooleanTag
    implements HasId, HasLabelAndAccessKey, HasImage, IsDisabled, HasAction,
               HasCommandType, HasValue, IsRendered, HasBinding,
               IsImmediateCommand {
  
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
