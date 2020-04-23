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

          var select2Options = jQuery.extend({}, element.data("tobago-select2"));

          var suggestId = element.data("tobago-suggest-id");
          if (typeof suggestId === "string") {
            var suggest = jQuery(Tobago.Utils.escapeClientId(suggestId));
            if (suggest.length) {
              select2Options.ajax = {
                suggestId: suggestId,
                url: "http://localhost/just/a/dummy/url",
                transport: Tobago.Select2.transport
              };
              var delay = suggest.data("tobago-suggest-delay");
              if (delay) {
                select2Options.ajax.delay = delay;
              }
            } else {
              console.error("Suggest2 ajax problem: could not find element with id " + suggestId);
              suggestId = undefined;
            }
          }

          if (element.hasClass("tobago-selectManyBox")) {
            select2Options.containerCss = {height: element.data("tobago-style").height};
            if (suggestId) {
              select2Options.dropdownCssClass = undefined; // it makes no sense to hide the ajax response
              select2Options.createTag = Tobago.Select2.createResponseTag;
              select2Options.templateResult = Tobago.Select2.suggestTemplateResult;
            }
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
          select2Options = jQuery.extend(select2Options, Tobago.Select2.getExtensions(element));
          console.info("Select2 select2Options " + element.attr("id") + " with data: " // @DEV_ONLY
              + JSON.stringify(select2Options));                                       // @DEV_ONLY
          element.select2(select2Options);
        });
  },

  createResponseTag: function (params) {

    var term = jQuery.trim(params.term);

    if (term === '') {
      return null;
    }

    return {
      id: term,
      text: term,
      hide: "query" === params._type
    }
  },

  suggestTemplateResult: function (result) {
    if (result.loading || result.hide) {
      return null;
    }
    return result.text;
  },

  transport: function (params, success, failure) {

    if (params.data && params.data.q !== undefined) {
      console.debug("SELECT2 tobago params.data.q : " + params.data.q); // @DEV_ONLY
      jQuery(Tobago.Utils.escapeClientId(params.suggestId)).val(params.data.q);
    } else {
      console.debug("SELECT2 tobago undef "); // @DEV_ONLY
    }

    Tobago.reloadComponent(null, params.suggestId, params.suggestId, {
      createOverlay: false,
      suggestId : params.suggestId,
      afterDoUpdateSuccess: function (requestOptions) {
        var data = jQuery(Tobago.Utils.escapeClientId(requestOptions.suggestId)).data("tobago-suggest-response-data");
        console.debug("SELECT2 tobago SUCCESS"); // @DEV_ONLY
        var currentValues = jQuery(Tobago.Utils.escapeClientId(params.suggestId))
            .next().children('.tobago-selectManyBox').val();
        if (currentValues !== undefined) {
          // remove already selected items in select many box
          data.results = Tobago.Select2.removeSelected(data.results, currentValues);
        }
        success(data);
      },
      afterDoUpdateError: function () {
        console.debug("SELECT2 tobago ERROR"); // @DEV_ONLY
        failure();
      }

    });

    return {
      abort: function () {
        console.debug("SELECT2 tobago ABORT transport");  // @DEV_ONLY
      }
    }
  },

  removeSelected: function (results, currentValue) {
    if (currentValue.length === 0) {
      return results;
    }
    var newData = [];
    for (var i = 0; i < results.length; i++) {
      var item = results[i];
      if (!currentValue.includes(item.id)) {
        newData.push(item);
      }
    }
    return newData;
  },

  getExtensions: function (element) {
    var extend = element.data("tobago-select2-extend");
    if (extend !== undefined) {
      for (var extName in extend) {
        if (extend.hasOwnProperty(extName)
            && typeof extend[extName] === "string") {
          var extensionName = extend[extName];
          var value = Tobago.Select2.registry[extensionName];
          if (value) {
            extend[extName] = value;
          } else {
            try {
              value = jQuery(extensionName);
            } catch (e) {}
            if (!(value && value.length) && extend[extName].charAt(0) !== '#') {
              try {
                value = jQuery(Tobago.Utils.escapeClientId(extensionName));
              } catch (e) {}
            }
            if (value && value.length) {
              extend[extName] = value;
            }
          }
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

Tobago.Select2.register('suppressMessages', (function() {

  var suppressMessages = (function () {

    function SuppressMessage (decorated, $element, options, dataAdapter) {
      decorated.call(this, $element, options, dataAdapter);
    }

    SuppressMessage.prototype.displayMessage = function(decorated, params) {
    };

    return SuppressMessage;
  })();

  var Utils = jQuery.fn.select2.amd.require('select2/utils');
  var resultAdapter = jQuery.fn.select2.amd.require('select2/results');
  var selectOnClose = jQuery.fn.select2.amd.require('select2/dropdown/selectOnClose');
  var adapter = Utils.Decorate(resultAdapter, selectOnClose);
  return Utils.Decorate(adapter, suppressMessages);

})());


Tobago.Select2.register('testDataAdapter', (function() {

  var testDataAdapter = (function () {

    function TestDataAdapter (decorated, $element, options) {
      decorated.call(this, $element, options);
    }

    TestDataAdapter.prototype.query = function(decorated, params, callback) {
      var select2Instance = this;

      if (!select2Instance.$element.data("tttxt")) {
        select2Instance.container.on("open", function (params) {
          console.warn("TestDataAdapter.container.open");
          if (!select2Instance.$search.val()) {
            select2Instance.container.$results.empty();
          }
        });
        select2Instance.$element.data("tttxt", true);
      }

      console.warn("TestDataAdapter.prototype.query");
      callback({results: []});
      function clearDuplicatesCallback(results) {
        callback(results);

        var values = [];
        select2Instance.$element.find("option[data-select2-tag='true']").each(function () {
           var option  = jQuery(this);
           if (values.includes(option.val())) {
             option.detach();
           } else {
             values.push(option.val());
           }
        });
      }
      decorated.call(this, params, clearDuplicatesCallback);
    };

    return TestDataAdapter;
  })();

  var Utils = jQuery.fn.select2.amd.require('select2/utils');


  var adapter = jQuery.fn.select2.amd.require('select2/data/ajax');


  /*
    {"tags":true,"tokenSeparators":[","],"language":"de","minimumInputLength":2,"placeholder":"Select countries"}'
   */

  adapter = Utils.Decorate(adapter, testDataAdapter);


  adapter = Utils.Decorate(adapter, jQuery.fn.select2.amd.require('select2/data/minimumInputLength'));
  adapter = Utils.Decorate(adapter, jQuery.fn.select2.amd.require('select2/data/tags'));
  return Utils.Decorate(adapter, jQuery.fn.select2.amd.require('select2/data/tokenizer'));

})());





Tobago.registerListener(Tobago.Select2.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Select2.init, Tobago.Phase.AFTER_UPDATE);
