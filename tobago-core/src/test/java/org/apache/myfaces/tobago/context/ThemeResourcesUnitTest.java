package org.apache.myfaces.tobago.context;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class ThemeResourcesUnitTest {

  @Test
  public void basic() {
    final ThemeResources resources = new ThemeResources(false);
    // empty
    // empty
    Assert.assertEquals(0, resources.getScriptList().size());
    Assert.assertEquals(0, resources.getStyleList().size());

    final ThemeScript a = new ThemeScript();
    a.setName("a");
    // a
    // empty
    resources.addIncludeScript(a);
    Assert.assertEquals(1, resources.getScriptList().size());
    Assert.assertEquals(0, resources.getStyleList().size());
    Assert.assertEquals("a", resources.getScriptList().get(0).getName());

    final ThemeScript b = new ThemeScript();
    b.setName("b");
    resources.addIncludeScript(b);
    // a b
    // empty
    Assert.assertEquals(2, resources.getScriptList().size());
    Assert.assertEquals(0, resources.getStyleList().size());
    Assert.assertEquals("a", resources.getScriptList().get(0).getName());
    Assert.assertEquals("b", resources.getScriptList().get(1).getName());

    final ThemeStyle c = new ThemeStyle();
    c.setName("c");
    resources.addIncludeStyle(c);
    // a b
    // c
    Assert.assertEquals(2, resources.getScriptList().size());
    Assert.assertEquals(1, resources.getStyleList().size());
    Assert.assertEquals("a", resources.getScriptList().get(0).getName());
    Assert.assertEquals("b", resources.getScriptList().get(1).getName());
    Assert.assertEquals("c", resources.getStyleList().get(0).getName());

    // merging exclude
    final ThemeResources resources2 = new ThemeResources(false);
    final ThemeScript aEx = new ThemeScript();
    aEx.setName("a");
    resources2.addExcludeScript(aEx);
    final ThemeStyle d = new ThemeStyle();
    d.setName("d");
    resources2.addIncludeStyle(d);

    final ThemeResources merge = ThemeResources.merge(resources, resources2);
    // a b  merge with -a       ->   b
    // c    merge with d        ->   c d
    Assert.assertEquals(1, merge.getScriptList().size());
    Assert.assertEquals(2, merge.getStyleList().size());
    Assert.assertEquals("b", merge.getScriptList().get(0).getName());
    Assert.assertEquals("c", merge.getStyleList().get(0).getName());
    Assert.assertEquals("d", merge.getStyleList().get(1).getName());
  }

  @Test
  public void prodVsDev() {
    final ThemeResources dev = new ThemeResources(false);
    Assert.assertFalse(dev.isProduction());

    final ThemeResources prod = new ThemeResources(true);
    Assert.assertTrue(prod.isProduction());
  }
}