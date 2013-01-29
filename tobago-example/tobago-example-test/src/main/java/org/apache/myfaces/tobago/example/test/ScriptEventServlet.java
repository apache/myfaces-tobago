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

package org.apache.myfaces.tobago.example.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ScriptEventServlet extends HttpServlet {

  private static final Logger LOG = LoggerFactory.getLogger(ScriptEventServlet.class);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    final HttpSession session = request.getSession(false);
    if (session != null) {
      final ScriptEvent scriptEvent = (ScriptEvent) session.getAttribute("scriptEvent");
      if (scriptEvent != null) {
        String event = request.getParameter("event");
        if (event.equals("onload")) {
          scriptEvent.onLoad();
        } else if (event.equals("onunload")) {
          scriptEvent.onUnload();
        } else if (event.equals("onexit")) {
          scriptEvent.onExit();
        } else {
          LOG.warn("Unknown event");
        }
      }
    }
    response.getOutputStream().write("done".getBytes());
  }
}
