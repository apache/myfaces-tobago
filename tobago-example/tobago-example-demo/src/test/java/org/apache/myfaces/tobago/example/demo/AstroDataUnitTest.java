package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.model.SelectItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class AstroDataUnitTest {

  //  @Inject // todo
  private AstroData astroData = new AstroData();

  @Test
  public void testTerrestrialPlanets() {
    final List<SelectItem> terrestrialPlanets = astroData.getTerrestrialPlanets();
    Assert.assertEquals(4, terrestrialPlanets.size());
    Assert.assertEquals("Mercury", terrestrialPlanets.get(0).getValue().toString());
  }

  @Test
  public void testGiantPlanets() {
    final List<SelectItem> giantPlanets = astroData.getGiantPlanets();
    Assert.assertEquals(4, giantPlanets.size());
    Assert.assertEquals("Jupiter", giantPlanets.get(0).getValue().toString());
  }

  @Test
  public void testOrbits() {
    final Map<String, SolarObject> all = astroData.findAllAsMap();
    for (SolarObject solarObject : all.values()) {
      // every orbit must be inside the list
      final String orbit = solarObject.getOrbit();
      Assert.assertTrue(orbit.equals("-") || all.containsKey(orbit));
    }
  }
}
