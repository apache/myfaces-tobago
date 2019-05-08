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

// todo: make a reload object

Tobago4.Reload = {
  timeoutMap: new Map<string, number>()
};

Tobago4.Reload.init = function (elements) {
  elements = elements.jQuery ? elements : jQuery(elements); // fixme jQuery -> ES5
  const reloads = Tobago4.Utils.selectWithJQuery(elements, "[data-tobago-reload]");
  reloads.each(function () {
    const reload = Number(this.dataset["tobagoReload"]);
    if (reload > 0) {
      Tobago4.Reload.addReloadTimeout(this.id, Tobago4.Reload.reloadWithAction, reload);
    }
  });
};

Tobago4.Reload.reschedule = function (id: string, reloadMillis: number) {
  if (reloadMillis > 0) {
    Tobago4.Reload.addReloadTimeout(id, Tobago4.Reload.reloadWithAction, reloadMillis);
  }
};

Tobago4.Reload.addReloadTimeout = function (id, func, time) {
  let oldTimeout = Tobago4.Reload.timeoutMap.get(id);
  if (oldTimeout) {
    console.debug("clearReloadTimer timeout='" + oldTimeout + "' id='" + id + "'");
    clearTimeout(oldTimeout);
    Tobago4.Reload.timeoutMap.delete(id);
  }
  let timeout = setTimeout(function () {
    func(id);
  }, time);
  console.debug("addReloadTimer timeout='" + timeout + "' id='" + id + "'");
  Tobago4.Reload.timeoutMap.set(id, timeout);
};

Tobago4.Reload.reloadWithAction = function (
    elementId: string,
    executeIds: string = elementId,
    renderIds: string = executeIds) {
  console.debug("reloadWithAction '" + elementId + "' '" + executeIds + "' '" + renderIds + "");
  // XXX FIXME: behaviorCommands will probably be empty and not working!
  // if (this.behaviorCommands && this.behaviorCommands.reload) {
  //   if (this.behaviorCommands.reload.execute) {
  //     executeIds += " " + this.behaviorCommands.reload.execute;
  //   }
  //   if (this.behaviorCommands.reload.render) {
  //     renderIds += " " + this.behaviorCommands.reload.render;
  //   }
  // }
  jsf.ajax.request(
      elementId,
      null,
      {
        "javax.faces.behavior.event": "reload",
        execute: executeIds,
        render: renderIds
      });
};

Tobago.Listener.register(Tobago4.Reload.init, Tobago.Phase.DOCUMENT_READY);
Tobago.Listener.register(Tobago4.Reload.init, Tobago.Phase.AFTER_UPDATE);
