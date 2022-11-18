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

package org.apache.myfaces.tobago.internal.behavior;

import org.apache.myfaces.tobago.exception.TobagoException;

import jakarta.el.ValueExpression;
import jakarta.faces.component.StateHelper;
import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// todo: clean up (is a copy of MyFaces, but not all stuff is refactored)

/**
 * A delta enabled state holder implementing the StateHolder Interface.
 * <p>
 * Components implementing the PartalStateHolder interface have an initial state
 * and delta states, the initial state is the one holding all root values
 * and deltas store differences to the initial states
 * </p>
 * <p>
 * For components not implementing partial state saving only the initial states are
 * of importance, everything is stored and restored continously there
 * </p>
 * <p>
 * The state helper seems to have three internal storage mechanisms:
 * one being a list which stores plain values,
 * one being a key value pair which stores key values in maps
 * add serves the plain list type while put serves the
 * key value type,
 * the third is the value which has to be stored plainly as is!
 * </p>
 * In other words, this map can be seen as a composite map. It has two maps:
 * initial state map and delta map.
 * <p>
 * If delta map is used (method component.initialStateMarked() ),
 * base or initial state map cannot be changed, since all changes
 * should be tracked on delta map.
 * </p>
 * <p>
 * The intention of this class is just hold property values
 * and do a clean separation between initial state and delta.
 * </p>
 * <p>
 * The code from this class comes from a refactor of
 * org.apache.myfaces.trinidad.bean.util.PropertyHashMap
 * </p>
 * <p>
 * The context from this class comes and that should be taken into account
 * is this:
 * </p>
 * <p>
 * First request:
 * </p>
 * <ul>
 * <li> A new template is created (using
 * jakarta.faces.view.ViewDeclarationLanguage.buildView method)
 * and component.markInitialState is called from its related TagHandler classes
 * (see jakarta.faces.view.facelets.ComponentHandler ).
 * When this method is executed, the component tree was populated from the values
 * set in the facelet abstract syntax tree (or in other words composition of
 * facelets templates). </li>
 * <li> From this point all updates on the variables are considered "delta". </li>
 * <li> SaveState, if initialStateMarked is true, only delta is saved. </li>
 * </ul>
 * <p>
 * Second request (and next ones)
 * </p>
 * <ul>
 * <li> A new template is created and component.markInitialState is called from
 * its related TagHandler classes again. In this way, components like c:forEach
 * or c:if, that add or remove components could notify about this and handle
 * them properly (see jakarta.faces.view.StateManagementStrategy). Note that a
 * component restored using this method is no different as the same component
 * at the first request at the same time. </li>
 * <li> A call for restoreState is done, passing the delta as object value. If no
 * delta, the state is complete and no call is triggered. </li>
 * <li> Lifecycle occur, changing the necessary stuff. </li>
 * <li> SaveState, if initialStateMarked is true, only delta is saved. </li>
 * </ul>
 * <p>
 * From the previous analysis, the following conclusions arise:
 * <ul>
 * <li>This class only needs to keep track of delta changes, so when
 * restoreState/saveState is called, the right objects are passed.</li>
 * <li>UIComponent.clearInitialState is used to reset the partial
 * state holder to a non delta state, so the state to be saved by
 * saveState is no longer a delta instead is a full state. If a call
 * to clearInitialState occur it is not expected a call for
 * UIComponent.markInitialState occur on the current request.</li>
 * <li>The state is handled in the same way on UIData, so components
 * inside UIData share its state on all rows. There is no way to save
 * delta per row.</li>
 * <li>The map backed by method put(Serializable,String,Object) is
 * a replacement of UIComponentBase.attributesMap and UIComponent.bindings map.
 * Note that on jsf 1.2, instances saved on attributesMap should not be
 * StateHolder, but on jsf 2.0 it is possible to have it. PartialStateHolder
 * instances are not handled in this map, or in other words delta state is not
 * handled in this classes (markInitialState and clearInitialState is not propagated).</li>
 * <li>The list backed by method add(Serializable,Object) should be (is not) a
 * replacement of UIComponentBase.facesListeners, but note that StateHelper
 * does not implement PartialStateHolder, and facesListener could have instances
 * of that class that needs to be notified when UIComponent.markInitialState or
 * UIComponent.clearInitialState is called, or in other words facesListeners
 * should deal with PartialStateHolder instances.</li>
 * <li>The list backed by method add(Serializable,Object) is
 * a replacement of UIViewRoot.phaseListeners list. Note that instances of
 * PhaseListener are not expected to implement StateHolder or PartialStateHolder.</li>
 * </ul>
 * </p>
 * <p>
 * NOTE: The current implementation of StateHelper on RI does not handle
 * stateHolder values internally. To prevent problems when developers create
 * custom components we should do this too. But anyway, the code that
 * handle this case should be let here as comment, if some day this feature
 * is provided. Note than stateHolder aware properties like converter,
 * validator or listeners should deal with StateHolder or PartialStateHolder
 * on component classes.
 * <p>
 * </p>
 *
 * @since 3.0.0
 */
