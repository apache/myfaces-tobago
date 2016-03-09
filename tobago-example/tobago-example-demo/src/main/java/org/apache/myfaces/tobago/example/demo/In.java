package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@RequestScoped
@Named
public class In {

  private String changeValue;

  public String getChangeValue() {
    return changeValue;
  }

  public void setChangeValue(String changeValue) {
    this.changeValue = changeValue;
  }
}
