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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.internal.component.AbstractUISelectManyBase;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectMany;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class SelectManyRendererBase extends MessageLayoutRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectManyRendererBase.class);

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final AbstractUISelectManyBase select = (AbstractUISelectManyBase) component;

    String[] newValues =
        facesContext.getExternalContext().getRequestParameterValuesMap().get(select.getClientId(facesContext));
    if (LOG.isDebugEnabled()) {
      LOG.debug("decode: key='" + component.getClientId(facesContext)
          + "' value='" + Arrays.toString(newValues) + "'");
      LOG.debug("size ... '" + (newValues != null ? newValues.length : -1) + "'");
      if (newValues != null) {
        for (final String newValue : newValues) {
          LOG.debug("newValues[i] = '" + newValue + "'");
        }
      }
    }

    if (newValues == null) {
      newValues = ArrayUtils.EMPTY_STRING_ARRAY; // because no selection will not submitted by browsers
    }
    select.setSubmittedValue(newValues);

    RenderUtils.decodeClientBehaviors(facesContext, select);
  }

  public String[] getSubmittedValues(final UIInput input) {
    return (String[]) input.getSubmittedValue();
  }

  @Override
  public Object getConvertedValue(
      final FacesContext facesContext, final UIComponent component, final Object submittedValue)
      throws ConverterException {

    if (submittedValue == null) {
      return null;
    } else {
      if (!(submittedValue instanceof String[])) {
        throw new ConverterException("Submitted value not of type String[] for component : "
            + component.getClientId(facesContext) + "expected");
      }
    }
    return getConvertedUISelectManyValue(facesContext, (UISelectMany) component, (String[]) submittedValue);
  }

  // #################################################################################################################
  // #################################################################################################################
  // ###  The following methods and classes are copied from myfaces api 2.2.8,
  // ###  slightly modified to compile in this context.
  // ###  We copy this to avoid the dependency
  // #################################################################################################################
  // #################################################################################################################

  // #################################################################################################################
  // ### BEGIN copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_SharedRendererUtils.java
  // #################################################################################################################
  static final String COLLECTION_TYPE_KEY = "collectionType";
  static final String VALUE_TYPE_KEY = "valueType";

  static Object getConvertedUISelectManyValue(final FacesContext facesContext, final UISelectMany component,
                                              final String[] submittedValue) throws ConverterException {
    return getConvertedUISelectManyValue(facesContext, component,
        submittedValue, false);
  }

  /**
   * Gets the converted value of a UISelectMany component.
   * If the considerValueType is true, this method will also consider the
   * valueType attribute of Tomahawk UISelectMany components.
   *
   * @throws ConverterException
   */
  static Object getConvertedUISelectManyValue(final FacesContext facesContext, final UISelectMany component,
                                              final String[] submittedValue, final boolean considerValueType)
      throws ConverterException {
    // Attention!
    // This code is duplicated in shared renderkit package (except for considerValueType).
    // If you change something here please do the same in the other class!

    if (submittedValue == null) {
      throw new NullPointerException("submittedValue");
    }

    final ValueExpression expression = component.getValueExpression("value");
    Object targetForConvertedValues = null;

    // if the component has an attached converter, use it
    Converter converter = component.getConverter();
    // at this point the valueType attribute is handled in shared.
    if (converter == null && considerValueType) {
      // try to get a converter from the valueType attribute
      converter = getValueTypeConverter(facesContext, component);
    }

    if (expression != null) {
      final Class<?> modelType = expression
          .getType(facesContext.getELContext());
      if (modelType == null) {
        // FIXME temporal workaround for MYFACES-2552
        return submittedValue;
      } else if (modelType.isArray()) {
        // the target should be an array
        final Class<?> componentType = modelType.getComponentType();
        // check for optimization if the target is
        // a string array --> no conversion needed
        if (String.class.equals(componentType)) {
          return submittedValue;
        }
        if (converter == null) {
          // the compononent does not have an attached converter
          // --> try to get a registered-by-class converter
          converter = facesContext.getApplication().createConverter(
              componentType);

          if (converter == null && !Object.class.equals(componentType)) {
            // could not obtain a Converter
            // --> check if we maybe do not really have to convert

            // target is not an Object array
            // and not a String array (checked some lines above)
            // and we do not have a Converter
            throw new ConverterException(
                "Could not obtain a Converter for "
                    + componentType.getName());
          }
        }
        // instantiate the array
        targetForConvertedValues = Array.newInstance(componentType,
            submittedValue.length);
      } else if (Collection.class.isAssignableFrom(modelType) || Object.class.equals(modelType)) {
        if (converter == null) {
          // try to get the by-type-converter from the type of the SelectItems
          final SelectItemsIterator iterator = new SelectItemsIterator(component, facesContext);
          converter = getSelectItemsValueConverter(iterator, facesContext);
        }

        final Object collectionTypeAttr = component.getAttributes().get(
            COLLECTION_TYPE_KEY);
        if (collectionTypeAttr != null) {
          final Class<?> collectionType = getClassFromAttribute(facesContext, collectionTypeAttr);
          if (collectionType == null) {
            throw new FacesException(
                "The attribute "
                    + COLLECTION_TYPE_KEY
                    + " of component "
                    + component.getClientId(facesContext)
                    + " does not evaluate to a "
                    + "String, a Class object or a ValueExpression pointing "
                    + "to a String or a Class object.");
          }
          // now we have a collectionType --> but is it really some kind of Collection
          if (!Collection.class.isAssignableFrom(collectionType)) {
            throw new FacesException("The attribute "
                + COLLECTION_TYPE_KEY + " of component "
                + component.getClientId(facesContext)
                + " does not point to a valid type of Collection.");
          }
          // now we have a real collectionType --> try to instantiate it
          try {
            targetForConvertedValues = collectionType.newInstance();
          } catch (final Exception e) {
            throw new FacesException("The Collection "
                + collectionType.getName()
                + "can not be instantiated.", e);
          }
        } else if (Collection.class.isAssignableFrom(modelType)) {
          // component.getValue() will implement Collection at this point
          final Collection<?> componentValue = (Collection<?>) component
              .getValue();
          // can we clone the Collection
          if (componentValue instanceof Cloneable) {
            // clone method of Object is protected --> use reflection
            try {
              final Method cloneMethod = componentValue.getClass()
                  .getMethod("clone");
              final Collection<?> clone = (Collection<?>) cloneMethod
                  .invoke(componentValue);
              clone.clear();
              targetForConvertedValues = clone;
            } catch (final Exception e) {
              LOG.error("Could not clone " + componentValue.getClass().getName(), e);
            }
          }

          // if clone did not work
          if (targetForConvertedValues == null) {
            // try to create the (concrete) collection from modelType
            // or with the class object of componentValue (if any)
            try {
              targetForConvertedValues = (componentValue != null
                  ? componentValue.getClass()
                  : modelType).newInstance();
            } catch (final Exception e) {
              // this did not work either
              // use the standard concrete type
              if (SortedSet.class.isAssignableFrom(modelType)) {
                targetForConvertedValues = new TreeSet();
              } else if (Queue.class.isAssignableFrom(modelType)) {
                targetForConvertedValues = new LinkedList();
              } else if (Set.class.isAssignableFrom(modelType)) {
                targetForConvertedValues = new HashSet(
                    submittedValue.length);
              } else {
                targetForConvertedValues = new ArrayList(
                    submittedValue.length);
              }
            }
          }
        } else /* if (Object.class.equals(modelType)) */ {
          // a modelType of Object is also permitted, in order to support
          // managed bean properties of type Object

          // optimization: if we don't have a converter, we can return the submittedValue
          if (converter == null) {
            return submittedValue;
          }

          targetForConvertedValues = new Object[submittedValue.length];
        }
      } else {
        // the expression does neither point to an array nor to a collection
        throw new ConverterException(
            "ValueExpression for UISelectMany must be of type Collection or Array.");
      }
    } else {
      targetForConvertedValues = new Object[submittedValue.length];
    }

    // convert the values with the selected converter (if any)
    // and store them in targetForConvertedValues
    final boolean isArray = targetForConvertedValues.getClass().isArray();
    for (int i = 0; i < submittedValue.length; i++) {
      // get the value
      final Object value;
      if (converter != null) {
        value = converter.getAsObject(facesContext, component,
            submittedValue[i]);
      } else {
        value = submittedValue[i];
      }
      // store it in targetForConvertedValues
      if (isArray) {
        Array.set(targetForConvertedValues, i, value);
      } else {
        ((Collection) targetForConvertedValues).add(value);
      }
    }

    return targetForConvertedValues;
  }

  /**
   * Gets a Class object from a given component attribute. The attribute can
   * be a ValueExpression (that evaluates to a String or a Class) or a
   * String (that is a fully qualified Java class name) or a Class object.
   *
   * @throws FacesException if the value is a String and the represented
   *                        class cannot be found
   */
  static Class<?> getClassFromAttribute(final FacesContext facesContext,
                                        final Object attribute) throws FacesException {
    // Attention!
    // This code is duplicated in shared renderkit package.
    // If you change something here please do the same in the other class!

    Class<?> type = null;

    // if there is a value, it must be a ...
    // ... a ValueExpression that evaluates to a String or a Class
    final Object attr = attribute instanceof ValueExpression
        // get the value of the ValueExpression
        ? ((ValueExpression) attribute).getValue(facesContext.getELContext())
        : attribute;
    // ... String that is a fully qualified Java class name
    if (attr instanceof String) {
      try {
        type = Class.forName((String) attr);
      } catch (final ClassNotFoundException cnfe) {
        throw new FacesException("Unable to find class " + attr + " on the classpath.", cnfe);
      }
    } else if (attr instanceof Class) {
      // ... a Class object
      type = (Class<?>) attr;
    }

    return type;
  }

  /**
   * Uses the valueType attribute of the given UISelectMany component to
   * get a by-type converter.
   *
   * @param facesContext
   * @param component
   * @return
   */
  static Converter getValueTypeConverter(final FacesContext facesContext, final UISelectMany component) {
    Converter converter = null;

    final Object valueTypeAttr = component.getAttributes().get(VALUE_TYPE_KEY);
    if (valueTypeAttr != null) {
      // treat the valueType attribute exactly like the collectionType attribute
      final Class<?> valueType = getClassFromAttribute(facesContext, valueTypeAttr);
      if (valueType == null) {
        throw new FacesException(
            "The attribute "
                + VALUE_TYPE_KEY
                + " of component "
                + component.getClientId(facesContext)
                + " does not evaluate to a "
                + "String, a Class object or a ValueExpression pointing "
                + "to a String or a Class object.");
      }
      // now we have a valid valueType
      // --> try to get a registered-by-class converter
      converter = facesContext.getApplication().createConverter(valueType);

      if (converter == null) {
        facesContext.getExternalContext().log("Found attribute valueType on component "
            + getPathToComponent(component)
            + ", but could not get a by-type converter for type "
            + valueType.getName());
      }
    }

    return converter;
  }

  /**
   * Iterates through the SelectItems with the given Iterator and tries to obtain
   * a by-class-converter based on the Class of SelectItem.getValue().
   *
   * @param iterator
   * @param facesContext
   * @return The first suitable Converter for the given SelectItems or null.
   */
  static Converter getSelectItemsValueConverter(final Iterator<SelectItem> iterator, final FacesContext facesContext) {
    // Attention!
    // This code is duplicated in jsfapi component package.
    // If you change something here please do the same in the other class!

    Converter converter = null;
    while (converter == null && iterator.hasNext()) {
      final SelectItem item = iterator.next();
      if (item instanceof SelectItemGroup) {
        final Iterator<SelectItem> groupIterator = Arrays.asList(
            ((SelectItemGroup) item).getSelectItems()).iterator();
        converter = getSelectItemsValueConverter(groupIterator, facesContext);
      } else {
        final Class<?> selectItemsType = item.getValue().getClass();

        // optimization: no conversion for String values
        if (String.class.equals(selectItemsType)) {
          return null;
        }

        try {
          converter = facesContext.getApplication().createConverter(selectItemsType);
        } catch (final FacesException e) {
          // nothing - try again
        }
      }
    }
    return converter;
  }
  // #################################################################################################################
  // ### END copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_SharedRendererUtils.java
  // #################################################################################################################

  // #################################################################################################################
  // ### BEGIN copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_ComponentUtils.java
  // #################################################################################################################
  static String getPathToComponent(final UIComponent component) {
    final StringBuilder builder = new StringBuilder();

    if (component == null) {
      builder.append("{Component-Path : ");
      builder.append("[null]}");
      return builder.toString();
    }

    getPathToComponent(component, builder);

    builder.insert(0, "{Component-Path : ");
    builder.append("}");

    return builder.toString();
  }

  private static void getPathToComponent(final UIComponent component, final StringBuilder builder) {
    if (component == null) {
      return;
    }

    final StringBuilder intBuilder = new StringBuilder();

    intBuilder.append("[Class: ");
    intBuilder.append(component.getClass().getName());
    if (component instanceof UIViewRoot) {
      intBuilder.append(",ViewId: ");
      intBuilder.append(((UIViewRoot) component).getViewId());
    } else {
      intBuilder.append(",Id: ");
      intBuilder.append(component.getId());
    }
    intBuilder.append("]");

    builder.insert(0, intBuilder.toString());

    getPathToComponent(component.getParent(), builder);
  }
  // #################################################################################################################
  // ### END copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_ComponentUtils.java
  // #################################################################################################################

  // #################################################################################################################
  // ### BEGIN copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_SelectItemsIterator.java
  // #################################################################################################################
  private static class SelectItemsIterator implements Iterator<SelectItem> {

    private static final Iterator<UIComponent> EMPTY_UICOMPONENT_ITERATOR = new EmptyIterator<>();

    // org.apache.myfaces.shared.util.SelectItemsIterator uses JSFAttr
    private static final String VAR_ATTR = "var";
    private static final String ITEM_VALUE_ATTR = "itemValue";
    private static final String ITEM_LABEL_ATTR = "itemLabel";
    private static final String ITEM_DESCRIPTION_ATTR = "itemDescription";
    private static final String ITEM_DISABLED_ATTR = "itemDisabled";
    private static final String ITEM_LABEL_ESCAPED_ATTR = "itemLabelEscaped";
    private static final String NO_SELECTION_VALUE_ATTR = "noSelectionValue";

    private final Iterator<UIComponent> children;
    private Iterator<?> nestedItems;
    private SelectItem nextItem;
    private UIComponent currentComponent;
    private UISelectItems currentUISelectItems;
    private FacesContext facesContext;

    SelectItemsIterator(final UIComponent selectItemsParent, final FacesContext facesContext) {
      children = selectItemsParent.getChildCount() > 0
          ? selectItemsParent.getChildren().iterator()
          : EMPTY_UICOMPONENT_ITERATOR;
      this.facesContext = facesContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean hasNext() {
      if (nextItem != null) {
        return true;
      }
      if (nestedItems != null) {
        if (nestedItems.hasNext()) {
          return true;
        }
        nestedItems = null;
        currentComponent = null;
      }
      if (children.hasNext()) {
        UIComponent child = children.next();
        // When there is other components nested that does
        // not extends from UISelectItem or UISelectItems
        // the behavior for this iterator is just skip this
        // element(s) until an element that extends from these
        // classes are found. If there is no more elements
        // that conform this condition, just return false.
        while (!(child instanceof UISelectItem) && !(child instanceof UISelectItems)) {
          // Try to skip it
          if (children.hasNext()) {
            // Skip and do the same check
            child = children.next();
          } else {
            // End loop, so the final result is return false,
            // since there are no more components to iterate.
            return false;
          }
        }
        if (child instanceof UISelectItem) {
          final UISelectItem uiSelectItem = (UISelectItem) child;
          Object item = uiSelectItem.getValue();
          if (item == null) {
            // no value attribute --> create the SelectItem out of the other attributes
            final Object itemValue = uiSelectItem.getItemValue();
            String label = uiSelectItem.getItemLabel();
            final String description = uiSelectItem.getItemDescription();
            final boolean disabled = uiSelectItem.isItemDisabled();
            final boolean escape = uiSelectItem.isItemEscaped();
            final boolean noSelectionOption = uiSelectItem.isNoSelectionOption();
            if (label == null) {
              label = itemValue.toString();
            }
            item = new SelectItem(itemValue, label, description, disabled, escape, noSelectionOption);
          } else if (!(item instanceof SelectItem)) {
            final ValueExpression expression = uiSelectItem.getValueExpression("value");
            throw new IllegalArgumentException("ValueExpression '"
                + (expression == null ? null : expression.getExpressionString()) + "' of UISelectItem : "
                + getPathToComponent(child) + " does not reference an Object of type SelectItem");
          }
          nextItem = (SelectItem) item;
          currentComponent = child;
          return true;
        } else if (child instanceof UISelectItems) {
          currentUISelectItems = (UISelectItems) child;
          final Object value = currentUISelectItems.getValue();
          currentComponent = child;

          if (value instanceof SelectItem) {
            nextItem = (SelectItem) value;
            return true;
          } else if (value != null && value.getClass().isArray()) {
            // value is any kind of array (primitive or non-primitive)
            // --> we have to use class Array to get the values
            final int length = Array.getLength(value);
            final Collection<Object> items = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
              items.add(Array.get(value, i));
            }
            nestedItems = items.iterator();
            return hasNext();
          } else if (value instanceof Iterable) {
            // value is Iterable --> Collection, DataModel,...
            nestedItems = ((Iterable<?>) value).iterator();
            return hasNext();
          } else if (value instanceof Map) {
            final Map<Object, Object> map = (Map<Object, Object>) value;
            final Collection<SelectItem> items = new ArrayList<>(map.size());
            for (final Map.Entry<Object, Object> entry : map.entrySet()) {
              items.add(new SelectItem(entry.getValue(), entry.getKey().toString()));
            }

            nestedItems = items.iterator();
            return hasNext();
          } else {

            if (facesContext.isProjectStage(ProjectStage.Production) && LOG.isDebugEnabled()
                || LOG.isWarnEnabled()) {
              final ValueExpression expression = currentUISelectItems.getValueExpression("value");
              final Object[] objects = {
                  expression == null ? null : expression.getExpressionString(),
                  getPathToComponent(child),
                  value == null ? null : value.getClass().getName()
              };
              final String message = "ValueExpression {0} of UISelectItems with component-path {1}"
                  + " does not reference an Object of type SelectItem,"
                  + " array, Iterable or Map, but of type: {2}";
              if (facesContext.isProjectStage(ProjectStage.Production)) {
                LOG.debug(message, objects);
              } else {
                LOG.warn(message, objects);
              }
            }
          }
        } else {
          currentComponent = null;
        }
      }
      return false;
    }

    @Override
    public SelectItem next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (nextItem != null) {
        final SelectItem value = nextItem;
        nextItem = null;
        return value;
      }
      if (nestedItems != null) {
        Object item = nestedItems.next();

        if (!(item instanceof SelectItem)) {
          // check new params of SelectItems (since 2.0): itemValue, itemLabel, itemDescription,...
          // Note that according to the spec UISelectItems does not provide Getter and Setter
          // methods for this values, so we have to use the attribute map
          final Map<String, Object> attributeMap = currentUISelectItems.getAttributes();

          // write the current item into the request map under the key listed in var, if available
          boolean wroteRequestMapVarValue = false;
          Object oldRequestMapVarValue = null;
          final String var = (String) attributeMap.get(VAR_ATTR);
          if (var != null && !"".equals(var)) {
            // save the current value of the key listed in var from the request map
            oldRequestMapVarValue = facesContext.getExternalContext().getRequestMap().put(var, item);
            wroteRequestMapVarValue = true;
          }

          // check the itemValue attribute
          Object itemValue = attributeMap.get(ITEM_VALUE_ATTR);
          if (itemValue == null) {
            // the itemValue attribute was not provided
            // --> use the current item as the itemValue
            itemValue = item;
          }

          // Spec: When iterating over the select items, toString()
          // must be called on the string rendered attribute values
          Object itemLabel = attributeMap.get(ITEM_LABEL_ATTR);
          if (itemLabel == null) {
            if (itemValue != null) {
              itemLabel = itemValue.toString();
            }
          } else {
            itemLabel = itemLabel.toString();
          }
          Object itemDescription = attributeMap.get(ITEM_DESCRIPTION_ATTR);
          if (itemDescription != null) {
            itemDescription = itemDescription.toString();
          }
          final Boolean itemDisabled = getBooleanAttribute(currentUISelectItems, ITEM_DISABLED_ATTR, false);
          final Boolean itemLabelEscaped = getBooleanAttribute(currentUISelectItems, ITEM_LABEL_ESCAPED_ATTR, true);
          final Object noSelectionValue = attributeMap.get(NO_SELECTION_VALUE_ATTR);
          item = new SelectItem(itemValue,
              (String) itemLabel,
              (String) itemDescription,
              itemDisabled,
              itemLabelEscaped,
              itemValue.equals(noSelectionValue));

          // remove the value with the key from var from the request map, if previously written
          if (wroteRequestMapVarValue) {
            // If there was a previous value stored with the key from var in the request map, restore it
            if (oldRequestMapVarValue != null) {
              facesContext.getExternalContext()
                  .getRequestMap().put(var, oldRequestMapVarValue);
            } else {
              facesContext.getExternalContext()
                  .getRequestMap().remove(var);
            }
          }
        }
        return (SelectItem) item;
      }
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }

    public UIComponent getCurrentComponent() {
      return currentComponent;
    }

    private boolean getBooleanAttribute(
        final UIComponent component, final String attrName, final boolean defaultValue) {
      final Object value = component.getAttributes().get(attrName);
      if (value == null) {
        return defaultValue;
      } else if (value instanceof Boolean) {
        return (Boolean) value;
      } else {
        // If the value is a String, parse the boolean.
        // This makes the following code work: <tag attribute="true" />,
        // otherwise you would have to write <tag attribute="#{true}" />.
        return Boolean.valueOf(value.toString());
      }
    }

    private String getPathToComponent(final UIComponent component) {
      final StringBuilder builder = new StringBuilder();

      if (component == null) {
        builder.append("{Component-Path : ");
        builder.append("[null]}");
        return builder.toString();
      }

      getPathToComponent(component, builder);

      builder.insert(0, "{Component-Path : ");
      builder.append("}");

      return builder.toString();
    }

    private void getPathToComponent(final UIComponent component, final StringBuilder builder) {
      if (component == null) {
        return;
      }

      final StringBuilder intBuilder = new StringBuilder();

      intBuilder.append("[Class: ");
      intBuilder.append(component.getClass().getName());
      if (component instanceof UIViewRoot) {
        intBuilder.append(",ViewId: ");
        intBuilder.append(((UIViewRoot) component).getViewId());
      } else {
        intBuilder.append(",Id: ");
        intBuilder.append(component.getId());
      }
      intBuilder.append("]");

      builder.insert(0, intBuilder);

      getPathToComponent(component.getParent(), builder);
    }
  }
  // #################################################################################################################
  // ### END copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_SelectItemsIterator.java
  // #################################################################################################################

  // #################################################################################################################
  // ### BEGIN copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_EmptyIterator.java
  // #################################################################################################################
  private static class EmptyIterator<T> implements Iterator<T> {

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public T next() {
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  // #################################################################################################################
  // ### END copy out of https://svn.apache.org/repos/asf/myfaces/core/tags/myfaces-core-module-2.2.8/
  // ###     api/src/main/java/javax/faces/component/_EmptyIterator.java
  // #################################################################################################################
}
