package org.apache.myfaces.tobago.example.demo.bestpractice;

public class BestPracticeController {

  public String throwException() {
    throw new RuntimeException("This exception is forced by the user.");
  }
}
