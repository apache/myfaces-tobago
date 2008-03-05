package org.apache.myfaces.tobago.layout;

import java.util.HashMap;
import java.util.Map;

/**
 * User: lofwyr
 * Date: 14.02.2008 12:08:52
 */
public class LayoutComponentImpl implements LayoutComponent {

  private Map<String, ComponentConstraints> componentConstraints;

  public LayoutComponentImpl() {
    componentConstraints = new HashMap<String, ComponentConstraints>();

  }

  public ComponentConstraints getComponentConstraints(String name) {
    return componentConstraints.get(name);
  }

  public void setComponentConstraints(String name, ComponentConstraints constraints) {
    componentConstraints.put(name, constraints);
  }
}
