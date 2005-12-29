package org.apache.myfaces.tobago.event;

import javax.faces.event.FacesListener;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 18.12.2005
 * Time: 14:13:38
 * To change this template use File | Settings | File Templates.
 */
public interface SheetStateChangeListener extends FacesListener {
  public void processSheetStateChange(SheetStateChangeEvent sheetStateChangeEvent);
}
