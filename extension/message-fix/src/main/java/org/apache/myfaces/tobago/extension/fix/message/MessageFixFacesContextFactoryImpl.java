package org.apache.myfaces.tobago.extension.fix.message;

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

import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FacesException;

/*
 * Date: Aug 16, 2007
 * Time: 7:41:04 PM
 */
public class MessageFixFacesContextFactoryImpl extends FacesContextFactory {
  private static final Logger LOG = LoggerFactory.getLogger(MessageFixFacesContextFactoryImpl.class);
  private FacesContextFactory facesContextFactory;

  public MessageFixFacesContextFactoryImpl(FacesContextFactory facesContextFactory) {
    this.facesContextFactory = facesContextFactory;
    if (LOG.isDebugEnabled()) {
      LOG.debug("Wrap FacesContext for message fix");
    }
  }

  public FacesContext getFacesContext(Object context, Object request, Object response, Lifecycle lifecycle)
      throws FacesException {
     return new MessageFixFacesContext(facesContextFactory.getFacesContext(context, request, response, lifecycle));
  }
}
