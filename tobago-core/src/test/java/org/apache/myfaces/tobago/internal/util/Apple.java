package org.apache.myfaces.tobago.internal.util;

public class Apple implements Fruit {

  public static final Apple SCHOENER_AUS_BOSKOOP = new Apple("Schöner aus Boskoop");
  public static final Apple GOLDEN_DELICIOUS = new Apple("Golden Delicious");

  private String name;

  public Apple(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return getName();
  }
}
