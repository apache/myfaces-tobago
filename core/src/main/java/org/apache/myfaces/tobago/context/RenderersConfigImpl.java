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

package org.apache.myfaces.tobago.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

  private static final Log LOG = LogFactory.getLog(RenderersConfigImpl.class);

  private Map<String, RendererConfig> renderer = new HashMap<String, RendererConfig>();

  private boolean merged = false;

  public boolean isMerged() {
    return merged;
  }

  public void setMerged(boolean merged) {
    this.merged = merged;
  }

  public Collection<RendererConfig> getRendererConfigs() {
    return renderer.values();
  }

  public void addRenderer(RendererConfig rendererConfig) {
    addRenderer(rendererConfig, false);
  }

  public void addRenderer(RendererConfig rendererConfig, boolean override) {
    if (override || !renderer.containsKey(rendererConfig.getName())) {
      renderer.put(rendererConfig.getName(), rendererConfig);
    }
  }

  public boolean isMarkupSupported(String rendererName, String markup) {
    RendererConfig rendererConfig = renderer.get(rendererName);
    if (rendererConfig != null) {
      return rendererConfig.contains(markup);
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No config found for renderer '" + rendererName + "'.");
      }
      return false;
    }
  }

  void merge(RenderersConfig renderersConfig, boolean override) {
    Collection<RendererConfig> renderers = renderersConfig.getRendererConfigs();
    for (RendererConfig rendererConfig : renderers) {
      addRenderer(rendererConfig, override);
    }
  }

  public String toString() {
    return renderer.toString();
  }
}
