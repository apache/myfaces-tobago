package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_REQUIRED;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_SELECT_ONE_CHOICE;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 31, 2005
 * Time: 7:47:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class UISelectOne extends javax.faces.component.UISelectOne {

  private static final Log LOG = LogFactory.getLog(UISelectOne.class);
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.SelectOne";
  public static final String MESSAGE_VALUE_REQUIRED
      = "tobago.SelectOne.MESSAGE_VALUE_REQUIRED";



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

  public void validate(FacesContext facesContext) {
    if (ComponentUtil.getBooleanAttribute(this, ATTR_REQUIRED)) {

      Object localValue = getLocalValue();
      if (localValue == null || "".equals(localValue)) {
        FacesMessage facesMessage = MessageFactory.createFacesMessage(
            facesContext, MESSAGE_VALUE_REQUIRED, FacesMessage.SEVERITY_ERROR);
        facesContext.addMessage(getClientId(facesContext), facesMessage);
        setValid(false);
      }
    }
    super.validate(facesContext);
  }

}
