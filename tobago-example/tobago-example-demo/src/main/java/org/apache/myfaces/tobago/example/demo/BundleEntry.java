package org.apache.myfaces.tobago.example.demo;

import java.io.Serializable;

public class BundleEntry implements Serializable {

  private String key;
  private String value;

  public BundleEntry(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
