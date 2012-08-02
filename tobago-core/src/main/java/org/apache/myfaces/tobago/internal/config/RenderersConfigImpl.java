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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/*
* Date: Sep 24, 2006
* Time: 3:46:11 PM
*/
public class RenderersConfigImpl implements RenderersConfig, Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger LOG = LoggerFactory.getLogger(RenderersConfigImpl.class);

  private Map<String, RendererConfig> rendererMap = new HashMap<String, RendererConfig>();

  private boolean merged = false;

  public boolean isMerged() {
    return merged;
  }

  public void setMerged(boolean merged) {
    this.merged = merged;
  }

  public Collection<RendererConfig> getRendererConfigs() {
    return rendererMap.values();
  }

  public void addRenderer(RendererConfig rendererConfig) {
    final String name = rendererConfig.getName();
    if (rendererMap.containsKey(name)) {
      rendererMap.get(name).merge(rendererConfig);
    } else {
      rendererMap.put(name, rendererConfig);
    }
  }

  public boolean isMarkupSupported(String rendererName, String markup) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("calling isMarkupSupported('{}', '{}')", rendererName, markup);
    }
    RendererConfig rendererConfig = rendererMap.get(rendererName);
    if (rendererConfig != null) {
      return rendererConfig.contains(markup);
    } else {
      LOG.error("Calling isMarkupSupported('{}', '{}'), but no configuration found.", rendererName, markup);
      return false;
    }
  }

  public void merge(RenderersConfig renderersConfig, boolean override) {
    Collection<RendererConfig> renderers = renderersConfig.getRendererConfigs();
    for (RendererConfig rendererConfig : renderers) {
      addRenderer(rendererConfig);
    }
  }

  public String toString() {
    return rendererMap.toString();
  }
}
