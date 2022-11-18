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

package org.apache.myfaces.tobago.facelets;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Visual;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import jakarta.el.ValueExpression;
import jakarta.faces.FacesException;
import jakarta.faces.component.ActionSource;
import jakarta.faces.component.ActionSource2;
import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.ValueHolder;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.event.MethodExpressionActionListener;
import jakarta.faces.event.MethodExpressionValueChangeListener;
import jakarta.faces.validator.MethodExpressionValidator;
import jakarta.faces.view.facelets.ComponentHandler;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagException;
import jakarta.faces.view.facelets.TagHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

public final class AttributeHandler extends TagHandler {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final TagAttribute name;

  private final TagAttribute value;

  private final TagAttribute mode;

  public AttributeHandler(final TagConfig config) {
    super(config);
    this.name = getRequiredAttribute(Attributes.name.getName());
    this.value = getRequiredAttribute(Attributes.value.getName());
    this.mode = getAttribute(Attributes.mode.getName());
  }

  @Override
  public void apply(final FaceletContext faceletContext, final UIComponent parent) throws ELException {
    if (parent == null) {
      throw new TagException(tag, "Parent UIComponent was null");
    }

    if (ComponentHandler.isNew(parent)) {

      if (mode != null) {
        if ("isNotSet".equals(mode.getValue())) {
          boolean result = false;
          String expressionString = value.getValue();
          if (!value.isLiteral()) {
            while (isSimpleExpression(expressionString)) {
              if (isMethodOrValueExpression(expressionString)) {
                final ValueExpression expression
                    = faceletContext.getVariableMapper().resolveVariable(removeElParenthesis(expressionString));
                if (expression == null) {
                  result = true;
                  break;
                } else {
                  expressionString = expression.getExpressionString();
                }
              } else {
                result = false;
                break;
              }
            }
          } else {
            result = StringUtils.isEmpty(expressionString);
          }
          parent.getAttributes().put(name.getValue(), result);
        } else if ("isSet".equals(mode.getValue())) {
          boolean result = true;
          String expressionString = value.getValue();
          if (!value.isLiteral()) {
            while (isSimpleExpression(expressionString)) {
              if (isMethodOrValueExpression(expressionString)) {
                final ValueExpression expression
                    = faceletContext.getVariableMapper().resolveVariable(removeElParenthesis(expressionString));
                if (expression == null) {
                  result = false;
                  break;
                } else {
                  expressionString = expression.getExpressionString();
                }
              } else {
                result = true;
                break;
              }
            }
          } else {
            result = StringUtils.isNotEmpty(expressionString);
          }
          parent.getAttributes().put(name.getValue(), result);
        } else if ("action".equals(mode.getValue())) {
          String expressionString = value.getValue();
          while (isSimpleExpression(expressionString)) {
            if (isMethodOrValueExpression(expressionString)) {
              final ValueExpression expression
                  = faceletContext.getVariableMapper().resolveVariable(removeElParenthesis(expressionString));
              if (expression == null) {
                // when the action hasn't been set while using a composition.
                if (LOG.isDebugEnabled()) {
                  LOG.debug("Variable can't be resolved: value='" + expressionString + "'");
                }
                expressionString = null;
                break;
              } else {
                expressionString = expression.getExpressionString();
              }
            } else {
              break;
            }
          }
          if (expressionString != null) {
            final ExpressionFactory expressionFactory = faceletContext.getExpressionFactory();
            final MethodExpression action = new TagMethodExpression(value, expressionFactory.createMethodExpression(
                faceletContext, expressionString, String.class, ComponentUtils.ACTION_ARGS));
            ((ActionSource2) parent).setActionExpression(action);
          }
        } else if ("actionListener".equals(mode.getValue())) {
          String expressionString = value.getValue();
          while (isSimpleExpression(expressionString)) {
            if (isMethodOrValueExpression(expressionString)) {
              final ValueExpression expression
                  = faceletContext.getVariableMapper().resolveVariable(removeElParenthesis(expressionString));
              if (expression == null) {
                if (LOG.isDebugEnabled()) {
                  // when the action hasn't been set while using a composition.
                  LOG.debug("Variable can't be resolved: value='" + expressionString + "'");
                }
                expressionString = null;
                break;
              } else {
                expressionString = expression.getExpressionString();
              }
            } else {
              LOG.warn("Only expressions are supported mode=actionListener value='" + expressionString + "'");
              expressionString = null;
              break;
            }
          }
          if (expressionString != null) {
            final ExpressionFactory expressionFactory = faceletContext.getExpressionFactory();
            final MethodExpression actionListener
                = new TagMethodExpression(value, expressionFactory.createMethodExpression(
                faceletContext, expressionString, null, ComponentUtils.ACTION_LISTENER_ARGS));
            ((ActionSource) parent).addActionListener(new MethodExpressionActionListener(actionListener));
          }
        } else if ("actionFromValue".equals(mode.getValue())) {
          if (!value.isLiteral()) {
            final String result = value.getValue(faceletContext);
            parent.getAttributes().put(name.getValue(), new ConstantMethodExpression(result));
          }
        } else if ("valueIfSet".equals(mode.getValue())) {
          String expressionString = value.getValue();
          String lastExpressionString = null;
          while (isMethodOrValueExpression(expressionString) && isSimpleExpression(expressionString)) {
            final ValueExpression expression
                = faceletContext.getVariableMapper().resolveVariable(removeElParenthesis(expressionString));
            if (expression != null) {
              lastExpressionString = expressionString;
              expressionString = expression.getExpressionString();
            } else {
              // restore last value
              expressionString = lastExpressionString;
              break;
            }
          }
          if (expressionString != null) {
            final String attributeName = name.getValue(faceletContext);
            if (containsMethodOrValueExpression(expressionString)) {
              final ValueExpression expression = value.getValueExpression(faceletContext, Object.class);
              parent.setValueExpression(attributeName, expression);
            } else {
              final Object literalValue = getValue(faceletContext, parent, expressionString, attributeName);
              parent.getAttributes().put(attributeName, literalValue);
            }
          }
        } else {
          throw new FacesException("Type " + mode + " not supported");
        }
      } else {

        final Attributes nameValue = Attributes.valueOfFailsafe(name.getValue(faceletContext));
        if (Attributes.rendered == nameValue) {
          if (value.isLiteral()) {
            parent.setRendered(value.getBoolean(faceletContext));
          } else {
            parent.setValueExpression(nameValue.getName(), value.getValueExpression(faceletContext, Boolean.class));
          }
        } else if (Attributes.markup == nameValue) {
          if (parent instanceof Visual) {
            if (value.isLiteral()) {
              ((Visual) parent).setMarkup(Markup.valueOf(value.getValue()));
            } else {
              final ValueExpression expression = value.getValueExpression(faceletContext, Object.class);
              parent.setValueExpression(nameValue.getName(), expression);
            }
          } else {
            LOG.error("Component is not instanceof Visual. Instance is: " + parent.getClass().getName());
          }
        } else if (parent instanceof EditableValueHolder && Attributes.validator == nameValue) {
          final MethodExpression methodExpression
              = getMethodExpression(faceletContext, null, ComponentUtils.VALIDATOR_ARGS);
          if (methodExpression != null) {
            ((EditableValueHolder) parent).addValidator(new MethodExpressionValidator(methodExpression));
          }
        } else if (parent instanceof EditableValueHolder && Attributes.valueChangeListener == nameValue) {
          final MethodExpression methodExpression =
              getMethodExpression(faceletContext, null, ComponentUtils.VALUE_CHANGE_LISTENER_ARGS);
          if (methodExpression != null) {
            ((EditableValueHolder) parent).addValueChangeListener(
                new MethodExpressionValueChangeListener(methodExpression));
          }
        } else if (parent instanceof ValueHolder && Attributes.converter == nameValue) {
          setConverter(faceletContext, parent, nameValue.getName());
        } else if (parent instanceof ActionSource && Attributes.action.equals(nameValue)) {
          final MethodExpression action = getMethodExpression(faceletContext, String.class, ComponentUtils.ACTION_ARGS);
          if (action != null) {
            ((ActionSource2) parent).setActionExpression(action);
          }
        } else if (parent instanceof ActionSource && Attributes.actionListener == nameValue) {
          final MethodExpression action
              = getMethodExpression(faceletContext, null, ComponentUtils.ACTION_LISTENER_ARGS);
          if (action != null) {
            ((ActionSource) parent).addActionListener(new MethodExpressionActionListener(action));
          }
        } else if (nameValue != null && !parent.getAttributes().containsKey(nameValue.getName())) {
          if (value.isLiteral()) {
            parent.getAttributes().put(nameValue.getName(), value.getValue());
          } else {
            parent.setValueExpression(nameValue.getName(), value.getValueExpression(faceletContext, Object.class));
          }
        } else if (nameValue == null) {
          LOG.warn("Null value for {}", name);
        }
      }
    }
  }

