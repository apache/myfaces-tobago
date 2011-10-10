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
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.apache.myfaces.tobago.validator.SubmittedValueLengthValidator;

public class AbstractTobagoTagLibrary  extends AbstractTagLibrary {

  public AbstractTobagoTagLibrary(String namespace) {
    super(namespace);
    addTagHandler("attribute", AttributeHandler.class);
    addTagHandler("tabChangeListener", TabChangeListenerHandler.class);
    addTagHandler("popupReference", PopupReferenceHandler.class);
    addTagHandler("resetInputActionListener", ResetInputActionListenerHandler.class);
    addTagHandler("loadBundle", LoadBundleHandler.class);
    addTagHandler("converter", ConverterHandler.class);
    addTagHandler(Tags.GRID_LAYOUT_CONSTRAINT, GridLayoutConstraintHandler.class);
    addValidator("validateFileItem", FileItemValidator.VALIDATOR_ID, TobagoValidateHandler.class);
    addValidator("validateSubmittedValueLength", SubmittedValueLengthValidator.VALIDATOR_ID);
    addTobagoComponent("script", "org.apache.myfaces.tobago.Script", "Script", ScriptHandler.class);
    addTobagoComponent("style", "org.apache.myfaces.tobago.Style", "Style", StyleHandler.class);
  }

  protected final void addTobagoComponent(String name, String componentType, String rendererType, Class handlerType) {
    if (!containsTagHandler(getNamespace(), name)) {
      addComponent(name, componentType, rendererType, handlerType);
    }
  }
}
