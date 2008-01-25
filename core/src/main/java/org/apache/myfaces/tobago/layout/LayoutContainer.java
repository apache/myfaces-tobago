package org.apache.myfaces.tobago.layout;

import java.util.List;

/**
 * User: lofwyr
 * Date: 23.01.2008 20:10:16
 */
public interface LayoutContainer extends LayoutComponent {

  Dimension getBeginInset();
  Dimension getEndInset();

  List<LayoutComponent> getComponents();
}
