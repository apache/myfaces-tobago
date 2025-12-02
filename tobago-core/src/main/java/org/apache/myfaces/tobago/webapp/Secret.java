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

package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.internal.util.RandomUtils;

import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;
import java.io.Serial;
import java.io.Serializable;

public final class Secret implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public static final String KEY = Secret.class.getName();

  private final String secret;

  public Secret() {
    secret = RandomUtils.nextString();
  }

  public static Secret getInstance(final FacesContext facesContext) {
    return (Secret) facesContext.getExternalContext().getSessionMap().get(Secret.KEY);
  }

  /**
   * Create a secret attribute in the session.
   * Should usually be called in a {@link jakarta.servlet.http.HttpSessionListener}.
   */
  public static void create(final HttpSession session) {
    session.setAttribute(Secret.KEY, new Secret());
  }

  public boolean check(final String test) {
    return secret.equals(test);
  }

  public String getSecret() {
    return secret;
  }
}
