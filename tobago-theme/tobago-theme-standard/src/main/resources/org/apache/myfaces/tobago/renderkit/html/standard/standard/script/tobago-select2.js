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

Tobago.Select2 = {

  registry: {},

  init: function (elements) {
    console.info("Tobago.Select2.init");                                    // @DEV_ONLY
    Tobago.Utils.selectWithJQuery(elements, "[data-tobago-select2]")
        .each( function () {
          var element = jQuery(this);

          var select2Options = jQuery.extend({}, element.data("tobago-select2"), Tobago.Select2.getExtensions(element));

          if (element.hasClass("tobago-selectManyBox")) {
            select2Options.containerCss = {height: element.data("tobago-style").height};
          }
          console.info("Select2.init" + element.attr("id") + " with data: " // @DEV_ONLY
              + JSON.stringify(select2Options));                            // @DEV_ONLY
          if (typeof select2Options.dropdownAdapter === "string") {
            select2Options.dropdownAdapter = jQuery.fn.select2.amd.require(select2Options.dropdownAdapter);
          }
          if (typeof select2Options.tokenizer === "string" && typeof eval(select2Options.tokenizer) === "function") {
            console.info("select2Options.tokenizer: " + typeof select2Options.tokenizer);
            console.info("select2Options.tokenizer: " + typeof eval(select2Options.tokenizer));
            select2Options.tokenizer = eval(select2Options.tokenizer)
          }
          var commands = element.data("tobago-commands");

          if (commands) {
            for (var name in commands) {
              if (commands.hasOwnProperty(name) && name.indexOf("select2:") === 0) {
                var command = commands[name];
                var actionId = command.action;
                if (command.script) {
                  // not allowed with Content Security Policy (CSP)
                  var func = eval(command.script);
                  element.on("select2:select", func);
                } else if (command.partially) {
                  var partially = command.partially;
                  element.on("select2:select", function () {
                    if (actionId !== undefined) {
                      console.info("select2:select reloadComponent(" + partially + ", " + actionId + ")");
                      Tobago.reloadComponent(this, partially, actionId);
                    }
                  });
                } else {
                  actionId = command.action;
                  element.on("select2:select", function () {
                    console.info("select2:select submitAction(" + actionId + ")");
                    Tobago.submitAction(this, actionId);
                  });
                }
              }
            }

          }
          element.select2(select2Options);
        });
  },

  getExtensions: function (element) {
    var extend = element.data("tobago-select2-extend");
    if (extend !== undefined) {
      for (var extName in extend) {
        if (extend.hasOwnProperty(extName)
            && typeof extend[extName] === "string") {
          extend[extName] = Tobago.Select2.registry[extend[extName]];
        }
      }
    }
    return extend;
  },

  register: function (name, object) {
    Tobago.Select2.registry[name] = object;
  }
};

Tobago.Select2.register('caseSensitiveMatcher', function (params, data) {
  // modified copy of original select2 matcher

  // Always return the object if there is nothing to compare
  if ($.trim(params.term) === '') {
    return data;
  }

  // Do a recursive check for options with children
  if (data.children && data.children.length > 0) {
    // Clone the data object if there are children
    // This is required as we modify the object to remove any non-matches
    var match = $.extend(true, {}, data);

    // Check each child of the option
    for (var c = data.children.length - 1; c >= 0; c--) {
      var child = data.children[c];

      var matches = matcher(params, child);

      // If there wasn't a match, remove the object in the array
      if (matches == null) {
        match.children.splice(c, 1);
      }
    }

    // If any children matched, return the new object
    if (match.children.length > 0) {
      return match;
    }

    // If there were no matching children, check just the plain object
    return matcher(params, match);
  }

  // var original = stripDiacritics(data.text).toUpperCase();
  // var term = stripDiacritics(params.term).toUpperCase();
  var original = data.text;
  var term = params.term;

  // Check if the text contains the term
  if (original.indexOf(term) > -1) {
    return data;
  }

  // If it doesn't contain the term, don't return anything
  return null;
});

Tobago.registerListener(Tobago.Select2.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Select2.init, Tobago.Phase.AFTER_UPDATE);
