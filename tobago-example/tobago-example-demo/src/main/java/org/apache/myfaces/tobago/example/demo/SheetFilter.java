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

import org.apache.myfaces.tobago.example.data.SolarObject;
import org.apache.myfaces.tobago.model.SelectItem;
import org.apache.myfaces.tobago.model.SheetState;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestScoped
@Named
public class SheetFilter {

  private String name;
  private String orbit;
  private DistanceRange distance;
  private SelectItem[] distanceItems;
  private List<DistanceRange> distanceRangeList;
  private String discoverer;
  private DistanceRangeConverter converter;
  private SheetState state;

  @Inject
  @Named("demo")
  private TobagoDemoController demo;

  private List<SolarObject> filtered = new ArrayList<SolarObject>();

  public SheetFilter() {
    distanceRangeList = Arrays.asList(
        new DistanceRange(0, 10),
        new DistanceRange(10, 100),
        new DistanceRange(100, 1000),
        new DistanceRange(1000, 10000),
        new DistanceRange(10000, 100000),
        new DistanceRange(100000, 1000000),
        new DistanceRange(1000000, Integer.MAX_VALUE)
    );
    distanceItems = new SelectItem[]{
        new SelectItem(Integer.toString(0), "≤ 10"),
        new SelectItem(Integer.toString(1), "10 < x ≤ 100"),
        new SelectItem(Integer.toString(2), "100 < x ≤ 1000"),
        new SelectItem(Integer.toString(3), "1000 < x ≤ 10000"),
        new SelectItem(Integer.toString(4), "10000 < x ≤ 100000"),
        new SelectItem(Integer.toString(5), "100000 < x ≤ 1000000"),
        new SelectItem(Integer.toString(6), "1000000 < x"),
    };

    converter = new DistanceRangeConverter();
  }

  public String filter() {
    filtered.clear();
    for (SolarObject solarObject : demo.getSolarArray()) {
      if (solarObject.getName().toLowerCase().contains(name.toLowerCase())
          && solarObject.getOrbit().toLowerCase().contains(orbit.toLowerCase())
          && distance.getMin() < solarObject.getDistance()
          && solarObject.getDistance() <= distance.getMax()
          && solarObject.getDiscoverer().toLowerCase().contains(discoverer.toLowerCase())) {
        filtered.add(solarObject);
      }
    }
    return null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOrbit() {
    return orbit;
  }

  public void setOrbit(String orbit) {
    this.orbit = orbit;
  }

  public DistanceRange getDistance() {
    return distance;
  }

  public void setDistance(DistanceRange distance) {
    this.distance = distance;
  }

  public String getDiscoverer() {
    return discoverer;
  }

  public void setDiscoverer(String discoverer) {
    this.discoverer = discoverer;
  }

  public List<SolarObject> getFiltered() {
    return filtered;
  }

  public SheetState getState() {
    return state;
  }

  public void setState(SheetState state) {
    this.state = state;
  }

  public javax.faces.model.SelectItem[] getDistanceItems() {
    return distanceItems;
  }

  public DistanceRangeConverter getConverter() {
    return converter;
  }

  private static class DistanceRange {

    private int min;
    private int max;

    private DistanceRange(int min, int max) {
      this.min = min;
      this.max = max;
    }

    public int getMin() {
      return min;
    }

    public int getMax() {
      return max;
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }
      if (object == null || getClass() != object.getClass()) {
        return false;
      }
      DistanceRange that = (DistanceRange) object;

      return max == that.max && min == that.min;
    }

    @Override
    public int hashCode() {
      return 31 * min + max;
    }
  }

  public class DistanceRangeConverter implements Converter {
    public Object getAsObject(
        FacesContext context, UIComponent component, String value) throws ConverterException {
      return distanceRangeList.get(Integer.valueOf(value));
    }

    public String getAsString(
        FacesContext context, UIComponent component, Object value) throws ConverterException {
      return Integer.toString(distanceRangeList.indexOf(value));
    }
  }
}
