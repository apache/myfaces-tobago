/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:21:55.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.mock.faces.MockFacesContext;
import com.atanion.mock.faces.MockRenderKit;
import com.atanion.mock.faces.MockExternalContext;
import com.atanion.mock.faces.MockViewTag;
import com.atanion.mock.servlet.MockPageContext;
import com.atanion.mock.servlet.MockServletContext;
import com.atanion.mock.servlet.MockHttpServletRequest;
import com.atanion.mock.servlet.MockHttpServletResponse;
import com.atanion.tobago.TobagoConstants;
import junit.framework.TestCase;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.webapp.UIComponentTag;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.context.ExternalContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Date;

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
      new GridLayoutTag(),
      new GroupBoxTag(),
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
      new Panel_GroupTag(),
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

