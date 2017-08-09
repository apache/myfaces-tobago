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

package org.apache.myfaces.tobago.example.test;

import org.apache.myfaces.tobago.util.FacesVersion;

import javax.faces.context.FacesContext;

public class Version {

  public boolean isVersion11() {
    return !FacesVersion.supports12();
  }

  public boolean isVersion12() {
    return FacesVersion.supports12() && !FacesVersion.supports20();
  }

  public boolean isVersion20() {
    return FacesVersion.supports20() && !FacesVersion.supports21();
  }

  public boolean isVersion21() {
    return FacesVersion.supports21() && !FacesVersion.supports22();
  }

  public boolean isVersion22() {
    return FacesVersion.supports22();
  }

  public boolean isMojarra() {
    return FacesVersion.isMojarra();
  }

  public boolean isMyfaces() {
    return FacesVersion.isMyfaces();
  }

  public String getJsfVersion() {
    return FacesContext.class.getPackage().getImplementationVersion();
  }

  public String getJsfTitle() {
    return FacesContext.class.getPackage().getImplementationTitle();
  }
}
