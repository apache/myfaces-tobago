package org.apache.myfaces.tobago.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.util.ComponentFindUtils;

import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


public class ValueExpressionPopupActionListener extends AbstractPopupActionListener implements StateHolder {

  private static final Log LOG = LogFactory.getLog(ValueExpressionPopupActionListener.class);

  private ValueExpression popupIdExpression;

  public ValueExpressionPopupActionListener(Object expression) {
    popupIdExpression = (ValueExpression) expression;
  }

  @Override
  protected UIComponent getPopup(ActionEvent actionEvent) {
    String id = (String) popupIdExpression.getValue(FacesContext.getCurrentInstance().getELContext());
    UIComponent popup = ComponentFindUtils.findComponent(actionEvent.getComponent(), id);
    if (popup == null) {
      LOG.error("Found no popup for \""
          + popupIdExpression.getExpressionString() + "\" := \""
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
    popupIdExpression = (ValueExpression) UIComponentBase.restoreAttachedState(context, values[0]);
  }

  public Object saveState(FacesContext context) {
    Object values[] = new Object[1];
    values[0] = UIComponentBase.saveAttachedState(context, popupIdExpression);
    return values;
  }


  public void setTransient(boolean newTransientValue) {
    // ignore
  }
}