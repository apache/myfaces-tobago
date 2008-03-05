package org.apache.myfaces.tobago.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: lofwyr
 * Date: 14.02.2008 12:03:31
 */
public class LayoutContainerImpl extends LayoutComponentImpl implements LayoutContainer {

  private Map<String, ContainerConstraints> containerConstraints;

  private List<LayoutComponent> components;

  public LayoutContainerImpl() {
    containerConstraints = new HashMap<String, ContainerConstraints>();
    components = new ArrayList<LayoutComponent>();
  }

  public ContainerConstraints getContainerConstraints(String name) {
    return containerConstraints.get(name);
  }

  public void setContainerConstraints(String name, ContainerConstraints containerConstraints) {
    this.containerConstraints.put(name, containerConstraints);
  }

  public List<LayoutComponent> getComponents() {
    return components;
  }
}
