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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderersConfigImpl implements RenderersConfig, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(RenderersConfigImpl.class);

  private Map<String, RendererConfig> rendererMap = new HashMap<String, RendererConfig>();

  private boolean merged = false;

  public boolean isMerged() {
    return merged;
  }

  public void setMerged(final boolean merged) {
    this.merged = merged;
  }

  public List<RendererConfig> getRendererConfigs() {
    final ArrayList<RendererConfig> result = new ArrayList<RendererConfig>();
    result.addAll(rendererMap.values());
    return result;
  }

  public void addRenderer(final RendererConfig rendererConfig) {
    final String name = rendererConfig.getName();
    if (rendererMap.containsKey(name)) {
      rendererMap.get(name).merge(rendererConfig);
    } else {
      rendererMap.put(name, rendererConfig);
    }
  }

  public boolean isMarkupSupported(final String rendererName, final String markup) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Calling isMarkupSupported('{}', '{}')", rendererName, markup);
    }
    final RendererConfig rendererConfig = rendererMap.get(rendererName);
    if (rendererConfig != null) {
      return rendererConfig.contains(markup);
    } else {
      LOG.error("Calling isMarkupSupported('{}', '{}'), but no configuration found.", rendererName, markup);
      return false;
    }
  }

  public void merge(final RenderersConfig renderersConfig, final boolean override) {
    final Collection<RendererConfig> renderers = renderersConfig.getRendererConfigs();
    for (final RendererConfig rendererConfig : renderers) {
      addRenderer(rendererConfig);
    }
  }

  public String toString() {
    return rendererMap.toString();
  }
}
