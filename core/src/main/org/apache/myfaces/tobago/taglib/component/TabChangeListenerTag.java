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
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UITabGroup;
import org.apache.myfaces.tobago.event.TabChangeListener;
import org.apache.myfaces.tobago.taglib.decl.HasTabChangeListenerType;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

// todo: maybe drop this tag and do that with an attribute in tabGroup
/**
 * Register an TabChangedListener instance on the UIComponent
 *  associated with the closest parent UIComponent custom action.
 */
@Tag(name="tabChangeListener", bodyContent=BodyContent.EMPTY)
public class TabChangeListenerTag extends TagSupport
    implements HasTabChangeListenerType {


  /**
   * <p>The fully qualified class name of the {@link TabChangeListener}
   * instance to be created.</p>
   */
  private String type = null;
  private String type_ = null;


  /**
   * Fully qualified Java class name of a TabChangeListener to be
   *  created and registered.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute()
  public void setType(String type) {

      this.type_ = type;

  }


  // --------------------------------------------------------- Public Methods


  /**
   * <p>Create a new instance of the specified {@link TabChangeListener}
   * class, and register it with the {@link javax.faces.component.UIComponent} instance associated
   * with our most immediately surrounding {@link javax.faces.webapp.UIComponentTag} instance, if
   * the {@link javax.faces.component.UIComponent} instance was created by this execution of the
   * containing JSP page.</p>
   *
   * @throws JspException if a JSP error occurs
   */
  public int doStartTag() throws JspException {

      // Locate our parent UIComponentTag
      UIComponentTag tag =
          UIComponentTag.getParentUIComponentTag(pageContext);
      if (tag == null) {
          throw new JspException("com.sun.faces.NOT_NESTED_IN_FACES_TAG_ERROR");
      }

      // Nothing to do unless this tag created a component
      if (!tag.getCreated()) {
          return (SKIP_BODY);
      }

      // evaluate any VB expression that we were passed
      if (UIComponentTag.isValueReference(type_)) {
        ValueBinding valueBinding = ComponentUtil.createValueBinding(type_, null);
        type = (String) valueBinding.getValue(FacesContext.getCurrentInstance());
      } else {
        type = type_;
      }

      // Create and register an instance with the appropriate component
      TabChangeListener handler = createStateChangeListener();

      UIComponent component = tag.getComponentInstance();
      if (component == null) {
          throw new JspException("com.sun.faces.NULL_COMPONENT_ERROR");
      }
      // We need to cast here because addTabChangeListener
      // method does not apply to al components (it is not a method on
      // UIComponent).
      if (component instanceof UITabGroup) {
          ((UITabGroup) component).addTabChangeListener(handler);
      }

      return (SKIP_BODY);

  }


  /**
   * <p>Release references to any acquired resources.
   */
  public void release() {

      this.type = null;

  }


  // ------------------------------------------------------ Protected Methods


  /**
   * <p>Create and return a new {@link TabChangeListener} to be registered
   * on our surrounding {@link javax.faces.component.UIComponent}.</p>
   *
   * @throws javax.servlet.jsp.JspException if a new instance cannot be created
   */
  protected TabChangeListener createStateChangeListener()
      throws JspException {

      try {
          Class clazz = getClass().getClassLoader().loadClass(type);
          return ((TabChangeListener) clazz.newInstance());
      } catch (Exception e) {
          throw new JspException(e);
      }

  }



}
