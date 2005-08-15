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
 * Created 21.08.2002 at 18:00:22.
 * $Id: SolarForm.java 865 2004-04-30 18:02:34 +0200 (Fr, 30 Apr 2004) lofwyr $
 */
package org.apache.myfaces.tobago.demo.model.solar;

public class SolarForm {

// ///////////////////////////////////////////// attributes

  private Solar solar;

// ///////////////////////////////////////////// constructor

  public SolarForm() {
    solar = new Solar();
  }

// ///////////////////////////////////////////// bean getter + setter

  public Solar getSolar() {
    return solar;
  }

  public void setSolar(Solar solar) {
    this.solar = solar;
  }
}
