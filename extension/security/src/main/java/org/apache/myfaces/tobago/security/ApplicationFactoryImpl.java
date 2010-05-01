package org.apache.myfaces.tobago.security;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;

/*
 * Date: 19.07.2006
 * Time: 15:42:14
 */
public class ApplicationFactoryImpl extends ApplicationFactory {
  private static final Logger LOG = LoggerFactory.getLogger(ApplicationFactoryImpl.class);

  private ApplicationFactory applicationFactory = null;

  public ApplicationFactoryImpl(ApplicationFactory applicationFactory) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Hiding ApplicationFactory");
    }
    this.applicationFactory = applicationFactory;
  }

  public Application getApplication() {
    return new ApplicationImpl(applicationFactory.getApplication());
  }

  public void setApplication(Application application) {
    applicationFactory.setApplication(application);
  }
}