  private boolean isMethodOrValueExpression(final String string) {
    return (string.startsWith("${") || string.startsWith("#{")) && string.endsWith("}");
  }

  private boolean containsMethodOrValueExpression(final String string) {
    return (string.contains("${") || string.contains("#{")) && string.contains("}");
  }

  private boolean isSimpleExpression(final String string) {
    return string.indexOf('.') < 0 && string.indexOf('[') < 0;
  }

  private String removeElParenthesis(final String string) {
    return string.substring(2, string.length() - 1);
  }

  private ValueExpression getExpression(final FaceletContext faceletContext) {
    final String myValue = removeElParenthesis(value.getValue());
    return faceletContext.getVariableMapper().resolveVariable(myValue);
  }

  private MethodExpression getMethodExpression(
      final FaceletContext faceletContext, final Class returnType, final Class[] args) {
    // in a composition may be we get the method expression string from the current variable mapper
    // the expression can be empty
    // in this case return nothing
    if (value.getValue().startsWith("${")) {
      final ValueExpression expression = getExpression(faceletContext);
      if (expression != null) {
        final ExpressionFactory expressionFactory = faceletContext.getExpressionFactory();
        return new TagMethodExpression(value, expressionFactory.createMethodExpression(faceletContext,
            expression.getExpressionString(), returnType, args));
      } else {
        return null;
      }
    } else {
      return value.getMethodExpression(faceletContext, returnType, args);
    }
  }

