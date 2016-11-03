package org.apache.myfaces.tobago.internal.behavior;

import java.io.Serializable;
import java.util.List;

// todo: clean up (is a copy of MyFaces, but not all stuff is refactored)

class AttachedListStateWrapper
    implements Serializable
{
  private static final long serialVersionUID = -3958718149793179776L;
  private List<Object> _wrappedStateList;

  public AttachedListStateWrapper(List<Object> wrappedStateList)
  {
    _wrappedStateList = wrappedStateList;
  }

  public List<Object> getWrappedStateList()
  {
    return _wrappedStateList;
  }
}
