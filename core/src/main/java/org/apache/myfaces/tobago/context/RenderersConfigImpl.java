package org.apache.myfaces.tobago.context;

/*
 * Copyright 2002-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.io.Serializable;

/*
* Created by IntelliJ IDEA.
* User: bommel
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

  Collection<RendererConfig>  getRendererConfigs() {
    return renderer.values();
  }

  public void addRenderer(RendererConfig rendererConfig) {
    if (!renderer.containsKey(rendererConfig.getName())) {
      renderer.put(rendererConfig.getName(), rendererConfig);
    }
  }

  public boolean isMarkupSupported(String rendererName, String markup) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("calling isMarkupSupported" + rendererName + " " +markup);
    }
    RendererConfig rendererConfig = renderer.get(rendererName);
    if (rendererConfig != null) {
      return rendererConfig.contains(markup);
    } else {
      LOG.error("Calling isMarkupSupported" + rendererName + " " +markup + "but no configuration found.");
      return false;
    }
  }

  void merge(RenderersConfigImpl renderersConfig) {
    Collection<RendererConfig> renderers = renderersConfig.getRendererConfigs();
    for (RendererConfig rendererConfig : renderers) {
      addRenderer(rendererConfig);
    }
  }

  public String toString() {
    return renderer.toString();
  }
}
