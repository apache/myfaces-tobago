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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named
public class SplitLayoutController implements Serializable {

  private String horizontalLayout = "1fr 1fr";

  private String verticalLayout = "1fr 1fr";

  public String getHorizontalLayout() {
    return horizontalLayout;
  }

  public void setHorizontalLayout(final String horizontalLayout) {
    this.horizontalLayout = horizontalLayout;
  }

  public String getVerticalLayout() {
    return verticalLayout;
  }

  public void setVerticalLayout(final String verticalLayout) {
    this.verticalLayout = verticalLayout;
  }

  public String getVerticalLayoutFirstToken() {
    return verticalLayout.split(" ")[0];
  }

  public String getVerticalLayoutSecondToken() {
    return verticalLayout.split(" ")[1];
  }

  public Object getHorizontalLayoutFirstToken() {
    return horizontalLayout.split(" ")[0];
  }

  public Object getHorizontalLayoutSecondToken() {
    return horizontalLayout.split(" ")[1];
  }
}
