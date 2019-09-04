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

import {Listener, Order, Phase} from "./tobago-listener";
import {Overlay} from "./tobago-overlay";
import {DomUtils, Tobago4Utils} from "./tobago-utils";
import {Collapse, Popup} from "./tobago-popup";
import {Setup} from "./tobago-core";

export class CommandMap {

  commands: Map<string, Command>;

  // XXX remove me later... may be, if using "Custom Elements"
  /** @deprecated */
  public static getData(element: HTMLElement, eventType: string): Command {
    const commandMap: CommandMap = new CommandMap(element.dataset["tobagoCommands"]);
    return commandMap.commands.get(eventType);
  }

  static change(event: TextEvent) {
    const element = event.currentTarget as HTMLElement;
    const change = CommandMap.getData(element, "change");

    if (change.execute || change.render) {
      jsf.ajax.request(
          element,
          // element.getAttribute("name"),
          event,
          {
            "javax.faces.behavior.event": "change",
            execute: change.execute,
            render: change.render
          });
    } else {
      Command.submitAction(this, change.action, change);
    }
  }

  static resize(event: Event) { // TBD MouseEvent?
    const element = event.currentTarget as HTMLElement;
    const resize = CommandMap.getData(element, "resize");
    console.debug("window resize event: " + resize);
    Command.submitAction(this, resize.action, resize);
  }

  static otherEvent(event: Event) {
    const element = event.currentTarget as HTMLElement;
    const command = CommandMap.getData(element, event.type);

    const confirmation = command.confirmation;
    if (confirmation) {
      if (!confirm(confirmation)) {
        event.preventDefault();
        return;
      }
    }
    const collapse = command.collapse;
    if (collapse) {
      Collapse.execute(collapse);
    }

    if (!command.omit) {
      const popup = command.popup;
      if (popup && popup.command === "close" && popup.immediate) {
        Popup.close(element);
      } else {
        const action = command.action ? command.action : element.id;
        if (command.execute || command.render) {
          Command.preparePartialOverlay(command);
          jsf.ajax.request(
              action,
              event,
              {
                "javax.faces.behavior.event": event.type,
                execute: command.execute,
                render: command.render
              });
        } else {
          Command.submitAction(this, action, command);
        }
        if (popup && popup.command === "close") {
          Popup.close(element);
        }
      }
    }
  }

  constructor(data: string) {
    this.commands = new Map<string, Command>();
    const object = JSON.parse(data);
    for (let key of Object.keys(object)) {
      const command5 = new Command(object[key]);
      this.commands.set(key, command5);
    }
  }

  get change() {
    return this.commands.get("change");
  }

  get complete() {
    return this.commands.get("complete");
  }

  get load() {
    return this.commands.get("load");
  }

  get resize() {
    return this.commands.get("resize");
  }

  public stringify(): string {
    let object = Object.create(null);
    for (let [k, v] of this.commands) {
      object[k] = v;
    }
    const outer = JSON.stringify(this);
    // remove {} and replace with object-JSON
    return outer.substring(0, outer.length - 3) + JSON.stringify(object) + "}";
  }
}

export class Command {

  // XXX this is a state of the page
  static isSubmit: boolean = false;

  confirmation: string;
  collapse: boolean; // XXX is boolean okay??? Should this be not an element or a structure?
  omit: boolean;
  popup; // todo: type
  action: string;
  execute: string;
  render: string;
  transition: boolean;
  delay: number;
  target: string;

  /**
   * Submitting the page with specified actionId.
   * options.transition
   * options.target
   */
  public static submitAction = function (source: any, actionId: string, command: Command = new Command()) {

    let transition = command.transition === undefined || command.transition;

    Transport.request(function () {
      if (!Command.isSubmit) {
        Command.isSubmit = true;
        const form = document.getElementsByTagName("form")[0] as HTMLFormElement;
        var oldTarget = form.getAttribute("target");
        const sourceHidden = document.getElementById("javax.faces.source") as HTMLInputElement;
        sourceHidden.disabled = false;
        sourceHidden.value = actionId;
        if (command.target) {
          form.setAttribute("target", command.target);
        }
        this.oldTransition = this.transition;
        this.transition = transition && !command.target;

        var listenerOptions = {
          source: source,
          actionId: actionId,
          options: command
        };
        var onSubmitResult = Command.onSubmit(listenerOptions);
        if (onSubmitResult) {
          try {
            form.submit();
            // reset the source field after submit, to be prepared for possible next AJAX with transition=false
            sourceHidden.disabled = true;
            sourceHidden.value = "";
          } catch (e) {
            Overlay.destroy(DomUtils.page().id);
            Command.isSubmit = false;
            alert('Submit failed: ' + e); // XXX localization, better error handling
          }
        }
        if (command.target) {
          if (oldTarget) {
            form.setAttribute("target", oldTarget);
          } else {
            form.removeAttribute("target");
          }
        }
        if (command.target || !transition || !onSubmitResult) {
          Command.isSubmit = false;
          Transport.pageSubmitted = false;
        }
      }
      if (!Command.isSubmit) {
        Transport.requestComplete(); // remove this from queue
      }
    }, true);
  };

