package org.apache.myfaces.tobago.application;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.el.ValueExpression;

public class LabelValueExpressionFacesMessage extends FacesMessage {
  public LabelValueExpressionFacesMessage() {
    super();
  }

  public LabelValueExpressionFacesMessage(FacesMessage.Severity severity, String summary, String detail) {
    super(severity, summary, detail);
  }

  public LabelValueExpressionFacesMessage(String summary, String detail) {
    super(summary, detail);
  }

  public LabelValueExpressionFacesMessage(String summary) {
    super(summary);
  }

  @Override
  public String getDetail() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ValueExpression value = facesContext.getApplication().getExpressionFactory().
        createValueExpression(facesContext.getELContext(), super.getDetail(), String.class);
    return (String) value.getValue(facesContext.getELContext());
  }

  @Override
  public String getSummary() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ValueExpression value = facesContext.getApplication().getExpressionFactory().
        createValueExpression(facesContext.getELContext(), super.getSummary(), String.class);
    return (String) value.getValue(facesContext.getELContext());
  }


}
