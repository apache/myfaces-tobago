/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.taglib.extension;

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.ExtensionTag;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUICommandBase;
import org.apache.myfaces.tobago.internal.taglib.MenuCommandTag;
import org.apache.myfaces.tobago.internal.taglib.SelectOneRadioTag;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import javax.faces.component.UIComponent;
import javax.faces.webapp.FacetTag;
import javax.servlet.jsp.JspException;

/**
 * Renders menu items like radio buttons (select one).
 * <pre>
 * &lt;tx:menuRadio>
 *   &lt;tc:selectItems/> &lt;!-- body -->
 * &lt;/tx:menuRadio></pre>
 * is the short form of
 * <pre>
 * &lt;tc:menuCommand>
 *   &lt;f:facet name="radio">
 *     &lt;tc:selectOneRadio>
 *       &lt;tc:selectItems/> &lt;!-- body -->
 *     &lt;/tc:selectOneRadio>
 *   &lt;/f:facet>
 * &lt;/tc:menuCommand></pre>
 */
@Tag(
    name = "menuRadio",
    tagExtraInfoClassName = "org.apache.myfaces.tobago.internal.taglib.component.CommandTagExtraInfo")
@ExtensionTag(
    baseClassName = "org.apache.myfaces.tobago.internal.taglib.MenuRadioTag",
    componentType = "org.apache.myfaces.tobago.MenuCommand",
    rendererType = "MenuCommand",
    faceletHandler = "org.apache.myfaces.tobago.facelets.extension.MenuRadioExtensionHandler")
public class MenuRadioExtensionTag extends TobagoExtensionBodyTagSupport {

  private javax.el.ValueExpression rendered;
  private javax.el.ValueExpression value;

  private MenuCommandTag menuCommandTag;
  private SelectOneRadioTag inTag;
  private FacetTag facetTag;
  private javax.el.MethodExpression action;
  private javax.el.MethodExpression actionListener;
  private javax.el.ValueExpression onclick;
  private javax.el.ValueExpression link;
  private javax.el.ValueExpression disabled;
  private javax.el.ValueExpression binding;
  private javax.el.ValueExpression label;
  private javax.el.ValueExpression immediate;
  private javax.el.ValueExpression transition;
  private javax.el.ValueExpression converter;
  private javax.el.ValueExpression renderedPartially;
  private String fieldId;

  @Override
  public int doStartTag() throws JspException {

    menuCommandTag = new MenuCommandTag();
    menuCommandTag.setPageContext(pageContext);
    menuCommandTag.setParent(getParent());
    if (id != null) {
      menuCommandTag.setId(id);
    }
    if (rendered != null) {
      menuCommandTag.setRendered(rendered);
    }
    if (action != null) {
      menuCommandTag.setAction(action);
    }
    if (actionListener != null) {
      menuCommandTag.setActionListener(actionListener);
    }
    if (onclick != null) {
      menuCommandTag.setOnclick(onclick);
    }
    if (link != null) {
      menuCommandTag.setLink(link);
    }
    if (disabled != null) {
      menuCommandTag.setDisabled(disabled);
    }
    if (binding != null) {
      menuCommandTag.setBinding(binding);
    }
    if (label != null) {
      menuCommandTag.setLabel(label);
    }
    if (immediate != null) {
      menuCommandTag.setImmediate(immediate);
    }
    if (transition != null) {
      menuCommandTag.setTransition(transition);
    }
    if (renderedPartially != null) {
      menuCommandTag.setRenderedPartially(renderedPartially);
    }
    menuCommandTag.setJspId(nextJspId());
    menuCommandTag.doStartTag();

    facetTag = new FacetTag();
    facetTag.setPageContext(pageContext);
    facetTag.setParent(menuCommandTag);
    facetTag.setName(Facets.RADIO);

    facetTag.doStartTag();
    inTag = new SelectOneRadioTag();
    inTag.setPageContext(pageContext);
    inTag.setParent(facetTag);
    if (converter != null) {
      inTag.setConverter(converter);
    }
    if (value != null) {
      inTag.setValue(value);
    }
    if (fieldId != null) {
      inTag.setId(fieldId);
    }
    inTag.setJspId(nextJspId());
    inTag.doStartTag();

    return super.doStartTag();
  }

  @Override
  public int doEndTag() throws JspException {

    if (renderedPartially == null) {
      // Move attribute renderedPartially from selectOne to menuCommand component
      final UIComponent inComponent = inTag.getComponentInstance();
      final AbstractUICommandBase command = (AbstractUICommandBase) menuCommandTag.getComponentInstance();
      final javax.el.ValueExpression expression = inComponent.getValueExpression(Attributes.RENDERED_PARTIALLY);
      if (expression != null) {
        command.setValueExpression(Attributes.RENDERED_PARTIALLY, expression);
      } else {
        final Object renderedPartially = inComponent.getAttributes().get(Attributes.RENDERED_PARTIALLY);
        command.setRenderedPartially(StringUtils.split((String) renderedPartially, ", "));
      }
    }

    inTag.doEndTag();
    facetTag.doEndTag();
    menuCommandTag.doEndTag();

    return super.doEndTag();
  }

