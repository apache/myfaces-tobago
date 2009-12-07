/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Dec 4, 2009
 * Time: 5:36:37 PM
 */
package org.apache.myfaces.tobago.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.util.ComponentFindUtils;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;

public class ValueBindingPopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Log LOG = LogFactory.getLog(ValueBindingPopupActionListener.class);

  private ValueBinding popupIdBinding;

  public ValueBindingPopupActionListener(Object binding) {
    popupIdBinding = (ValueBinding) binding;
  }

  @Override
  protected UIComponent getPopup(ActionEvent actionEvent) {
    String id = (String) popupIdBinding.getValue(FacesContext.getCurrentInstance());
    UIComponent popup = ComponentFindUtils.findComponent(actionEvent.getComponent(), id);
    if (popup == null) {
      LOG.error("Found no popup for \""
          + popupIdBinding.getExpressionString() + "\" := \""
          + id + "\"! Search base componentId : "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
    }
    return popup;
  }

  public boolean isTransient() {
    return false;
  }

  public void restoreState(FacesContext context, Object state) {
    Object values[] = (Object[]) state;
    popupIdBinding = (ValueBinding) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(FacesContext context) {
    Object values[] = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, popupIdBinding);
    return values;
  }


  public void setTransient(boolean newTransientValue) {
    // ignore
  }
}