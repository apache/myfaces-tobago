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

package org.apache.myfaces.tobago.example.demo.bestpractice;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.faces.context.FacesContext;

public class Bird {

  private static final Log LOG = LogFactory.getLog(Bird.class);

  private String name;
  private int size;

  public Bird(String name, int size) {
    this.name = name;
    this.size = size;
  }

  public String select() {
    LOG.info("Select Bird: name=" + name + " size=" + size);
    BestPracticeController controller = (BestPracticeController)
        VariableResolverUtil.resolveVariable(FacesContext.getCurrentInstance(), "bestPracticeController");
    controller.setStatus("Selected bird is " + name);
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }
}
