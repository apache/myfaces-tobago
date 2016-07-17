package org.apache.myfaces.tobago.component;

import javax.faces.component.behavior.ClientBehaviorHolder;

public interface SupportsAjaxBehaviorHolder extends ClientBehaviorHolder {

  String[] getRenderPartially();

  void setRenderPartially(String[] renderedPartially);

  String[] getExecutePartially();

  void setExecutePartially(String[] renderedPartially);

}
