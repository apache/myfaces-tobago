/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIColumnSelector;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

/**
 * Renders a column with checkboxes to mark selected row's.
 */
@Tag(name="columnSelector", bodyContent=BodyContent.EMPTY)
public class ColumnSelectorTag extends TobagoTag
    implements IsDisabled, IsRendered, HasBinding
    {

  private static final Log LOG = LogFactory.getLog(ColumnSelectorTag.class);

  private String disabled ;

  public String getComponentType() {
    return UIColumnSelector.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_DISABLED, disabled, getIterationHelper());
  }

  public void release() {
    super.release();
    disabled = null;
  }

  public String getDisabled() {
    return disabled;
  }

  public void setDisabled(String disabled) {
    this.disabled = disabled;
  }
}
