package org.apache.myfaces.tobago.application;

import org.apache.myfaces.tobago.util.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class LabelValueBindingFacesMessage extends FacesMessage {
  private Locale locale;
  private Object[] args;

  public LabelValueBindingFacesMessage() {
		super();
	}

	public LabelValueBindingFacesMessage(FacesMessage.Severity severity, String summary, String detail,
      Locale locale, Object... args) {
		super(severity, summary, detail);
    this.locale = locale;
    this.args = args;
  }

	public LabelValueBindingFacesMessage(String summary, String detail) {
		super(summary, detail);
	}

	public LabelValueBindingFacesMessage(String summary) {
		super(summary);
	}

	@Override
	public String getDetail() {
    String detail = super.getDetail();
    if (args != null && args.length > 0) {
      if (args.length == 1 && UIComponentTag.isValueReference(args[0].toString())) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding value = facesContext.getApplication().createValueBinding(detail);
		    return MessageUtils.getFormatedMessage(detail, locale, value.getValue(facesContext));
      }
      return MessageUtils.getFormatedMessage(detail, locale, args);
    }
    return detail;
  }

  @Override
	public String getSummary() {
    String summary = super.getSummary();
    if (args != null && args.length > 0) {
      if (args.length == 1 && UIComponentTag.isValueReference(args[0].toString())) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ValueBinding value = facesContext.getApplication().createValueBinding(summary);
		    return MessageUtils.getFormatedMessage(summary, locale, value.getValue(facesContext));
      }
      return MessageUtils.getFormatedMessage(summary, locale, args);
    }
    return summary;
  }

}
