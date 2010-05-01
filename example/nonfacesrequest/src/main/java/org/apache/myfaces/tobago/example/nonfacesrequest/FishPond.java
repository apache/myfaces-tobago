package org.apache.myfaces.tobago.example.nonfacesrequest;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class FishPond {

  private static final Logger LOG = LoggerFactory.getLogger(FishPond.class);

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
