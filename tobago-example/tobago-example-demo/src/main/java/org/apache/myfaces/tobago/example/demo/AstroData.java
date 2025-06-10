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

package org.apache.myfaces.tobago.example.demo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.myfaces.tobago.model.SelectItem;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//XXX Using SessionScoped, because Singleton is not passivation capable.
@SessionScoped
@Named
public class AstroData implements Serializable {

  private final List<SolarObject> dataList;
  private final Map<String, SolarObject> dataMap;

  private final List<SelectItem> planets;
  private final List<SelectItem> terrestrialPlanets;
  private final List<SelectItem> giantPlanets;

  public AstroData() {

    final InputStreamReader reader
        = new InputStreamReader(AstroData.class.getResourceAsStream("astro-data.json"), StandardCharsets.UTF_8);

    Gson gson = new GsonBuilder().create();
    dataList = gson.fromJson(reader, new TypeToken<ArrayList<SolarObject>>() {
    }.getType());
    dataMap = new HashMap<>(dataList.size());
    for (SolarObject solarObject : dataList) {
      dataMap.put(solarObject.getName(), solarObject);
    }

    planets = findByName("Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune")
        .map(planet -> new SelectItem(planet, planet.getName())).collect(Collectors.toList());
    terrestrialPlanets = findByName("Mercury", "Venus", "Earth", "Mars")
        .map(planet -> new SelectItem(planet, planet.getName())).collect(Collectors.toList());
    giantPlanets = findByName("Jupiter", "Saturn", "Uranus", "Neptune")
        .map(planet -> new SelectItem(planet, planet.getName())).collect(Collectors.toList());
  }

  public Stream<SolarObject> findAll() {
    final AtomicInteger counter = new AtomicInteger(0);
    return dataList.stream().peek(object -> object.setId(counter.incrementAndGet()));
  }

  public Stream<SolarObject> findOrFill(int size) {

    final List<SolarObject> baseList = findAll().toList();
    return IntStream.range(0, size)
        .mapToObj(i -> {
          final SolarObject next = new SolarObject(baseList.get(i % baseList.size()));
          next.setName("#" + i + " " + next.getName());
          return next;
        });
  }

  public Map<String, SolarObject> findAllAsMap() {
    return dataMap;
  }

  public Stream<SolarObject> findAllAsCopy() {
    return dataList.stream().map(SolarObject::new);
  }

  public SolarObject find(final String name) {
    return dataList.stream().filter(solarObject -> name.equals(solarObject.getName())).findFirst()
        .orElse(null);
  }

  public Stream<SolarObject> findByName(String... filter) {
    return dataList.stream()
        .filter(solarObject -> Arrays.asList(filter).contains(solarObject.getName()));
  }

  public DefaultMutableTreeNode getAllAsTree() {
    final Map<String, DefaultMutableTreeNode> cache = new HashMap<>();
    for (final SolarObject solar : (Iterable<SolarObject>) dataList.stream()::iterator) {
      final DefaultMutableTreeNode node = new DefaultMutableTreeNode(solar);
      cache.put(solar.getName(), node);
      final String orbitName = solar.getOrbit();
      if (orbitName.equals("-")) {
        continue;
      }
      // adds a solar object as node to its orbit as tree child.
      cache.get(orbitName).add(node);
    }
    return cache.get("Sun");
  }

  public List<SolarObject> getSatellites(final String center) {
    final List<SolarObject> collect = new ArrayList<>();
    for (final SolarObject solar : (Iterable<SolarObject>) dataList.stream()::iterator) {
      // todo: use lambda
      if (solar.getOrbit().equals(center)) {
        collect.add(solar);
      }
    }
    return collect;
  }

  private List<SelectItem> createSelectItems(final List<SolarObject> objects) {
    final List<SelectItem> list = new ArrayList<>();
    for (SolarObject object : objects) {
      list.add(new SelectItem(object, object.getName()));
    }
    return list;
  }

  public List<SelectItem> getPlanets() {
    return planets;
  }

  public List<SelectItem> getTerrestrialPlanets() {
    return terrestrialPlanets;
  }

  public List<SelectItem> getGiantPlanets() {
    return giantPlanets;
  }

  public String namesFromArray(SolarObject[] objects) {
    if (objects == null) {
      return null;
    } else {
      StringBuilder builder = new StringBuilder();
      for (SolarObject object : objects) {
        builder.append(object.getName());
        builder.append(", ");
      }
      if (builder.length() >= 2) {
        builder.delete(builder.length() - 2, builder.length());
      }
      return builder.toString();
    }
  }
}
