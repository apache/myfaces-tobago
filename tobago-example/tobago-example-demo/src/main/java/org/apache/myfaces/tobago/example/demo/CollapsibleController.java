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

import org.apache.myfaces.tobago.model.CollapseState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class CollapsibleController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(CollapsibleController.class);

  private CollapseState collapsed = CollapseState.hidden;

  public String action() {
    LOG.info("action(): collapsed={}", collapsed);
    return null;
  }

  public String cancel() {
    LOG.info("cancel(): collapsed={}", collapsed);
    return null;
  }

  public CollapseState getCollapsed() {
    return collapsed;
  }

  public void setCollapsed(CollapseState collapsed) {
    this.collapsed = collapsed;
  }
}
