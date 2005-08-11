package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Feb 28, 2005
 * Time: 3:05:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class UIPanel extends javax.faces.component.UIPanel {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Panel";


  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }




  public void encodeChildren(FacesContext facesContext) throws IOException {
   if (isRendered() ) {
     UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
   }
  }
}
