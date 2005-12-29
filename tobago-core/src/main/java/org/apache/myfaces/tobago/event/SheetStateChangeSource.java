package org.apache.myfaces.tobago.event;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 18.12.2005
 * Time: 13:56:59
 * To change this template use File | Settings | File Templates.
 */
public interface SheetStateChangeSource {

    public javax.faces.el.MethodBinding getStateChangeListener();

    public void setStateChangeListener(javax.faces.el.MethodBinding actionListener);

    public void addStateChangeListener(SheetStateChangeListener listener);

    public SheetStateChangeListener[] getStateChangeListeners();

    public void removeStateChangeListener(SheetStateChangeListener listener);
}
