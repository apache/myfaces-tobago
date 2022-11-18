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

import org.apache.commons.lang3.StringUtils;
import org.apache.myfaces.tobago.model.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@SessionScoped
@Named
public class SheetFilterController implements Serializable {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private AstroData astroData;

  private List<SolarObject> filteredSolarList = new ArrayList<>();

  private SelectItem[] distanceItems;
  private SelectItem[] minYearItems;
  private SelectItem[] maxYearItems;

  private DistanceRangeConverter distanceRangeConverter;

  private String name;
  private String orbit;
  private DistanceRange distance;
  private String discoverer;
  private Integer minYear;
  private Integer maxYear;

  private String nameSuggestionQuery;

  @PostConstruct
  private void init() {
    distanceItems = new SelectItem[]{
        new SelectItem(new DistanceRange(-1, Integer.MAX_VALUE), "any"),
        new SelectItem(new DistanceRange(-1, 10), "≤ 10"),
        new SelectItem(new DistanceRange(10, 100), "10 < x ≤ 100"),
        new SelectItem(new DistanceRange(100, 1000), "100 < x ≤ 1000"),
        new SelectItem(new DistanceRange(1000, 10000), "1000 < x ≤ 10000"),
        new SelectItem(new DistanceRange(10000, 100000), "10000 < x ≤ 100000"),
        new SelectItem(new DistanceRange(100000, 1000000), "100000 < x ≤ 1000000"),
        new SelectItem(new DistanceRange(1000000, Integer.MAX_VALUE), "1000000 < x")
    };

    final Set<Integer> years = new TreeSet<>();
    // todo: use lambda
    for (final SolarObject solarObject : astroData.findAll().collect(Collectors.toList())) {
      if (solarObject.getDiscoverYear() != null) {
        years.add(solarObject.getDiscoverYear());
      }
    }
    minYearItems = new SelectItem[years.size() + 1];
    maxYearItems = new SelectItem[years.size() + 1];
    minYearItems[0] = new SelectItem(0, "min");
    maxYearItems[0] = new SelectItem(Integer.MAX_VALUE, "max");
    int i = 1;
    for (final Integer year : years) {
      final SelectItem selectItem = new SelectItem(year, String.valueOf(year));

      minYearItems[i] = selectItem;
      maxYearItems[i] = selectItem;

      i++;
    }

    distanceRangeConverter = new DistanceRangeConverter();

    filter();
  }

  public String filter() {
    filteredSolarList.clear();
    if (name == null) {
      name = "";
    }
    if (orbit == null) {
      orbit = "";
    }
    if (distance == null) {
      distance = (DistanceRange) distanceItems[0].getValue();
    }
    if (discoverer == null) {
      discoverer = "";
    }
    if (minYear == null) {
      minYear = 0;
    }
    if (maxYear == null) {
      maxYear = Integer.MAX_VALUE;
    }
    // todo: use lambda
    for (final SolarObject solarObject : astroData.findAll().collect(Collectors.toList())) {
      int discoverYear = 0;
      if (solarObject.getDiscoverYear() != null) {
        discoverYear = solarObject.getDiscoverYear();
      }
      if (StringUtils.containsIgnoreCase(solarObject.getName(), name)
          && StringUtils.containsIgnoreCase(solarObject.getOrbit(), orbit)
          && distance.getMin() < solarObject.getDistance() && solarObject.getDistance() <= distance.getMax()
          && StringUtils.containsIgnoreCase(solarObject.getDiscoverer(), discoverer)
          && minYear <= discoverYear && discoverYear <= maxYear) {
        filteredSolarList.add(solarObject);
      }
    }
    return null;
  }

  public void filter(final AjaxBehaviorEvent event) {
    filter();
  }

  public List<SolarObject> getFilteredSolarList() {
    return filteredSolarList;
  }

  public SelectItem[] getDistanceItems() {
    return distanceItems;
  }

  public SelectItem[] getMinYearItems() {
    return minYearItems;
  }

  public SelectItem[] getMaxYearItems() {
    return maxYearItems;
  }

  public DistanceRangeConverter getDistanceRangeConverter() {
    return distanceRangeConverter;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getOrbit() {
    return orbit;
  }

  public void setOrbit(final String orbit) {
    this.orbit = orbit;
  }

  public DistanceRange getDistance() {
    return distance;
  }

  public void setDistance(final DistanceRange distance) {
    this.distance = distance;
  }

  public String getDiscoverer() {
    return discoverer;
  }

  public void setDiscoverer(final String discoverer) {
    this.discoverer = discoverer;
  }

  public Integer getMinYear() {
    return minYear;
  }

  public void setMinYear(final Integer minYear) {
    this.minYear = minYear;
  }

  public Integer getMaxYear() {
    return maxYear;
  }

  public void setMaxYear(final Integer maxYear) {
    this.maxYear = maxYear;
  }

  public String getNameSuggestionQuery() {
    return nameSuggestionQuery;
  }

  public void setNameSuggestionQuery(final String nameSuggestionQuery) {
    this.nameSuggestionQuery = nameSuggestionQuery;
  }

  public List<String> getSuggestionSolarList() {
    final String substring = nameSuggestionQuery != null ? nameSuggestionQuery : "";
    LOG.info("Creating items for substring: '" + substring + "'");
    final List<String> result = new ArrayList<>();
    // todo: use lambda
    for (final SolarObject solarObject : astroData.findAll().collect(Collectors.toList())) {
      final String solarObjectName = solarObject.getName();
      if (StringUtils.containsIgnoreCase(solarObjectName, substring)) {
        result.add(solarObjectName);
      }
    }
    return result;
  }

  private class DistanceRange {

    private int min;
    private int max;
    private final String label;

    DistanceRange(final int min, final int max) {
      this.min = min;
      this.max = max;
      label = String.valueOf(min) + " < x ≤ " + String.valueOf(max);
    }

    DistanceRange(final int min, final int max, final String label) {
      this.min = min;
      this.max = max;
      this.label = label;
    }

    public int getMin() {
      return min;
    }

    public int getMax() {
      return max;
    }

    public String getLabel() {
      return label;
    }

    @Override
    public boolean equals(final Object object) {
      if (this == object) {
        return true;
      }
      if (object == null || getClass() != object.getClass()) {
        return false;
      }
      final DistanceRange that = (DistanceRange) object;
      return max == that.max && min == that.min;
    }

    @Override
    public int hashCode() {
      return 31 * min + max;
    }
  }

  public class DistanceRangeConverter implements Converter {
    @Override
    public Object getAsObject(final FacesContext context, final UIComponent component, final String value)
        throws ConverterException {
      try {
        for (final SelectItem distanceItem : distanceItems) {
          if (distanceItem.getLabel().equals(value)) {
            return distanceItem.getValue();
          }
        }
        return distanceItems[0].getValue();
      } catch (final RuntimeException e) {
        LOG.warn("unknown value='" + value + "'", e);
        return distanceItems[0].getValue();
      }
    }

    @Override
    public String getAsString(
        final FacesContext context, final UIComponent component, final Object value) throws ConverterException {
      if (value instanceof DistanceRange) {
        for (final SelectItem distanceItem : distanceItems) {
          if (distanceItem.getValue().equals(value)) {
            return distanceItem.getLabel();
          }
        }
      }
      return distanceItems[0].getLabel();
    }
  }
}
