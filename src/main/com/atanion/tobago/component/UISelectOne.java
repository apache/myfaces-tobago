package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static com.atanion.tobago.TobagoConstants.ATTR_REQUIRED;
import static com.atanion.tobago.TobagoConstants.RENDERER_TYPE_SELECT_ONE_CHOICE;

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
  public static final String COMPONENT_TYPE = "com.atanion.tobago.SelectOne";

  private Validator validator;

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
    if (RENDERER_TYPE_SELECT_ONE_CHOICE.equals(getRendererType())
        && ComponentUtil.getBooleanAttribute(this, ATTR_REQUIRED)) {
      try {
        getSelectItemValueRequiredValidator().validate(facesContext, this, null);
      } catch (ValidatorException ve) {
        // If the validator throws an exception, we're
        // invalid, and we need to add a message
        setValid(false);
        FacesMessage message = ve.getFacesMessage();
        if (message != null) {
          message.setSeverity(FacesMessage.SEVERITY_ERROR);
          facesContext.addMessage(getClientId(facesContext), message);
        }
      }
    }
    super.validate(facesContext);
  }

  private Validator getSelectItemValueRequiredValidator() {
    if (validator == null) {
      final Application app = FacesContext.getCurrentInstance().getApplication();
      validator = app.createValidator("com.atanion.tobago.SelectItemValueRequiredValidator");
    }
    return validator;
  }

}
