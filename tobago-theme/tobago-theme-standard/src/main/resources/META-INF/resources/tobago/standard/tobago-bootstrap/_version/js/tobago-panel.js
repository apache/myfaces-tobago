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
Tobago.Panel = function(panelId, enableAjax, autoReload) {
  this.id = panelId;
  this.autoReload = autoReload;
  this.options = {
  };

  this.setup();
};

Tobago.Panel.init = function(elements) {
  var reloads = Tobago.Utils.selectWithJQuery(elements, ".tobago-panel[data-tobago-reload]");
  reloads.each(function(){
    var id = jQuery(this).attr("id");
    var period = jQuery(this).data("tobago-reload");
    new Tobago.Panel(id, true, period);
  });
};

Tobago.Panel.prototype.setup = function() {
  this.initReload();
};

Tobago.Panel.prototype.initReload = function() {
  if (typeof this.autoReload == 'number' && this.autoReload > 0) {
    Tobago.addReloadTimeout(this.id, Tobago.Panel.reloadWithAction, this.autoReload);
  }
};

Tobago.Panel.reloadWithAction = function(elementId) {
  jsf.ajax.request(
      elementId,
      null,
      {
        "javax.faces.behavior.event": "reload",
        execute: elementId,
        render: elementId
      });
};

Tobago.registerListener(Tobago.Panel.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Panel.init, Tobago.Phase.AFTER_UPDATE);
