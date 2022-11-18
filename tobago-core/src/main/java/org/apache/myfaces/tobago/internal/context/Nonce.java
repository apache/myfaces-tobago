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

package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.internal.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;

public class Nonce implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String KEY = Nonce.class.getName();

  private Nonce() {
  }

  public static String getNonce(final FacesContext facesContext) {
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    String nonce = (String) viewRoot.getViewMap().get(KEY);
    if (nonce == null) {
      nonce = RandomUtils.nextString();
      LOG.debug("Creating nonce='{}'", nonce);
      viewRoot.getViewMap().put(KEY, nonce);
    }
    return nonce;
  }
}
