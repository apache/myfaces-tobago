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
public class SheetStateChangeEvent extends FacesEvent {

  public SheetStateChangeEvent(UIComponent uiComponent) {
    super(uiComponent);
  }

  public boolean isAppropriateListener(FacesListener facesListener) {
    return true;    //todo
  }

  public void processListener(FacesListener facesListener) {
    //todo
  }

}
