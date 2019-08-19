package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.renderkit.html.JsonUtils;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class Select2Controller implements Serializable {

  private String one2;

  public String getOne2() {
    return one2;
  }

  public void setOne2(String one2) {
    this.one2 = one2;
  }

  public String getCaseSensitiveMatcher() {
    return "{\"matcher\": \"Tobago.Select2.caseSensitiveMatcher\"}";
//    JsonUtils.encode()
  }
}
