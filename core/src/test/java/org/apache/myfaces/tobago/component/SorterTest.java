package org.apache.myfaces.tobago.component;

import junit.framework.TestCase;

/**
 * Created 04.02.2009 18:00:40
 */
public class SorterTest extends TestCase {

  public void testIsSimpleProperty() {
    assertTrue(new Sorter().isSimpleProperty("#{bean}"));
    assertTrue(new Sorter().isSimpleProperty("#{bean.a.b}"));
    assertTrue(new Sorter().isSimpleProperty("#{bean.prop}"));
    assertTrue(new Sorter().isSimpleProperty("#{bean.prop.prop}"));
  }

}