  public void release() {
    super.release();
    rendered = null;
    value = null;
    action = null;
    actionListener = null;
    onclick = null;
    link = null;
    disabled = null;
    binding = null;
    label = null;
    immediate = null;
    transition = null;
    converter = null;
    renderedPartially = null;
    fieldId = null;
    menuCommandTag = null;
    facetTag = null;
    inTag = null;
  }

  /**
   * Action to invoke when clicked.
   * This must be a MethodBinding or a String representing the application action to invoke when
   * this component is activated by the user.
   * The MethodBinding must evaluate to a public method that takes no parameters,
   * and returns a String (the logical outcome) which is passed to the
   * NavigationHandler for this application.
   * The String is directly passed to the Navigationhandler.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {}, expression = DynamicExpression.METHOD_EXPRESSION,
      methodReturnType = "java.lang.Object")
  public void setAction(final javax.el.MethodExpression action) {
    this.action = action;
  }

  /**
   * MethodBinding representing an action listener method that will be
   * notified when this component is activated by the user.
   * The expression must evaluate to a public method that takes an ActionEvent
   * parameter, with a return type of void.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {}, expression = DynamicExpression.METHOD_EXPRESSION_REQUIRED,
      methodSignature = "javax.faces.event.ActionEvent")
  public void setActionListener(final javax.el.MethodExpression actionListener) {
    this.actionListener = actionListener;
  }

  /**
   * Script to be invoked when clicked
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setOnclick(final javax.el.ValueExpression onclick) {
    this.onclick = onclick;
  }

  /**
   * Link to an arbitrary URL
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLink(final javax.el.ValueExpression link) {
    this.link = link;
  }

  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.component.UIComponent")
  public void setBinding(final javax.el.ValueExpression binding) throws JspException {
    this.binding = binding;
  }

  /**
   * Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setRendered(final javax.el.ValueExpression rendered) {
    this.rendered = rendered;
  }

  /**
   * Flag indicating that this element is disabled.
   */
  @TagAttribute()
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setDisabled(final javax.el.ValueExpression disabled) {
    this.disabled = disabled;
  }

  /**
   * The current value of this component.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "java.lang.Object")
  public void setValue(final javax.el.ValueExpression value) {
    this.value = value;
  }

  /**
   * Text value to display as label.
   * If text contains an underscore the next character is used as accesskey.
   */
  @TagAttribute
  @UIComponentTagAttribute()
  public void setLabel(final javax.el.ValueExpression label) {
    this.label = label;
  }

  /**
   * Flag indicating that, if this component is activated by the user,
   * notifications should be delivered to interested listeners and actions
   * immediately (that is, during Apply Request Values phase) rather than
   * waiting until Invoke Application phase.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "false")
  public void setImmediate(final javax.el.ValueExpression immediate) {
    this.immediate = immediate;
  }

  /**
   * Specify, if the command calls an JSF-Action.
   * Useful to switch off the Double-Submit-Check and Waiting-Behavior.
   *
   * @param transition Indicates the transition.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "boolean", defaultValue = "true")
  public void setTransition(final javax.el.ValueExpression transition) {
    this.transition = transition;
  }

  /**
   * An expression that specifies the Converter for this component.
   * If the value binding expression is a String,
   * the String is used as an ID to look up a Converter.
   * If the value binding expression is a Converter,
   * uses that instance as the converter.
   * The value can either be a static value (ID case only)
   * or an EL expression.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = "javax.faces.convert.Converter",
      expression = DynamicExpression.VALUE_EXPRESSION)
  public void setConverter(final javax.el.ValueExpression converter) {
    this.converter = converter;
  }

  /**
   * Indicate the partially rendered Components in a case of a submit.
   */
   @TagAttribute
   @UIComponentTagAttribute(type = "java.lang.String[]")
  public void setRenderedPartially(final javax.el.ValueExpression renderedPartially) {
    this.renderedPartially = renderedPartially;
  }

  /**
   * The component identifier for the input field component inside of the container.
   * This value must be unique within the closest parent component that is a naming container.
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setFieldId(final String fieldId) {
    this.fieldId = fieldId;
  }

  /**
   * The component identifier for this component.
   * This value must be unique within the closest parent component that is a naming container.
   * For tx components the id will be set to the container (e. g. the panel).
   * To set the id of the input field, you have to use the attribute "fieldId".
   */
  @TagAttribute(rtexprvalue = true)
  @UIComponentTagAttribute
  public void setId(final String id) {
    super.setId(id);
  }
}
