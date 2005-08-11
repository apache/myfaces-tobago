package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UISelectMany extends javax.faces.component.UISelectMany {

  private static final Log LOG = LogFactory.getLog(UISelectMany.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectMany";



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
