package org.apache.myfaces.tobago.facelets;

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

import com.sun.facelets.tag.AbstractTagLibrary;

/**
 * Date: 15.10.2007 15:08:23
 */
public class TobagoSandboxTagLibrary extends AbstractTagLibrary {

  public static final String NAMESPACE = "http://myfaces.apache.org/tobago/sandbox";

  public static final TobagoSandboxTagLibrary INSTANCE = new TobagoSandboxTagLibrary();

  public TobagoSandboxTagLibrary() {

    super(NAMESPACE);

//    addTobagoComponent("wizard", "org.apache.myfaces.tobago.Wizard", "Wizard", TobagoComponentHandler.class);
  }

  // fixme: double like TobagoTagLibrary
  protected final void addTobagoComponent(String name, String componentType, String rendererType, Class handlerType) {
    if (!containsTagHandler(getNamespace(), name)) {
      addComponent(name, componentType, rendererType, handlerType);
    }
  }

}
