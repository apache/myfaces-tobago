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

package org.apache.myfaces.tobago.internal.config;

import org.apache.myfaces.tobago.context.ThemeImpl;
import org.apache.myfaces.tobago.sanitizer.IgnoringSanitizer;
import org.apache.myfaces.tobago.sanitizer.JsoupSanitizer;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TobagoConfigSorter implements Comparator<TobagoConfigFragment> {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoConfigSorter.class);

  private List<TobagoConfigFragment> list;
  private List<Pair> pairs;

  public TobagoConfigSorter(final List<TobagoConfigFragment> list) {
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
      for (final TobagoConfigFragment fragment : list) {
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

  public TobagoConfigImpl merge() {

    final TobagoConfigImpl result = new TobagoConfigImpl();

    // default sanitizer
    String sanitizerClass = JsoupSanitizer.class.getName();
    Properties sanitizerProperties = new Properties();
    sanitizerProperties.setProperty("whitelist", "relaxed");

    for (final TobagoConfigFragment fragment : list) {
      // default theme
      final String defaultTheme = fragment.getDefaultThemeName();
      if (defaultTheme != null) {
        result.setDefaultThemeName(defaultTheme);
      }

      // supported themes
      for (final String supported : fragment.getSupportedThemeNames()) {
        result.addSupportedThemeName(supported);
      }

      // renderers config
      if (fragment.getRenderersConfig() != null) {
        if (result.getRenderersConfig() instanceof RenderersConfigImpl) {
          ((RenderersConfigImpl) result.getRenderersConfig()).merge(fragment.getRenderersConfig(), false);
        } else if (result.getRenderersConfig() == null) {
          result.setRenderersConfig(fragment.getRenderersConfig());
        }
      }

      // session secret
      if (fragment.getCreateSessionSecret() != null) {
        result.setCreateSessionSecret(fragment.getCreateSessionSecret());
      }
      if (fragment.getCheckSessionSecret() != null) {
        result.setCheckSessionSecret(fragment.getCheckSessionSecret());
      }

      if (fragment.getPreventFrameAttacks() != null) {
        result.setPreventFrameAttacks(fragment.getPreventFrameAttacks());
      }

      if (fragment.getContentSecurityPolicy() != null) {
        result.getContentSecurityPolicy().merge(fragment.getContentSecurityPolicy());
      }

      if (fragment.getSetNosniffHeader() != null) {
        result.setSetNosniffHeader(fragment.getSetNosniffHeader());
      }

      if (fragment.getSanitizerClass() != null) {
        sanitizerClass = fragment.getSanitizerClass();
        sanitizerProperties = fragment.getSanitizerProperties();
      }

      // theme definition
      result.toString();
      for (ThemeImpl theme : fragment.getThemeDefinitions()) {
        result.addAvailableTheme(theme);
      }

      // url
      // todo???

      final Map<String, String> mimeTypes = result.getMimeTypes();
      for (final Map.Entry<String, String> entry : fragment.getMimeTypes().entrySet()) {
        mimeTypes.put(entry.getKey(), entry.getValue());
      }

    }

    resolveThemes(result, result.getAvailableThemes());

    if (sanitizerClass != null) {
      try {
        final Class<? extends Sanitizer> aClass = Class.forName(sanitizerClass).asSubclass(Sanitizer.class);
        final Sanitizer sanitizer = aClass.newInstance();
        sanitizer.setProperties(sanitizerProperties);
        result.setSanitizer(sanitizer);
      } catch (Throwable e) {
        LOG.error("Can't create sanitizer: '" + sanitizerClass + "'", e);
        result.setSanitizer(new IgnoringSanitizer());
      }
    }

    return result;
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
    for (final Pair a : pairs) {
        if (a.getLower() == a.getHigher()) {
          final StringBuilder buffer = new StringBuilder();
          buffer.append("Ordering problem. There are conflicting order rules. Not irreflexive. '");
          buffer.append(a.getLower());
          buffer.append("' < '");
          buffer.append(a.getHigher());
          buffer.append("'!\nThe reason may be a cycle.\n");
          buffer.append("Complete list of rules: \n");
          for (final Pair pair : pairs) {
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
    for (final Pair a : pairs) {
      for (final Pair b : pairs) {
        if (a.getLower() == b.getHigher() && a.getHigher() == b.getLower()) {
          final StringBuilder buffer = new StringBuilder();
          buffer.append("Ordering problem. There are conflicting order rules. Not antisymmetric. '");
          buffer.append(a.getLower());
          buffer.append("' < '");
          buffer.append(a.getHigher());
          buffer.append("'" + "'");
          buffer.append(a.getLower());
          buffer.append("' > '");
          buffer.append(a.getHigher());
          buffer.append("'!\nThe reason may be a cycle.\n");
          buffer.append("Complete list of rules: \n");
          for (final Pair pair : pairs) {
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

  @Override
  public int compare(final TobagoConfigFragment a, final TobagoConfigFragment b) {
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
    for (final TobagoConfigFragment tobagoConfig : list) {
      for (final String befores : tobagoConfig.getBefore()) {
        final TobagoConfigFragment before = findByName(befores);
        if (before != null) {
          pairs.add(new Pair(tobagoConfig, before));
        }
      }
      for (final String afters : tobagoConfig.getAfter()) {
        final TobagoConfigFragment after = findByName(afters);
        if (after != null) {
          pairs.add(new Pair(after, tobagoConfig));
        }
      }
    }
  }

  protected void sort0() {
    Collections.sort(list, this);
  }

  private boolean isInRelation(final TobagoConfigFragment lower, final TobagoConfigFragment higher) {
    for (final Pair p : pairs) {
      if (p.getLower() == lower && p.getHigher() == higher) {
        return true;
      }
    }
    return false;
  }

  private TobagoConfigFragment findByName(final String name) {
    for (final TobagoConfigFragment tobagoConfig : list) {
      if (name.equals(tobagoConfig.getName())) {
        return tobagoConfig;
      }
    }
    return null;
  }

  private void resolveThemes(TobagoConfigImpl tobagoConfig, Map<String, ThemeImpl> map) {
    for (final ThemeImpl theme : map.values()) {
      final String fallbackName = theme.getFallbackName();
      final ThemeImpl fallback = map.get(fallbackName);
      theme.setFallback(fallback);
    }
    for (final ThemeImpl theme : map.values()) {
      theme.resolveFallbacks();
    }
    for (final ThemeImpl theme : map.values()) {
      theme.resolveRendererConfig(tobagoConfig.getRenderersConfig());
    }
    for (final ThemeImpl theme : map.values()) {
      theme.resolveResources();
    }
    for (final ThemeImpl theme : map.values()) {
      theme.init();
    }
  }

  protected List<Pair> getPairs() {
    return pairs;
  }

  private static class Pair {

    private final TobagoConfigFragment lower;
    private final TobagoConfigFragment higher;

    private Pair(final TobagoConfigFragment lower, final TobagoConfigFragment higher) {
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
