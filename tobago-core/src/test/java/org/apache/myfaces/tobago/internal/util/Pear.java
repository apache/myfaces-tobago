package org.apache.myfaces.tobago.internal.util;

public class Pear implements Fruit {

  public static final Pear WILLIAMS_CHRIST =new Pear("Williams Christ");
  public static final Pear KOESTLICHE_AUS_CHARNEUX =new Pear("Köstliche aus Charneux");

  private String name;

  public Pear(String name) {
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
