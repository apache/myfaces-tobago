package org.apache.myfaces.tobago.model;

/**
 * User: lofwyr
 * Date: 23.10.2007 16:46:48
 */
public class BeanItem {

  private String type;
  private String name;
  private String value;

  public BeanItem(String type, String name, String value) {
    this.type = type;
    this.name = name;
    this.value = value;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return type + " " + name + " " + value;
  }
}