  static initEnter(element: HTMLElement) {
    for (const page of DomUtils.selfOrQuerySelectorAll(element, ".tobago-page")) {
      page.addEventListener("keypress", function (event: KeyboardEvent) {
        let code = event.which; // XXX deprecated
        if (code === 0) {
          code = event.keyCode;
        }
        if (code === 13) {
          let target = event.target as HTMLElement;
          if (target.tagName === "A" || target.tagName === "BUTTON") {
            return;
          }
          if (target.tagName === "TEXTAREA") {
            if (!event.metaKey && !event.ctrlKey) {
              return;
            }
          }
          const name = target.getAttribute("name");
          let id = name ? name : target.id;
          while (id != null) {
            const command = document.querySelector("[data-tobago-default='" + id + "']");
            if (command) {
              command.dispatchEvent(new MouseEvent("click"));
              break;
            }
            id = Tobago4Utils.getNamingContainerId(id);
          }
          return false;
        }
      });
    }
  }

  static init = function (element: HTMLElement, force: boolean = false) {

    for (const commandElement of DomUtils.selfOrQuerySelectorAll(element, "[data-tobago-commands]")) {

      // TODO hack to set command eventListeners after tobago-tab EventListeners
      if (force || commandElement.parentElement.tagName !== "TOBAGO-TAB") {

        const commandMap = new CommandMap(commandElement.dataset["tobagoCommands"]);

        for (const entry of commandMap.commands.entries()) {
          const key: string = entry[0];
          const value: Command = entry[1];

          switch (key) {
            case "change":
              commandElement.addEventListener("change", CommandMap.change);
              break;
            case "complete":
              if (parseFloat(commandElement.getAttribute("value")) >= parseFloat(commandElement.getAttribute("max"))) {
                if (commandMap.complete.execute || commandMap.complete.render) {
                  jsf.ajax.request(
                      this.id,
                      null,
                      {
                        "javax.faces.behavior.event": "complete",
                        execute: commandMap.complete.execute,
                        render: commandMap.complete.render
                      });
                } else {
                  Command.submitAction(this, commandMap.complete.action, commandMap.complete);
                }
              }
              break;
            case "load":
              setTimeout(function () {
                    Command.submitAction(this, commandMap.load.action, commandMap.load);
                  },
                  commandMap.load.delay || 100);
              break;
            case "resize":
              window.addEventListener("resize", CommandMap.resize);
              break;
            default:
              commandElement.addEventListener(key, CommandMap.otherEvent);
          }
        }
      }
    }
  };

  static onSubmit = function (listenerOptions) {
    Listener.executeBeforeSubmit();
    /*
    XXX check if we need the return false case
    XXX maybe we cancel the submit, but we continue the rest?
    XXX should the other phases also have this feature?

        var result = true; // Do not continue if any function returns false
        for (var order = 0; order < Listeners.beforeSubmit.length; order++) {
          var list = Listeners.beforeSubmit[order];
          for (var i = 0; i < list.length; i++) {
            result = list[i](listenerOptions);
            if (result === false) {
              break;
            }
          }
        }
        if (result === false) {
          this.isSubmit = false;
          return false;
        }
    */
    Command.isSubmit = true;

    Setup.onBeforeUnload();

    return true;
  };

  static preparePartialOverlay = function (command: Command) {
    if (command.transition === undefined || command.transition == null || command.transition) {
      console.debug("[tobago-command] render: '" + command.render + "'");
      if (command.render) {
        let partialIds = command.render.split(" ");
        for (let i = 0; i < partialIds.length; i++) {
          new Overlay(document.getElementById(partialIds[i]), true);
        }
      }
    }
  };

  constructor(data?: string | object) {
    let object;
    if (data) {
      if (typeof data === "string") {
        object = JSON.parse(data);
      } else {
        object = data;
      }
      for (let key of Object.keys(object)) {
        this[key] = object[key];
      }
    }
  }

  public stringify(): string {
    return JSON.stringify(this);
  }
}

Listener.register(Command.initEnter, Phase.DOCUMENT_READY);

Listener.register(Command.init, Phase.DOCUMENT_READY, Order.LATER);
Listener.register(Command.init, Phase.AFTER_UPDATE, Order.LATER);

class Transport {
  static requests = [];
  static currentActionId = null;
  static pageSubmitted = false;
  static startTime: Date;

  /**
   * @return true if the request is queued.
   */
  static request = function (req, submitPage, actionId?) {
    var index = 0;
    if (submitPage) {
      Transport.pageSubmitted = true;
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
    } else if (!Transport.pageSubmitted) { // AJAX case
      console.debug('Current ActionId = ' + Transport.currentActionId + ' action= ' + actionId);
      if (actionId && Transport.currentActionId === actionId) {
        console.info('Ignoring request');
        // If actionId equals currentActionId assume double request: do nothing
        return false;
      }
      index = Transport.requests.push(req);
      //console.debug('index = ' + index)
      Transport.currentActionId = actionId;
    } else {
      console.debug("else case");
      return false;
    }
    console.debug('index = ' + index);
    if (index === 1) {
      console.info('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    } else {
      console.info('Request queued!');
    }
    return true;
  };


// TBD XXX REMOVE is this called in non AJAX case?

  static requestComplete = function () {
    Transport.requests.shift();
    Transport.currentActionId = null;
    console.debug('Request complete! Duration: ' + (new Date().getTime() - Transport.startTime.getTime()) + 'ms; '
        + 'Queue size : ' + Transport.requests.length);
    if (Transport.requests.length > 0) {
      console.debug('Execute request!');
      Transport.startTime = new Date();
      Transport.requests[0]();
    }
  };
}
