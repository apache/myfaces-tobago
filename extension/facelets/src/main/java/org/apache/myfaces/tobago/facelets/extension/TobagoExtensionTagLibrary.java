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
import org.apache.myfaces.tobago.facelets.TobagoComponentHandler;

/*
 * Date: Jul 31, 2007
 * Time: 6:04:32 PM
 */
public class TobagoExtensionTagLibrary extends AbstractTagLibrary {

  public static final String NAMESPACE = "http://myfaces.apache.org/tobago/extension";

  public TobagoExtensionTagLibrary() {
    super(NAMESPACE);
    addComponent("in", "org.apache.myfaces.tobago.Panel", "Panel", InExtensionHandler.class);
    addComponent("file", "org.apache.myfaces.tobago.Panel", "Panel", FileExtensionHandler.class);
    addComponent("date", "org.apache.myfaces.tobago.Panel", "Panel", DateExtensionHandler.class);
    addComponent("menuCheckbox", "org.apache.myfaces.tobago.MenuCommand", "MenuCommand",
        MenuCheckboxExtensionHandler.class);
    addComponent("menuRadio", "org.apache.myfaces.tobago.MenuCommand", "MenuCommand",
        MenuRadioExtensionHandler.class);
    addComponent("time", "org.apache.myfaces.tobago.Panel", "Panel", TimeExtensionHandler.class);
    addComponent("textarea", "org.apache.myfaces.tobago.Panel", "Panel", TextAreaExtensionHandler.class);
    addComponent("selectBooleanCheckbox", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectBooleanCheckboxExtensionHandler.class);
    addComponent("selectManyCheckbox", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectManyCheckboxExtensionHandler.class);
    addComponent("selectManyListbox", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectManyListboxExtensionHandler.class);
    addComponent("selectOneChoice", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectOneChoiceExtensionHandler.class);
    addComponent("selectOneRadio", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectOneRadioExtensionHandler.class);
    addComponent("selectOneListbox", "org.apache.myfaces.tobago.Panel", "Panel",
        SelectOneListboxExtensionHandler.class);
    addComponent("separator", "org.apache.myfaces.tobago.Separator", "Separator",
        SeparatorExtensionHandler.class);
    addComponent("wizard", "org.apache.myfaces.tobago.Wizard", "Wizard", TobagoComponentHandler.class);
  }
}
