/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 17.01.2005 11:29:00.
 * $Id$
 */
package org.apache.myfaces.tobago.model;

public class PageStateImpl implements PageState {

  private int clientWidth;
  private int clientHeight;

  public int getClientWidth() {
    return clientWidth;
  }

  public void setClientWidth(int clientWidth) {
    this.clientWidth = clientWidth;
  }

  public int getClientHeight() {
    return clientHeight;
  }

  public void setClientHeight(int clientHeight) {
    this.clientHeight = clientHeight;
  }
}
