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
/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Sep 24, 2006
 * Time: 3:46:11 PM
 */
public class MarkupConfigImpl implements MarkupConfig {

  private static final Log LOG = LogFactory.getLog(MarkupConfigImpl.class);

  private Map<String,RendererMarkup> renderer = new HashMap<String,RendererMarkup>();

  Collection<RendererMarkup>  getRendererMarkups() {
    return renderer.values();
  }

  public void addRenderer(RendererMarkup rendererMarkup) {
    if (!renderer.containsKey(rendererMarkup.getName())) {
      renderer.put(rendererMarkup.getName(), rendererMarkup);
    }
  }

  public boolean isMarkupSupported(String rendererName, String markup) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("calling isMarkupSupported" + rendererName + " " +markup);
    }
    RendererMarkup rendererMarkup = renderer.get(rendererName);
    if (rendererMarkup != null) {
      return rendererMarkup.contains(markup);
    } else {
      LOG.error("Calling isMarkupSupported" + rendererName + " " +markup + "but no configuration found.");
      return false;
    }
  }

  void merge(MarkupConfigImpl markupConfig) {
    Collection<RendererMarkup> markups = markupConfig.getRendererMarkups();
    for (RendererMarkup markup: markups) {
      addRenderer(markup);
    }
  }

  public String toString() {
    return renderer.toString();
  }
}