class DeltaStateHelper<A extends EventBehavior> implements StateHelper {

  /**
   * We need to hold a component instance because:
   * <p>
   * - The component is the one who knows if we are on initial or delta mode
   * - eval assume calls to component.ValueExpression
   */
  private A target;

  /**
   * This map holds the full current state
   */
  private Map<Serializable, Object> fullState;

  /**
   * This map only keep track of delta changes to be saved
   */
  private Map<Serializable, Object> deltas;

  /**
   * This map keep track of StateHolder keys, to be saved when
   * saveState is called.
   */
  //private Set<Serializable> _stateHolderKeys;

  private boolean transientBoolean = false;

  DeltaStateHelper(final A target) {
    super();
    this.target = target;
    fullState = new HashMap<>();
    deltas = null;
    //_stateHolderKeys = new HashSet<Serializable>();
  }

  /**
   * Used to create delta map on demand
   */
  private boolean createDeltas() {
    if (isInitialStateMarked()) {
      if (deltas == null) {
        deltas = new HashMap<>(2);
      }
      return true;
    }

    return false;
  }

  protected boolean isInitialStateMarked() {
    return target.initialStateMarked();
  }

  @Override
  public void add(final Serializable key, final Object value) {
    if (createDeltas()) {
      //Track delta case
      Map<Object, Boolean> deltaListMapValues = (Map<Object, Boolean>) deltas
          .get(key);
      if (deltaListMapValues == null) {
        deltaListMapValues = new DeltaStateHelper.InternalDeltaListMap<>(
            3);
        deltas.put(key, deltaListMapValues);
      }
      deltaListMapValues.put(value, Boolean.TRUE);
    }

    //Handle change on full map
    List<Object> fullListValues = (List<Object>) fullState.get(key);
    if (fullListValues == null) {
      fullListValues = new DeltaStateHelper.InternalList<>(3);
      fullState.put(key, fullListValues);
    }
    fullListValues.add(value);
  }

  @Override
  public Object eval(final Serializable key) {
    final Object returnValue = fullState.get(key);
    if (returnValue != null) {
      return returnValue;
    }
    final ValueExpression expression = target.getValueExpression(key
        .toString());
    if (expression != null) {
      return expression.getValue(FacesContext.getCurrentInstance()
          .getELContext());
    }
    return null;
  }

  @Override
  public Object eval(final Serializable key, final Object defaultValue) {
    final Object returnValue = fullState.get(key);
    if (returnValue != null) {
      return returnValue;
    }
    final ValueExpression expression = target.getValueExpression(key
        .toString());
    if (expression != null) {
      return expression.getValue(FacesContext.getCurrentInstance()
          .getELContext());
    }
    return defaultValue;
  }

