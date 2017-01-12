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

package org.apache.myfaces.tobago.facelets;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.ResourceResolver;
import java.io.IOException;
import java.net.URL;

/*
 * Was copied from MyFaces-Impl, because there is no JSF base class.
 */
public class DefaultResourceResolver extends ResourceResolver {

  public DefaultResourceResolver() {
    super();
  }

  @Override
  public URL resolveUrl(final String path) {
    try {
      return Resource.getResourceUrl(FacesContext.getCurrentInstance(), path);
    } catch (final IOException e) {
      throw new FacesException(e);
    }
  }

  public String toString() {
    return "DefaultResourceResolver";
  }

}
