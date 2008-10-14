package org.apache.myfaces.tobago.layout;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Date: 14.02.2008 12:03:31
 */
public class LayoutContainerImpl extends LayoutComponentImpl implements LayoutContainer {

  private LayoutManager layoutManager;

  private Map<String, ContainerConstraints> containerConstraints;

  private List<LayoutComponent> components;

  public LayoutContainerImpl() {
    containerConstraints = new HashMap<String, ContainerConstraints>();
    components = new ArrayList<LayoutComponent>();
  }

  public ContainerConstraints getContainerConstraints(String name) {
    return containerConstraints.get(name);
  }

  public void setContainerConstraints(String name, ContainerConstraints containerConstraints) {
    this.containerConstraints.put(name, containerConstraints);
  }

  public List<LayoutComponent> getComponents() {
    return components;
  }

  public LayoutManager getLayoutManager() {
    return layoutManager;
  }
}
