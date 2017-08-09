/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.demo.nonfacesrequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SessionScoped
@Named
public class FishPond implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(FishPond.class);

  private Map<Integer, String> fishes;

  private Integer selectedFishId = null;

  public FishPond() {
    fishes = new HashMap<Integer, String>();
    fishes.put(0, "Scholle");
    fishes.put(1, "Hai");
    fishes.put(2, "Luce");
    fishes.put(3, "Halibut");
    fishes.put(4, "Tamboril");
  }

  public void action() {
    LOG.info("Event is called! selectedFishId='{}'", selectedFishId);
    // not needed for this example
  }

  public String random() {
    final Random random = new Random(System.currentTimeMillis());
    selectedFishId = random.nextInt(fishes.size());

    LOG.info("select via random: '" + getSelectedFish() + "'");

    return null; // is AJAX
  }

  public String select(final Integer fishId) {
    selectedFishId = fishId;
    LOG.info("select via id: '" + getSelectedFish() + "'");
    return "/content/90-non-faces-request/x-fish-pond.xhtml";
  }

  public String getSelectedFish() {
    return fishes.get(selectedFishId);
  }

  public Integer getSelectedFishId() {
    return selectedFishId;
  }

  public void setSelectedFishId(Integer selectedFishId) {
    this.selectedFishId = selectedFishId;
    LOG.info("setSelectedFishId via setter: '" + selectedFishId + "'");
  }
}
