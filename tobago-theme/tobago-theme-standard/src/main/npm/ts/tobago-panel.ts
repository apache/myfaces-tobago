/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// XXX: 2nd parameter enableAjax is deprecated
Tobago4.Panel = function(panelId, enableAjax, autoReload) {
  this.id = panelId;
  this.autoReload = autoReload;
  this.options = {
  };

  this.setup();
};

Tobago4.Panel.init = function(elements) {
  elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
  var reloads = Tobago4.Utils.selectWithJQuery(elements, ".tobago-panel[data-tobago-reload]");
  reloads.each(function(){
    var id = jQuery(this).attr("id");
    var period = jQuery(this).data("tobago-reload");
    new Tobago4.Panel(id, true, period);
  });
};

Tobago4.Panel.prototype.setup = function() {
  this.initReload();
};

Tobago4.Panel.prototype.initReload = function() {
  if (typeof this.autoReload == 'number' && this.autoReload > 0) {
    Tobago4.addReloadTimeout(this.id, Tobago4.Panel.reloadWithAction, this.autoReload);
  }
};

Tobago4.Panel.reloadWithAction = function(elementId) {
  jsf.ajax.request(
      elementId,
      null,
      {
        "javax.faces.behavior.event": "reload",
        execute: elementId,
        render: elementId
      });
};

Tobago.Listener.register(Tobago4.Panel.init, Tobago.Phase.DOCUMENT_READY);
Tobago.Listener.register(Tobago4.Panel.init, Tobago.Phase.AFTER_UPDATE);
