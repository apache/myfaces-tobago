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

package org.apache.myfaces.tobago.renderkit.fo.standard.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.renderkit.LayoutManager;

import javax.faces.component.UIComponent;
import javax.faces.component.UIMessages;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/*
 * Created: Dec 1, 2004 7:25:02 PM
 * User: bommel
 * $Id:GridLayoutRenderer.java 472227 2006-11-07 21:05:00 +0100 (Tue, 07 Nov 2006) bommel $
 */
public class GridLayoutRenderer extends FoRendererBase
    implements LayoutManager {

  private static final Log LOG = LogFactory.getLog(GridLayoutRenderer.class);

  public boolean getRendersChildren() {
    return false;
  }

  public void encodeBegin(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** begin    " + component);
    }
    try {


      layoutBegin(facesContext, component);
    } catch (RuntimeException e) {
      LOG.error("caught RuntimeException :", e);
      throw e;
    } catch (Throwable e) {
      LOG.error("caught Throwable :", e);
      throw new RuntimeException(e);
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("*   begin    " + component);
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("*** end      " + component);
    }
    try {
      layoutEnd(facesContext, component);

    } catch (RuntimeException e) {
      LOG.error("caught " + e + " :" + e.getMessage(), e);
      throw e;
    } catch (Throwable e) {
      LOG.error("caught Throwable :", e);
      throw new RuntimeException(e);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*   end      " + component);
    }
  }

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
  }

  private void layoutEnd(FacesContext facesContext, UIComponent component) {
    Layout layout = Layout.getLayout(component.getParent());
    if (layout == null) {
      throw new IllegalStateException("no Layout from " + component.getParent() + " "
          + component.getParent().getClientId(facesContext));
    }
    layout.setOrientation(Layout.TOP_ORIENTATION);
    if (component.getAttributes().get("columns") != null) {
      layout.setOrientation(Layout.LEFT_ORIENTATION);
    }
    Layout.putLayout(component, layout);
    LOG.error(layout);
    //new Exception().printStackTrace();
    //suppe++;
    //if (suppe==10) {
    //  throw new IllegalStateException("");
    //}
    List children = component.getParent().getChildren();
    ResponseWriter writer = facesContext.getResponseWriter();
    LOG.error("parent is " + component.getParent() + "|"
        + component.getParent().getClientId(facesContext) + " component is "
        + component + "|" + component.getClientId(facesContext));
    if (children.size() > 0) {
      int incrementX = layout.getWidth() / children.size();
      int incrementY = layout.getHeight() / children.size();
      int height = layout.getHeight();
      int width = layout.getWidth();
      int x = layout.getX();
      int y = layout.getY();

      for (int i = 0; i < children.size(); i++) {
        LOG.error("i = " + i + " size = " + children.size());

        UIComponent cell = (UIComponent) children.get(i);
        //LOG.error(cell+ " | "+cell.getClientId(facesContext));
        if (!(cell instanceof UIMessages || cell instanceof UIGridLayout)) {
          try {
            FoUtils.startBlockContainer(writer, component);
            if (layout.getOrientation() == Layout.TOP_ORIENTATION) {

              FoUtils.layoutBlockContainer(writer, incrementY,
                  width, x, y + incrementY * i);
            } else {
              FoUtils.layoutBlockContainer(writer, height,
                  incrementY, x + incrementX * i, y);
            }
            Layout.setInLayout(cell, true);

            encodeSuppe(facesContext, cell);

            FoUtils.endBlockContainer(writer);
          } catch (IOException e) {
            LOG.error("", e);
          }
        }
      }
    }

  }

  private void encodeSuppe(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.getRendersChildren()) {
      component.encodeBegin(facesContext);
      component.encodeChildren(facesContext);
    } else {
      Iterator kids = component.getChildren().iterator();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        encodeSuppe(facesContext, kid);
      }
    }
    component.encodeEnd(facesContext);
  }

}
