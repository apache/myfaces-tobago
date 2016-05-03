package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.component.UILabel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@SessionScoped
@Named
public class AccessKeyController implements Serializable {

  private UILabel labelComponent = new UILabel();
  private String hello = "Hello";
  private String im = "I'm";
  private String alabel = "a label";

  public AccessKeyController() {
    labelComponent.setValue(hello);
  }

  public UILabel getLabelComponent() {
    return labelComponent;
  }

  public void setLabelComponent(UILabel labelComponent) {
    if (labelComponent.getValue().equals(hello)) {
      labelComponent.setValue(im);
    } else if (labelComponent.getValue().equals(im)) {
      labelComponent.setValue(alabel);
    } else {
      labelComponent.setValue(hello);
    }

    this.labelComponent = labelComponent;
  }

  public Date getCurrentDate() {
    return new Date();
  }
}
