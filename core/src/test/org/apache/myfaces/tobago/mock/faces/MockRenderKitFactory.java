/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 26.08.2004 10:52:11.
 * $Id: MockRenderKitFactory.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class MockRenderKitFactory extends RenderKitFactory {

  private Map renderKits = new HashMap();

  public void addRenderKit(String renderKitId, RenderKit renderKit) {
      if ((renderKitId == null) || (renderKit == null)) {
          throw new NullPointerException();
      }
      synchronized (renderKits) {
          if (renderKits.containsKey(renderKitId)) {
              throw new IllegalArgumentException(renderKitId);
          }
          renderKits.put(renderKitId, renderKit);
      }
  }


  public RenderKit getRenderKit(FacesContext context, String renderKitId) {
      if (renderKitId == null) {
          throw new NullPointerException();
      }
      synchronized (renderKits) {
          RenderKit renderKit = (RenderKit) renderKits.get(renderKitId);
          if (renderKit == null) {
              throw new IllegalArgumentException(renderKitId);
          }
          return (renderKit);
      }
  }


  public Iterator getRenderKitIds() {
      synchronized (renderKits) {
          return (renderKits.keySet().iterator());
      }
  }

}
