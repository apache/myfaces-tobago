package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class MetaController implements Serializable {

  private boolean formatDetection = true;

  public boolean isFormatDetection() {
    return formatDetection;
  }

  public void setFormatDetection(boolean formatDetection) {
    this.formatDetection = formatDetection;
  }
}
