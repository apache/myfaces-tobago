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

Tobago.Suggest = {};

Tobago.Suggest.loadFromServer = function(input) {

  var timeout;

  return function findMatches(query, syncResults, asyncResults) {

    var suggest = jQuery(Tobago.Utils.escapeClientId(input.data("tobago-suggest-for")));

    if (suggest.val() != query) {

      if (timeout) {
        clearTimeout(timeout);
      }

      var delay = suggest.data("tobago-suggest-delay");

      timeout = setTimeout(function() {
        suggest.val(query);
        suggest.data("tobago-suggest-callback", asyncResults);
        suggest.removeData("tobago-suggest-data"); // clear jQuery-data-cache
        var id = suggest.attr("id");
        console.info("query: '" + query + "'"); // @DEV_ONLY

        jsf.ajax.request(
            id,
            null, // todo: event?
            {
              "javax.faces.behavior.event": "suggest",
              execute: id,
              render: id
            });
      }, delay);

    }
  };
};

Tobago.Suggest.fromClient = function(data) {
  return function findMatches(query, syncResults) {
    var result = [];
    for (i = 0; i < data.length; i++) {
      if (data[i].indexOf(query) >= 0) {
        result.push(data[i]);
      }
    }
    syncResults(result);
  };
};

Tobago.Suggest.init = function(elements) {

  var suggests = Tobago.Utils.selectWithJQuery(elements, ".tobago-suggest");

  suggests.each(function() {
    var suggest = jQuery(this);
    var input = jQuery(Tobago.Utils.escapeClientId(suggest.data("tobago-suggest-for")));

    var minChars = suggest.data("tobago-suggest-min-chars");
    var maxItems = suggest.data("tobago-suggest-max-items");

    var update = typeof suggest.data("tobago-suggest-update") != "undefined";
    var totalCount = suggest.data("tobago-suggest-total-count"); // todo

    if (update && input.hasClass("tt-input")) { // already initialized: so only update data
      var asyncResults = suggest.data("tobago-suggest-callback"); // comes from "findMatches()"
      if (asyncResults) {
        var data1 = suggest.data("tobago-suggest-data");
        asyncResults(data1);
      }
    } else { // new
      input.data("tobago-suggest-for", suggest.attr("id"));
      input.attr("autocomplete", "off");

      var source;
      if (update) {
        source = Tobago.Suggest.loadFromServer(input);
      } else {
        var data2 = suggest.data("tobago-suggest-data");
        source = Tobago.Suggest.fromClient(data2);
      }

      var $suggestPopup = jQuery(Tobago.Utils.escapeClientId(suggest.attr('id') + "::popup"));
      if ($suggestPopup.length > 0) {
        $suggestPopup.remove();
      }

      jQuery(".tobago-page-menuStore")
          .append("<div id='" + suggest.attr('id') + "::popup" + "' class='tt-menu tt-empty'/>");
      $suggestPopup = jQuery($suggestPopup.selector);

      input.typeahead({
        menu: $suggestPopup,
        minLength: minChars,
        hint: true,// todo
        highlight: true // todo
      }, {
        //name: 'test',// todo
        limit: maxItems,
        source: source
      }).on('typeahead:change', function(event) {
        input.trigger('change');
      });

      input.bind('typeahead:open', function() {
        var $input = jQuery(this);
        var $suggest = $input.parent().siblings(".tobago-suggest");
        var $suggestPopup = jQuery(Tobago.Utils.escapeClientId($suggest.attr('id') + "::popup"));
        $suggestPopup.css("top", $input.offset().top + $input.outerHeight() + "px");
        $suggestPopup.css("left", $input.offset().left + "px");
        $suggestPopup.css("min-width", $input.outerWidth() + "px");
      });
    }
  });
};

// using "EARLY", because it must be called before Tobago.Layout.init
// this is because the suggest puts a span around the input field and doesn't copy the style.
Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.DOCUMENT_READY, Tobago.Phase.Order.EARLY);
Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.AFTER_UPDATE, Tobago.Phase.Order.EARLY);
