/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 08.04.2003 at 16:13:51.
  * $Id$
  */
package com.atanion.tobago.lifecycle;

import javax.faces.lifecycle.Lifecycle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LifecycleFactory extends javax.faces.lifecycle.LifecycleFactory{

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  Map lifecycleMap;

// ///////////////////////////////////////////// constructor

  public LifecycleFactory() {
    lifecycleMap = new HashMap();
  }

// ///////////////////////////////////////////// code

  public void addLifecycle(String s, Lifecycle lifecycle) {
    lifecycleMap.put(s, lifecycle);
  }

  public Lifecycle getLifecycle(String s) {
    Lifecycle lifecycle = (Lifecycle) lifecycleMap.get(s);
    if (lifecycle == null && DEFAULT_LIFECYCLE.equals(s)) {
      lifecycle = new LifecycleImpl();
      addLifecycle(s, lifecycle);
    }
    return lifecycle;
  }

  public Iterator getLifecycleIds() {
    return lifecycleMap.keySet().iterator();
  }

// ///////////////////////////////////////////// bean getter + setter

}

