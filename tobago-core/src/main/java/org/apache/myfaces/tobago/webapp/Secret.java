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
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class Secret implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String KEY = Secret.class.getName();

  private final String secret;

  public Secret() {
    secret = RandomUtils.nextString();
  }

  public boolean check(final String test) {
    return secret.equals(test);
  }

  public void encode(TobagoResponseWriter writer) throws IOException {
    writer.writeAttribute(HtmlAttributes.VALUE, this.secret, false);
  }
}
