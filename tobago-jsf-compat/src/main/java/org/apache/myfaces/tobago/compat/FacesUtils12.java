package org.apache.myfaces.tobago.compat;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.el.ValueExpression;


public class FacesUtils12 {

  public static Object getValueFromValueBindingOrValueExpression(FacesContext context, UIComponent component, String name) {
    return component.getValueExpression(name).getValue(context.getELContext());
  }

  public static boolean hasValueBindingOrValueExpression(UIComponent component, String name) {
    return component.getValueExpression(name) != null;
  }

  public static boolean isReadonlyValueBindingOrValueExpression(FacesContext context,
      UIComponent component, String name) {
    return component.getValueExpression(name).isReadOnly(context.getELContext());
  }

  public static String getExpressionString(UIComponent component, String name) {
    return component.getValueExpression(name).getExpressionString();
  }

  public static void setValueOfBindingOrExpression(FacesContext context, Object value,
      UIComponent component, String bindingName) {
    ValueExpression ve = component.getValueExpression(bindingName);
    if (ve != null) {
      ve.setValue(context.getELContext(), value);
    }
  }

  public static void copyValueBindingOrValueExpression(UIComponent fromComponent, String fromName,
      UIComponent toComponent, String toName) {
    ValueExpression ve = fromComponent.getValueExpression(fromName);
    if (ve != null) {
      toComponent.setValueExpression(toName, ve);
    }
  }
}
