package com.atanion.tobago.event;

import javax.faces.event.FacesListener;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: 13.12.2004
 * Time: 16:21:54
 * To change this template use File | Settings | File Templates.
 */
public interface StateChangeListener extends FacesListener {

  public void processStateChange(StateChangeEvent stateChangeEvent);
}
