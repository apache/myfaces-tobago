package org.apache.myfaces.tobago.example.nonfacesrequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class FishPond {

    private Log LOG = LogFactory.getLog(FishPond.class);

  private Map<String, String> fishes;

  private String selectedFish;

  public FishPond() {
    fishes = new HashMap<String, String>();
    fishes.put("0", "Scholle");
    fishes.put("1", "Hai");
    fishes.put("2", "Luce");
    fishes.put("3", "Halibut");
    fishes.put("4", "Tamboril");
  }

  public String random() {
    Random random = new Random(System.currentTimeMillis());

    selectedFish = fishes.get("" + random.nextInt(fishes.size()));

    LOG.info("select via random: '" + selectedFish + "'");

    return "view";
  }

  public String select(String id) {
    selectedFish = fishes.get(id);

    LOG.info("select via id: '" + selectedFish + "'");

    return "view";
  }

  public String getSelectedFish() {
    return selectedFish;
  }


}
