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

import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.Serializable;

public class Bird implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(Bird.class);

  private String name;
  private int size;

  public Bird(final String name, final int size) {
    this.name = name;
    this.size = size;
  }

  /**
   * Selects this bird in the controller. It would be nicer to implement this method in the controller, but this is to
   * show that it is possible.
   */
  public String select() {
    LOG.info("Select Bird: name=" + name + " size=" + size);
    final BirdController controller = (BirdController)
        VariableResolverUtils.resolveVariable(FacesContext.getCurrentInstance(), "birdController");
    controller.setStatus("Selected bird is " + name);
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getSize() {
    return size;
  }

  public void setSize(final int size) {
    this.size = size;
  }
}
