package com.atanion.tobago.event;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: 13.12.2004
 * Time: 16:25:03
 * To change this template use File | Settings | File Templates.
 */
public class StateChangeEvent extends FacesEvent {

  private Object oldState;
  private Object newState;

  public StateChangeEvent(UIComponent uiComponent, Object oldState, Object newState) {
    super(uiComponent);
    this.oldState = oldState;
    this.newState = newState;
  }

  public boolean isAppropriateListener(FacesListener facesListener) {
    return facesListener instanceof TabChangeListener;
  }

  public void processListener(FacesListener facesListener) {
    ((TabChangeListener)facesListener).processStateChange(this);
  }

  public Object getOldState() {
    return oldState;
  }

  public void setOldState(Object oldState) {
    this.oldState = oldState;
  }

  public Object getNewState() {
    return newState;
  }

  public void setNewState(Object newState) {
    this.newState = newState;
  }
}