  @Override
  public Object get(final Serializable key) {
    return fullState.get(key);
  }

  @Override
  public Object put(final Serializable key, final Object value) {
    Object returnValue = null;
    if (createDeltas()) {
      if (deltas.containsKey(key)) {
        returnValue = deltas.put(key, value);
        fullState.put(key, value);
      } else if (value == null && !fullState.containsKey(key)) {
        returnValue = null;
      } else {
        deltas.put(key, value);
        returnValue = fullState.put(key, value);
      }
    } else {
            /*
            if (value instanceof StateHolder)
            {
                _stateHolderKeys.add(key);
            }
            */
      returnValue = fullState.put(key, value);
    }
    return returnValue;
  }

  @Override
  public Object put(final Serializable key, final String mapKey, final Object value) {
    boolean returnSet = false;
    Object returnValue = null;
    if (createDeltas()) {
      //Track delta case
      Map<String, Object> mapValues = (Map<String, Object>) deltas
          .get(key);
      if (mapValues == null) {
        mapValues = new DeltaStateHelper.InternalMap<>();
        deltas.put(key, mapValues);
      }
      if (mapValues.containsKey(mapKey)) {
        returnValue = mapValues.put(mapKey, value);
        returnSet = true;
      } else {
        mapValues.put(mapKey, value);
      }
    }

    //Handle change on full map
    Map<String, Object> mapValues = (Map<String, Object>) fullState
        .get(key);
    if (mapValues == null) {
      mapValues = new DeltaStateHelper.InternalMap<>();
      fullState.put(key, mapValues);
    }
    if (returnSet) {
      mapValues.put(mapKey, value);
    } else {
      returnValue = mapValues.put(mapKey, value);
    }
    return returnValue;
  }

  @Override
  public Object remove(final Serializable key) {
    Object returnValue = null;
    if (createDeltas()) {
      if (deltas.containsKey(key)) {
        // Keep track of the removed values using key/null pair on the delta map
        returnValue = deltas.put(key, null);
        fullState.remove(key);
      } else {
        // Keep track of the removed values using key/null pair on the delta map
        deltas.put(key, null);
        returnValue = fullState.remove(key);
      }
    } else {
      returnValue = fullState.remove(key);
    }
    return returnValue;
  }

  @Override
  public Object remove(final Serializable key, final Object valueOrKey) {
    // Comment by lu4242 : The spec javadoc says if it is a Collection
    // or Map deal with it. But the intention of this method is work
    // with add(?,?) and put(?,?,?), this ones return instances of
    // InternalMap and InternalList to prevent mixing, so to be
    // consistent we'll cast to those classes here.

    final Object collectionOrMap = fullState.get(key);
    Object returnValue = null;
    if (collectionOrMap instanceof DeltaStateHelper.InternalMap) {
      if (createDeltas()) {
        returnValue = removeValueOrKeyFromMap(deltas, key,
            valueOrKey, true);
        removeValueOrKeyFromMap(fullState, key, valueOrKey, false);
      } else {
        returnValue = removeValueOrKeyFromMap(fullState, key,
            valueOrKey, false);
      }
    } else if (collectionOrMap instanceof DeltaStateHelper.InternalList) {
      if (createDeltas()) {
        returnValue = removeValueOrKeyFromCollectionDelta(deltas,
            key, valueOrKey);
        removeValueOrKeyFromCollection(fullState, key, valueOrKey);
      } else {
        returnValue = removeValueOrKeyFromCollection(fullState, key,
            valueOrKey);
      }
    }
    return returnValue;
  }

  private static Object removeValueOrKeyFromCollectionDelta(
      final Map<Serializable, Object> stateMap, final Serializable key,
      final Object valueOrKey) {
    Object returnValue = null;
    final Map<Object, Boolean> c = (Map<Object, Boolean>) stateMap.get(key);
    if (c != null) {
      if (c.containsKey(valueOrKey)) {
        returnValue = valueOrKey;
      }
      c.put(valueOrKey, Boolean.FALSE);
    }
    return returnValue;
  }

