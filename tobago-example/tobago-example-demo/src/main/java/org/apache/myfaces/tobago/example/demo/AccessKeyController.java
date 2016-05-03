/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.component.UILabel;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;

@SessionScoped
@Named
public class AccessKeyController implements Serializable {

  private UILabel labelComponent = new UILabel();
  private String hello = "Hello";
  private String im = "I'm";
  private String alabel = "a label";

  public AccessKeyController() {
    labelComponent.setValue(hello);
  }

  public UILabel getLabelComponent() {
    return labelComponent;
  }

  public void setLabelComponent(UILabel labelComponent) {
    if (labelComponent.getValue().equals(hello)) {
      labelComponent.setValue(im);
    } else if (labelComponent.getValue().equals(im)) {
      labelComponent.setValue(alabel);
    } else {
      labelComponent.setValue(hello);
    }

    this.labelComponent = labelComponent;
  }

  public Date getCurrentDate() {
    return new Date();
  }
}
