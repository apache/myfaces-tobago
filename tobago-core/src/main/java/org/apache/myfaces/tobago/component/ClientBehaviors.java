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

package org.apache.myfaces.tobago.component;

public enum ClientBehaviors {

  action, // pure JSF (not a JavaScript event
  blur,
  change,
  click,
  complete,
  dblclick,
  focus,
  keydown,
  keypress,
  keyup,
  input,
  load,
  mouseover,
  mouseout,
  reload, // tbd - may be called timeout?
  resize,
  suggest; // tbd

  public static final String ACTION = "action";
  public static final String BLUR = "blur";
  public static final String CHANGE = "change";
  public static final String CLICK = "click";
  public static final String COMPLETE = "complete";
  public static final String DBLCLICK = "dblclick";
  public static final String FOCUS = "focus";
  public static final String KEYDOWN = "keydown";
  public static final String KEYPRESS = "keypress";
  public static final String KEYUP = "keyup";
  public static final String INPUT = "input";
  public static final String LOAD = "load";
  public static final String MOUSEOVER = "mouseover";
  public static final String MOUSEOUT = "mouseout";
  public static final String RELOAD = "reload"; // tbd - may be called timeout?
  public static final String RESIZE = "resize";
  public static final String SUGGEST = "suggest"; // tbd

}