  private static Object removeValueOrKeyFromCollection(
      final Map<Serializable, Object> stateMap, final Serializable key,
      final Object valueOrKey) {
    Object returnValue = null;
    final Collection c = (Collection) stateMap.get(key);
    if (c != null) {
      if (c.remove(valueOrKey)) {
        returnValue = valueOrKey;
      }
      if (c.isEmpty()) {
        stateMap.remove(key);
      }
    }
    return returnValue;
  }

  private static Object removeValueOrKeyFromMap(
      final Map<Serializable, Object> stateMap, final Serializable key,
      final Object valueOrKey, final boolean delta) {
    if (valueOrKey == null) {
      return null;
    }

    Object returnValue = null;
    final Map<String, Object> map = (Map<String, Object>) stateMap.get(key);
    if (map != null) {
      if (delta) {
        // Keep track of the removed values using key/null pair on the delta map
        returnValue = map.put((String) valueOrKey, null);
      } else {
        returnValue = map.remove(valueOrKey);
      }

      if (map.isEmpty()) {
        //stateMap.remove(key);
        stateMap.put(key, null);
      }
    }
    return returnValue;
  }

  @Override
  public boolean isTransient() {
    return transientBoolean;
  }

  /**
   * Serializing cod
   * the serialized data structure consists of key value pairs unless the value itself is an internal array
   * or a map in case of an internal array or map the value itself is another array with its initial value
   * myfaces.InternalArray, myfaces.internalMap
   * <p>
   * the internal Array is then mapped to another array
   * <p>
   * the internal Map again is then mapped to a map with key value pairs
   */
  @Override
  public Object saveState(final FacesContext context) {
    final Map serializableMap = (isInitialStateMarked()) ? deltas : fullState;

    if (serializableMap == null || serializableMap.size() == 0) {
      return null;
    }

        /*
        int stateHolderKeyCount = 0;
        if (isInitalStateMarked())
        {
            for (Iterator<Serializable> it = _stateHolderKeys.iterator(); it.hasNext();)
            {
                Serializable key = it.next();
                if (!deltas.containsKey(key))
                {
                    stateHolderKeyCount++;
                }
            }
        }*/

    Map.Entry<Serializable, Object> entry;
    //entry == key, value, key, value
    final Object[] retArr = new Object[serializableMap.entrySet().size() * 2];
    //Object[] retArr = new Object[serializableMap.entrySet().size() * 2 + stateHolderKeyCount];

    final Iterator<Map.Entry<Serializable, Object>> it = serializableMap.entrySet().iterator();
    int cnt = 0;
    while (it.hasNext()) {
      entry = it.next();
      retArr[cnt] = entry.getKey();

      final Object value = entry.getValue();

      // The condition in which the call to saveAttachedState
      // is to handle List, StateHolder or non Serializable instances.
      // we check it here, to prevent unnecessary calls.
      if (value instanceof StateHolder || value instanceof List || !(value instanceof Serializable)) {
        final Object savedValue = saveAttachedState(context, value);
        retArr[cnt + 1] = savedValue;
      } else {
        retArr[cnt + 1] = value;
      }
      cnt += 2;
    }

        /*
        if (isInitalStateMarked())
        {
            for (Iterator<Serializable> it2 = _stateHolderKeys.iterator(); it.hasNext();)
            {
                Serializable key = it2.next();
                if (!deltas.containsKey(key))
                {
                    retArr[cnt] = key;
                    Object value = fullState.get(key);
                    if (value instanceof PartialStateHolder)
                    {
                        //Could contain delta, save it as _AttachedDeltaState
                        PartialStateHolder holder = (PartialStateHolder) value;
                        if (holder.isTransient())
                        {
                            retArr[cnt + 1] = null;
                        }
                        else
                        {
                            retArr[cnt + 1] = new _AttachedDeltaWrapper(value.getClass(), holder.saveState(context));
                        }
                    }
                    else
                    {
                        //Save everything
                        retArr[cnt + 1] = saveAttachedState(context, fullState.get(key));
                    }
                    cnt += 2;
                }
            }
        }
        */
    return retArr;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    if (state == null) {
      return;
    }

    final Object[] serializedState = (Object[]) state;

    if (!isInitialStateMarked() && !fullState.isEmpty()) {
      fullState.clear();
      if (deltas != null) {
        deltas.clear();
      }
    }

    for (int cnt = 0; cnt < serializedState.length; cnt += 2) {
      final Serializable key = (Serializable) serializedState[cnt];
      final Object savedValue = restoreAttachedState(context,
          serializedState[cnt + 1]);

      if (isInitialStateMarked()) {
        if (savedValue instanceof DeltaStateHelper.InternalDeltaListMap) {
          for (final Map.Entry<Object, Boolean> mapEntry : ((Map<Object, Boolean>) savedValue)
              .entrySet()) {
            final boolean addOrRemove = mapEntry.getValue();
            if (addOrRemove) {
              //add
              this.add(key, mapEntry.getKey());
            } else {
              //remove
              this.remove(key, mapEntry.getKey());
            }
          }
        } else if (savedValue instanceof DeltaStateHelper.InternalMap) {
          for (final Map.Entry<String, Object> mapEntry : ((Map<String, Object>) savedValue)
              .entrySet()) {
            this.put(key, mapEntry.getKey(), mapEntry.getValue());
          }
                /*
                else if (savedValue instanceof _AttachedDeltaWrapper)
                {
                    AttachedStateWrapper wrapper = (AttachedStateWrapper) savedValue;
                    //Restore delta state
                    ((PartialStateHolder)fullState.get(key)).restoreState(context, wrapper.getWrappedStateObject());
                    //Add this key as StateHolder key
                    _stateHolderKeys.add(key);
                }
                */
        } else {
          put(key, savedValue);
        }
      } else {
        put(key, savedValue);
      }
    }
  }

