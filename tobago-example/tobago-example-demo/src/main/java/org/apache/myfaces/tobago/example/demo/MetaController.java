package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class MetaController implements Serializable {

  private boolean formatDetection = true;

  public boolean isFormatDetection() {
    return formatDetection;
  }

  public void setFormatDetection(final boolean formatDetection) {
    this.formatDetection = formatDetection;
  }

  public boolean isRenderNoindex() {
    final String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
    return viewId.contains("content/35-deprecated") || viewId.contains("content/40-test");
  }
}
