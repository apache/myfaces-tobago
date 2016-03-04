package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@SessionScoped
@Named
public class SelectOne implements Serializable {

  private String value;
  private List<String> types = Arrays.asList("A", "B", "C");

  public String submit() {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
        "Selected item: " + value, null));
    return null;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<String> getTypes() {
    return types;
  }

  public void setTypes(List<String> types) {
    this.types = types;
  }
}
