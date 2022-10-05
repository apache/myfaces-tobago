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

import org.apache.myfaces.tobago.util.FacesVersion;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named
@ApplicationScoped
public class Version {

  private static final boolean CDI10 = hasClass("jakarta.enterprise.context.Conversation");
  private static final boolean CDI1112 = hasClass("jakarta.enterprise.context.Destroyed");
  private static final boolean CDI20 = hasClass("jakarta.enterprise.context.BeforeDestroyed");

  private static boolean hasClass(String clazz) {
    try {
      Class.forName(clazz);
      return true;
    } catch (ClassNotFoundException e) {
      return false;
    }
  }

  public boolean isVersion20() {
    return FacesVersion.supports20() && !FacesVersion.supports21();
  }

  public boolean isVersion21() {
    return FacesVersion.supports21() && !FacesVersion.supports22();
  }

  public boolean isVersion22() {
    return FacesVersion.supports22() && !FacesVersion.supports23();
  }

  public boolean isVersion23() {
    return FacesVersion.supports23();
  }

  public boolean isMojarra() {
    return FacesVersion.isMojarra();
  }

  public boolean isMyfaces() {
    return FacesVersion.isMyfaces();
  }

  public boolean isCdiVersion10() {
    return CDI10 && !CDI1112;
  }

  public boolean isCdiVersion1112() {
    return CDI1112 && !CDI20;
  }

  public boolean isCdiVersion20() {
    return CDI20;
  }
}
