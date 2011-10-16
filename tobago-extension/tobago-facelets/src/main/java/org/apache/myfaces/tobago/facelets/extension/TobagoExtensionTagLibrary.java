package org.apache.myfaces.tobago.facelets.extension;

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
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.facelets.TobagoComponentHandler;

public class TobagoExtensionTagLibrary extends AbstractTagLibrary {

  public static final String NAMESPACE = "http://myfaces.apache.org/tobago/extension";

  public TobagoExtensionTagLibrary() {
    super(NAMESPACE);
    addComponent("in", ComponentTypes.PANEL, "Panel", InExtensionHandler.class);
    addComponent("file", ComponentTypes.PANEL, "Panel", FileExtensionHandler.class);
    addComponent("date", ComponentTypes.PANEL, "Panel", DateExtensionHandler.class);
    addComponent("menuCheckbox", "org.apache.myfaces.tobago.MenuCommand", "MenuCommand",
        MenuCheckboxExtensionHandler.class);
    addComponent("menuRadio", "org.apache.myfaces.tobago.MenuCommand", "MenuCommand",
        MenuRadioExtensionHandler.class);
    addComponent("time", ComponentTypes.PANEL, "Panel", TimeExtensionHandler.class);
    addComponent("textarea", ComponentTypes.PANEL, "Panel", TextareaExtensionHandler.class);
    addComponent("selectBooleanCheckbox", ComponentTypes.PANEL, "Panel",
        SelectBooleanCheckboxExtensionHandler.class);
    addComponent("selectManyCheckbox", ComponentTypes.PANEL, "Panel",
        SelectManyCheckboxExtensionHandler.class);
    addComponent("selectManyListbox", ComponentTypes.PANEL, "Panel",
        SelectManyListboxExtensionHandler.class);
    addComponent("selectManyShuttle", ComponentTypes.PANEL, "Panel",
            SelectManyShuttleExtensionHandler.class);
    addComponent("selectOneChoice", ComponentTypes.PANEL, "Panel",
        SelectOneChoiceExtensionHandler.class);
    addComponent("selectOneRadio", ComponentTypes.PANEL, "Panel",
        SelectOneRadioExtensionHandler.class);
    addComponent("selectOneListbox", ComponentTypes.PANEL, "Panel",
        SelectOneListboxExtensionHandler.class);
    addComponent("separator", "org.apache.myfaces.tobago.Separator", "Separator",
        SeparatorExtensionHandler.class);
    addComponent("wizard", "org.apache.myfaces.tobago.Wizard", "Wizard", TobagoComponentHandler.class);
  }
}
