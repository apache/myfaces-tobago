/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:21:55.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

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
      new MenubarTag(),
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

