/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * Created 26.08.2004 10:54:52.
 * $Id: MockRenderKit.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.faces;

import javax.faces.render.RenderKit;
import javax.faces.render.Renderer;
import javax.faces.render.ResponseStateManager;
import javax.faces.component.UIData;
import javax.faces.component.UIInput;
import javax.faces.component.UIOutput;
import javax.faces.component.UIPanel;
import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseStream;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.HashMap;
import java.io.Writer;
import java.io.OutputStream;
import java.io.IOException;

public class MockRenderKit extends RenderKit {

  public MockRenderKit() {
      addRenderer(UIData.COMPONENT_FAMILY,
      "javax.faces.Table", new TestRenderer());
addRenderer(UIInput.COMPONENT_FAMILY,
      "TestRenderer", new TestRenderer());
      addRenderer(UIInput.COMPONENT_FAMILY,
      "javax.faces.Text", new TestRenderer());
addRenderer(UIOutput.COMPONENT_FAMILY,
      "TestRenderer", new TestRenderer());
      addRenderer(UIOutput.COMPONENT_FAMILY,
      "javax.faces.Text", new TestRenderer());
      addRenderer(UIPanel.COMPONENT_FAMILY,
      "javax.faces.Grid", new TestRenderer());
  }


  private Map<String,Renderer> renderers = new HashMap<String, Renderer>();


  public void addRenderer(String family, String rendererType,
                          Renderer renderer) {
      if ((family == null) || (rendererType == null) || (renderer == null)) {
          throw new NullPointerException();
      }
      renderers.put(family + "|" + rendererType, renderer);
  }


  public Renderer getRenderer(String family, String rendererType) {
      if ((family == null) || (rendererType == null)) {
          throw new NullPointerException();
      }
      return (renderers.get(family + "|" + rendererType));
  }


  public ResponseWriter createResponseWriter(Writer writer,
               String contentTypeList,
               String characterEncoding) {
      return new MockResponseWriter(writer, characterEncoding);
  }

  public ResponseStream createResponseStream(OutputStream out) {
final OutputStream os = out;
return new ResponseStream() {
  public void close() throws IOException {
      os.close();
  }
  public void flush() throws IOException {
      os.flush();
  }
  public void write(byte[] b) throws IOException {
      os.write(b);
  }
  public void write(byte[] b, int off, int len) throws IOException {
      os.write(b, off, len);
  }
  public void write(int b) throws IOException {
      os.write(b);
  }
    };
  }


  public ResponseStateManager getResponseStateManager() {
return null;
  }


  class TestRenderer extends Renderer {

public TestRenderer() {}

      public void decode(FacesContext context, UIComponent component) {

          if ((context == null) || (component == null)) {
              throw new NullPointerException();
          }

          if (!(component instanceof UIInput)) {
              return;
          }
          UIInput input = (UIInput) component;
          String clientId = input.getClientId(context);
          // System.err.println("decode(" + clientId + ")");

          // Decode incoming request parameters
          Map params = context.getExternalContext().getRequestParameterMap();
          if (params.containsKey(clientId)) {
              // System.err.println("  '" + input.currentValue(context) +
              //                    "' --> '" + params.get(clientId) + "'");
              input.setSubmittedValue(params.get(clientId));
          }

      }

      public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {

          if ((context == null) || (component == null)) {
              throw new NullPointerException();
          }
          ResponseWriter writer = context.getResponseWriter();
          writer.write
              ("<text id='" + component.getClientId(context) + "' value='" +
               component.getAttributes().get("value") + "'/>\n");

      }

      public void encodeChildren(FacesContext context, UIComponent component)
          throws IOException {

          if ((context == null) || (component == null)) {
              throw new NullPointerException();
          }

      }

      public void encodeEnd(FacesContext context, UIComponent component)
          throws IOException {

          if ((context == null) || (component == null)) {
              throw new NullPointerException();
          }

      }

  }



}
