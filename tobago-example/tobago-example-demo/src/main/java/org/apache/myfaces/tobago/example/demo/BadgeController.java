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
import java.util.ArrayList;
import java.util.List;

@SessionScoped
@Named
public class BadgeController implements Serializable {

  private List<String> badges;

  public BadgeController() {
    reset();
  }

  public void reset() {
    badges = new ArrayList<>();
    badges.add("primary");
    badges.add("secondary");
    badges.add("danger");
    badges.add("warning");
    badges.add("success");
    badges.add("info");
    badges.add("light");
    badges.add("dark");
  }

  public void remove(final String string) {
    badges.remove(string);
  }

  public List<String> getBadges() {
    return badges;
  }
}
