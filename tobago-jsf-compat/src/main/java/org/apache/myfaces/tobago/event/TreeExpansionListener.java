package org.apache.myfaces.tobago.event;

import javax.faces.event.FacesListener;

public interface TreeExpansionListener  extends FacesListener {

  void treeExpanded(TreeExpansionEvent event);
}
