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

package org.apache.myfaces.tobago.internal.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.internal.util.FastStringWriter;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * In some cases the rendered output must be places in the Response in a different order than it was rendered.
 * The <code>ResponseWriterDivider</code> helps to manage a list of buffers which holds the temporary output.
 */
public final class ResponseWriterDivider {

  private static final Logger LOG = LoggerFactory.getLogger(ResponseWriterDivider.class);

  private List<ResponseWriter> writers;
  private List<FastStringWriter> buffers;
  
  private ResponseWriter original;
  
  private int current;
  
  private String nameInRequest;

  public static ResponseWriterDivider getInstance(FacesContext facesContext, String nameInRequest) {
    final Map<String, Object> map = facesContext.getExternalContext().getRequestMap();
    ResponseWriterDivider divider = (ResponseWriterDivider) map.get(nameInRequest);
    if (divider == null) {
      divider = new ResponseWriterDivider(facesContext);
      map.put(nameInRequest, divider);
      divider.nameInRequest = nameInRequest;
      
    }
    return divider;
  }

  private ResponseWriterDivider(FacesContext facesContext) {
    writers = new ArrayList<ResponseWriter>();
    buffers = new ArrayList<FastStringWriter>();
    current = -1;
    original = facesContext.getResponseWriter();
  }

  /**
   * Create (if needed) and activate a new branch. 
   * After this call, all output will be stored in this new branch. 
   * <p>
   * It is usually needed to get the response writer again with HtmlRendererUtils.getTobagoResponseWriter();
   * @return true if the branch was not created new. So the branch was already existent.
   */
  public boolean activateBranch(FacesContext facesContext) {

    assert writers.size() == buffers.size();

    boolean created = true;
    current++;
    if (writers.size() == current) {
      FastStringWriter buffer = new FastStringWriter();
      buffers.add(buffer);
      ResponseWriter newWriter = facesContext.getResponseWriter().cloneWithWriter(buffer);
      writers.add(newWriter);
      created = false;
    }
    facesContext.setResponseWriter(writers.get(current));
    if (LOG.isDebugEnabled()) {
      LOG.debug(this.toString());
    }
    return created;
  }
  
  /**
   * Passivate the current branch. 
   * After this call, all output will be written in the former branch (if any) or into the original writer.
   * <p>
   * It is usually needed to get the response writer again with HtmlRendererUtils.getTobagoResponseWriter();
   * @return true, if the current writer is not the original writer. So the "stack" is at the bottom. 
   */
  public boolean passivateBranch(FacesContext facesContext) {

    assert writers.size() == buffers.size();
    
    current--;
    if (current >= 0) {
      facesContext.setResponseWriter(writers.get(current));
      if (LOG.isDebugEnabled()) {
        LOG.debug(this.toString());
      }
      return true;
    } else {
      facesContext.setResponseWriter(original);
      if (LOG.isDebugEnabled()) {
        LOG.debug(this.toString());
      }
      return false;
    }
  }

  /**
   * Write the collected stuff in the original writer.
   * This is always the last call on this object.
   */
  public void writeOutAndCleanUp(FacesContext facesContext) throws IOException {
    facesContext.setResponseWriter(original);
    for (FastStringWriter buffer : buffers) {
      original.write(buffer.toString());
    }
    // clean up.
    final Map<String, Object> map = facesContext.getExternalContext().getRequestMap();
    map.remove(nameInRequest);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("StringBuilder(");
    builder.append(System.identityHashCode(this));
    builder.append(") current=");
    builder.append(current);
    builder.append("\n");
    int i = 0;
    for (FastStringWriter buffer : buffers) {
      builder.append("\n- buffer ");
      builder.append(i++);
      builder.append(" ------------------------------------------------------------\n");
      builder.append(buffer.toString());
    }
    builder.append("\n-----------------------------------------------------------------------\n");
    return builder.toString();
  }
}
