/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: May 14, 2002
 * Time: 6:00:59 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package org.apache.myfaces.tobago.demo.model;

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
