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

package org.apache.myfaces.tobago.lifecycle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import java.util.Iterator;

public class TobagoLifecycleFactory extends LifecycleFactory {

  private static final Log LOG = LogFactory.getLog(TobagoLifecycleFactory.class);

  private LifecycleFactory factory;
  private TobagoLifecycle defaultLifecycle;

  public TobagoLifecycleFactory(LifecycleFactory factory) {
    this.factory = factory;
    defaultLifecycle = new TobagoLifecycle();
    if (LOG.isInfoEnabled()) {
      LOG.info("new TobagoLifecycleFactory");
    }
  }

  public void addLifecycle(String lifecycleId, Lifecycle lifecycle) {
    factory.addLifecycle(lifecycleId, lifecycle);
    if (LOG.isInfoEnabled()) {
      LOG.info("Lifecycle added : " + lifecycleId + " = " + lifecycle.getClass().getName() + "");
    }
  }

  public Lifecycle getLifecycle(String lifecycleId) {
    if (LifecycleFactory.DEFAULT_LIFECYCLE.equals(lifecycleId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("getLifecycle(\"" + lifecycleId + "\")  -> TobagoLifecycle");
      }
      return defaultLifecycle;
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("getLifecycle(\"" + lifecycleId + "\")  -> other Lifecycle");
      }
      return factory.getLifecycle(lifecycleId);
    }
  }

  public Iterator getLifecycleIds() {
    if (LOG.isInfoEnabled()) {
      LOG.info("getLifecycleIds()");
    }
    return factory.getLifecycleIds();
  }
}
