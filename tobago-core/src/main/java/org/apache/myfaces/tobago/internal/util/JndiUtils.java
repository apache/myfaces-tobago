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

package org.apache.myfaces.tobago.internal.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class JndiUtils {

  private static final Logger LOG = LoggerFactory.getLogger(JndiUtils.class);

  public static Object getJndiProperty(Context ctx, String... path) throws NamingException {
    return getJndiProperty(ctx, null, path);
  }

  public static Object getJndiProperty(Context ctx, Object defaultValue, String... path) throws NamingException {
    String name = "java:comp/env";
    // avoid error messages from websphere
    for (int i = 0; i < path.length; i++) {
      Binding b = getBinding(ctx, name, path[i]);
      if (b == null) {
        break;
      }
      if (i == path.length - 1) {
        Object obj = b.getObject();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Value: " + obj);
        }
        return obj;
      } else {
        name = name + "/" + path[i];
      }
    }
    return defaultValue;
  }

  private static Binding getBinding(Context ctx, String name, String path)
      throws NamingException {
    NamingEnumeration<Binding> ne = ctx.listBindings(name);
    while (ne.hasMore()) {
      Binding b = ne.next();
      if (LOG.isDebugEnabled()) {
        LOG.debug("Property: " + b.getName());
      }
      if (path.equals(b.getName())) {
        return b;
      }
    }
    return null;
  }

}
