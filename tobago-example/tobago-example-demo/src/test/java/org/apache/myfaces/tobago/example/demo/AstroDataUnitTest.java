package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.model.SelectItem;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class AstroDataUnitTest {

//  @Inject // todo
  private AstroData astroData = new AstroData();

  @Test
public void testTerrestrialPlanets() {
    final List<SelectItem> terrestrialPlanets = astroData.getTerrestrialPlanets();
    Assert.assertEquals("Mercury", terrestrialPlanets.get(0).getValue().toString());
  }

  @Test
public void testGiantPlanets() {
    final  List<SelectItem>  giantPlanets = astroData.getGiantPlanets();
    Assert.assertEquals("Jupiter", giantPlanets.get(0).getValue().toString());
  }
}
