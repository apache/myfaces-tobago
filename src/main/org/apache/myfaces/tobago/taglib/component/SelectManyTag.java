package org.apache.myfaces.tobago.taglib.component;

import javax.faces.component.UISelectMany;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Apr 18, 2005
 * Time: 4:59:34 PM
 * To change this template use File | Settings | File Templates.
 */

public class SelectManyTag extends InputTag implements org.apache.myfaces.tobago.taglib.decl.SelectManyTag {
  public String getComponentType() {
    return UISelectMany.COMPONENT_TYPE;
  }
}