  private Object getValue(
      final FaceletContext faceletContext, final UIComponent parent, final String expressionString,
      final String attributeName) {
    Class type = Object.class;
    try {
      type = new PropertyDescriptor(attributeName, parent.getClass()).getReadMethod().getReturnType();
    } catch (final IntrospectionException e) {
      LOG.warn("Can't determine expected type", e);
    }
    final ExpressionFactory expressionFactory = faceletContext.getExpressionFactory();
    final ValueExpression valueExpression = expressionFactory
        .createValueExpression(faceletContext, expressionString, type);
    return valueExpression.getValue(faceletContext);
  }

  private void setConverter(final FaceletContext faceletContext, final UIComponent parent, final String nameValue) {
    // in a composition may be we get the converter expression string from the current variable mapper
    // the expression can be empty
    // in this case return nothing
    if (value.getValue().startsWith("${")) {
      final ValueExpression expression = getExpression(faceletContext);
      if (expression != null) {
        setConverter(faceletContext, parent, nameValue, expression);
      }
    } else {
      setConverter(faceletContext, parent, nameValue, value.getValueExpression(faceletContext, Object.class));
    }
  }

  private void setConverter(
      final FaceletContext faceletContext, final UIComponent parent, final String nameValue,
      final ValueExpression expression) {
    if (expression.isLiteralText()) {
      final Converter converter =
          faceletContext.getFacesContext().getApplication().createConverter(expression.getExpressionString());
      ((ValueHolder) parent).setConverter(converter);
    } else {
      parent.setValueExpression(nameValue, expression);
    }
  }

  private static class ConstantMethodExpression extends MethodExpression implements StateHolder {

    private String outcome;

    private boolean transientFlag;

    ConstantMethodExpression() {
    }

    ConstantMethodExpression(final String outcome) {
      this.outcome = outcome;
    }

    @Override
    public MethodInfo getMethodInfo(final ELContext context)
        throws NullPointerException, ELException {
      return null;
    }

    @Override
    public Object invoke(final ELContext context, final Object[] params)
        throws NullPointerException, ELException {
      return outcome;
    }

    @Override
    public boolean equals(final Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      final ConstantMethodExpression that = (ConstantMethodExpression) o;

      return Objects.equals(outcome, that.outcome);
    }

    @Override
    public int hashCode() {
      return outcome.hashCode();
    }

    @Override
    public String getExpressionString() {
      return outcome;
    }

    @Override
    public boolean isLiteralText() {
      return true;
    }

    @Override
    public Object saveState(final FacesContext context) {
      return outcome;
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
      this.outcome = (String) state;
    }

    @Override
    public void setTransient(final boolean transientFlagParameter) {
      this.transientFlag = transientFlag;
    }

    @Override
    public boolean isTransient() {
      return transientFlag;
    }
  }
}
