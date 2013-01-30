package org.apache.myfaces.tobago.example.addressbook;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("test")
@RequestScoped
public class Test {

  private String test = "test";

  public String getTest() {
    return test;
  }

  public void setTest(String test) {
    this.test = test;
  }
}