  @Override
  public void setTransient(final boolean transientValue) {
    transientBoolean = transientValue;
  }

  //We use our own data structures just to make sure
  //nothing gets mixed up internally
  static class InternalMap<K, V> extends HashMap<K, V> implements StateHolder {
    InternalMap() {
      super();
    }

    InternalMap(final int initialCapacity, final float loadFactor) {
      super(initialCapacity, loadFactor);
    }

    InternalMap(final Map<? extends K, ? extends V> m) {
      super(m);
    }

    InternalMap(final int initialSize) {
      super(initialSize);
    }

    @Override
    public boolean isTransient() {
      return false;
    }

    @Override
    public void setTransient(final boolean newTransientValue) {
      // No op
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
      final Object[] listAsMap = (Object[]) state;
      for (int cnt = 0; cnt < listAsMap.length; cnt += 2) {
        this.put((K) listAsMap[cnt], (V) UIComponentBase.restoreAttachedState(context, listAsMap[cnt + 1]));
      }
    }

    @Override
    public Object saveState(final FacesContext context) {
      int cnt = 0;
      final Object[] mapArr = new Object[this.size() * 2];
      for (final Map.Entry<K, V> entry : this.entrySet()) {
        mapArr[cnt] = entry.getKey();
        final Object value = entry.getValue();

        if (value instanceof StateHolder || value instanceof List || !(value instanceof Serializable)) {
          mapArr[cnt + 1] = saveAttachedState(context, value);
        } else {
          mapArr[cnt + 1] = value;
        }
        cnt += 2;
      }
      return mapArr;
    }
  }

  /**
   * Map used to keep track of list changes
   */
  static class InternalDeltaListMap<K, V> extends DeltaStateHelper.InternalMap<K, V> {

