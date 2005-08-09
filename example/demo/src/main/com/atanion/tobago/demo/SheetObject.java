/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 14, 2002
 * Time: 6:00:59 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.atanion.tobago.demo;

public class SheetObject {
  protected int index;
  protected String elementIndex;


  public SheetObject (int index){
    this.index = index;
    elementIndex = "zeile " + index;
  }

  public int getIndex(){
    return index;
  }

  public void setElementIndex(String index){
    elementIndex = index;
  }

  public String getElementIndex() {
    return elementIndex;
  }

  public String getLink(){
    return "Sheet.Test.link?index=" + index;
  }

  public String getLinkText(){
    return "" + index + ". Element";
  }
}
