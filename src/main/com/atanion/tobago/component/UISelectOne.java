package com.atanion.tobago.component;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UISelectOne extends javax.faces.component.UISelectOne {

  public static final String COMPONENT_TYPE = "com.atanion.tobago.SelectOne";

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
