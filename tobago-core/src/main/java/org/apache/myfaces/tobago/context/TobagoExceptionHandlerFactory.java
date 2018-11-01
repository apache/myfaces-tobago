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

package org.apache.myfaces.tobago.context;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;

/**
 * To enable the TobagoExceptionHandler insert this class in the faces-config.xml like:
 * <pre>
 *   &lt;factory&gt;
 *     &lt;exception-handler-factory&gt;
 *       org.apache.myfaces.tobago.example.demo.TobagoExceptionHandlerFactory
 *     &lt;/exception-handler-factory&gt;
 *   &lt;/factory&gt;
 * </pre>
 *
 */
public class TobagoExceptionHandlerFactory extends ExceptionHandlerFactory {

  private ExceptionHandlerFactory parent;

  public TobagoExceptionHandlerFactory(final ExceptionHandlerFactory parent) {
    this.parent = parent;
  }

  @Override
  public ExceptionHandler getExceptionHandler() {
    ExceptionHandler result = parent.getExceptionHandler();
    result = new TobagoExceptionHandler(result);
    return result;
  }

}
