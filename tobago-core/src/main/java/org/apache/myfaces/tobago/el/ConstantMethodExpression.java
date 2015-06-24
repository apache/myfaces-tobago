package org.apache.myfaces.tobago.el;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.MethodNotFoundException;
import javax.el.PropertyNotFoundException;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

public class ConstantMethodExpression extends MethodExpression implements StateHolder {

  private String outcome;

  private boolean transientFlag;

  public ConstantMethodExpression() {
  }

  public ConstantMethodExpression(String outcome) {
    this.outcome = outcome;
  }

  @Override
  public MethodInfo getMethodInfo(ELContext context)
      throws NullPointerException, PropertyNotFoundException, MethodNotFoundException, ELException {
    return null;
  }

  @Override
  public Object invoke(ELContext context, Object[] params)
      throws NullPointerException, PropertyNotFoundException, MethodNotFoundException, ELException {
    return outcome;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConstantMethodExpression that = (ConstantMethodExpression) o;

    return !(outcome != null ? !outcome.equals(that.outcome) : that.outcome != null);

  }

  @Override
  public int hashCode() {
    return outcome.hashCode();
  }

  @Override
  public String getExpressionString() {
    return outcome;
  }

  @Override
  public boolean isLiteralText() {
    return true;
  }

  @Override
  public Object saveState(FacesContext context) {
    return outcome;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    this.outcome = (String) state;
  }

  @Override
  public void setTransient(final boolean transientFlag) {
    this.transientFlag = transientFlag;
  }

  @Override
  public boolean isTransient() {
    return transientFlag;
  }
}