    InternalDeltaListMap() {
      super();
    }

    InternalDeltaListMap(final int initialCapacity, final float loadFactor) {
      super(initialCapacity, loadFactor);
    }

    InternalDeltaListMap(final int initialSize) {
      super(initialSize);
    }

    InternalDeltaListMap(final Map<? extends K, ? extends V> m) {
      super(m);
    }
  }

  static class InternalList<T> extends ArrayList<T> implements StateHolder {
    InternalList() {
      super();
    }

    InternalList(final Collection<? extends T> c) {
      super(c);
    }

    InternalList(final int initialSize) {
      super(initialSize);
    }

    @Override
    public boolean isTransient() {
      return false;
    }

    @Override
    public void setTransient(final boolean newTransientValue) {
    }

    @Override
    public void restoreState(final FacesContext context, final Object state) {
      final Object[] listAsArr = (Object[]) state;
      //since all other options would mean dual iteration
      //we have to do it the hard way
      for (final Object elem : listAsArr) {
        add((T) restoreAttachedState(context, elem));
      }
    }

    @Override
    public Object saveState(final FacesContext context) {
      final Object[] values = new Object[size()];
      for (int i = 0; i < size(); i++) {
        final Object value = get(i);

        if (value instanceof StateHolder || value instanceof List || !(value instanceof Serializable)) {
          values[i] = saveAttachedState(context, value);
        } else {
          values[i] = value;
        }
      }
      return values;
    }
  }

  private static Object saveAttachedState(final FacesContext context, final Object attachedObject) {
    if (context == null) {
      throw new NullPointerException("context");
    }

    if (attachedObject == null) {
      return null;
    }
    // StateHolder interface should take precedence over
    // List children
    if (attachedObject instanceof StateHolder) {
      final StateHolder holder = (StateHolder) attachedObject;
      if (holder.isTransient()) {
        return null;
      }

      return new AttachedStateWrapper(attachedObject.getClass(), holder.saveState(context));
    } else if (attachedObject instanceof List) {
      final List<Object> lst = new ArrayList<>(((List<?>) attachedObject).size());
      for (final Object item : (List<?>) attachedObject) {
        if (item != null) {
          lst.add(saveAttachedState(context, item));
        }
      }

      return new AttachedListStateWrapper(lst);
    } else if (attachedObject instanceof Serializable) {
      return attachedObject;
    } else {
      return new AttachedStateWrapper(attachedObject.getClass(), null);
    }
  }

  private static Object restoreAttachedState(final FacesContext context, final Object stateObj)
      throws IllegalStateException {
    if (context == null) {
      throw new NullPointerException("context");
    }
    if (stateObj == null) {
      return null;
    }
    if (stateObj instanceof AttachedListStateWrapper) {
      final List<Object> lst = ((AttachedListStateWrapper) stateObj).getWrappedStateList();
      final List<Object> restoredList = new ArrayList<>(lst.size());
      for (final Object item : lst) {
        restoredList.add(restoreAttachedState(context, item));
      }
      return restoredList;
    } else if (stateObj instanceof AttachedStateWrapper) {
      final Class<?> clazz = ((AttachedStateWrapper) stateObj).getClazz();
      final Object restoredObject;
      try {
        restoredObject = clazz.newInstance();
      } catch (final InstantiationException e) {
        throw new TobagoException("Could not restore StateHolder of type " + clazz.getName()
            + " (missing no-args constructor?)", e);
      } catch (final IllegalAccessException e) {
        throw new TobagoException(e);
      }
      if (restoredObject instanceof StateHolder) {
        final AttachedStateWrapper wrapper = (AttachedStateWrapper) stateObj;
        final Object wrappedState = wrapper.getWrappedStateObject();

        final StateHolder holder = (StateHolder) restoredObject;
        holder.restoreState(context, wrappedState);
      }
      return restoredObject;
    } else {
      return stateObj;
    }
  }
}
