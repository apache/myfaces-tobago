package org.apache.myfaces.tobago.event;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 22.12.2005
 * Time: 23:01:03
 * To change this template use File | Settings | File Templates.
 */
public interface TabChangeSource {

  public javax.faces.el.MethodBinding getTabChangeListener();

  public void setTabChangeListener(javax.faces.el.MethodBinding actionListener);

  public void addTabChangeListener(TabChangeListener listener);

  public TabChangeListener[] getTabChangeListeners();

  public void removeTabChangeListener(TabChangeListener listener);
}
