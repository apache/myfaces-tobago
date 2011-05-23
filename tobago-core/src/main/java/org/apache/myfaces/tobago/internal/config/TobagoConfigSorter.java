package org.apache.myfaces.tobago.internal.config;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TobagoConfigSorter implements Comparator<TobagoConfigFragment> {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigSorter.class);

  private List<TobagoConfigFragment> list;
  private List<Pair> pairs;

  public TobagoConfigSorter(List<TobagoConfigFragment> list) {
    this.list = list;
  }

  public void sort() {

    createRelevantPairs();

    makeTransitive();

    ensureIrreflexive();

    ensureAntiSymmetric();

    sort0();

    if (LOG.isInfoEnabled()) {
      LOG.info("Order of the Tobago config files:");
      for (TobagoConfigFragment fragment : list) {
        String name = fragment.getName();
        if (name == null) {
          name = "<unnamed>";
        } else {
          name = "'" + name + "'";
        }
        LOG.info("name=" + name + " url='" + fragment.getUrl() + "'");
      }
    }

  }

  protected void makeTransitive() {
    // make the half order transitive: a < b && b < c => a < c
    boolean growing = true;
    while (growing) {
      growing = false;
      for (int i = 0; i < pairs.size(); i++) {
        for (int j = 0; j < pairs.size(); j++) {
          if (pairs.get(i).getHigher() == pairs.get(j).getLower()
              && !isInRelation(pairs.get(i).getLower(), pairs.get(j).getHigher())) {
            pairs.add(new Pair(pairs.get(i).getLower(), pairs.get(j).getHigher()));
            growing = true;
          }
        }
      }
    }
  }

  protected void ensureIrreflexive() {
    for (Pair a : pairs) {
        if (a.getLower() == a.getHigher()) {
          StringBuffer buffer = new StringBuffer();
          buffer.append("Ordering problem. There are conflicting order rules. Not irreflexive. " + "'");
          buffer.append(a.getLower());
          buffer.append("' < '");
          buffer.append(a.getHigher());
          buffer.append("'!\nThe reason may be a cycle.\n");
          buffer.append("Complete list of rules: \n");
          for (Pair pair : pairs) {
            buffer.append("'");
            buffer.append(pair.getLower());
            buffer.append("' < '");
            buffer.append(pair.getHigher());
            buffer.append("'\n");

          }
          throw new RuntimeException(buffer.toString());
        }
      }
  }

  protected void ensureAntiSymmetric() {
    for (Pair a : pairs) {
      for (Pair b : pairs) {
        if (a.getLower() == b.getHigher() && a.getHigher() == b.getLower()) {
          StringBuffer buffer = new StringBuffer();
          buffer.append("Ordering problem. There are conflicting order rules. Not antisymmetric. " + "'");
          buffer.append(a.getLower());
          buffer.append("' < '");
          buffer.append(a.getHigher());
          buffer.append("'" + "'");
          buffer.append(a.getLower());
          buffer.append("' > '");
          buffer.append(a.getHigher());
          buffer.append("'!\nThe reason may be a cycle.\n");
          buffer.append("Complete list of rules: \n");
          for (Pair pair : pairs) {
            buffer.append("'");
            buffer.append(pair.getLower());
            buffer.append("' < '");
            buffer.append(pair.getHigher());
            buffer.append("'\n");

          }
          throw new RuntimeException(buffer.toString());
        }
      }
    }
  }

  public int compare(TobagoConfigFragment a, TobagoConfigFragment b) {
    if (isInRelation(a, b)) {
      return -1;
    }
    if (isInRelation(b, a)) {
      return 1;
    }
    return 0;
  }

  protected void createRelevantPairs() {

    pairs = new ArrayList<Pair>();

    // collecting all relations, which are relevant for us. We don't need "before" and "after" of unknown names.
    for (TobagoConfigFragment tobagoConfig : list) {
      for (String befores : tobagoConfig.getBefore()) {
        TobagoConfigFragment before = findByName(befores);
        if (before != null) {
          pairs.add(new Pair(tobagoConfig, before));
        }
      }
      for (String afters : tobagoConfig.getAfter()) {
        TobagoConfigFragment after = findByName(afters);
        if (after != null) {
          pairs.add(new Pair(after, tobagoConfig));
        }
      }
    }
  }

  protected void sort0() {
    Collections.sort(list, this);
  }

  private boolean isInRelation(TobagoConfigFragment lower, TobagoConfigFragment higher) {
    for (Pair p : pairs) {
      if (p.getLower() == lower && p.getHigher() == higher) {
        return true;
      }
    }
    return false;
  }

  private TobagoConfigFragment findByName(String name) {
    for (TobagoConfigFragment tobagoConfig : list) {
      if (name.equals(tobagoConfig.getName())) {
        return tobagoConfig;
      }
    }
    return null;
  }

  protected List<Pair> getPairs() {
    return pairs;
  }

  private static class Pair {

    private final TobagoConfigFragment lower;
    private final TobagoConfigFragment higher;

    private Pair(TobagoConfigFragment lower, TobagoConfigFragment higher) {
      this.lower = lower;
      this.higher = higher;
    }

    public TobagoConfigFragment getLower() {
      return lower;
    }

    public TobagoConfigFragment getHigher() {
      return higher;
    }

    @Override
    public String toString() {
      return lower + "<" + higher;
    }
  }

}
