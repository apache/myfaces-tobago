package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.event.TabChangeListener;
import com.atanion.tobago.taglib.decl.HasTabChangeListenerType;
import com.atanion.util.annotation.BodyContent;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

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
