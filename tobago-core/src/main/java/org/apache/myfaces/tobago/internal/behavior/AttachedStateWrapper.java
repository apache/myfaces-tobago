package org.apache.myfaces.tobago.internal.behavior;

import java.io.Serializable;

// todo: clean up (is a copy of MyFaces, but not all stuff is refactored)

class AttachedStateWrapper implements Serializable
{
  private static final long serialVersionUID = 4948301780259917764L;
  private Class<?> _class;
  private Object _wrappedStateObject;

  /**
   * @param clazz
   *            null means wrappedStateObject is a List of state objects
   * @param wrappedStateObject
   */
  public AttachedStateWrapper(Class<?> clazz, Object wrappedStateObject)
  {
    if (wrappedStateObject != null && !(wrappedStateObject instanceof Serializable))
    {
      throw new IllegalArgumentException("Attached state for Object of type " + clazz + " (Class "
          + wrappedStateObject.getClass().getName() + ") is not serializable");
    }
    _class = clazz;
    _wrappedStateObject = wrappedStateObject;
  }

  public Class<?> getClazz()
  {
    return _class;
  }

  public Object getWrappedStateObject()
  {
    return _wrappedStateObject;
  }
}
