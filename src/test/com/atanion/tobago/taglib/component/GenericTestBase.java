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
      new CheckBoxGroupTag(),
      new CheckBoxTag(),
      new Color16ChooserTag(),
      new ColumnTag(),
      new DateTag(),
      new FileTag(),
      new FocusTag(),
      new FormTag(),
      new FormattedTag(),
      new GridLayoutTag(),
      new BoxTag(),
      new HiddenTag(),
      new ImageTag(),
      new ItemsTag(),
      new LabelTag(),
      new LinkTag(),
      new MenubarTag(),
      new MessagesTag(),
      new MessageTag(),
      new MultiSelectTag(),
      new PageTag(),
      new PanelTag(),
      new ProgressTag(),
      new RadioGroupTag(),
      new RichTextEditorTag(),
      new SheetTag(),
      new SingleSelectTag(),
      new TabGroupTag(),
      new TabTag(),
      new TextAreaTag(),
      new TextBoxTag(),
      new TextTag(),
      new ToolbarTag(),
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

