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

document.addEventListener("tobago.init", function () {
  /** @type {import("tobago-box").Box} */
  const box = document.getElementById("page:mainForm:collapsibleBox");
  /** @type {import("tobago-panel").Panel} */
  const panel = document.getElementById("page:mainForm:collapsiblePanel");
  /** @type {import("tobago-popup").Popup} */
  const popup = document.getElementById("page:mainForm:popup");
  /** @type {import("tobago-offcanvas").Offcanvas} */
  const offcanvas = document.getElementById("page:mainForm:offcanvas");
  /** @type {import("tobago-section").Section} */
  const section = document.getElementById("page:mainForm:collapsibleSection");

  document.getElementById("page:mainForm:expandBoxPanelSection").addEventListener("click", () => {
    box.expand();
    panel.expand();
    section.expand();
  });
  document.getElementById("page:mainForm:collapseBoxPanelSection").addEventListener("click", () => {
    box.collapse();
    panel.collapse();
    section.collapse();
  });
  document.getElementById("page:mainForm:expandPopup").addEventListener("click", () => {
    popup.expand();
  });
  document.getElementById("page:mainForm:popup:collapsePopup").addEventListener("click", () => {
    popup.collapse();
  });
  document.getElementById("page:mainForm:expandOffcanvas").addEventListener("click", () => {
    offcanvas.expand();
  });
  document.getElementById("page:mainForm:collapseOffcanvas").addEventListener("click", () => {
    offcanvas.collapse();
  });
});
