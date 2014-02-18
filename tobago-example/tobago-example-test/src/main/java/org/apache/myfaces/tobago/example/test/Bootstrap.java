package org.apache.myfaces.tobago.example.test;

public class Bootstrap {

  private String value;

  public Bootstrap() {
    reset();
  }

  public String save() {
    return "test/html/bootstrap.xhtml";
  }

  public String delete() {
    value = null;
    return "test/html/bootstrap.xhtml";
  }

  public String reset() {
    value = "initial value";
    return "test/html/bootstrap.xhtml";
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }
}
