/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * All rights reserved. Created 25.08.2004 10:21:55.
 * $Id:GenericTestBase.java 1300 2005-08-10 16:40:23 +0200 (Mi, 10 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.taglib.component;

import junit.framework.TestCase;

import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.tagext.Tag;

abstract public class GenericTestBase extends TestCase {
// ----------------------------------------------------------- class attributes

// ----------------------------------------------------------------- attributes

  protected UIComponentTag[] componentTagList;
  protected Tag[] ordinaryTagList;

// --------------------------------------------------------------- constructors

  public GenericTestBase(String name) {
    super(name);
    componentTagList = new UIComponentTag[]{
      new ButtonTag(),
      new CalendarTag(),
      new CellTag(),
      new SelectManyCheckboxTag(),
      new SelectBooleanCheckboxTag(),
      new ColumnTag(),
      new DateTag(),
      new FileTag(),
      new FormTag(),
      new GridLayoutTag(),
      new BoxTag(),
      new HiddenTag(),
      new ImageTag(),
      new InTag(),
      new SelectReferenceTag(),
      new LabelTag(),
      new LinkTag(),
      new MenuBarTag(),
      new MessagesTag(),
      new MessageTag(),
      new SelectManyListboxTag(),
      new PageTag(),
      new PanelTag(),
      new ProgressTag(),
      new SelectOneRadioTag(),
      new RichTextEditorTag(),
      new SheetTag(),
      new SelectOneTag(),
      new TabGroupTag(),
      new TabTag(),
      new TextAreaTag(),
      new OutTag(),
      new ToolBarTag(),
      new TreeTag(),
    };
    ordinaryTagList = new Tag[]{
      new IncludeTag(),
      new LoadBundleTag(),
      new ScriptTag(),
      new StyleTag(),
    };
  }

}

