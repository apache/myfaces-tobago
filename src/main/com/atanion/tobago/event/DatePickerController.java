package com.atanion.tobago.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Jul 18, 2005
 * Time: 3:24:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatePickerController extends MethodBinding {

  private static final Log LOG = LogFactory.getLog(DatePickerController.class);
  public static final String OPEN_POPUP = "openPopup";
  public static final String CLOSE_POPUP = "closePopup";

  private static DatePickerController instance;

  private DatePickerController() {
  }

  public static synchronized DatePickerController getInstance() {
    if (instance == null) {
      instance = new DatePickerController();
    }
    return instance;
  }

  public Object invoke(FacesContext facesContext, Object[] objects)
      throws EvaluationException, MethodNotFoundException {

    if (objects[0] instanceof ActionEvent) {
      javax.faces.component.UICommand command =
          (javax.faces.component.UICommand) ((ActionEvent) objects[0]).getSource();
      final String commandId = command.getId();
      LOG.info("commandId = " + commandId);
      if (LOG.isDebugEnabled()) {
        LOG.debug("commandId = " + commandId);
      }
      if (commandId.endsWith(OPEN_POPUP)) {
        final UIComponent popup
            = command.getFacet(TobagoConstants.FACET_PICKER_POPUP);
        if (popup != null) {
          popup.setRendered(true);
        }
      } else if (commandId.endsWith(CLOSE_POPUP)) {
        command.getParent().getParent().setRendered(false);
      }

    }
    return null;
  }

  public Class getType(FacesContext facesContext) throws MethodNotFoundException {
    return String.class;
  }
}
