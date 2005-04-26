package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.StateHolder;
import com.atanion.tobago.event.StateChangeListener;
import com.atanion.tobago.taglib.decl.HasStateChangeListenerType;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;
import com.atanion.util.annotation.BodyContent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * Register an StateChangedListener instance on the UIComponent
 *  associated with the closest parent UIComponent custom action.
 */
@Tag(name="stateChangeListener", bodyContent=BodyContent.EMPTY)
public class StateChangeListenerTag extends TagSupport
    implements HasStateChangeListenerType{


  /**
   * <p>The fully qualified class name of the {@link StateChangeListener}
   * instance to be created.</p>
   */
  private String type = null;
  private String type_ = null;


  /**
   * Fully qualified Java class name of a StateChangeListener to be
   *  created and registered.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setType(String type) {

      this.type_ = type;

  }


  // --------------------------------------------------------- Public Methods


  /**
   * <p>Create a new instance of the specified {@link StateChangeListener}
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
      StateChangeListener handler = createStateChangeListener();

      UIComponent component = tag.getComponentInstance();
      if (component == null) {
          throw new JspException("com.sun.faces.NULL_COMPONENT_ERROR");
      }
      // We need to cast here because addStateChangeListener
      // method does not apply to al components (it is not a method on
      // UIComponent/UIComponentBase).
      if (component instanceof StateHolder) {
          ((StateHolder) component).addStateChangeListener(handler);
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
   * <p>Create and return a new {@link StateChangeListener} to be registered
   * on our surrounding {@link javax.faces.component.UIComponent}.</p>
   *
   * @throws javax.servlet.jsp.JspException if a new instance cannot be created
   */
  protected StateChangeListener createStateChangeListener()
      throws JspException {

      try {
          Class clazz = getClass().getClassLoader().loadClass(type);
          return ((StateChangeListener) clazz.newInstance());
      } catch (Exception e) {
          throw new JspException(e);
      }

  }



}
