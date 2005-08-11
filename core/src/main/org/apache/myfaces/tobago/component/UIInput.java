/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 20, 2002 at 11:39:23 AM.
 * $Id$
 */
package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class UIInput extends javax.faces.component.UIInput {

  private static final Log LOG = LogFactory.getLog(UIInput.class);
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Input";

  public void updateModel(FacesContext facesContext) {
    if (ComponentUtil.mayUpdateModel(this)) {
      super.updateModel(facesContext);
    }
  }

  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.getLayout(this).layoutBegin(facesContext, this);    
    super.encodeBegin(facesContext);
  }

  public void encodeChildren(FacesContext facesContext) throws IOException {
   UILayout layout = UILayout.getLayout(this);
   if (layout instanceof UILabeledInputLayout) {
     if (isRendered() ) {
       layout.encodeChildrenOfComponent(facesContext, this);
     }
   } else {
     super.encodeChildren(facesContext);
   }
  }

  public void encodeEnd(FacesContext facesContext) throws IOException {
    if (! (UILayout.getLayout(this) instanceof UILabeledInputLayout)) {
      super.encodeEnd(facesContext);
    }
  }

}
