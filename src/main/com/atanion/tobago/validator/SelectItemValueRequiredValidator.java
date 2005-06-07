package com.atanion.tobago.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.context.ResourceManagerUtil;

import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.application.FacesMessage;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jun 7, 2005
 * Time: 2:00:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectItemValueRequiredValidator implements Validator {

  private static final Log LOG = LogFactory.getLog(SelectItemValueRequiredValidator.class);
  public static final String MESSAGE_VALUE_REQUIRED
      = "tobago.SelectItemValueRequiredValidator.MESSAGE_VALUE_REQUIRED";
  public void validate(FacesContext context, UIComponent component, Object value)
      throws ValidatorException {
    UISelectOne selectOne = (UISelectOne) component;
    Object localValue = selectOne.getLocalValue();
    if (localValue == null || "".equals(localValue)) {
        String message = ResourceManagerUtil.getProperty(
            context, "tobago", MESSAGE_VALUE_REQUIRED);
        throw new ValidatorException(new FacesMessage(message));
    }
  }
}
