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

import org.apache.myfaces.tobago.validator.FileItemValidator;

import javax.faces.view.facelets.MetaRuleset;
import javax.faces.view.facelets.ValidatorConfig;
import javax.faces.view.facelets.ValidatorHandler;

public class TobagoValidateHandler extends ValidatorHandler {

  public TobagoValidateHandler(final ValidatorConfig config) {
    super(config);
  }

  protected MetaRuleset createMetaRuleset(final Class aClass) {
    final MetaRuleset metaRuleset = super.createMetaRuleset(aClass);
    if (FileItemValidator.class.isAssignableFrom(aClass)) {
      metaRuleset.addRule(ContentTypeRule.INSTANCE);
    }
    return metaRuleset;
  }
}

